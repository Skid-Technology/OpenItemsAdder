package dev.lone.itemsadder.world;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.CollectionUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PersistentCounter;
import dev.lone.itemsadder.util.StoreException;
import dev.lone.itemsadder.util.serial.SerialRecordA;
import dev.lone.itemsadder.util.serial.SerialRecordB;
import dev.lone.itemsadder.util.serial.SerialRecordC;
import dev.lone.itemsadder.util.serial.SerialRecordF;
import dev.lone.itemsadder.util.serial.SerialRecordG;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlockChunkStoreBase {
   private static final long CHUNK_EXPIRY_MS = 60000L;
   public final World world;
   public final int regionId;
   final int regionX;
   final int regionZ;
   final String fileName;
   public final ChunkStoreFileWriter fileWriter;
   private final Lock lock;
   private long lastAccessMs;
   private final Map chunkAccessTimes = Collections.synchronizedMap(new HashMap());
   final AtomicBoolean dirty = new AtomicBoolean();
   public final ChunkFileStore fileStore;

   public CustomBlockChunkStoreBase(File var1, World var2) {
      this.world = var2;
      this.lock = new ReentrantReadWriteLock().writeLock();
      this.fileName = var1.getName();
      String[] var3 = this.fileName.split("\\.");
      this.regionX = Integer.parseInt(var3[1]);
      this.regionZ = Integer.parseInt(var3[2]);
      this.regionId = chunkHash(var2, this.regionX, this.regionZ);

      try {
         this.lock.lock();
         this.fileWriter = new ChunkStoreFileWriter(var1.getParentFile());
         this.fileWriter.loadCache();
         this.fileStore = new ChunkFileStore(var1);
      } finally {
         this.lock.unlock();
      }

      this.markAccessed();
   }

   CustomBlockChunkStoreBase() {
      this.world = null;
      this.lock = new ReentrantReadWriteLock().writeLock();
      this.fileName = "dummy";
      this.regionX = 0;
      this.regionZ = 0;
      this.regionId = 0;
      this.fileWriter = null;
      this.fileStore = null;
   }

   @Nullable
   public SerialRecordC loadChunkRecord(int var1, int var2, boolean var3) {
      String var4 = chunkKey(var1, var2);
      this.markChunkAccessed(var4);
      SerialRecordC var5 = (SerialRecordC)this.fileStore.get(var4);
      if (var5 != null) {
         return var5;
      }

      if (!var3 && !this.fileStore.contains(var4)) {
         return null;
      }

      try {
         this.lock.lock();
         PersistentCounter var6 = new PersistentCounter();
         if (var3) {
            AtomicBoolean var7 = new AtomicBoolean(true);
            var5 = (SerialRecordC)this.fileStore.getOrCompute(var4, var4x -> {
               var7.set(false);
               if (Settings.get().logBlockSaveLoad) {
                  Log.log("Created chunk '" + var1 + "," + var2 + "' " + this.getDebugName());
               }

               return SerialRecordC.create();
            });
            if (var7.get() && Settings.get().logBlockSaveLoad) {
               long var8 = var6.lapFromStart();
               if (var8 >= Settings.get().logBlockSaveLoadMinMs) {
                  Log.log("Loaded chunk '" + var1 + "," + var2 + "' in " + var8 + "ms | " + this.getDebugName());
               }
            }
         } else {
            try {
               var5 = (SerialRecordC)this.fileStore.readEntry(var4);
            } catch (EOFException var14) {
               var5 = SerialRecordC.create();
               Log.error("Empty chunk will be used to avoid problems for chunk " + var4 + " | " + this.getDebugName());
            }

            if (var5 != null && Settings.get().logBlockSaveLoad) {
               long var18 = var6.lapFromStart();
               if (var18 >= Settings.get().logBlockSaveLoadMinMs) {
                  Log.log("Loaded chunk '" + var1 + "," + var2 + "' of '" + this.fileName + "' in " + var18 + "ms | " + this.getDebugName());
               }
            }
         }

         return var5;
      } catch (Throwable var15) {
         Log.error("Failed to load chunk '" + var1 + ", " + var2 + "' | " + this.getDebugName(), var15);
         Log.error("Empty chunk will be used to avoid problems for chunk " + var4 + " | " + this.getDebugName());
         return SerialRecordC.create();
      } finally {
         this.lock.unlock();
      }
   }

   @Nullable
   private SerialRecordC getChunkRecord(int var1, int var2) {
      return this.loadChunkRecord(var1, var2, false);
   }

   @Nullable
   private SerialRecordC getChunkRecordOf(Chunk var1) {
      return this.getChunkRecord(var1.getX(), var1.getZ());
   }

   @NotNull
   private SerialRecordC getOrCreateChunkRecord(int var1, int var2) {
      return this.loadChunkRecord(var1, var2, true);
   }

   @NotNull
   private SerialRecordC getOrCreateChunkRecordOf(Chunk var1) {
      return this.getOrCreateChunkRecord(var1.getX(), var1.getZ());
   }

   public boolean hasChunkRecord(int var1, int var2) {
      return this.getChunkRecord(var1, var2) != null;
   }

   public boolean hasChunkRecordOf(Chunk var1) {
      return this.hasChunkRecord(var1.getX(), var1.getZ());
   }

   @Nullable
   private SerialRecordB getBlockRecordInChunk(Chunk var1, int var2, int var3, int var4) {
      return this.getBlockRecord(var1.getX(), var1.getZ(), var2, var3, var4);
   }

   @Nullable
   private SerialRecordB getBlockRecord(int var1, int var2, int var3, int var4, int var5) {
      SerialRecordC var6 = this.getChunkRecord(var1, var2);
      return var6 == null ? null : var6.getBlockEntry(localBlockKey(var3, var4, var5));
   }

   @Nullable
   private SerialRecordB getLoadedBlockRecord(Location var1) {
      if (!var1.getWorld().isChunkLoaded(var1.getBlockX() >> 4, var1.getBlockZ() >> 4)) {
         throw new StoreException("Chunk is not loaded!");
      } else {
         return this.getBlockRecord(var1.getBlockX() >> 4, var1.getBlockZ() >> 4, var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
      }
   }

   @Nullable
   private SerialRecordB getBlockRecordAt(Location var1) {
      return this.getBlockRecord(var1.getBlockX() >> 4, var1.getBlockZ() >> 4, var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   @Nullable
   private SerialRecordB getBlockRecordOf(Block var1) {
      return this.getBlockRecord(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   @NotNull
   private SerialRecordB getOrCreateBlockRecordInChunk(Chunk var1, int var2, int var3, int var4) {
      return this.getOrCreateBlockRecord(var1.getX(), var1.getZ(), var2, var3, var4);
   }

   @NotNull
   private SerialRecordB getOrCreateBlockRecord(int var1, int var2, int var3, int var4, int var5) {
      String var6 = localBlockKey(var3, var4, var5);
      SerialRecordC var7 = this.getOrCreateChunkRecord(var1, var2);
      SerialRecordB var8 = var7.getBlockEntry(var6);
      if (var8 != null) {
         return var8;
      }

      var8 = SerialRecordB.create().withIntValue(0);
      this.dirty.set(true);
      var7.putBlock(var6, var8);
      return var8;
   }

   @NotNull
   private SerialRecordB getOrCreateBlockRecordOf(Block var1) {
      return this.getOrCreateBlockRecord(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   public boolean hasBlockEntryInChunk(Chunk var1, int var2, int var3, int var4) {
      return this.hasBlockEntry(var1.getX(), var1.getZ(), var2, var3, var4);
   }

   public boolean hasBlockEntry(int var1, int var2, int var3, int var4, int var5) {
      SerialRecordC var6 = this.getChunkRecord(var1, var2);
      return var6 == null ? false : var6.hasBlockKey(localBlockKey(var3, var4, var5));
   }

   public boolean hasBlockEntryAt(Block var1) {
      return this.hasBlockEntry(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   @Nullable
   private SerialRecordA getBlockAttributes(Block var1) {
      SerialRecordB var2 = this.getBlockRecordOf(var1);
      return var2 == null ? null : var2.getAttributes();
   }

   private SerialRecordA getOrCreateBlockAttributes(Block var1) {
      return this.getOrCreateBlockAttributesAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   private SerialRecordA getOrCreateBlockAttributesAt(int var1, int var2, int var3, int var4, int var5) {
      SerialRecordB var6 = this.getOrCreateBlockRecord(var1, var2, var3, var4, var5);
      SerialRecordA var7 = var6.getAttributes();
      if (var7 != null) {
         return var7;
      }

      var7 = SerialRecordA.create();
      this.dirty.set(true);
      var6.withAttributes(var7);
      return var7;
   }

   @Nullable
   public List getStoredItems(Block var1) {
      ArrayList var2 = new ArrayList();
      SerialRecordF var3 = this.getItemRecord(var1);
      if (var3 == null) {
         return null;
      }

      if (var3.getItems() != null && var3.getItems().size() > 0) {
         for (String var5 : var3.getItems()) {
            var2.add(NItem.compoundStrToBukkitItem(var5));
         }
      }

      return var2;
   }

   @Nullable
   public ItemStack getStoredItem(Block var1) {
      SerialRecordF var2 = this.getItemRecord(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.getItems() != null && var2.getItems().size() > 0 ? NItem.compoundStrToBukkitItem((String)var2.getItems().get(0)) : null;
      }
   }

   @Nullable
   SerialRecordF getItemRecord(Block var1) {
      SerialRecordB var2 = this.getBlockRecordOf(var1);
      if (var2 == null) {
         return null;
      }

      SerialRecordA var3 = this.getBlockAttributes(var1);
      return var3 == null ? null : var3.getInventory();
   }

   public boolean hasStoredItems(Block var1) {
      SerialRecordB var2 = this.getBlockRecordOf(var1);
      if (var2 == null) {
         return false;
      }

      SerialRecordA var3 = this.getBlockAttributes(var1);
      return var3 == null ? false : var3.hasInventory();
   }

   public boolean isCustomBlock(Block block) {
      return this.isCustomBlockAt(block.getLocation());
   }

   public boolean isCustomBlockLoaded(Location var1) {
      if (this.getVanillaCustomBlockId(var1) != null) {
         return true;
      }

      SerialRecordB var2 = this.getLoadedBlockRecord(var1);
      return var2 == null ? false : var2.getIntValue() > 0;
   }

   public boolean isCustomBlockAt(Location var1) {
      if (this.getVanillaCustomBlockId(var1) != null) {
         return true;
      }

      SerialRecordB var2 = this.getBlockRecordAt(var1);
      return var2 == null ? false : var2.getIntValue() > 0;
   }

   @Nullable
   public String getVanillaCustomBlockId(Location var1) {
      if (Main.getInstance().aEO) {
         String var2 = BlockEffectsService.getAdapter().getMinecraftId(var1.getBlock());
         if (ContentManager.method8084().am.containsKey(var2)) {
            return var2;
         }
      }

      return null;
   }

   @Nullable
   public String getCustomBlockIdAt(int var1, int var2, int var3, int var4, int var5) {
      SerialRecordB var6 = this.getBlockRecord(var4, var5, var1, var2, var3);
      if (var6 == null) {
         return null;
      }

      int var7 = var6.getIntValue();
      return var7 == 0 ? null : this.lookupBlockPalette(this.getChunkRecord(var4, var5), var7);
   }

   @Nullable
   public String getCustomBlockIdInChunk(int var1, int var2, int var3, Chunk var4) {
      return this.getCustomBlockIdAt(var1, var2, var3, var4.getX(), var4.getZ());
   }

   @Nullable
   public String getCustomBlockId(Block var1) {
      String var2 = this.getVanillaCustomBlockId(var1.getLocation());
      return var2 != null ? var2 : this.getCustomBlockIdAt(var1.getX(), var1.getY(), var1.getZ(), var1.getX() >> 4, var1.getZ() >> 4);
   }

   @Nullable
   public String getCustomBlockIdAtLocation(Location var1) {
      String var2 = this.getVanillaCustomBlockId(var1);
      return var2 != null ? var2 : this.getCustomBlockIdAt(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var1.getBlockX() >> 4, var1.getBlockZ() >> 4);
   }

   @Nullable
   private String lookupPaletteEntry(@NotNull SerialRecordC var1, StoreCategory var2, int var3) {
      SerialRecordG var4;
      if (var2 == StoreCategory.BLOCKS) {
         var4 = var1.getBlocksPalette();
      } else if (var2 == StoreCategory.BIOMES) {
         var4 = var1.getBiomesPalette();
      } else {
         if (var2 != StoreCategory.CROPS_SEEDS) {
            throw new RuntimeException("Unknown palette! " + var2);
         }

         var4 = var1.getCropsSeedsPalette();
      }

      return var4 != null ? var4.getDataValue(var3) : null;
   }

   @Nullable
   private String lookupBlockPalette(@NotNull SerialRecordC var1, int var2) {
      return this.lookupPaletteEntry(var1, StoreCategory.BLOCKS, var2);
   }

   private int getOrAddBlockPaletteId(SerialRecordC var1, String var2) {
      return this.getOrAddPaletteId(var1, StoreCategory.BLOCKS, var2);
   }

   private int getOrAddPaletteId(SerialRecordC var1, StoreCategory var2, String var3) {
      SerialRecordG var5;
      if (var2 == StoreCategory.BLOCKS) {
         var5 = var1.getBlocksPalette();
      } else if (var2 == StoreCategory.BIOMES) {
         var5 = var1.getBiomesPalette();
      } else {
         if (var2 != StoreCategory.CROPS_SEEDS) {
            throw new RuntimeException("Unknown palette! " + var2);
         }

         var5 = var1.getCropsSeedsPalette();
      }

      Integer var4;
      if (var5 == null) {
         var4 = 1;
         var5 = SerialRecordG.create();
         this.addPaletteEntry(var2, var5, var4, var3, var1);
      } else {
         var4 = (Integer)CollectionUtils.findKeyByValue(var5.getData(), var3);

         for (Entry var7 : var5.getData().entrySet()) {
            if (((String)var7.getValue()).equals(var3)) {
               var4 = (Integer)var7.getKey();
               break;
            }
         }

         if (var4 == null) {
            var4 = Math.max(1, var5.dataSize() + 1);
            this.addPaletteEntry(var2, var5, var4, var3, null);
         }
      }

      return var4;
   }

   private void addPaletteEntry(StoreCategory var1, SerialRecordG var2, Integer var3, String var4, @Nullable SerialRecordC var5) {
      this.dirty.set(true);
      var2.putEntry(var3, var4);
      if (var5 != null) {
         if (var1 == StoreCategory.BLOCKS) {
            var5.withBlocksPalette(var2);
         } else if (var1 == StoreCategory.BIOMES) {
            var5.withBiomesPalette(var2);
         } else {
            if (var1 != StoreCategory.CROPS_SEEDS) {
               throw new RuntimeException("Unknown palette! " + var1);
            }

            var5.withCropsSeedsPalette(var2);
         }
      }
   }

   @Nullable
   public String getBlockStringData(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      return var2 == null ? null : var2.getSong();
   }

   public void setBlockStringData(Block var1, String var2) {
      this.dirty.set(true);
      SerialRecordA var3 = this.getOrCreateBlockAttributes(var1);
      var3.withSong(var2);
   }

   public void setBlockStringDataAt(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.dirty.set(true);
      SerialRecordA var7 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var7.withSong(var6);
   }

   @Nullable
   public String getCropSeedId(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      if (var2 == null) {
         return null;
      }

      int var3 = var2.getCropSeedId();
      if (var3 == 0) {
         return null;
      }

      SerialRecordC var4 = this.getChunkRecord(var1.getX() >> 4, var1.getZ() >> 4);
      return var4 == null ? null : this.lookupPaletteEntry(var4, StoreCategory.CROPS_SEEDS, var3);
   }

   public void setCropSeedId(Block var1, String var2) {
      this.dirty.set(true);
      SerialRecordC var3 = this.getOrCreateChunkRecord(var1.getX() >> 4, var1.getZ() >> 4);
      int var4 = this.getOrAddPaletteId(var3, StoreCategory.CROPS_SEEDS, var2);
      SerialRecordA var5 = this.getOrCreateBlockAttributes(var1);
      var5.withCropSeedId(var4);
   }

   public void setCropSeedIdAt(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.dirty.set(true);
      SerialRecordC var7 = this.getOrCreateChunkRecord(var1, var2);
      int var8 = this.getOrAddPaletteId(var7, StoreCategory.CROPS_SEEDS, var6);
      SerialRecordA var9 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var9.withCropSeedId(var8);
   }

   @Nullable
   public String getCustomBlockIdIfFlagged(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.isLiquid() ? this.getCustomBlockId(var1) : null;
      }
   }

   public void setCustomBlockIdAndFlag(Block var1, String var2) {
      this.setCustomBlock(var1, var2);
      this.getOrCreateBlockAttributes(var1).withIsLiquid(true);
   }

   public List getFlaggedBlockIds(Chunk var1) {
      ArrayList var2 = new ArrayList();
      if (!this.hasChunkRecordOf(var1)) {
         return var2;
      }

      SerialRecordC var3 = this.getChunkRecordOf(var1);
      if (var3 == null) {
         return var2;
      }

      for (Entry var5 : var3.getBlock().entrySet()) {
         SerialRecordA var6 = ((SerialRecordB)var5.getValue()).getAttributes();
         if (var6 != null && var6.isLiquid()) {
            SerialRecordB var7 = (SerialRecordB)var5.getValue();
            int var8 = var7.getIntValue();
            if (var8 != 0) {
               var2.add(this.lookupBlockPalette(var3, var8));
            }
         }
      }

      return var2;
   }

   @Nullable
   public List getAllBlocksLocationsList(Chunk chunk) {
      if (!this.hasChunkRecordOf(chunk)) {
         return null;
      }

      SerialRecordC var2 = this.getChunkRecordOf(chunk);
      if (var2 == null) {
         return null;
      }

      ArrayList var3 = new ArrayList();

      for (Entry var5 : var2.getBlock().entrySet()) {
         SerialRecordB var6 = (SerialRecordB)var5.getValue();
         int var7 = var6.getIntValue();
         if (var7 != 0) {
            var3.add(locationFromLocalKey(this.world, chunk, (String)var5.getKey()));
         }
      }

      return var3;
   }

   @Nullable
   public Map getAllBlocksLocations(Chunk chunk) {
      if (!this.hasChunkRecordOf(chunk)) {
         return null;
      }

      SerialRecordC var2 = this.getChunkRecordOf(chunk);
      if (var2 == null) {
         return null;
      }

      HashMap var3 = new HashMap();

      for (Entry var5 : var2.getBlock().entrySet()) {
         SerialRecordB var6 = (SerialRecordB)var5.getValue();
         int var7 = var6.getIntValue();
         if (var7 != 0) {
            var3.put(locationFromLocalKey(this.world, chunk, (String)var5.getKey()), this.lookupBlockPalette(var2, var7));
         }
      }

      return var3;
   }

   public void forEachBlockInChunk(Chunk var1, BiConsumer var2) {
      if (this.hasChunkRecordOf(var1)) {
         SerialRecordC var3 = this.getChunkRecordOf(var1);
         if (var3 != null) {
            for (Entry var5 : var3.getBlock().entrySet()) {
               SerialRecordB var6 = (SerialRecordB)var5.getValue();
               int var7 = var6.getIntValue();
               if (var7 != 0) {
                  var2.accept(this.lookupBlockPalette(var3, var7), locationFromLocalKey(this.world, var1, (String)var5.getKey()));
               }
            }
         }
      }
   }

   public boolean hasNeighborRegionData(Chunk var1) {
      int var2 = var1.getX();
      int var3 = var1.getZ();
      if (var2 >= 0 && var3 >= 0) {
         return false;
      }

      for (int var4 = 0; var4 <= (var2 < 0 ? 1 : 0); var4++) {
         for (int var5 = 0; var5 <= (var3 < 0 ? 1 : 0); var5++) {
            if (var4 != 0 || var5 != 0) {
               String var6 = chunkKey(var2 + var4, var3 + var5);
               if (this.fileStore.get(var6) != null || this.fileStore.contains(var6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void refreshNeighborBlocks(Chunk var1) {
      if (this.world.isChunkLoaded(var1.getX(), var1.getZ()) && this.hasNeighborRegionData(var1)) {
         int var2 = var1.getX();
         int var3 = var1.getZ();

         for (int var4 = 0; var4 <= (var2 < 0 ? 1 : 0); var4++) {
            for (int var5 = 0; var5 <= (var3 < 0 ? 1 : 0); var5++) {
               if (var4 != 0 || var5 != 0) {
                  int var6 = var2 + var4;
                  int var7 = var3 + var5;
                  SerialRecordC var8 = this.getChunkRecord(var6, var7);
                  if (var8 != null && var8.getBlock() != null) {
                     for (String var10 : var8.getBlock().keySet()) {
                        int var11 = var10.indexOf(46);
                        int var12 = var10.indexOf(46, var11 + 1);
                        if (var11 >= 0 && var12 >= 0) {
                           int var13;
                           int var14;
                           int var15;
                           try {
                              var13 = Integer.parseInt(var10, 0, var11, 10);
                              var14 = Integer.parseInt(var10, var11 + 1, var12, 10);
                              var15 = Integer.parseInt(var10, var12 + 1, var10.length(), 10);
                           } catch (NumberFormatException var18) {
                              continue;
                           }

                           if (var13 >= 0
                              && var13 <= 15
                              && var15 >= 0
                              && var15 <= 15
                              && var14 >= this.world.getMinHeight()
                              && var14 < this.world.getMaxHeight()) {
                              int var16 = (var2 << 4) + var13;
                              int var17 = (var3 << 4) + var15;
                              if (var16 / 16 == var6 && var17 / 16 == var7) {
                                 this.relocateBlockRecord(this.world.getBlockAt(var16, var14, var17));
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean relocateBlockRecord(Block var1) {
      int var2 = var1.getX();
      int var3 = var1.getZ();
      int var4 = var2 / 16;
      int var5 = var3 / 16;
      int var6 = var2 >> 4;
      int var7 = var3 >> 4;
      if (var4 == var6 && var5 == var7) {
         return false;
      }

      if (this.world.isChunkLoaded(var6, var7)
         && var6 >> 5 == this.regionX
         && var7 >> 5 == this.regionZ
         && this.getBlockRecord(var6, var7, var2, var1.getY(), var3) == null) {
         SerialRecordC var8 = this.getChunkRecord(var4, var5);
         if (var8 == null) {
            return false;
         }

         String var9 = localBlockKey(var2, var1.getY(), var3);
         SerialRecordB var10 = var8.getBlockEntry(var9);
         if (var10 != null && var10.getIntValue() != 0) {
            String var11 = this.lookupBlockPalette(var8, var10.getIntValue());
            CustomBlockDefinition var12 = var11 == null ? null : ContentManager.method8084().method8160(var11);
            if (var12 != null && var12.anF != null) {
               Material var13 = var12.anF.getMaterial();
               if (var1.getType() != var13) {
                  return false;
               }

               CustomBlockDefinition var14 = ContentManager.method8166(var1.getBlockData());
               if (var14 != null && !var11.equals(var14.getNamespacedID())) {
                  return false;
               }

               int var15 = var4 * 16 + (var2 & 15);
               int var16 = var5 * 16 + (var3 & 15);
               if (this.world.isChunkLoaded(var4, var5)
                  && var12.anF.getBlockData() != null
                  && var12.anF.getBlockData().equals(this.world.getBlockAt(var15, var1.getY(), var16).getBlockData())) {
                  return false;
               }

               int var17 = 0;

               for (int var21 : new int[]{var15, var15 - 16}) {
                  for (int var25 : new int[]{var16, var16 - 16}) {
                     if ((var21 == var15 || var2 < 0 && (var2 & 15) != 0) && (var25 == var16 || var3 < 0 && (var3 & 15) != 0)) {
                        if (!this.world.isChunkLoaded(var21 >> 4, var25 >> 4)) {
                           return false;
                        }

                        if (this.world.getBlockAt(var21, var1.getY(), var25).getType() == var13) {
                           var17++;
                        }
                     }
                  }
               }

               if (var17 != 1) {
                  return false;
               }

               this.lock.lock();

               try {
                  if (var8.getBlockEntry(var9) != var10) {
                     return false;
                  }

                  SerialRecordC var30 = this.getOrCreateChunkRecord(var6, var7);
                  if (var30.hasBlockKey(var9)) {
                     return false;
                  }

                  int var31 = this.getOrAddBlockPaletteId(var30, var11);
                  SerialRecordA var32 = var10.getAttributes();
                  String var33 = var32 != null && var32.hasCropSeedId()
                     ? this.lookupPaletteEntry(var8, StoreCategory.CROPS_SEEDS, var32.getCropSeedId())
                     : null;
                  String var34 = var32 != null && var32.hasRealBiomeId() ? this.lookupBlockPalette(var8, var32.getRealBiomeId()) : null;
                  if (var32 != null && (var32.hasCropSeedId() && var33 == null || var32.hasRealBiomeId() && var34 == null)) {
                     return false;
                  }

                  int var35 = var33 == null ? 0 : this.getOrAddPaletteId(var30, StoreCategory.CROPS_SEEDS, var33);
                  int var36 = var34 == null ? 0 : this.getOrAddBlockPaletteId(var30, var34);
                  SerialRecordB var37 = SerialRecordB.create().withIntValue(var31);
                  if (var32 != null) {
                     SerialRecordA var26 = SerialRecordA.create();
                     if (var32.hasInventory()) {
                        var26.withInventory(var32.getInventory());
                     }

                     if (var32.hasCropSeedId()) {
                        var26.withCropSeedId(var35);
                     }

                     if (var32.hasIsLiquid()) {
                        var26.withIsLiquid(var32.isLiquid());
                     }

                     if (var32.hasRealBiomeId()) {
                        var26.withRealBiomeId(var36);
                     }

                     if (var32.hasSong()) {
                        var26.withSong(var32.getSong());
                     }

                     if (var32.hasInstrument()) {
                        var26.withInstrument(var32.getInstrument());
                     }

                     if (var32.hasFireAge()) {
                        var26.withFireAge(var32.getFireAge());
                     }

                     if (var32.hasIsOtherPluginCustomBlock()) {
                        var26.withIsOtherPluginCustomBlock(var32.isOtherPluginCustomBlock());
                     }

                     var37.withAttributes(var26);
                  }

                  var30.putBlock(var9, var37);
                  if (!var8.removeBlockEntryIf(var9, var10)) {
                     var30.removeBlockEntryIf(var9, var37);
                     return false;
                  }

                  this.dirty.set(true);
               } finally {
                  this.lock.unlock();
               }

               if (var4 >> 5 == this.regionX && var5 >> 5 == this.regionZ && this.world.isChunkLoaded(var4, var5)) {
                  ContentManager.method8084().ap.clearBlockState(this.world, var4, var5, var15, var1.getY(), var16);
               }

               ContentManager.method8084().ap.setBlockState(this.world, var6, var7, var2, var1.getY(), var3, var11);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean relocateMatchingNeighbor(Block var1, Material var2, BlockData var3) {
      if (var3 != null && var3.equals(var1.getBlockData())) {
         return false;
      }

      int var4 = var1.getX() >> 4;
      int var5 = var1.getZ() >> 4;

      for (int var6 = 0; var6 <= (var4 <= 0 && (var1.getX() & 15) != 0 ? 1 : 0); var6++) {
         for (int var7 = 0; var7 <= (var5 <= 0 && (var1.getZ() & 15) != 0 ? 1 : 0); var7++) {
            if (var6 != 0 || var7 != 0) {
               int var8 = var1.getX() - var6 * 16;
               int var9 = var1.getZ() - var7 * 16;
               if (var8 >> 9 == this.regionX && var9 >> 9 == this.regionZ) {
                  if (!this.world.isChunkLoaded(var8 >> 4, var9 >> 4)) {
                     return true;
                  }

                  Block var10 = this.world.getBlockAt(var8, var1.getY(), var9);
                  if (var10.getType() == var2) {
                     this.relocateBlockRecord(var10);
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   @Nullable
   public String getSecondaryBlockId(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      if (var2 == null) {
         return null;
      }

      int var3 = var2.getRealBiomeId();
      if (var3 == 0) {
         return null;
      }

      SerialRecordC var4 = this.getChunkRecord(var1.getX() >> 4, var1.getZ() >> 4);
      return var4 == null ? null : this.lookupBlockPalette(var4, var3);
   }

   public void setSecondaryBlockId(Block var1, String var2) {
      this.setSecondaryBlockIdAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ(), var2);
   }

   public void setSecondaryBlockIdAt(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.dirty.set(true);
      SerialRecordC var7 = this.getOrCreateChunkRecord(var1, var2);
      int var8 = this.getOrAddBlockPaletteId(var7, var6);
      SerialRecordA var9 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var9.withRealBiomeId(var8);
   }

   @Nullable
   public Byte getStoredByte(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      return var2 == null ? null : (byte)var2.getInstrument();
   }

   public byte getStoredByteOrDefault(Block var1, byte var2) {
      SerialRecordA var3 = this.getBlockAttributes(var1);
      return var3 == null ? var2 : (byte)var3.getInstrument();
   }

   public void setStoredByte(Block var1, int var2) {
      this.dirty.set(true);
      SerialRecordA var3 = this.getOrCreateBlockAttributes(var1);
      var3.withInstrument(var2);
   }

   public void setStoredByteAt(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.dirty.set(true);
      SerialRecordA var7 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var7.withInstrument(var6);
   }

   @Nullable
   public Integer getStoredInt(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      return var2 == null ? null : var2.getFireAge();
   }

   public void setStoredInt(Block var1, int var2) {
      this.dirty.set(true);
      SerialRecordA var3 = this.getOrCreateBlockAttributes(var1);
      var3.withFireAge(var2);
   }

   public void setStoredIntAt(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.dirty.set(true);
      SerialRecordA var7 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var7.withFireAge(var6);
   }

   public boolean isStoredFlagSet(Block var1) {
      SerialRecordA var2 = this.getBlockAttributes(var1);
      return var2 == null ? false : var2.isOtherPluginCustomBlock();
   }

   public void setStoredFlag(Block var1, boolean var2) {
      this.setStoredFlagAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ(), var2);
   }

   public void setStoredFlagAt(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.dirty.set(true);
      SerialRecordA var7 = this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5);
      var7.withIsOtherPluginCustomBlock(var6);
   }

   public void setCustomBlockAt(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.dirty.set(true);
      SerialRecordC var7 = this.getOrCreateChunkRecord(var1, var2);
      int var8 = this.getOrAddBlockPaletteId(var7, var6);
      SerialRecordB var9 = SerialRecordB.create().withIntValue(var8);
      var7.putBlock(localBlockKey(var3, var4, var5), var9);
      ContentManager.method8084().ap.setBlockState(this.world, var1, var2, var3, var4, var5, var6);
   }

   public void setCustomBlockInChunk(Chunk var1, int var2, int var3, int var4, String var5) {
      this.setCustomBlockAt(var1.getX(), var1.getZ(), var2, var3, var4, var5);
   }

   public void setCustomBlock(Block var1, String var2) {
      this.setCustomBlockAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ(), var2);
   }

   public void setCustomBlockAtLocation(Location var1, String var2) {
      this.setCustomBlockAt(var1.getBlockX() >> 4, var1.getBlockZ() >> 4, var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var2);
   }

   public void removeCustomBlockInChunk(Chunk var1, int var2, int var3, int var4) {
      this.removeCustomBlockAt(var1.getX(), var1.getZ(), var2, var3, var4);
   }

   public void removeCustomBlockAt(int var1, int var2, int var3, int var4, int var5) {
      SerialRecordC var6 = this.getChunkRecord(var1, var2);
      if (var6 != null) {
         this.dirty.set(true);
         var6.removeBlockEntry(localBlockKey(var3, var4, var5));
         ContentManager.method8084().ap.clearBlockState(this.world, var1, var2, var3, var4, var5);
      }
   }

   public void removeCustomBlock(Block var1) {
      this.removeCustomBlockAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   public void removeCustomBlockAndRefresh(Block var1, boolean var2) {
      this.removeCustomBlockAndRefreshNms(var1, var2, false);
   }

   public void removeCustomBlockAndRefreshNms(Block var1, boolean var2, boolean var3) {
      if (var2) {
         NmsBlockSetterService.OD().method3404(var1, var3);
      }

      this.removeCustomBlockAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ());
   }

   public void clearChunk(Chunk var1) {
      SerialRecordC var2 = this.getChunkRecordOf(var1);
      if (var2 != null) {
         this.dirty.set(true);
         var2.getBlock().clear();
         ContentManager.method8084().ap.unloadChunk(this.world, var1.getX(), var1.getZ());
      }
   }

   public void setStoredItems(Block var1, List var2) {
      this.dirty.set(true);
      SerialRecordF var3 = SerialRecordF.create();
      var3.withItems(new ArrayList());

      for (int var4 = 0; var4 < var2.size(); var4++) {
         var3.getItems().add(NItem.bukkitItemToCompoundStr((ItemStack)var2.get(var4)));
      }

      this.getOrCreateBlockAttributes(var1).withInventory(var3);
   }

   public void setStoredItem(Block var1, ItemStack var2) {
      this.setStoredItemAt(var1.getX() >> 4, var1.getZ() >> 4, var1.getX(), var1.getY(), var1.getZ(), var2);
   }

   public void setStoredItemAt(int var1, int var2, int var3, int var4, int var5, ItemStack var6) {
      this.dirty.set(true);
      SerialRecordF var7 = SerialRecordF.create();
      var7.withItems(new ArrayList());
      var7.getItems().add(NItem.bukkitItemToCompoundStr(var6));
      this.getOrCreateBlockAttributesAt(var1, var2, var3, var4, var5).withInventory(var7);
   }

   public void resetBlockAttributes(Block var1) {
      SerialRecordB var2 = this.getBlockRecordOf(var1);
      if (var2 != null) {
         SerialRecordA var3 = var2.getAttributes();
         if (var3 != null) {
            this.dirty.set(true);
            var3.clearInventory();
         }
      }
   }

   public static String chunkKeyOf(Chunk var0) {
      return chunkKey(var0.getX(), var0.getZ());
   }

   public static String chunkKey(int var0, int var1) {
      return var0 + "." + var1;
   }

   public static String chunkKeyOfBlock(int var0, int var1) {
      return (var0 >> 4) + "." + (var1 >> 4);
   }

   private static String localBlockKey(int var0, int var1, int var2) {
      return (var0 & 15) + "." + var1 + "." + (var2 & 15);
   }

   private static Location locationFromLocalKey(World var0, Chunk var1, String var2) {
      int var3 = var2.indexOf(46);
      int var4 = var2.lastIndexOf(46);
      int var5 = Integer.parseInt(var2.substring(0, var3));
      int var6 = Integer.parseInt(var2.substring(var3 + 1, var4));
      int var7 = Integer.parseInt(var2.substring(var4 + 1));
      return new Location(var0, var1.getX() * 16 + var5, var6, var1.getZ() * 16 + var7);
   }

   public static int chunkHash(World var0, int var1, int var2) {
      int var3 = 1;
      var3 = 31 * var3 + var0.hashCode();
      var3 = 31 * var3 + var1;
      return 31 * var3 + var2;
   }

   public static int regionHash(World var0, int var1, int var2) {
      int var3 = 1;
      var3 = 31 * var3 + var0.hashCode();
      var3 = 31 * var3 + (var1 >> 5);
      return 31 * var3 + (var2 >> 5);
   }

   public static int regionHashOfChunk(Chunk var0) {
      return regionHash(var0.getWorld(), var0.getX(), var0.getZ());
   }

   public void save() {
      this.saveRegion(true);
   }

   public void saveRegion(boolean var1) {
      try {
         this.lock.lock();
         if (this.fileWriter != null) {
            this.fileWriter.saveCache();
         }

         if (this.fileStore.entries.isEmpty()) {
            return;
         }

         if (!this.dirty.get()) {
            this.fileStore.entries.clear();
            return;
         }

         PersistentCounter var2 = new PersistentCounter();
         if (Settings.get().logBlockSaveLoad && Settings.get().logBlockSaveLoadMinMs == 0) {
            Log.log("Saving region " + this.getDebugName());
         }

         this.fileStore.saveAll(var1, var1);
         this.dirty.set(false);
         if (Settings.get().logBlockSaveLoad) {
            long var3 = var2.lapFromStart();
            if (var3 >= Settings.get().logBlockSaveLoadMinMs) {
               Log.log("Saved " + this.fileStore.entries.size() + " chunks of " + this.fileName + " region in " + var3 + "ms | " + this.getDebugName());
            }
         }
      } catch (IOException var8) {
         this.dirty.set(true);
         throw new UncheckedIOException("Failed to save region '" + this.regionX + "," + this.regionZ + "' | " + this.getDebugName(), var8);
      } finally {
         if (var1) {
            this.closeFiles();
         }

         this.lock.unlock();
      }
   }

   public int unloadExpiredChunks() {
      int var1 = 0;
      boolean var2 = false;
      Iterator var3 = this.fileStore.entries.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         Object var5 = (String)var4.getKey();
         if (this.isChunkExpired((String)var5)) {
            var1++;
            if (!var2) {
               if (Settings.get().logBlockSaveLoad) {
                  Log.log(ChatColor.AQUA + var5 + " is expired, so I save the whole region " + this.getDebugName());
               }

               this.saveRegion(false);
               var2 = true;
            }

            var3.remove();
            this.unloadChunkKey((String)var5);
            if (Settings.get().logBlockSaveLoad) {
               Log.log(ChatColor.GREEN + "---- " + var5 + " is expired, removed it from cache " + this.getDebugName());
            }
         }
      }

      return var1;
   }

   public SaveStatus handleAutosave() {
      if (this.isIdle()) {
         this.saveRegion(true);
         if (Settings.get().logBlockSaveLoad) {
            Log.log("Unloaded and saved region " + this.getDebugName());
         }

         this.closeFiles();
         return SaveStatus.SAVED;
      } else {
         int var1 = this.unloadExpiredChunks();
         if (var1 > 0 && Settings.get().logBlockSaveLoad) {
            Log.log("Saved region (" + var1 + " chunks) " + this.getDebugName());
         }

         return SaveStatus.SAVED_CHUNK;
      }
   }

   private int[] parseChunkKey(String var1) {
      String[] var2 = var1.split("\\.");
      return new int[]{Integer.parseInt(var2[0]), Integer.parseInt(var2[1])};
   }

   public void unloadChunk(Chunk var1) {
      this.unloadChunkKey(chunkKeyOf(var1));
   }

   public void unloadChunkKey(String var1) {
      this.fileStore.remove(var1);
   }

   public String getDebugName() {
      return this.world.getName() + " -> " + this.fileStore.storageFile.getAbsolutePath().replace(this.world.getWorldFolder().getAbsolutePath(), "");
   }

   public int getLoadedChunkCount() {
      return this.fileStore.entries.size();
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getFileSizeText() {
      return FileUtils.byteCountToDisplaySize(this.fileStore.storageFile.length());
   }

   public void closeFiles() {
      this.fileStore.close();
   }

   public boolean isIdle() {
      return System.currentTimeMillis() - this.lastAccessMs > 60000L;
   }

   void markAccessed() {
      this.lastAccessMs = System.currentTimeMillis();
   }

   public boolean isChunkExpired(String var1) {
      Long var2 = (Long)this.chunkAccessTimes.get(var1);
      if (var2 == null) {
         return false;
      }

      if (System.currentTimeMillis() - var2 < 60000L) {
         return false;
      }

      int[] var3 = this.parseChunkKey(var1);
      return !this.world.isChunkLoaded(var3[0], var3[1]);
   }

   void markChunkAccessed(String var1) {
      this.markAccessed();
      this.chunkAccessTimes.put(var1, System.currentTimeMillis());
   }
}
