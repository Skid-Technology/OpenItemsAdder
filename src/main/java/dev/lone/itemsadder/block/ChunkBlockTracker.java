package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.ReloadCommand;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import dev.lone.itemsadder.util.BlockDataBatch;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.SchedulerUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ChunkBlockTracker implements Listener {
   private final Plugin jt;
   private final NamespacedKey ju;
   private final ConcurrentHashMap jv = new ConcurrentHashMap();
   private final ConcurrentHashMap jw = new ConcurrentHashMap();
   private final ConcurrentHashMap jx = new ConcurrentHashMap();
   private final Map jy = new ConcurrentHashMap();
   @NotNull
   private static final BlockData jz = Material.AIR.createBlockData();

   public ChunkBlockTracker(Plugin var1) {
      this.jt = var1;
      this.ju = new NamespacedKey(var1, "ia_crops");
      Bukkit.getPluginManager().registerEvents(this, var1);
      this.jy.put((byte)0, Material.WHEAT.createBlockData("[age=0]"));
      this.jy.put((byte)1, Material.WHEAT.createBlockData("[age=1]"));
      this.jy.put((byte)2, Material.WHEAT.createBlockData("[age=2]"));
      this.jy.put((byte)3, Material.WHEAT.createBlockData("[age=3]"));
      this.jy.put((byte)4, Material.WHEAT.createBlockData("[age=4]"));
      this.jy.put((byte)5, Material.WHEAT.createBlockData("[age=5]"));
      this.jy.put((byte)6, Material.WHEAT.createBlockData("[age=6]"));
      this.jy.put((byte)7, Material.WHEAT.createBlockData("[age=7]"));
   }

   @Nullable
   public BlockDisplayRegistry getOrCreateDisplayRegistry(Player var1) {
      BlockDisplayRegistry var2 = (BlockDisplayRegistry)this.jx.get(var1);
      if (var2 != null) {
         return var2;
      } else if (var1.isOnline() && !ReloadCommand.aHx) {
         var2 = new BlockDisplayRegistry(var1, StepSoundTracker.kL);
         this.jx.put(var1, var2);
         return var2;
      } else {
         return null;
      }
   }

   @Nullable
   public BlockDisplayRegistry getDisplayRegistry(Player var1) {
      return (BlockDisplayRegistry)this.jx.get(var1);
   }

   public void releasePlayer(Player var1) {
      this.releasePlayerInternal(var1, true);
   }

   void releasePlayerInternal(Player var1, boolean var2) {
      BlockDisplayRegistry var3 = (BlockDisplayRegistry)this.jx.remove(var1);
      if (var3 != null) {
         var3.clearEntries(var2);
      }
   }

   public BlockStateTable getChunkTable(Chunk var1) {
      ChunkKey var2 = ChunkKey.of(var1);
      return this.jv.computeIfAbsent(var2, var2x -> {
         BlockStateTable var3 = this.loadChunkTable(var1);
         BlockStateTableCache var4 = new BlockStateTableCache(var3);
         var4.jB = this.jw.getOrDefault(var2x, 0);
         return var4;
      }).jO;
   }

   public BlockStateTable getChunkTableAt(int var1, int var2, World var3) {
      ChunkKey var4 = new ChunkKey(var3.getUID(), var1, var2);
      return this.jv.computeIfAbsent(var4, var4x -> {
         Chunk var5 = var3.getChunkAt(var1, var2);
         BlockStateTable var6 = this.loadChunkTable(var5);
         BlockStateTableCache var7 = new BlockStateTableCache(var6);
         var7.jB = this.jw.getOrDefault(var4x, 0);
         return var7;
      }).jO;
   }

   int getChunkVersion(UUID var1, int var2, int var3) {
      return this.jw.getOrDefault(new ChunkKey(var1, var2, var3), 0);
   }

   public void unloadChunkTable(Chunk var1) {
      ChunkKey var2 = ChunkKey.of(var1);
      this.jv.remove(var2);
      var1.getPersistentDataContainer().remove(this.ju);
   }

   @Nullable
   public ByteNamedKey getBlockStateKey(Block var1) {
      return this.getChunkTable(var1.getChunk()).getStateKey(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
   }

   public void setBlockState(Chunk var1, int var2, int var3, int var4, String var5, byte var6) {
      BlockStateTable var7 = this.getChunkTable(var1);
      var7.setState(var2 & 15, var3, var4 & 15, var5, var6);
      this.markChunkDirty(var1);
      Block var8 = toWorldLocation(var1, var2, var3, var4).getBlock();
      this.broadcastBlockState(var8, var5, var6);
   }

   public boolean isCustomCrop(@NotNull Block var1) {
      return this.hasCustomCrop(var1.getX(), var1.getY(), var1.getZ(), var1.getWorld(), true);
   }

   public boolean isCustomCropAt(IntVector3 var1, Chunk var2) {
      return this.hasCustomCrop(var1.x, var1.y, var1.z, var2.getWorld(), true);
   }

   public boolean hasCustomCrop(int var1, int var2, int var3, World var4, boolean var5) {
      int var6 = var1 >> 4;
      int var7 = var3 >> 4;
      int var8 = var1 & 15;
      int var9 = var3 & 15;
      ChunkKey var10 = new ChunkKey(var4.getUID(), var6, var7);
      BlockStateTableCache var11 = (BlockStateTableCache)this.jv.get(var10);
      if (var11 != null) {
         return var11.jO.getStateKey(var8, var2, var9) != null;
      }

      if (!var5 && !var4.isChunkLoaded(var6, var7)) {
         return false;
      }

      Chunk var12 = var4.getChunkAt(var6, var7);
      BlockStateTable var13 = this.loadChunkTable(var12);
      return var13.getStateKey(var8, var2, var9) != null;
   }

   public boolean removeBlockState(Chunk var1, int var2, int var3, int var4) {
      BlockStateTable var5 = this.getChunkTable(var1);
      boolean var6 = var5.removeState(var2 & 15, var3, var4 & 15);
      if (var6) {
         this.markChunkDirty(var1);
         Location var7 = toWorldLocation(var1, var2, var3, var4);

         for (Player var9 : BlockFaceUtils.playersInView(var7)) {
            if (BlockInteractionListener.isLightweightCrops(var9)) {
               this.sendCropState(var9, var7, null);
            } else {
               ContentManager.method8084().method8142().nb.markNeedsUpdate(var9);
            }
         }
      }

      return var6;
   }

   public void setBlockStateInt(Block var1, String var2, int var3) {
      this.setBlockStateAt(var1, var2, (byte)var3);
   }

   public void setBlockStateAt(Block var1, String var2, byte var3) {
      this.setBlockState(var1.getChunk(), var1.getX() & 15, var1.getY(), var1.getZ() & 15, var2, var3);
   }

   public boolean removeBlockStateAt(Block var1) {
      return this.removeBlockState(var1.getChunk(), var1.getX() & 15, var1.getY(), var1.getZ() & 15);
   }

   public ByteNamedKey getStateKeyAt(Chunk var1, int var2, int var3, int var4) {
      return this.getChunkTable(var1).getStateKey(var2 & 15, var3, var4 & 15);
   }

   public void markChunkDirty(Chunk var1) {
      BlockStateTableCache var2 = (BlockStateTableCache)this.jv.get(ChunkKey.of(var1));
      if (var2 != null) {
         var2.dL = true;
      }
   }

   public void flushChunkIfDirty(Chunk var1) {
      BlockStateTableCache var2 = (BlockStateTableCache)this.jv.get(ChunkKey.of(var1));
      if (var2 != null && var2.dL) {
         this.saveChunkTable(var1, var2.jO);
         var2.dL = false;

         for (Player var4 : var1.getWorld().getPlayers()) {
            if (this.jx.containsKey(var4)) {
               ContentManager.method8084().method8142().nb.releaseChunkDisplays(var4, var1.getX(), var1.getZ(), var1.getWorld());
            }
         }
      }
   }

   public void flushAll() {
      for (Entry var2 : this.jv.entrySet()) {
         ChunkKey var3 = (ChunkKey)var2.getKey();
         BlockStateTableCache var4 = (BlockStateTableCache)var2.getValue();
         if (var4.dL) {
            Chunk var5 = var3.resolveChunk();
            if (var5 != null) {
               this.saveChunkTable(var5, var4.jO);
               var4.dL = false;
            }
         }
      }

      for (Player var7 : Bukkit.getOnlinePlayers()) {
         this.releasePlayer(var7);
      }
   }

   public void forgetChunkCache(Chunk var1) {
      ChunkKey var2 = ChunkKey.of(var1);
      this.jv.remove(var2);
   }

   public int collectNearbyCrops(Location var1, int var2, ObjectArrayList var3) {
      int var4 = var1.getBlockX();
      int var5 = var1.getBlockZ();
      ObjectArrayList var6 = new ObjectArrayList();
      this.collectChunkStates(var1, var2, var6);
      return this.filterNearbyBlocks(var4, var5, var2, var6, var3);
   }

   public void collectChunkStates(Location var1, int var2, ObjectArrayList var3) {
      var3.clear();
      World var4 = var1.getWorld();
      int var5 = var1.getBlockX();
      int var6 = var1.getBlockZ();
      int var7 = var5 - var2 >> 4;
      int var8 = var5 + var2 >> 4;
      int var9 = var6 - var2 >> 4;
      int var10 = var6 + var2 >> 4;

      for (int var11 = var7; var11 <= var8; var11++) {
         for (int var12 = var9; var12 <= var10; var12++) {
            if (var4.isChunkLoaded(var11, var12)) {
               BlockStateTable var13 = this.getChunkTable(var4.getChunkAt(var11, var12));
               if (var13 != null) {
                  var3.add(new BlockStateKey(var11, var12, var13.snapshot()));
               }
            }
         }
      }
   }

   public int filterNearbyBlocks(int var1, int var2, int var3, List var4, ObjectArrayList var5) {
      var5.clear();
      int[] var6 = new int[]{StepSoundTracker.kQ, 16, 32, 64, var3};
      ObjectArrayList var7 = new ObjectArrayList();
      int var8 = var3 * var3;

      for (BlockStateKey var10 : var4) {
         int var11 = var10.x();
         int var12 = var10.z();

         for (ChunkLocalBlock var14 : var10.view().entries()) {
            int var15 = (var11 << 4) + var14.relX();
            int var16 = (var12 << 4) + var14.relZ();
            int var17 = var15 - var1;
            int var18 = var16 - var2;
            int var19 = var17 * var17 + var18 * var18;
            if (var19 <= var8) {
               var7.add(new ChunkLocalBlockDup2(var15, var14.y(), var16, var14.namespacedId(), var14.age()));
            }
         }
      }

      int var20 = 0;

      for (int var24 : var6) {
         int var25 = var24 * var24;
         ObjectListIterator var26 = var7.iterator();

         while (var26.hasNext()) {
            ChunkLocalBlockDup2 var27 = (ChunkLocalBlockDup2)var26.next();
            int var28 = var27.relX() - var1;
            int var29 = var27.relZ() - var2;
            int var30 = var28 * var28 + var29 * var29;
            if (var30 >= var20 && var30 <= var25) {
               var5.add(var27);
            }
         }

         var20 = var25;
      }

      return var5.size();
   }

   public void forEachChunkTable(BiConsumer var1) {
      for (Entry var3 : this.jv.entrySet()) {
         Chunk var4 = ((ChunkKey)var3.getKey()).resolveChunk();
         if (var4 != null) {
            var1.accept(var4, ((BlockStateTableCache)var3.getValue()).jO);
         }
      }
   }

   public void snapshotChunks(Consumer var1) {
      for (Entry var3 : this.jv.entrySet()) {
         ChunkKey var4 = (ChunkKey)var3.getKey();
         Chunk var5 = var4.resolveChunk();
         if (var5 != null) {
            BlockStateTableCache var6 = (BlockStateTableCache)var3.getValue();
            int var7 = this.jw.getOrDefault(var4, 0);
            var1.accept(new ChunkBlockEntry(var4, var7, var6.jO.snapshot()));
         }
      }
   }

   public void applyChunkRecords(ChunkKey var1, int var2, Iterable var3) {
      int var4 = this.jw.getOrDefault(var1, 0);
      if (var4 == var2) {
         Chunk var5 = var1.resolveChunk();
         if (var5 != null) {
            for (ChunkBlockStateRecord var7 : var3) {
               this.setBlockState(var5, var7.jJ, var7.jK, var7.jL, var7.getNamespacedId(), var7.jN);
            }
         }
      }
   }

   public static boolean hasCropData(Chunk var0) {
      return var0.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "ia_crops"), PersistentDataType.BYTE_ARRAY);
   }

   private BlockStateTable loadChunkTable(Chunk var1) {
      byte[] var2 = (byte[])var1.getPersistentDataContainer().get(this.ju, PersistentDataType.BYTE_ARRAY);
      return var2 == null ? new BlockStateTable() : BlockStateTable.deserialize(var2);
   }

   private void saveChunkTable(Chunk var1, BlockStateTable var2) {
      var1.getPersistentDataContainer().set(this.ju, PersistentDataType.BYTE_ARRAY, var2.serialize());
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent var1) {
      this.preloadChunk(var1.getChunk());
   }

   @EventHandler(ignoreCancelled = true)
   public void onChunkUnload(ChunkUnloadEvent var1) {
      ChunkKey var2 = ChunkKey.of(var1.getChunk());
      this.jw.merge(var2, 1, Integer::sum);
      this.flushChunkIfDirty(var1.getChunk());
      this.forgetChunkCache(var1.getChunk());
   }

   @EventHandler
   public void onPluginDisable(PluginDisableEvent var1) {
      if (var1.getPlugin() == this.jt) {
         this.flushAll();
         this.jv.clear();
      }
   }

   public void preloadChunk(Chunk var1) {
      this.getChunkTable(var1);
   }

   public void sendChunkCrops(Player var1, Chunk var2) {
      if (BlockInteractionListener.isLightweightCrops(var1)) {
         BlockDataBatch var3 = new BlockDataBatch(List.of(var1));

         for (ChunkLocalBlock var5 : this.getChunkTable(var2).snapshot().entries()) {
            BlockSoundProfile var6 = (BlockSoundProfile)ContentManager.method8084().au.get(var5.namespacedId());
            if (var6 != null) {
               byte var7 = this.computeStageIndex(var6, var5.age());
               var3.setBlock(var5.toWorldLocation(var2).toVector(), (BlockData)this.jy.get(var7));
            }
         }

         var3.flush();
      } else {
         ContentManager.method8084().method8142().nb.markNeedsUpdate(var1);
      }
   }

   public byte computeStageIndex(BlockSoundProfile var1, byte var2) {
      if (var1.jW <= 0) {
         return 0;
      }

      double var3 = (double)var2 / var1.jW;
      int var5 = Math.min(this.jy.size(), Math.max(0, (int)Math.round(var3 * 7.0)));
      return (byte)var5;
   }

   public void sendCropState(Player var1, Location var2, @Nullable ByteNamedKey var3) {
      if (var3 == null) {
         var1.sendBlockChange(var2, jz);
      } else {
         BlockSoundProfile var4 = (BlockSoundProfile)ContentManager.method8084().au.get(var3.namespacedId());
         if (var4 != null) {
            BlockData var5 = (BlockData)this.jy.get(this.computeStageIndex(var4, var3.age()));
            var1.sendBlockChange(var2, var5);
         }
      }
   }

   public void revealAllCrops(Player var1) {
      if (BlockInteractionListener.isLightweightCrops(var1)) {
         BlockDataBatch var2 = new BlockDataBatch(List.of(var1));
         ContentManager.method8084().method8142().mZ.forEachChunkTable((var2x, var3) -> {
            Location var4 = new Location(var2x.getWorld(), (var2x.getX() << 4) + 8, 0.0, (var2x.getZ() << 4) + 8);
            if (!(var1.getLocation().distance(var4) > 128.0)) {
               for (ChunkLocalBlock var6 : var3.snapshot().entries()) {
                  var2.setBlock(var6.toWorldLocation(var2x).toVector(), jz);
               }
            }
         });
         SchedulerUtils.runSync(() -> var2.flush());
      }
   }

   public void renderCropStagesFor(Player var1) {
      if (!BlockInteractionListener.isLightweightCrops(var1)) {
         BlockDataBatch var2 = new BlockDataBatch(List.of(var1));
         ContentManager.method8084().method8142().mZ.forEachChunkTable((var3, var4) -> {
            Location var5 = new Location(var3.getWorld(), (var3.getX() << 4) + 8, 0.0, (var3.getZ() << 4) + 8);
            if (!(var1.getLocation().distance(var5) > 128.0)) {
               for (ChunkLocalBlock var7 : var4.snapshot().entries()) {
                  BlockSoundProfile var8 = (BlockSoundProfile)ContentManager.method8084().au.get(var7.namespacedId());
                  if (var8 != null) {
                     byte var9 = this.computeStageIndex(var8, var7.age());
                     var2.setBlock(var7.toWorldLocation(var3).toVector(), (BlockData)this.jy.get(var9));
                  }
               }
            }
         });
         SchedulerUtils.runSync(() -> var2.flush());
      }
   }

   public void broadcastBlockState(Block var1, String var2, byte var3) {
      Location var4 = var1.getLocation();

      for (Player var6 : BlockFaceUtils.playersInView(var1.getLocation())) {
         if (BlockInteractionListener.isLightweightCrops(var6)) {
            this.sendCropState(var6, var4, new ByteNamedKey(var2, var3));
         } else {
            ContentManager.method8084().method8142().nb.markNeedsUpdate(var6);
         }
      }
   }

   static long packBlockPosition(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095;
   }

   static long packLocation(Location var0) {
      return packBlockPosition(var0.getBlockX(), var0.getBlockY(), var0.getBlockZ());
   }

   public static long packIntVector(IntVector3 var0) {
      return packBlockPosition(var0.x, var0.y, var0.z);
   }

   static IntVector3 unpackBlockPosition(long var0) {
      int var2 = (int)(var0 >> 38);
      int var3 = (int)(var0 & 4095L);
      int var4 = (int)(var0 >> 12 & 67108863L);
      if ((var2 & 33554432) != 0) {
         var2 |= -67108864;
      }

      if ((var3 & 2048) != 0) {
         var3 |= -4096;
      }

      if ((var4 & 33554432) != 0) {
         var4 |= -67108864;
      }

      return new IntVector3(var2, var3, var4);
   }

   public static Location toWorldLocation(Chunk var0, int var1, int var2, int var3) {
      return new Location(var0.getWorld(), (var0.getX() << 4) + (var1 & 15), var2, (var0.getZ() << 4) + (var3 & 15));
   }

   public static void drawBoundingBox(World var0, BoundingBox var1, Location var2) {
      DustOptions var3 = new DustOptions(Color.RED, 0.5F);
      Vector var4 = var1.getMin().subtract(var2.toVector());
      Vector var5 = var1.getMax().subtract(var2.toVector());
      Vector[] var6 = new Vector[]{
         new Vector(var4.getX(), var4.getY(), var4.getZ()),
         new Vector(var5.getX(), var4.getY(), var4.getZ()),
         new Vector(var5.getX(), var5.getY(), var4.getZ()),
         new Vector(var4.getX(), var5.getY(), var4.getZ()),
         new Vector(var4.getX(), var4.getY(), var5.getZ()),
         new Vector(var5.getX(), var4.getY(), var5.getZ()),
         new Vector(var5.getX(), var5.getY(), var5.getZ()),
         new Vector(var4.getX(), var5.getY(), var5.getZ())
      };
      byte var7 = 20;

      for (int var8 = 0; var8 < 4; var8++) {
         drawLine(var0, var6[var8], var6[(var8 + 1) % 4], var2, var7, var3);
         drawLine(var0, var6[var8 + 4], var6[(var8 + 1) % 4 + 4], var2, var7, var3);
         drawLine(var0, var6[var8], var6[var8 + 4], var2, var7, var3);
      }
   }

   public static void drawEntityBox(Entity var0) {
      drawBoundingBox(var0.getWorld(), var0.getBoundingBox(), var0.getLocation());
   }

   public static void drawLine(World var0, Vector var1, Vector var2, Location var3, int var4, DustOptions var5) {
      Vector var6 = var2.clone().subtract(var1).divide(new Vector(var4, var4, var4));

      for (int var7 = 0; var7 <= var4; var7++) {
         Vector var8 = var1.clone().add(var6.clone().multiply(var7));
         Location var9 = var3.clone().add(var8);
         var0.spawnParticle(Particle.DUST, var9, 1, var5);
      }
   }
}
