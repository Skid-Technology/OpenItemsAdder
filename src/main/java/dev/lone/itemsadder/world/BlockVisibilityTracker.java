package dev.lone.itemsadder.world;

import beer.devs.nativepackets.api.ChunkWithLightPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.DisplayEntityKey;
import dev.lone.itemsadder.entity.DisplayEntityPool;
import dev.lone.itemsadder.entity.DisplayUpdateRecord;
import dev.lone.itemsadder.entity.PacketDisplayBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.network.ViewerPlacementBundle;
import dev.lone.itemsadder.network.ViewerPlacementRecord;
import dev.lone.itemsadder.util.BlockDataBatch;
import dev.lone.itemsadder.util.ChunkKeyMath;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.LongKeyedBucketMap;
import dev.lone.itemsadder.util.LongKeyedBuckets;
import dev.lone.itemsadder.util.PackedIntTable;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.SpatialMath;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class BlockVisibilityTracker implements Listener {
   private static final double VIEW_COS_THRESHOLD = Math.cos(Math.toRadians(80.0));
   private static final int WORKER_THREADS = Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors() / 2));
   static final int SECTION_BLOCKS = 64;
   private static final int UNUSED_BATCH_SIZE = 32;
   private static final double HIDDEN_Y = -4096.0;
   private final BlockStateManager blockStateManager;
   private final DisplayEntityPool displayPool;
   private final Map viewerStates = new ConcurrentHashMap();
   private final Map playerWorlds = new ConcurrentHashMap();
   private final Map viaVersionCache = new ConcurrentHashMap();
   private final ChunkViewerIndex viewerIndex = new ChunkViewerIndex();
   private final Map bucketsByWorld = new ConcurrentHashMap();
   private final ConcurrentLinkedQueue placementQueue = new ConcurrentLinkedQueue();
   private final Set pendingSections = ConcurrentHashMap.newKeySet();
   private ExecutorService worker;
   private BukkitTask tickTask;
   private BlockData barrierData;
   private BlockData[] materialData = new BlockData[0];
   private PacketListenerHandle packetListener;
   private boolean registered;
   private volatile int lifecycle;
   private volatile int tickCounter;

   public BlockVisibilityTracker(BlockStateManager var1, DisplayEntityPool var2) {
      this.blockStateManager = var1;
      this.displayPool = var2;
   }

   public void start() {
      this.stop();
      if (Settings.get().customBlocksEntity && this.displayPool.wh() != 0 && NativePacketsHolder.PD().isSupported()) {
         this.barrierData = Material.BARRIER.createBlockData();
         this.materialData = new BlockData[this.displayPool.wi()];

         for (int var1 = 0; var1 < this.displayPool.wi(); var1++) {
            BlockData var2 = this.displayPool.getMaterialAt(var1).createBlockData();
            this.materialData[var1] = var2;
         }

         this.lifecycle++;
         AtomicInteger var4 = new AtomicInteger();
         ThreadPoolExecutor var5 = new ThreadPoolExecutor(
            WORKER_THREADS, WORKER_THREADS, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(Math.max(1, Bukkit.getMaxPlayers())), var1x -> {
               Thread var2x = new Thread(var1x, "ItemsAdder-Entity-Blocks-" + var4.incrementAndGet());
               var2x.setDaemon(true);
               return var2x;
            }, new AbortPolicy()
         );
         var5.prestartAllCoreThreads();
         this.worker = var5;
         if (!this.registered) {
            EventRegistrar.registerListener(this, Main.getInstance());
            this.registered = true;
         }

         Bukkit.getOnlinePlayers().forEach(var1x -> this.playerWorlds.put(var1x.getUniqueId(), var1x.getWorld().getUID()));
         this.packetListener = NativePacketsHolder.PE()
            .method1888(
               Main.getInstance(),
               PacketSelector.exact(PacketTypes.CHUNK_WITH_LIGHT),
               ListenerPriority.NORMAL,
               false,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1x -> {
                  UUID var2x = NativePacketsHolder.PE().method1883(var1x);
                  UUID var3x = var2x == null ? null : (UUID)this.playerWorlds.get(var2x);
                  if (var3x != null) {
                     ChunkWithLightPacketView var4x = (ChunkWithLightPacketView)var1x.view(ChunkWithLightPacketView.class);
                     ChunkSectionKey var5x = new ChunkSectionKey(var2x, var1x.connection().id(), var3x, var4x.chunkX(), var4x.chunkZ(), this.lifecycle);
                     var1x.afterWrite(var2xx -> {
                        switch (PackedIntTable.SWITCH_PACKET_OUTCOME[var2xx.outcome().ordinal()]) {
                           case 1:
                              if (var5x.rendererLifecycle() == this.lifecycle) {
                                 this.pendingSections.add(var5x);
                              }
                           case 2:
                           case 3:
                           case 4:
                        }
                     });
                  }
               }
            );
         int var3 = this.lifecycle;
         SchedulerUtils.runLaterSync(() -> {
            if (this.lifecycle == var3 && this.worker != null) {
               Bukkit.getOnlinePlayers().forEach(this::resendPlayerSections);
            }
         }, 1L);
         this.tickTask = SchedulerUtils.scheduleRepeatingSync(var1x -> this.tick(), 1L, 1L);
      }
   }

   public void stop() {
      this.lifecycle++;
      if (this.tickTask != null) {
         this.tickTask.cancel();
         this.tickTask = null;
      }

      if (this.registered) {
         EventRegistrar.unregisterListener(this);
         this.registered = false;
      }

      if (this.packetListener != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.packetListener);
         this.packetListener = null;
      }

      if (this.barrierData != null) {
         Bukkit.getOnlinePlayers().forEach(this::clearPlayerSections);
      }

      for (ChunkViewerState var2 : this.viewerStates.values()) {
         Player var3 = Bukkit.getPlayer(var2.playerId);
         if (var3 != null) {
            this.clearPlayerSlots(var3, var2.blockStore);
         }

         var2.viewDistance++;
      }

      this.viewerStates.clear();
      this.viaVersionCache.clear();
      this.viewerIndex.clear();
      this.bucketsByWorld.clear();
      ExecutorService var5 = this.worker;
      this.worker = null;
      if (var5 != null) {
         var5.shutdownNow();

         try {
            var5.awaitTermination(1L, TimeUnit.SECONDS);
         } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
         }
      }

      this.placementQueue.clear();
      this.pendingSections.clear();
      this.playerWorlds.clear();
      this.barrierData = null;
      this.materialData = new BlockData[0];
   }

   public void markRegionDirty(UUID var1, int var2, int var3) {
      int var4 = Settings.get().blockEntityRenderRadius + 16;
      double var5 = (double)var4 * var4;
      int var7 = toSectionIndex(var2 + 0.5 - var4);
      int var8 = toSectionIndex(var2 + 0.5 + var4);
      int var9 = toSectionIndex(var3 + 0.5 - var4);
      int var10 = toSectionIndex(var3 + 0.5 + var4);

      for (int var11 = var7; var11 <= var8; var11++) {
         for (int var12 = var9; var12 <= var10; var12++) {
            for (ChunkViewerState var14 : this.viewerIndex.getViewers(var1, sectionKey(var11, var12))) {
               if (var1.equals(var14.worldId)) {
                  double var15 = var14.cachedX - var2 - 0.5;
                  double var17 = var14.cachedZ - var3 - 0.5;
                  if (var15 * var15 + var17 * var17 <= var5) {
                     var14.renderPending.set(true);
                     var14.needsFullUpdate.set(true);
                  }
               }
            }
         }
      }
   }

   public void markChunkDirty(UUID var1, int var2, int var3) {
      this.markRegionDirty(var1, (var2 << 4) + 8, (var3 << 4) + 8);
   }

   void sendBlockUpdate(World var1, int var2, int var3, int var4, int var5) {
      if (this.worker != null && var1.isChunkLoaded(var2 >> 4, var4 >> 4)) {
         boolean var6 = var5 >= 0;
         long var7 = ChunkKeyMath.packChunkKey(var2, var3, var4);
         int var9 = var2 >> 4;
         int var10 = var4 >> 4;
         int var11 = Bukkit.getServer().getViewDistance() + 1;
         Location var12 = new Location(var1, var2, var3, var4);
         BlockData var13 = var6 ? null : var1.getBlockAt(var2, var3, var4).getBlockData();

         for (Player var15 : var1.getPlayers()) {
            Location var16 = var15.getLocation();
            if (Math.abs((var16.getBlockX() >> 4) - var9) <= var11 && Math.abs((var16.getBlockZ() >> 4) - var10) <= var11) {
               ChunkViewerState var17 = (ChunkViewerState)this.viewerStates.get(var15.getUniqueId());
               boolean var18 = var17 != null && var1.getUID().equals(var17.worldId) && var17.blockStore.primaryKeys.contains(var7);
               if (!var6 && var17 != null) {
                  var17.blockStore.primaryKeys.remove(var7);
                  var17.blockStore.secondaryKeys.remove(var7);
               }

               var15.sendBlockChange(var12, var6 ? (var18 ? this.barrierData : this.materialData[var5]) : var13);
            }
         }
      }
   }

   private void sendSectionTo(Player var1, ChunkSectionKey var2, BlockSectionData var3, LongOpenHashSet var4) {
      if (this.worker != null
         && var2.rendererLifecycle() == this.lifecycle
         && NativePacketsHolder.PE().method1885(var2.playerId(), var2.connectionId()) == var1
         && var2.worldId().equals(this.playerWorlds.get(var2.playerId()))) {
         BlockDataBatch var5 = new BlockDataBatch(List.of(var1));
         this.addSectionBlocks(var5, var3, var2.chunkX(), var2.chunkZ(), true, var4);
         var5.flush();
      }
   }

   private void resendPlayerSections(Player var1) {
      this.syncPlayerSections(var1, true, true);
   }

   private void clearPlayerSections(Player var1) {
      this.syncPlayerSections(var1, false, false);
   }

   private void syncPlayerSections(Player var1, boolean var2, boolean var3) {
      if (var1.isOnline()) {
         Location var4 = var1.getLocation();
         int var5 = var4.getBlockX() >> 4;
         int var6 = var4.getBlockZ() >> 4;
         int var7 = Bukkit.getServer().getViewDistance();
         BlockRegionIndex var8 = this.blockStateManager.buildRegionIndex(var1.getWorld().getUID(), var5 - var7, var5 + var7, var6 - var7, var6 + var7);
         ChunkViewerState var9 = (ChunkViewerState)this.viewerStates.get(var1.getUniqueId());
         LongOpenHashSet var10 = var3 && var9 != null && var1.getWorld().getUID().equals(var9.worldId) ? var9.blockStore.primaryKeys : null;
         BlockDataBatch var11 = new BlockDataBatch(List.of(var1));

         for (int var12 = 0; var12 < var8.sectionCount(); var12++) {
            BlockSectionData var13 = var8.getSection(var12);
            if (var13 != null) {
               this.addSectionBlocks(var11, var13, var8.chunkXAt(var12), var8.chunkZAt(var12), var2, var10);
            }
         }

         var11.flush();
      }
   }

   private void addSectionBlocks(BlockDataBatch var1, BlockSectionData var2, int var3, int var4, boolean var5, LongOpenHashSet var6) {
      for (int var7 = 0; var7 < var2.layerCount(); var7++) {
         int var8 = var2.layerKeyAt(var7);
         CompressedBlockArray var9 = var2.layerAt(var7);

         for (int var10 = 0; var10 < var9.size(); var10++) {
            int var11 = var9.blockIdAt(var10) & 4095;
            int var12 = var3 << 4 | var11 & 15;
            int var13 = var8 << 4 | var11 >>> 8;
            int var14 = var4 << 4 | var11 >>> 4 & 15;
            long var15 = ChunkKeyMath.packChunkKey(var12, var13, var14);
            var1.setBlockAt(var12, var13, var14, var5 && (var6 == null || !var6.contains(var15)) ? this.materialData[var9.stateIdAt(var10)] : this.barrierData);
         }
      }
   }

   public void onBlockPlaced(UUID var1, int var2, int var3, int var4, int var5, byte var6) {
      if (var6 != 0) {
         long var7 = ChunkKeyMath.packChunkKey(var2, var3, var4);
         int var9 = this.tickCounter;
         int var10 = Settings.get().blockEntityRenderRadius;
         double var11 = (double)var10 * var10;
         int var13 = toSectionIndex(var2 + 0.5 - var10);
         int var14 = toSectionIndex(var2 + 0.5 + var10);
         int var15 = toSectionIndex(var4 + 0.5 - var10);
         int var16 = toSectionIndex(var4 + 0.5 + var10);
         LongKeyedBucketMap var17 = (LongKeyedBucketMap)this.bucketsByWorld.get(var1);
         LongKeyedBuckets var18 = var17 == null ? LongKeyedBuckets.EMPTY : var17.getSnapshot();

         for (int var19 = var13; var19 <= var14; var19++) {
            for (int var20 = var15; var20 <= var16; var20++) {
               for (ChunkViewerState var22 : this.viewerIndex.getViewers(var1, sectionKey(var19, var20))) {
                  if (var1.equals(var22.worldId)) {
                     double var23 = var22.cachedX - var2 - 0.5;
                     double var25 = var22.cachedZ - var4 - 0.5;
                     if (!(var23 * var23 + var25 * var25 > var11)) {
                        Player var27 = Bukkit.getPlayer(var22.playerId);
                        if (var27 != null && var27.isOnline() && var22.sendPending.compareAndSet(false, true)) {
                           try {
                              Location var28 = var27.getEyeLocation();
                              Vector var29 = var28.getDirection();
                              if (!(
                                 SpatialMath.distanceSqForKey(
                                       var7,
                                       var6,
                                       var28.getX(),
                                       var28.getY(),
                                       var28.getZ(),
                                       var29.getX(),
                                       var29.getZ(),
                                       (double)var10 * var10,
                                       VIEW_COS_THRESHOLD
                                    )
                                    < 0.0
                              )) {
                                 this.updateViewerChunk(var22, var1, var28.getX(), var28.getZ());
                                 synchronized (var22.blockStore) {
                                    PackedBlockStore var31 = var22.blockStore;
                                    int var32 = this.resolveDisplayState(var18, var7, var5, (byte)0);
                                    int var33 = var31.slotOf(var7);
                                    int var34 = var33 < 0 ? -1 : var31.slotStates[var33];
                                    boolean var35 = var33 >= 0;
                                    var33 = var31.acquireSlot(var7, var32, var9);
                                    if (var33 >= 0) {
                                       if (var31.primaryKeys.add(var7)) {
                                          var27.sendBlockChange(new Location(var27.getWorld(), var2, var3, var4), this.barrierData);
                                       }

                                       DisplayEntityKey var36 = this.displayPool.keyAt(var33);
                                       if (var35 && var31.slotScheduled[var33]) {
                                          if (var34 != var32) {
                                             NativePacketsHolder.PD().sendEntityData(var27, var36.entityId(), this.displayObjectOf(var32));
                                          }
                                       } else if (var31.slotScheduled[var33]) {
                                          NativePacketsHolder.PD().sendMove(var27, var36.entityId(), var2 + 0.5, var3 + 0.5, var4 + 0.5);
                                          NativePacketsHolder.PD().sendEntityData(var27, var36.entityId(), this.displayObjectOf(var32));
                                       } else {
                                          NativePacketsHolder.PD().sendPosition(var27, var36, var2 + 0.5, var3 + 0.5, var4 + 0.5, this.displayObjectOf(var32));
                                          var31.slotScheduled[var33] = true;
                                       }
                                    }
                                 }
                              }
                           } finally {
                              var22.sendPending.set(false);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void updateBlockBucket(UUID var1, int var2, int var3, int var4, int var5, int var6) {
      long var7 = ChunkKeyMath.packChunkKey(var2, var3, var4);
      boolean var9;
      synchronized (this.bucketsByWorld) {
         LongKeyedBucketMap var11 = (LongKeyedBucketMap)this.bucketsByWorld.get(var1);
         if (var11 == null) {
            if (var6 < 0) {
               return;
            }

            var11 = new LongKeyedBucketMap();
            this.bucketsByWorld.put(var1, var11);
         }

         var9 = var11.updateStage(var7, var5, var6);
         if (var11.isEmpty()) {
            this.bucketsByWorld.remove(var1);
         }
      }

      if (var9) {
         this.markRegionDirty(var1, var2, var4);
      }
   }

   private void tick() {
      this.drainPlacementQueue();
      this.drainPendingSections();
      int var1 = ++this.tickCounter;
      int var2 = Math.max(1, Settings.get().blockEntityUpdateIntervalTicks);

      for (Player var4 : Bukkit.getOnlinePlayers()) {
         if (!this.isRendererAvailable(var4)) {
            this.removeViewer(var4);
         } else {
            ChunkViewerState var5 = this.viewerStates.computeIfAbsent(var4.getUniqueId(), var2x -> {
               ChunkViewerState var3 = new ChunkViewerState(var4, this.displayPool.wh(), Settings.get().blockEntityMaxDisplaysPerPlayer);
               Location var4x = var4.getEyeLocation();
               this.updateViewerChunk(var3, var3.worldId, var4x.getX(), var4x.getZ());
               return var3;
            });
            if (var1 >= var5.minSectionY) {
               var5.needsFullUpdate.set(true);
            }

            if (!var5.worldId.equals(var4.getWorld().getUID())) {
               this.onViewerWorldChanged(var4, var5);
               var5.worldId = var4.getWorld().getUID();
               Location var6 = var4.getEyeLocation();
               this.updateViewerChunk(var5, var5.worldId, var6.getX(), var6.getZ());
            }

            if (var5.needsFullUpdate.get()
               && (var5.renderPending.get() || Math.floorMod(var4.getUniqueId().hashCode(), var2) == var1 % var2)
               && var5.sendPending.compareAndSet(false, true)) {
               if (!var5.needsFullUpdate.compareAndSet(true, false)) {
                  var5.sendPending.set(false);
               } else {
                  var5.renderPending.set(false);
                  this.startViewerPlacement(var4, var5, var1);
               }
            }
         }
      }
   }

   private void drainPlacementQueue() {
      ViewerPlacementBundle var1;
      while ((var1 = (ViewerPlacementBundle)this.placementQueue.poll()) != null) {
         try {
            this.applyPlacement(var1.WF, var1.WG, var1.WH);
         } catch (Throwable var3) {
            var3.printStackTrace();
            var1.WH.needsFullUpdate.set(true);
            var1.WH.sendPending.set(false);
         }
      }
   }

   private void drainPendingSections() {
      for (ChunkSectionKey var2 : this.pendingSections) {
         if (this.pendingSections.remove(var2) && var2.rendererLifecycle() == this.lifecycle) {
            this.scheduleSectionSend(var2);
         }
      }
   }

   private void scheduleSectionSend(ChunkSectionKey var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.playerId(), var1.connectionId());
      if (var2 != null) {
         try {
            NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> this.sendSectionForPlayer(var1, var2), () -> {});
         } catch (RuntimeException | LinkageError var4) {
            Log.warn("Failed to schedule entity-block chunk player ownership", var4);
         }
      }
   }

   private void sendSectionForPlayer(ChunkSectionKey var1, Player var2) {
      if (var1.rendererLifecycle() == this.lifecycle
         && this.worker != null
         && NativePacketsHolder.PE().method1885(var1.playerId(), var1.connectionId()) == var2
         && var2.isOnline()
         && var1.worldId().equals(var2.getWorld().getUID())
         && var1.worldId().equals(this.playerWorlds.get(var1.playerId()))
         && isNearChunk(var2, var1.chunkX(), var1.chunkZ())) {
         World var3 = var2.getWorld();
         LongOpenHashSet var4 = null;
         ChunkViewerState var5 = (ChunkViewerState)this.viewerStates.get(var1.playerId());
         if (var5 != null && var1.worldId().equals(var5.worldId)) {
            synchronized (var5.blockStore) {
               var4 = new LongOpenHashSet(var5.blockStore.primaryKeys);
            }
         }

         LongOpenHashSet var11 = var4;
         Location var7 = new Location(var3, (var1.chunkX() << 4) + 8, var3.getMinHeight(), (var1.chunkZ() << 4) + 8);

         try {
            NativePacketsHolder.PE()
               .IP()
               .region(
                  Main.getInstance(),
                  var7,
                  () -> {
                     if (var1.rendererLifecycle() == this.lifecycle
                        && this.worker != null
                        && NativePacketsHolder.PE().method1885(var1.playerId(), var1.connectionId()) == var2
                        && var1.worldId().equals(this.playerWorlds.get(var1.playerId()))
                        && var3.isChunkLoaded(var1.chunkX(), var1.chunkZ())) {
                        BlockSectionData var5x = this.blockStateManager.getSection(var1.worldId(), var1.chunkX(), var1.chunkZ());
                        if (var5x != null) {
                           this.sendSectionTo(var2, var1, var5x, var11);
                        }
                     }
                  }
               );
         } catch (RuntimeException | LinkageError var9) {
            Log.warn("Failed to schedule entity-block chunk region ownership", var9);
         }
      }
   }

   private static boolean isNearChunk(Player var0, int var1, int var2) {
      Location var3 = var0.getLocation();
      int var4 = Bukkit.getServer().getViewDistance() + 1;
      return Math.abs((var3.getBlockX() >> 4) - var1) <= var4 && Math.abs((var3.getBlockZ() >> 4) - var2) <= var4;
   }

   private void startViewerPlacement(Player var1, ChunkViewerState var2, int var3) {
      Location var4 = var1.getEyeLocation();
      Vector var5 = var4.getDirection();
      this.updateViewerChunk(var2, var2.worldId, var4.getX(), var4.getZ());
      var2.updateViewPosition(var4);
      int var6 = Settings.get().blockEntityRenderRadius;
      int var7 = var6 + 15 >> 4;
      int var8 = var4.getBlockX() >> 4;
      int var9 = var4.getBlockZ() >> 4;
      ViewerPlacementRecord var10 = new ViewerPlacementRecord(
         var1.getUniqueId(),
         var2.worldId,
         var2.viewDistance,
         this.lifecycle,
         var3,
         var4.getX(),
         var4.getY(),
         var4.getZ(),
         var5.getX(),
         var5.getZ(),
         var8,
         var9,
         var7
      );
      ExecutorService var11 = this.worker;
      if (var11 == null) {
         var2.needsFullUpdate.set(true);
         var2.sendPending.set(false);
      } else {
         try {
            var11.execute(() -> this.computePlacement(var2, var10));
         } catch (RejectedExecutionException var13) {
            var2.needsFullUpdate.set(true);
            var2.sendPending.set(false);
         }
      }
   }

   private void computePlacement(ChunkViewerState var1, ViewerPlacementRecord var2) {
      if (var1.viewDistance == var2.Wu && this.lifecycle == var2.Wv) {
         try {
            BlockRegionIndex var3 = this.blockStateManager
               .buildRegionIndex(var2.Wt, var2.WC - var2.WE, var2.WC + var2.WE, var2.WD - var2.WE, var2.WD + var2.WE);
            if (var1.viewDistance != var2.Wu || this.lifecycle != var2.Wv || !this.blockStateManager.regionIndexMatches(var2.Wt, var3)) {
               var1.needsFullUpdate.set(true);
               var1.sendPending.set(false);
               return;
            }

            LongKeyedBucketMap var4 = (LongKeyedBucketMap)this.bucketsByWorld.get(var2.Wt);
            LongKeyedBuckets var5 = var4 == null ? LongKeyedBuckets.EMPTY : var4.getSnapshot();
            synchronized (var1.blockStore) {
               SpatialMath.collectVisibleBlocks(
                  var3,
                  var1.blockStore,
                  var2.Wx,
                  var2.Wy,
                  var2.Wz,
                  var2.WA,
                  var2.WB,
                  (double)Settings.get().blockEntityRenderRadius * Settings.get().blockEntityRenderRadius,
                  VIEW_COS_THRESHOLD
               );
               expandLayerEntries(var1.blockStore, var5.blockKeys);
            }

            this.placementQueue.add(new ViewerPlacementBundle(var2, var3, var1));
         } catch (Throwable var9) {
            var9.printStackTrace();
            var1.needsFullUpdate.set(true);
            var1.sendPending.set(false);
         }
      } else {
         var1.needsFullUpdate.set(true);
         var1.sendPending.set(false);
      }
   }

   static void expandLayerEntries(PackedBlockStore var0, long[] var1) {
      int var2 = var0.heapSize;

      for (int var3 = 0; var3 < var2; var3++) {
         if (var0.heapLayers[var3] != 0) {
            for (long var7 : var1) {
               long var9 = var0.heapKeys[var3];
               if (isAdjacentLayerKey(var9, var0.heapLayers[var3], var7)) {
                  int var11 = ChunkKeyMath.unpackX(var9);
                  int var12 = ChunkKeyMath.unpackY(var9);
                  int var13 = ChunkKeyMath.unpackZ(var9);
                  int var14 = var0.heapValues[var3];
                  double var15 = var0.heapDistances[var3];
                  byte var17 = var0.heapLayers[var3];
                  var0.heapLayers[var3] = 0;

                  for (int var18 = 0; var18 < 3; var18++) {
                     for (int var19 = 0; var19 < 3; var19++) {
                        if (var19 != 0 || var18 != 0) {
                           var0.offerEntry(
                              var17 == 1
                                 ? ChunkKeyMath.packChunkKey(var11 + var19, var12 + var18, var13)
                                 : ChunkKeyMath.packChunkKey(var11 + var19, var12, var13 + var18),
                              var14,
                              (byte)0,
                              var15
                           );
                        }
                     }
                  }
                  break;
               }
            }
         }
      }
   }

   private static boolean isAdjacentLayerKey(long var0, byte var2, long var3) {
      int var5 = ChunkKeyMath.unpackX(var3) - ChunkKeyMath.unpackX(var0);
      int var6 = ChunkKeyMath.unpackZ(var3) - ChunkKeyMath.unpackZ(var0);
      int var7 = ChunkKeyMath.unpackY(var3) - ChunkKeyMath.unpackY(var0);

      return switch (var2) {
         case 1 -> var5 >= 0 && var5 < 3 && var7 >= 0 && var7 < 3 && var6 == 0;
         case 2 -> var5 >= 0 && var5 < 3 && var6 >= 0 && var6 < 3 && var7 == 0;
         default -> false;
      };
   }

   private void applyPlacement(ViewerPlacementRecord var1, BlockRegionIndex var2, ChunkViewerState var3) {
      Player var4 = Bukkit.getPlayer(var1.Ws);
      if (var4 != null
         && var4.isOnline()
         && this.viewerStates.get(var1.Ws) == var3
         && var3.viewDistance == var1.Wu
         && this.lifecycle == var1.Wv
         && var4.getWorld().getUID().equals(var1.Wt)
         && this.blockStateManager.regionIndexMatches(var1.Wt, var2)) {
         synchronized (var3.blockStore) {
            if (this.syncDisplaySlots(var4, var3.blockStore, var2, var1.Ww, var3.placementQueue)) {
               var3.needsFullUpdate.set(true);
               var3.renderPending.set(true);
            }

            var3.minSectionY = var3.blockStore.queuedCount == 0 ? Integer.MAX_VALUE : var1.Ww + Math.max(1, Settings.get().blockEntityWarmSlotTimeoutTicks);
         }

         var3.sendPending.set(false);
      } else {
         if (var4 != null && var4.isOnline() && this.viewerStates.get(var1.Ws) == var3) {
            var3.needsFullUpdate.set(true);
         }

         var3.sendPending.set(false);
      }
   }

   private boolean syncDisplaySlots(Player var1, PackedBlockStore var2, BlockRegionIndex var3, int var4, ArrayList var5) {
      PacketDisplayBridge var6 = NativePacketsHolder.PD();
      LongKeyedBucketMap var7 = (LongKeyedBucketMap)this.bucketsByWorld.get(var1.getWorld().getUID());
      LongKeyedBuckets var8 = var7 == null ? LongKeyedBuckets.EMPTY : var7.getSnapshot();
      var5.clear();
      boolean var9 = false;
      boolean var10 = false;

      for (int var11 = 0; var11 < var2.heapSize; var11++) {
         long var12 = var2.heapKeys[var11];
         byte var14 = var2.heapLayers[var11];
         int var15 = var2.slotOfWithLayer(var12, var14);
         var2.heapIds[var11] = var15;
         if (var15 >= 0) {
            int var16 = this.resolveDisplayState(var8, var12, var2.heapValues[var11], var14);
            int var17 = var2.slotStates[var15];
            var2.assignSlot(var15, var16, var14, var4);
            if (var17 != var16) {
               var5.add(new DisplayUpdateRecord(this.displayPool.keyAt(var15), 0.0, 0.0, 0.0, this.displayObjectOf(var16), false, false));
            }
         }
      }

      for (int var24 = 0; var24 < var2.heapSize; var24++) {
         long var26 = var2.heapKeys[var24];
         byte var30 = var2.heapLayers[var24];
         if (var2.heapIds[var24] < 0) {
            int var32 = this.resolveDisplayState(var8, var26, var2.heapValues[var24], var30);
            int var35 = var2.acquireSlotWithLayer(var26, var32, var30, var4);
            if (var35 >= 0) {
               var10 = true;
               var2.heapIds[var24] = var35;
               DisplayEntityKey var37 = this.displayPool.keyAt(var35);
               double var18 = keyX(var26, var30);
               double var20 = keyY(var26, var30);
               double var22 = keyZ(var26, var30);
               if (var2.slotScheduled[var35]) {
                  var5.add(new DisplayUpdateRecord(var37, var18, var20, var22, this.displayObjectOf(var32), false, true));
               } else {
                  var5.add(new DisplayUpdateRecord(var37, var18, var20, var22, this.displayObjectOf(var32), true, false));
                  var2.slotScheduled[var35] = true;
               }
            }
         }
      }

      int var25 = 0;

      for (int var27 = 0; var27 < var2.heapSize; var27++) {
         long var13 = var2.heapKeys[var27];
         byte var33 = var2.heapLayers[var27];
         if (var2.heapIds[var27] < 0) {
            while (var25 < var2.capacity() && (!var2.slotActive[var25] || var2.isSlotCurrent(var25))) {
               var25++;
            }

            if (var25 == var2.capacity()) {
               break;
            }

            int var36 = this.resolveDisplayState(var8, var13, var2.heapValues[var27], var33);
            int var38 = var2.replaceSlotWithLayer(var25, var13, var36, var33, var4);
            var10 = true;
            var5.add(
               new DisplayUpdateRecord(
                  this.displayPool.keyAt(var25),
                  keyX(var13, var33),
                  keyY(var13, var33),
                  keyZ(var13, var33),
                  var38 == var36 ? null : this.displayObjectOf(var36),
                  false,
                  true
               )
            );
            var25++;
         }
      }

      for (int var28 = 0; var28 < var2.capacity(); var28++) {
         if (var2.slotActive[var28] && !var2.isSlotCurrent(var28)) {
            DisplayEntityKey var29 = this.displayPool.keyAt(var28);
            boolean var31 = hasHeapEntry(var2, var2.slotKeys[var28], var2.slotLayers[var28]);
            boolean var34 = hasNeighborInPrimary(var2, var2.slotKeys[var28], var2.slotLayers[var28]);
            if (markSlotIfKept(var2, var28, var31, var34)) {
               var9 = true;
            } else {
               if (!var31 && !var34) {
                  var5.add(new DisplayUpdateRecord(var29, 0.0, -4096.0, 0.0, this.displayPool.wg(), false, true));
               }

               var2.releaseSlot(var28, var4);
               var10 = true;
               if (var31 || var34) {
                  var2.unlinkSlot(var28, var29.entityId());
               }
            }
         }
      }

      this.releaseStaleSlots(var2, var4);
      if (var10) {
         this.syncBlockKeys(var1, var2, var3);
      }

      if (!var5.isEmpty() || var2.removalCount > 0) {
         var6.sendPassengerUpdate(var1, var5, var2.pendingRemovals, var2.removalCount);
      }

      var5.clear();
      var2.removalCount = 0;
      return var9;
   }

   private void syncBlockKeys(Player var1, PackedBlockStore var2, BlockRegionIndex var3) {
      LongOpenHashSet var4 = var2.secondaryKeys;
      var4.clear();

      for (int var5 = 0; var5 < var2.capacity(); var5++) {
         if (var2.slotActive[var5]) {
            addKeyWithLayer(var4, var2.slotKeys[var5], var2.slotLayers[var5]);
         }
      }

      LongIterator var16 = var4.iterator();

      while (var16.hasNext()) {
         if (!isKeyInRegion(var3, var16.nextLong())) {
            var16.remove();
         }
      }

      World var6 = var1.getWorld();
      BlockDataBatch var7 = null;
      LongIterator var8 = var2.primaryKeys.iterator();

      while (var8.hasNext()) {
         long var9 = var8.nextLong();
         if (!var4.contains(var9)) {
            int var11 = ChunkKeyMath.unpackX(var9);
            int var12 = ChunkKeyMath.unpackY(var9);
            int var13 = ChunkKeyMath.unpackZ(var9);
            int var14 = stateIdInRegion(var3, var9);
            if (var14 < 0) {
               var14 = stateIdInSection(this.blockStateManager.getSection(var6.getUID(), var11 >> 4, var13 >> 4), var11, var12, var13);
            }

            if (var14 >= 0 || var6.isChunkLoaded(var11 >> 4, var13 >> 4)) {
               BlockData var15 = var14 >= 0 ? this.materialData[var14] : var6.getBlockAt(var11, var12, var13).getBlockData();
               if (var7 == null) {
                  var7 = new BlockDataBatch(List.of(var1));
               }

               var7.setBlockAt(var11, var12, var13, var15);
            }
         }
      }

      var16 = var4.iterator();

      while (var16.hasNext()) {
         long var18 = var16.nextLong();
         if (!var2.primaryKeys.contains(var18)) {
            if (var7 == null) {
               var7 = new BlockDataBatch(List.of(var1));
            }

            var7.setBlockAt(ChunkKeyMath.unpackX(var18), ChunkKeyMath.unpackY(var18), ChunkKeyMath.unpackZ(var18), this.barrierData);
         }
      }

      if (var7 != null) {
         var7.flush();
      }

      var2.primaryKeys.clear();
      var2.primaryKeys.addAll(var4);
      var4.clear();
   }

   private static boolean isKeyInRegion(BlockRegionIndex var0, long var1) {
      return stateIdInRegion(var0, var1) >= 0;
   }

   private static int stateIdInRegion(BlockRegionIndex var0, long var1) {
      int var3 = ChunkKeyMath.unpackX(var1);
      int var4 = ChunkKeyMath.unpackY(var1);
      int var5 = ChunkKeyMath.unpackZ(var1);
      return stateIdInSection(var0.getSectionAt(var3 >> 4, var5 >> 4), var3, var4, var5);
   }

   private static int stateIdInSection(BlockSectionData var0, int var1, int var2, int var3) {
      if (var0 == null) {
         return -1;
      }

      CompressedBlockArray var4 = var0.layerForKey(var2 >> 4);
      return var4 == null ? -1 : var4.getStateId(BlockStateManager.localBlockIndex(var1, var2, var3));
   }

   static void addKeyWithLayer(LongOpenHashSet var0, long var1, byte var3) {
      if (var3 == 0) {
         var0.add(var1);
      } else {
         int var4 = ChunkKeyMath.unpackX(var1);
         int var5 = ChunkKeyMath.unpackY(var1);
         int var6 = ChunkKeyMath.unpackZ(var1);

         for (int var7 = 0; var7 < 3; var7++) {
            for (int var8 = 0; var8 < 3; var8++) {
               var0.add(var3 == 1 ? ChunkKeyMath.packChunkKey(var4 + var8, var5 + var7, var6) : ChunkKeyMath.packChunkKey(var4 + var8, var5, var6 + var7));
            }
         }
      }
   }

   static boolean hasHeapEntry(PackedBlockStore var0, long var1, byte var3) {
      if (var3 != 0) {
         return false;
      }

      for (int var4 = 0; var4 < var0.heapSize; var4++) {
         if (var0.heapLayers[var4] != 0 && isAdjacentLayerKey(var0.heapKeys[var4], var0.heapLayers[var4], var1)) {
            return true;
         }
      }

      return false;
   }

   static boolean hasNeighborInPrimary(PackedBlockStore var0, long var1, byte var3) {
      if (var3 == 0) {
         return false;
      }

      int var4 = ChunkKeyMath.unpackX(var1);
      int var5 = ChunkKeyMath.unpackY(var1);
      int var6 = ChunkKeyMath.unpackZ(var1);

      for (int var7 = 0; var7 < 3; var7++) {
         for (int var8 = 0; var8 < 3; var8++) {
            long var9 = var3 == 1 ? ChunkKeyMath.packChunkKey(var4 + var8, var5 + var7, var6) : ChunkKeyMath.packChunkKey(var4 + var8, var5, var6 + var7);
            if (var0.containsKeyWithLayer(var9, (byte)0)) {
               return true;
            }
         }
      }

      return false;
   }

   static boolean markSlotIfKept(PackedBlockStore var0, int var1, boolean var2, boolean var3) {
      return (var2 || var3) && var0.markVisited(var1);
   }

   private int resolveDisplayState(LongKeyedBuckets var1, long var2, int var4, byte var5) {
      return var5 != 0 ? DisplayEntityPool.method9318(var4, var5) : DisplayEntityPool.method9317(var4, var1.stageFor(var2));
   }

   private Object displayObjectOf(int var1) {
      return this.displayPool.aE(var1);
   }

   private static double keyX(long var0, byte var2) {
      return ChunkKeyMath.unpackX(var0) + (var2 != 0 ? 1.5 : 0.5);
   }

   private static double keyY(long var0, byte var2) {
      return ChunkKeyMath.unpackY(var0) + (var2 == 1 ? 1.5 : 0.5);
   }

   private static double keyZ(long var0, byte var2) {
      return ChunkKeyMath.unpackZ(var0) + (var2 == 2 ? 1.5 : 0.5);
   }

   private void releaseStaleSlots(PackedBlockStore var1, int var2) {
      int var3 = Math.min(32, Math.max(1, var1.capacity() / 16));
      int var4 = Math.max(0, Settings.get().blockEntityWarmSlotTimeoutTicks);

      for (int var5 = 0; var5 < var1.capacity(); var5++) {
         if (var1.slotScheduled[var5] && !var1.slotActive[var5] && (var1.queuedCount > var3 || var2 - var1.slotPriorities[var5] >= var4)) {
            var1.unlinkSlot(var5, this.displayPool.keyAt(var5).entityId());
         }
      }
   }

   private void clearPlayerSlots(Player var1, PackedBlockStore var2) {
      synchronized (var2) {
         for (int var4 = 0; var4 < var2.capacity(); var4++) {
            if (var2.slotScheduled[var4]) {
               var2.unlinkSlot(var4, this.displayPool.keyAt(var4).entityId());
            }
         }

         if (var2.removalCount > 0) {
            NativePacketsHolder.PD().sendPassengers(var1, var2.pendingRemovals, var2.removalCount);
         }

         var2.clear();
      }
   }

   private void onViewerWorldChanged(Player var1, ChunkViewerState var2) {
      var2.viewDistance++;
      this.clearPlayerSlots(var1, var2.blockStore);
      var2.needsFullUpdate.set(true);
   }

   private boolean isRendererAvailable(Player var1) {
      return !ServerCompat.viaVersionPresent || this.viaVersionCache.computeIfAbsent(var1.getUniqueId(), var1x -> ViaVersionIntegration.method4872(var1));
   }

   private void updateViewerChunk(ChunkViewerState var1, UUID var2, double var3, double var5) {
      var1.cachedX = var3;
      var1.cachedZ = var5;
      long var7 = sectionKey(toSectionIndex(var3), toSectionIndex(var5));
      if (!var2.equals(var1.lastWorldId) || var7 != var1.lastUpdateMs) {
         this.viewerIndex.addViewer(var2, var7, var1);
         if (var1.lastWorldId != null) {
            this.viewerIndex.removeViewer(var1.lastWorldId, var1.lastUpdateMs, var1);
         }

         var1.lastWorldId = var2;
         var1.lastUpdateMs = var7;
      }
   }

   private void removeViewerFromIndex(ChunkViewerState var1) {
      if (var1.lastWorldId != null) {
         this.viewerIndex.removeViewer(var1.lastWorldId, var1.lastUpdateMs, var1);
         var1.lastWorldId = null;
      }
   }

   static int toSectionIndex(double var0) {
      return (int)Math.floor(var0 / 64.0);
   }

   static long sectionKey(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void removeViewer(Player var1) {
      this.removeViewerState(var1, true);
   }

   private void removeViewerState(Player var1, boolean var2) {
      ChunkViewerState var3 = (ChunkViewerState)this.viewerStates.remove(var1.getUniqueId());
      if (var3 != null) {
         this.removeViewerFromIndex(var3);
         var3.viewDistance++;
         if (var2) {
            this.clearPlayerSlots(var1, var3.blockStore);
         } else {
            synchronized (var3.blockStore) {
               var3.blockStore.clear();
            }
         }
      }
   }

   @EventHandler
   private void onPlayerMove(PlayerMoveEvent var1) {
      ChunkViewerState var2 = (ChunkViewerState)this.viewerStates.get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         Location var3 = var1.getTo();
         UUID var4 = var3.getWorld().getUID();
         if (var4.equals(var2.worldId)) {
            this.updateViewerChunk(var2, var4, var3.getX(), var3.getZ());
         }

         if (var2.hasViewChanged(var3)) {
            var2.needsFullUpdate.set(true);
         }
      }
   }

   @EventHandler
   private void onPlayerTeleport(PlayerTeleportEvent var1) {
      ChunkViewerState var2 = (ChunkViewerState)this.viewerStates.get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         Location var3 = var1.getTo();
         UUID var4 = var3.getWorld().getUID();
         if (var4.equals(var2.worldId)) {
            this.updateViewerChunk(var2, var4, var3.getX(), var3.getZ());
         }

         var2.needsFullUpdate.set(true);
      }
   }

   @EventHandler
   private void onPlayerRespawn(PlayerRespawnEvent var1) {
      this.removeViewer(var1.getPlayer());
      this.playerWorlds.put(var1.getPlayer().getUniqueId(), var1.getRespawnLocation().getWorld().getUID());
   }

   @EventHandler
   private void onPlayerChangedWorld(PlayerChangedWorldEvent var1) {
      this.removeViewer(var1.getPlayer());
      this.playerWorlds.put(var1.getPlayer().getUniqueId(), var1.getPlayer().getWorld().getUID());
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent var1) {
      this.playerWorlds.put(var1.getPlayer().getUniqueId(), var1.getPlayer().getWorld().getUID());
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.removeViewerState(var1.getPlayer(), false);
      this.playerWorlds.remove(var1.getPlayer().getUniqueId());
      this.viaVersionCache.remove(var1.getPlayer().getUniqueId());
   }
}
