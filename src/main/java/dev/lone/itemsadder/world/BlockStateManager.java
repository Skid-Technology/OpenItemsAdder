package dev.lone.itemsadder.world;

import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public final class BlockStateManager {
   private static final int HAS_ENTITY_FLAG = Integer.MIN_VALUE;
   static final int[] FACE_DX = new int[]{0, 0, 0, 0, -1, 1};
   static final int[] FACE_DY = new int[]{-1, 1, 0, 0, 0, 0};
   static final int[] FACE_DZ = new int[]{0, 0, -1, 1, 0, 0};
   private final Map sectionsByWorld = new HashMap();

   public void rebuildFromStore(ChunkDataStore var1) {
      this.clear();
      if (Settings.get().customBlocksEntity && ContentManager.method8084().ao.wi() != 0) {
         for (World var3 : Bukkit.getWorlds()) {
            for (Chunk var7 : var3.getLoadedChunks()) {
               CustomBlockChunkStoreBase var8 = var1.lookupRegionForChunk(var7);
               if (var8 != null) {
                  this.loadChunkStates(var7, var8);
               }
            }
         }
      }
   }

   public void loadChunkStates(Chunk var1, CustomBlockChunkStoreBase var2) {
      if (Settings.get().customBlocksEntity && ContentManager.method8084().ao.wi() != 0) {
         HashMap var3 = new HashMap();
         var2.forEachBlockInChunk(
            var1,
            (var1x, var2x) -> {
               int var3x = resolveStateId(var1x);
               if (var3x != -1) {
                  var3.computeIfAbsent(var2x.getBlockY() >> 4, var0x -> new BlockLayerBuilder())
                     .add(localBlockIndex(var2x.getBlockX(), var2x.getBlockY(), var2x.getBlockZ()), var3x);
               }
            }
         );
         synchronized (this) {
            UUID var5 = var1.getWorld().getUID();
            long var6 = sectionKey(var1.getX(), var1.getZ());
            if (var3.isEmpty()) {
               Long2ObjectOpenHashMap var8 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var5);
               if (var8 == null) {
                  return;
               }

               var8.remove(var6);
               this.removeIfEmpty(var5, var8);
            } else {
               var3.forEach((var1x, var2x) -> var2x.resolveStateIds(var1.getWorld(), var1.getX(), var1x, var1.getZ()));
               Long2ObjectOpenHashMap var11 = this.sectionsByWorld.computeIfAbsent(var5, var0 -> new Long2ObjectOpenHashMap());
               var11.put(var6, BlockSectionData.fromLayerBuilders(var3));
            }
         }

         ContentManager.method8084().aq.markChunkDirty(var1.getWorld().getUID(), var1.getX(), var1.getZ());
      }
   }

   public void setBlockState(World var1, int var2, int var3, int var4, int var5, int var6, String var7) {
      if (var1.isChunkLoaded(var2, var3)) {
         int var8 = resolveStateId(var7);
         this.setSectionBlockState(var1.getUID(), var2, var3, var5 >> 4, localBlockIndex(var4, var5, var6), var8, (byte)0);
         byte var9 = this.updateBlockAndNeighbors(var1, var4, var5, var6, var8 != -1 ? Boolean.TRUE : null);
         ContentManager.method8084().aq.sendBlockUpdate(var1, var4, var5, var6, var8 == -1 ? -1 : stripEntityFlag(var8));
         if (var8 != -1) {
            ContentManager.method8084().aq.onBlockPlaced(var1.getUID(), var4, var5, var6, stripEntityFlag(var8), var9);
         }

         ContentManager.method8084().aq.markRegionDirty(var1.getUID(), var4, var6);
      }
   }

   public void clearBlockState(World var1, int var2, int var3, int var4, int var5, int var6) {
      this.setSectionBlockState(var1.getUID(), var2, var3, var5 >> 4, localBlockIndex(var4, var5, var6), -1, (byte)0);
      this.updateBlockAndNeighbors(var1, var4, var5, var6, Boolean.FALSE);
      ContentManager.method8084().aq.sendBlockUpdate(var1, var4, var5, var6, -1);
      ContentManager.method8084().aq.markRegionDirty(var1.getUID(), var4, var6);
   }

   public synchronized void unloadChunk(World var1, int var2, int var3) {
      Long2ObjectOpenHashMap var4 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1.getUID());
      if (var4 != null) {
         var4.remove(sectionKey(var2, var3));
         this.removeIfEmpty(var1.getUID(), var4);
         ContentManager.method8084().aq.markChunkDirty(var1.getUID(), var2, var3);
      }
   }

   public synchronized void unloadWorld(World var1) {
      this.sectionsByWorld.remove(var1.getUID());
   }

   public synchronized BlockSectionData getSection(UUID var1, int var2, int var3) {
      Long2ObjectOpenHashMap var4 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      return var4 == null ? null : (BlockSectionData)var4.get(sectionKey(var2, var3));
   }

   public synchronized boolean canPlaceBlockState(UUID var1, int var2, int var3, int var4, int var5) {
      Long2ObjectOpenHashMap var6 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      BlockSectionData var7 = var6 == null ? null : (BlockSectionData)var6.get(sectionKey(var2 >> 4, var4 >> 4));
      return isWithinLimit(var7, var3 >> 4, localBlockIndex(var2, var3, var4), var5);
   }

   static boolean isWithinLimit(BlockSectionData var0, int var1, short var2, int var3) {
      if (var3 > 0 && var0 != null) {
         CompressedBlockArray var4 = var0.layerForKey(var1);
         if (var4 != null && var4.getStateId(var2) >= 0) {
            return true;
         }

         int var5 = 0;

         for (int var6 = 0; var6 < var0.layerCount(); var6++) {
            var5 += var0.layerAt(var6).size();
            if (var5 >= var3) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   synchronized BlockRegionIndex buildRegionIndex(UUID var1, int var2, int var3, int var4, int var5) {
      Long2ObjectOpenHashMap var6 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      int var7 = var5 - var4 + 1;
      BlockSectionData[] var8 = new BlockSectionData[(var3 - var2 + 1) * var7];
      if (var6 != null) {
         for (int var9 = var2; var9 <= var3; var9++) {
            for (int var10 = var4; var10 <= var5; var10++) {
               var8[(var9 - var2) * var7 + var10 - var4] = (BlockSectionData)var6.get(sectionKey(var9, var10));
            }
         }
      }

      return new BlockRegionIndex(var2, var4, var7, var8);
   }

   synchronized boolean regionIndexMatches(UUID var1, BlockRegionIndex var2) {
      return var2.matchesSections((Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1));
   }

   public synchronized void clear() {
      this.sectionsByWorld.clear();
   }

   private byte updateBlockAndNeighbors(World var1, int var2, int var3, int var4, Boolean var5) {
      HashMap var6 = new HashMap(4);
      byte var7 = this.updateSingleBlock(var1, var2, var3, var4, var2, var3, var4, var5, var6);

      for (int var8 = 0; var8 < 6; var8++) {
         this.updateSingleBlock(var1, var2 + FACE_DX[var8], var3 + FACE_DY[var8], var4 + FACE_DZ[var8], var2, var3, var4, var5, var6);
      }

      this.applyAccumulators(var1.getUID(), var6);
      return var7;
   }

   private byte updateSingleBlock(World var1, int var2, int var3, int var4, int var5, int var6, int var7, Boolean var8, Map var9) {
      int var10 = var2 >> 4;
      int var11 = var4 >> 4;
      if (!var1.isChunkLoaded(var10, var11)) {
         return 0;
      }

      short var12 = localBlockIndex(var2, var3, var4);
      int var13 = this.getRawStateAt(var1.getUID(), var10, var11, var3 >> 4, var12);
      if (var13 == -1) {
         return 0;
      }

      byte var14 = computeOcclusionMask(var1, var2, var3, var4, var5, var6, var7, var8);
      ChunkBlockKey var15 = new ChunkBlockKey(var10, var11, var3 >> 4);
      var9.computeIfAbsent(var15, var0 -> new BlockLayerAccumulator()).add(var12, var14);
      return var14;
   }

   private synchronized void applyAccumulators(UUID var1, Map var2) {
      Long2ObjectOpenHashMap var3 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      if (var3 != null) {
         for (Entry var5 : var2.entrySet()) {
            ChunkBlockKey var6 = (ChunkBlockKey)var5.getKey();
            long var7 = sectionKey(var6.chunkX, var6.chunkZ);
            BlockSectionData var9 = (BlockSectionData)var3.get(var7);
            if (var9 != null) {
               var3.put(var7, var9.applyLayerAccumulator(var6.sectionY, (BlockLayerAccumulator)var5.getValue()));
            }
         }
      }
   }

   private synchronized int getRawStateAt(UUID var1, int var2, int var3, int var4, short var5) {
      Long2ObjectOpenHashMap var6 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      BlockSectionData var7 = var6 == null ? null : (BlockSectionData)var6.get(sectionKey(var2, var3));
      CompressedBlockArray var8 = var7 == null ? null : var7.layerForKey(var4);
      return var8 == null ? -1 : var8.getRawStateCode(var5);
   }

   private static byte computeOcclusionMask(World var0, int var1, int var2, int var3, int var4, int var5, int var6, Boolean var7) {
      int var8 = 0;

      for (int var9 = 0; var9 < 6; var9++) {
         int var10 = var1 + FACE_DX[var9];
         int var11 = var2 + FACE_DY[var9];
         int var12 = var3 + FACE_DZ[var9];
         boolean var13;
         if (var7 != null && var10 == var4 && var11 == var5 && var12 == var6) {
            var13 = var7;
         } else if (var11 >= var0.getMinHeight() && var11 < var0.getMaxHeight()) {
            int var14 = var10 >> 4;
            int var15 = var12 >> 4;
            var13 = !var0.isChunkLoaded(var14, var15) || var0.getBlockAt(var10, var11, var12).getType().isOccluding();
         } else {
            var13 = false;
         }

         if (var13) {
            var8 |= 1 << var9;
         }
      }

      return invertFaceMask(var8);
   }

   static byte invertFaceMask(int var0) {
      return (byte)(~var0 & 63);
   }

   private synchronized void setSectionBlockState(UUID var1, int var2, int var3, int var4, short var5, int var6, byte var7) {
      Long2ObjectOpenHashMap var8 = (Long2ObjectOpenHashMap)this.sectionsByWorld.get(var1);
      long var9 = sectionKey(var2, var3);
      BlockSectionData var11 = var8 == null ? null : (BlockSectionData)var8.get(var9);
      if (var11 != null || var6 != -1) {
         BlockSectionData var12 = (var11 == null ? BlockSectionData.EMPTY : var11).withBlockState(var4, var5, var6, var7);
         if (var12.isEmpty()) {
            var8.remove(var9);
            this.removeIfEmpty(var1, var8);
         } else {
            if (var8 == null) {
               var8 = new Long2ObjectOpenHashMap();
               this.sectionsByWorld.put(var1, var8);
            }

            var8.put(var9, var12);
         }
      }
   }

   private void removeIfEmpty(UUID var1, Long2ObjectOpenHashMap var2) {
      if (var2.isEmpty()) {
         this.sectionsByWorld.remove(var1);
      }
   }

   private static int resolveStateId(String var0) {
      CustomBlockDefinition var1 = ContentManager.method8084().method8160(var0);
      return var1 != null && var1.aps == BlockRealizationMode.ENTITY ? withEntityFlag(var1.anB, ContentManager.method8084().ao.hasEntry(var1.anB)) : -1;
   }

   static int withEntityFlag(int var0, boolean var1) {
      return var1 ? var0 | -2147483648 : var0;
   }

   private static int stripEntityFlag(int var0) {
      return var0 & 2147483647;
   }

   private static boolean hasEntityFlag(int var0) {
      return (var0 & -2147483648) != 0;
   }

   static long sectionKey(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   static short localBlockIndex(int var0, int var1, int var2) {
      return (short)(var0 & 15 | (var2 & 15) << 4 | (var1 & 15) << 8);
   }
}
