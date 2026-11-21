package dev.lone.itemsadder.block;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.ChunkWithLightPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PickItemFromBlockPacketView;
import beer.devs.nativepackets.api.PlayerAction;
import beer.devs.nativepackets.api.PlayerActionPacketView;
import beer.devs.nativepackets.api.RespawnPacketView;
import beer.devs.nativepackets.api.SynchronizedPacketAccess;
import beer.devs.nativepackets.api.UnloadChunkPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.DisplayIndexPair;
import dev.lone.itemsadder.internal.SwitchMap87;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.DoubleStringPair;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockInteractionListener implements Listener {
   private static NamespacedKey mW;
   private static final Map mX = new ConcurrentHashMap();
   private static final Map mY = new WeakHashMap();
   public ChunkBlockTracker mZ;
   private BlockSoundScheduler na;
   public StepSoundTracker nb;
   private PacketListenerHandle nc;
   private PacketListenerHandle nd;
   private PacketListenerHandle ne;
   private PacketListenerHandle nf;
   private PacketListenerHandle ng;
   private final Map nh = new ConcurrentHashMap();
   private final Map ni = new ConcurrentHashMap();
   private final AtomicLong nj = new AtomicLong();
   private volatile boolean field50;
   private volatile long nk;

   public void onEnable() {
      mW = new NamespacedKey(Main.getInstance(), "lightweight_crops_enabled");
      this.mZ = new ChunkBlockTracker(Main.getInstance());
      this.na = new BlockSoundScheduler(Main.getInstance(), this.mZ, 1);
      this.nb = new StepSoundTracker(this);
      Bukkit.getOnlinePlayers().forEach(var1 -> this.nh.put(var1.getUniqueId(), var1.getWorld().getUID()));
      this.nc = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(), PacketSelector.exact(PacketTypes.PLAYER_ACTION), ListenerPriority.LOWEST, false, ListenerFailurePolicy.CANCEL_PACKET, var1 -> {
               if (this.isActive()) {
                  PlayerActionPacketView var2 = (PlayerActionPacketView)var1.view(PlayerActionPacketView.class);
                  PlayerAction var3 = var2.action();
                  if (var3 == PlayerAction.START_DESTROY_BLOCK || var3 == PlayerAction.ABORT_DESTROY_BLOCK || var3 == PlayerAction.STOP_DESTROY_BLOCK) {
                     UUID var4 = NativePacketsHolder.PE().method1883(var1);
                     if (var4 != null) {
                        long var5 = var1.connection().id();
                        int var7 = var2.blockX();
                        int var8 = var2.blockY();
                        int var9 = var2.blockZ();
                        Player var10 = NativePacketsHolder.PE().method1885(var4, var5);
                        if (var10 != null) {
                           PlayerStepState var11 = this.nb.getStateForPlayer(var4, var10);
                           if (var11 != null && var11.isInContinuousRange(var7, var8, var9)) {
                              var1.cancel();
                           }
                        }
                     }
                  }
               }
            }
         );
      this.nd = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.CHUNK_WITH_LIGHT),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> {
               if (this.isActive()) {
                  UUID var2 = NativePacketsHolder.PE().method1883(var1);
                  UUID var3 = var2 == null ? null : (UUID)this.nh.get(var2);
                  if (var3 != null) {
                     ChunkWithLightPacketView var4 = (ChunkWithLightPacketView)var1.view(ChunkWithLightPacketView.class);
                     BlockSessionKey var5 = new BlockSessionKey(var2, var3, var4.chunkX(), var4.chunkZ());
                     long var6 = this.nj.incrementAndGet();
                     this.ni.put(var5, var6);
                     BlockSessionTick var8 = new BlockSessionTick(
                        var5, var1.connection().id(), this.nk, var6, this.mZ.getChunkVersion(var3, var4.chunkX(), var4.chunkZ())
                     );
                     var1.afterWrite(var5x -> {
                        switch (SwitchMap87.np[var5x.outcome().ordinal()]) {
                           case 1:
                              this.scheduleCropDelivery(var8);
                              break;
                           case 2:
                           case 3:
                           case 4:
                              this.ni.remove(var5, var6);
                        }
                     });
                  }
               }
            }
         );
      this.ne = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.UNLOAD_CHUNK),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            this::onUnloadChunkPacket
         );
      this.nf = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(), PacketSelector.exact(PacketTypes.RESPAWN), ListenerPriority.MONITOR, false, ListenerFailurePolicy.PASS_ORIGINAL, var1 -> {
               if (this.isActive()) {
                  var1.view(RespawnPacketView.class);
                  UUID var2 = NativePacketsHolder.PE().method1883(var1);
                  if (var2 != null) {
                     PlayerTimedAction var3 = new PlayerTimedAction(var2, var1.connection().id(), this.nk);
                     var1.afterWrite(var2x -> {
                        switch (SwitchMap87.np[var2x.outcome().ordinal()]) {
                           case 1:
                              this.resetPlayerAfterRespawn(var3);
                           case 2:
                           case 3:
                           case 4:
                        }
                     });
                  }
               }
            }
         );
      if (Version.isAtLeast(Version.v1_21_4)) {
         this.ng = NativePacketsHolder.PE()
            .method1888(
               Main.getInstance(),
               PacketSelector.exact(PacketTypes.PICK_ITEM_FROM_BLOCK),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1 -> this.onPickItemPacket(var1, (PickItemFromBlockPacketView)var1.view(PickItemFromBlockPacketView.class))
            );
      }

      EventRegistrar.registerListener(this, Main.getInstance());
      this.reloadCropTracking();
      Bukkit.getOnlinePlayers().forEach(BlockInteractionListener::isLightweightCrops);
   }

   public void reloadCropTracking() {
      this.nk++;
      this.ni.clear();
      this.na.stop();
      this.nb.stop();
      if (this.mZ != null) {
         this.mZ.flushAll();
      }

      this.nb.applySettings();
      this.na.applySettings();
      ArrayList var1 = new ArrayList();

      for (World var3 : Bukkit.getWorlds()) {
         for (Chunk var7 : var3.getLoadedChunks()) {
            if (ChunkBlockTracker.hasCropData(var7)) {
               var1.add(var7);
            }
         }
      }

      AtomicInteger var8 = new AtomicInteger(0);
      new BlockInteractionTask(this, var8, var1).runTaskTimer(Main.getInstance(), 0L, 2L);
   }

   public void stop() {
      if (this.na != null) {
         this.na.stop();
      }

      if (this.nb != null) {
         this.nb.stop();
      }

      if (this.mZ != null) {
         this.mZ.flushAll();
      }
   }

   public void shutdown() {
      if (this.tryBeginShutdown()) {
         this.stop();
         if (this.nc != null) {
            NativePacketsHolder.PE().method1889(Main.getInstance(), this.nc);
         }

         if (this.nd != null) {
            NativePacketsHolder.PE().method1889(Main.getInstance(), this.nd);
         }

         if (this.ne != null) {
            NativePacketsHolder.PE().method1889(Main.getInstance(), this.ne);
         }

         if (this.nf != null) {
            NativePacketsHolder.PE().method1889(Main.getInstance(), this.nf);
         }

         if (this.ng != null) {
            NativePacketsHolder.PE().method1889(Main.getInstance(), this.ng);
         }

         this.nh.clear();
         this.ni.clear();
         EventRegistrar.unregisterListener(this);
      }
   }

   private void onUnloadChunkPacket(PacketEvent var1) {
      if (this.isActive()) {
         UUID var2 = NativePacketsHolder.PE().method1883(var1);
         UUID var3 = var2 == null ? null : (UUID)this.nh.get(var2);
         if (var3 != null) {
            DisplayIndexPair var4 = NativePacketsHolder.PD().toDisplayIndex(((UnloadChunkPacketView)var1.view(UnloadChunkPacketView.class)).position());
            BlockSessionKey var5 = new BlockSessionKey(var2, var3, var4.entityIndex(), var4.poseIndex());
            long var6 = this.nj.incrementAndGet();
            this.ni.put(var5, var6);
            BlockSessionRecord var8 = new BlockSessionRecord(var5, var1.connection().id(), this.nk, var6);
            var1.afterWrite(var2x -> {
               switch (SwitchMap87.np[var2x.outcome().ordinal()]) {
                  case 1:
                     this.scheduleChunkRelease(var8);
                     break;
                  case 2:
                  case 3:
                  case 4:
                     this.discardSession(var8);
               }
            });
         }
      }
   }

   private void scheduleChunkRelease(BlockSessionRecord var1) {
      if (this.isSessionCurrent(var1)) {
         Player var2 = NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId());
         if (var2 == null) {
            this.discardSession(var1);
         } else {
            try {
               NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> this.releaseChunkNow(var1), () -> this.discardSession(var1));
            } catch (RuntimeException | LinkageError var4) {
               this.discardSession(var1);
               Log.warn("Failed to schedule crop chunk release", var4);
            }
         }
      }
   }

   private void releaseChunkNow(BlockSessionRecord var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId());
      if (this.isSessionCurrent(var1) && var2 != null && var2.isOnline() && var1.key().worldId().equals(var2.getWorld().getUID())) {
         if (!this.nb
            .releaseChunkDisplaysIf(
               var2,
               var1.key().chunkX(),
               var1.key().chunkZ(),
               var2.getWorld(),
               () -> this.isSessionCurrent(var1)
                  && NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId()) == var2
                  && this.ni.remove(var1.key(), var1.chunkGeneration())
            )) {
            this.discardSession(var1);
         }
      } else {
         this.discardSession(var1);
      }
   }

   private boolean isSessionCurrent(BlockSessionRecord var1) {
      return this.isActive()
         && this.nk == var1.callbackGeneration()
         && var1.key().worldId().equals(this.nh.get(var1.key().playerId()))
         && Objects.equals(this.ni.get(var1.key()), var1.chunkGeneration());
   }

   private void discardSession(BlockSessionRecord var1) {
      this.ni.remove(var1.key(), var1.chunkGeneration());
   }

   private void scheduleCropDelivery(BlockSessionTick var1) {
      if (this.isTickCurrent(var1)) {
         try {
            NativePacketsHolder.PE().IP().globalLater(Main.getInstance(), () -> this.scheduleDeliveryOwner(var1), 5L);
         } catch (RuntimeException | LinkageError var3) {
            this.ni.remove(var1.key(), var1.chunkGeneration());
            Log.warn("Failed to schedule crop chunk delivery", var3);
         }
      }
   }

   private void scheduleDeliveryOwner(BlockSessionTick var1) {
      if (this.isTickCurrent(var1)) {
         Player var2 = NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId());
         if (var2 == null) {
            this.ni.remove(var1.key(), var1.chunkGeneration());
         } else {
            try {
               NativePacketsHolder.PE()
                  .IP()
                  .entity(Main.getInstance(), var2, () -> this.scheduleDeliveryRegion(var1), () -> this.ni.remove(var1.key(), var1.chunkGeneration()));
            } catch (RuntimeException | LinkageError var4) {
               this.ni.remove(var1.key(), var1.chunkGeneration());
               Log.warn("Failed to schedule crop chunk player ownership", var4);
            }
         }
      }
   }

   private void scheduleDeliveryRegion(BlockSessionTick var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId());
      if (this.isTickCurrent(var1) && var2 != null && var2.isOnline() && var1.key().worldId().equals(var2.getWorld().getUID())) {
         World var3 = var2.getWorld();
         Location var4 = new Location(var3, (var1.key().chunkX() << 4) + 8, var3.getMinHeight(), (var1.key().chunkZ() << 4) + 8);

         try {
            NativePacketsHolder.PE().IP().region(Main.getInstance(), var4, () -> this.deliverCropChunk(var1, var3));
         } catch (RuntimeException | LinkageError var6) {
            this.ni.remove(var1.key(), var1.chunkGeneration());
            Log.warn("Failed to schedule crop chunk region ownership", var6);
         }
      } else {
         this.ni.remove(var1.key(), var1.chunkGeneration());
      }
   }

   private void deliverCropChunk(BlockSessionTick var1, World var2) {
      Player var3 = NativePacketsHolder.PE().method1885(var1.key().playerId(), var1.connectionId());
      if (this.isTickCurrent(var1)
         && var3 != null
         && var1.key().worldId().equals(this.nh.get(var1.key().playerId()))
         && this.mZ.getChunkVersion(var1.key().worldId(), var1.key().chunkX(), var1.key().chunkZ()) == var1.repositoryGeneration()
         && BlockFaceUtils.isChunkLoadedAtCoords(var2, var1.key().chunkX(), var1.key().chunkZ())) {
         Chunk var4 = var2.getChunkAt(var1.key().chunkX(), var1.key().chunkZ());

         try {
            this.mZ.sendChunkCrops(var3, var4);
         } finally {
            this.ni.remove(var1.key(), var1.chunkGeneration());
         }
      } else {
         this.ni.remove(var1.key(), var1.chunkGeneration());
      }
   }

   private boolean isTickCurrent(BlockSessionTick var1) {
      return this.isActive() && this.nk == var1.callbackGeneration() && Objects.equals(this.ni.get(var1.key()), var1.chunkGeneration());
   }

   private void resetPlayerAfterRespawn(PlayerTimedAction var1) {
      if (this.isActive() && this.nk == var1.callbackGeneration()) {
         Player var2 = NativePacketsHolder.PE().method1885(var1.playerId(), var1.connectionId());
         if (var2 != null) {
            try {
               NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> {
                  Player var3 = NativePacketsHolder.PE().method1885(var1.playerId(), var1.connectionId());
                  if (this.isActive() && this.nk == var1.callbackGeneration() && var3 == var2 && var2.isOnline()) {
                     this.nb.resetPlayer(var2);
                  }
               }, () -> {});
            } catch (RuntimeException | LinkageError var4) {
               Log.warn("Failed to schedule crop respawn reset", var4);
            }
         }
      }
   }

   private void onPickItemPacket(PacketEvent var1, PickItemFromBlockPacketView var2) {
      BlockSessionCounter var3 = new BlockSessionCounter(var1.connection().id(), var2.blockX(), var2.blockY(), var2.blockZ());
      var1.sync(
         (var2x, var3x) -> {
            if (this.isActive() && var3x.player() instanceof Player var4 && var4.isOnline() && var4.getGameMode() == GameMode.CREATIVE) {
               BlockDisplayRegistry var11 = this.mZ.getDisplayRegistry(var4);
               BlockDisplayEntry var6 = var11 == null ? null : var11.raycastEntry(var4);
               if (var6 != null) {
                  Location var7 = var6.getLocation();
                  World var8 = var7.getWorld();
                  if (var8 != null && var8.getUID().equals(var4.getWorld().getUID())) {
                     var2x.cancel();
                     BlockSessionEntry var9 = new BlockSessionEntry(
                        var4.getUniqueId(), var3.connectionId(), var8.getUID(), var7.getBlockX(), var7.getBlockY(), var7.getBlockZ()
                     );
                     Location var10 = (Location)var3x.location(var9.x() + 0.5, var9.y() + 0.5, var9.z() + 0.5);
                     var2x.syncAt(var10, (var2xx, var3xx) -> this.resolvePickedBlock(var2xx, var3xx, var9));
                  }
               }
            }
         }
      );
   }

   private void resolvePickedBlock(PacketEvent var1, SynchronizedPacketAccess var2, BlockSessionEntry var3) {
      if (this.isActive()) {
         Block var4 = (Block)var2.block(var3.x(), var3.y(), var3.z());
         if (var4 != null) {
            ByteNamedKey var5 = this.mZ.getBlockStateKey(var4);
            if (var5 != null) {
               this.schedulePickResult(new BlockSessionMessage(var3, var5.namespacedId()));
            }
         }
      }
   }

   private void schedulePickResult(BlockSessionMessage var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.target().playerId(), var1.target().connectionId());
      if (var2 == null) {
         Log.warn("Skipped custom crop pick result because the player connection ended: " + var1.target().playerId());
      } else {
         try {
            NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> this.applyPickResult(var1), () -> this.discardPickResult(var1));
         } catch (RuntimeException | LinkageError var4) {
            Log.warn("Failed to schedule custom crop pick result", var4);
         }
      }
   }

   private void applyPickResult(BlockSessionMessage var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.target().playerId(), var1.target().connectionId());
      if (this.isActive()) {
         if (var2 == null) {
            Log.warn("Skipped custom crop pick result because the player connection changed: " + var1.target().playerId());
         } else if (!NativePacketsHolder.PE().IP().ownsEntity(var2)) {
            Log.warn("Skipped custom crop pick result outside player ownership: " + var1.target().playerId());
         } else if (var2.isOnline() && var2.getGameMode() == GameMode.CREATIVE && var2.getWorld().getUID().equals(var1.target().worldId())) {
            BlockSoundProfile var3 = (BlockSoundProfile)ContentManager.method8084().au.get(var1.namespacedId());
            if (var3 != null) {
               pickItemIntoHotbar(var2.getInventory(), var3.jQ.Ds());
            }
         }
      }
   }

   private void discardPickResult(BlockSessionMessage var1) {
      if (this.isActive()) {
         Log.warn("Skipped custom crop pick result because player ownership retired: " + var1.target().playerId());
      }
   }

   private static void pickItemIntoHotbar(PlayerInventory var0, ItemStack var1) {
      for (int var2 = 0; var2 <= 8; var2++) {
         if (!ItemStackUtils.aE(var0.getItem(var2)) && CustomBlockDefinition.c(var0.getItem(var2), var1)) {
            var0.setHeldItemSlot(var2);
            var0.setItem(var2, var1);
            return;
         }
      }

      if (ItemStackUtils.aE(var0.getItemInMainHand())) {
         var0.setItemInMainHand(var1);
      } else {
         for (int var3 = 0; var3 <= 8; var3++) {
            if (ItemStackUtils.aE(var0.getItem(var3))) {
               var0.setHeldItemSlot(var3);
               var0.setItem(var3, var1);
               return;
            }
         }

         var0.setItemInMainHand(var1);
      }
   }

   boolean tryBeginShutdown() {
      if (this.field50) {
         return false;
      }

      this.field50 = true;
      this.nk++;
      return true;
   }

   boolean isActive() {
      return !this.field50;
   }

   public static boolean isLightweightCrops(Player var0) {
      Boolean var1 = (Boolean)mX.get(var0);
      if (var1 != null) {
         return var1;
      }

      setLightweightCrops(var0, false);
      return false;
   }

   public static void setLightweightCrops(Player var0, boolean var1) {
      var0.getPersistentDataContainer().set(mW, PersistentDataType.BOOLEAN, var1);
      mX.put(var0, var1);
   }

   public boolean isOnActionCooldown(@NotNull Player var1) {
      return System.currentTimeMillis() - mY.getOrDefault(var1, 0L) < 60L;
   }

   private void markActionTime(Player var1) {
      mY.put(var1, System.currentTimeMillis());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerInteract(PlayerInteractEvent var1) {
      Player var2 = var1.getPlayer();
      Block var3 = var1.getClickedBlock();
      if (var1.getHand() != null) {
         Block var4;
         if (isLightweightCrops(var2)) {
            if (var3 == null) {
               return;
            }

            var4 = var3;
            if (var3.getType() == Material.AIR) {
               if (var1.getAction() == Action.LEFT_CLICK_BLOCK) {
                  this.breakCropAt(var4, null, var2, true);
                  return;
               }
            } else {
               var4 = var3.getRelative(var1.getBlockFace());
            }
         } else {
            BlockDisplayRegistry var5 = this.mZ.getDisplayRegistry(var2);
            BlockDisplayEntry var6 = var5 == null ? null : var5.raycastEntryInternal(var2, false);
            if (var6 != null) {
               var4 = var6.getBlock();
               var1.setCancelled(true);
            } else {
               if (var3 == null) {
                  return;
               }

               var4 = var3.getRelative(var1.getBlockFace());
               if (this.mZ.isCustomCrop(var4)) {
                  return;
               }
            }
         }

         this.handleInteraction(var2, var1.getHand(), null, var4, var1.getItem(), EventRegistrar.isLeftClick(var1), var1);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onInteractEntity(PlayerInteractEntityEvent var1) {
      if (var1.getRightClicked() instanceof Interaction var2) {
         BlockDisplayEntry var4 = this.findInteractedDisplay(var1.getPlayer(), var2);
         if (var4 != null) {
            var1.setCancelled(true);
            this.handleInteraction(var1.getPlayer(), var1.getHand(), var2, var4.getBlock(), null, false, null);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onEntityDamageByEntity(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Player var2 && var1.getEntity() instanceof Interaction var3) {
         BlockDisplayEntry var6 = this.findInteractedDisplay(var2, var3);
         if (var6 != null) {
            var1.setCancelled(true);
            this.breakCropAt(var6.getBlock(), var6, var2, true);
         }
      }
   }

   @Nullable
   private BlockDisplayEntry findInteractedDisplay(Player var1, Interaction var2) {
      PlayerStepState var3 = this.nb.getState(var1);
      BlockDisplayRegistry var4 = this.mZ.getDisplayRegistry(var1);
      if (var3 != null && var4 != null && var3.isOwnInteraction(var2) && var3.lx != null) {
         BlockDisplayEntry var5 = var4.raycastEntryInternal(var1, false);
         if (var5 != null && var5.getBlock().equals(var3.lx.getBlock())) {
            var3.extendHitboxShort();
            return var5;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void handleInteraction(
      Player var1,
      EquipmentSlot var2,
      @Nullable Interaction var3,
      @Nullable Block var4,
      @Nullable ItemStack var5,
      boolean var6,
      @Nullable PlayerInteractEvent var7
   ) {
      if (var1.getGameMode() != GameMode.SPECTATOR) {
         if (var3 == null && var4 == null) {
            throw new IllegalArgumentException("Either interaction or targetBlock must be non-null");
         }

         if (var3 != null && var4 == null) {
            var4 = var3.getLocation().getBlock();
         }

         if (var5 == null) {
            var5 = var1.getInventory().getItem(var2);
         }

         try {
            ByteNamedKey var8 = this.mZ.getBlockStateKey(var4);
            BlockDisplayRegistry var9 = this.mZ.getDisplayRegistry(var1);
            if (var8 != null) {
               PlayerStepState var10 = this.nb.getState(var1);
               if (var10 != null) {
                  var10.extendHitboxShort();
                  if (var6) {
                     var10.refreshContinuousTimer();
                  }
               }
            }

            if (var9 != null && var5.getType() == Material.BONE_MEAL && !var6 && var8 != null) {
               BlockSoundProfile var17 = (BlockSoundProfile)ContentManager.method8084().au.get(var8.namespacedId());
               if (var17 == null) {
                  Log.warn(
                     var1.getName()
                        + " interacted with an unknown crop '"
                        + var8.namespacedId()
                        + "' at "
                        + BlockFaceUtils.formatLocation(var4.getLocation(), true)
                  );
                  return;
               }

               int var11 = Math.min(var8.age() + (int)Math.ceil(var17.jW * var17.ke), var17.jW);
               if (var8.age() < var17.jW) {
                  if (var1.getGameMode() != GameMode.CREATIVE) {
                     ItemStackUtils.au(var5);
                  }

                  this.mZ.setBlockStateInt(var4, var8.namespacedId(), var11);
                  BlockDropHelper.playBoneMealEffect(var4);
                  StepSoundTracker.markStepUpdate(var1, true);
                  return;
               }
            }

            Location var18 = var4.getLocation();
            if (var9 != null && var2 == EquipmentSlot.HAND && var8 != null) {
               if (!BlockFaceUtils.simulateBreak(var18.getBlock().getRelative(BlockFace.DOWN), var1)) {
                  return;
               }

               if (var6) {
                  BlockDisplayEntry var19 = var9.raycastEntry(var1);
                  if (var19 != null && var19.mr instanceof ItemDisplay var12) {
                     Block var23 = var12.getLocation().getBlock();
                     if (!this.breakCropAt(var23, var19, var1, true)) {
                        return;
                     }
                  }
               } else {
                  BlockSoundProfile var20 = (BlockSoundProfile)ContentManager.method8084().au.get(var8.namespacedId());
                  if (var20 == null) {
                     return;
                  }

                  if (var20.handleInteraction(var1, var8.age(), var18, BlockInteractionKind.INTERACT, true)) {
                     return;
                  }
               }
            }

            CustomItemDefinition var21 = ContentManager.method8084().method8149(var5);
            if (var21 != null && !var6) {
               BlockSoundProfile var22 = (BlockSoundProfile)ContentManager.method8084().av.get(var21);
               if (var22 != null) {
                  if (var4.getType().isAir() || var4.isReplaceable()) {
                     if (var22.matchesPlacementBase(var4)) {
                        if (var18.getBlock().getType().isAir()) {
                           if (var22.matchesBottomBlock(var18.getBlock())) {
                              if (var2 != EquipmentSlot.HAND || var8 == null) {
                                 if (BlockFaceUtils.simulatePlace(var18.getBlock(), var1)) {
                                    if (this.isPlacementBlocked(var18, var22)) {
                                       var18.getWorld().spawnParticle(Particle.SMOKE, var18.add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
                                    } else if (this.isCropAreaFull(var18)) {
                                       var18.getWorld().spawnParticle(Particle.SMOKE, var18.add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
                                       DoubleStringPair var25 = this.getCropLimitDirections(var18);
                                       if (var25 != null) {
                                          Log.sendLocalizedWith(var1, "max-crops-reached-directions", var1x -> {
                                             var1x = var1x.replace("{direction}", var25.getText());
                                             return var1x.replace("{distance}", String.format("%.1f", var25.getNumber()));
                                          });
                                       } else {
                                          Log.sendLocalized(var1, "max-crops-reached-no-directions");
                                       }
                                    } else {
                                       if (var7 != null) {
                                          var7.setCancelled(true);
                                       }

                                       if (var1.getGameMode() == GameMode.SURVIVAL) {
                                          ItemStackUtils.au(var5);
                                       }

                                       this.mZ.setBlockStateInt(var18.getBlock(), var22.jP, 0);
                                       var22.playPlaceSound(var18);
                                       if (var2 == EquipmentSlot.HAND) {
                                          var1.swingMainHand();
                                       } else {
                                          var1.swingOffHand();
                                       }

                                       if (var9 != null) {
                                          BlockDisplayEntry var24 = var9.raycastEntry(var1);
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
         } finally {
            this.refreshCropStateLater(var1, var4);
         }
      }
   }

   @Nullable
   private DoubleStringPair getCropLimitDirections(Location var1) {
      int var2 = Settings.get().cropAreaLimitRadius;
      World var3 = var1.getWorld();
      if (var3 != null) {
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;

         for (int var7 = -var2; var7 <= var2; var7++) {
            for (int var8 = -var2; var8 <= var2; var8++) {
               if (this.mZ.hasCustomCrop(var1.getBlockX() + var7, var1.getBlockY(), var1.getBlockZ() + var8, var3, false)) {
                  var4 += var7;
                  var5 += var8;
                  var6++;
               }
            }
         }

         if (var6 > 0) {
            double var22 = (double)var4 / var6;
            double var9 = (double)var5 / var6;
            double var11 = -var22;
            double var13 = -var9;
            double var16 = Math.toDegrees(Math.atan2(var13, var11)) + 180.0;
            String[] var18 = new String[]{"west", "north-west", "north", "north-east", "east", "south-east", "south", "south-west"};
            int var19 = (int)Math.round(var16 / 45.0) % 8;
            String var15 = var18[var19];
            double var20 = var2 - Math.sqrt(var22 * var22 + var9 * var9);
            if (var20 < 1.0) {
               var20 = 1.0;
            }

            return new DoubleStringPair(var20, var15);
         }
      }

      return null;
   }

   public boolean isCropAreaFull(Location var1) {
      int var2 = Settings.get().cropAreaLimitRadius;
      int var3 = Settings.get().cropMaxInArea;
      World var4 = var1.getWorld();
      if (var4 == null) {
         return false;
      }

      int var5 = 0;
      HashSet var6 = new HashSet();
      int var7 = (int)Math.ceil(var2 / 16.0);
      int var8 = var1.getBlockX() >> 4;
      int var9 = var1.getBlockZ() >> 4;

      for (int var10 = var8 - var7; var10 <= var8 + var7; var10++) {
         for (int var11 = var9 - var7; var11 <= var9 + var7; var11++) {
            long var12 = (long)var10 << 32 | var11 & 4294967295L;
            if (!var6.contains(var12)) {
               var6.add(var12);
               Chunk var14 = var4.getChunkAt(var10, var11);
               BlockStateTable var15 = this.mZ.getChunkTable(var14);
               if (var15 != null) {
                  if (var15.size() >= var3) {
                     return true;
                  }

                  var5 += var15.size();
                  if (var5 >= var3) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean isPlacementBlocked(Location var1, BlockSoundProfile var2) {
      if (var2.kd != null && var2.kd > 0) {
         World var3 = var1.getWorld();
         if (var3 == null) {
            return false;
         }

         int var4 = var2.kd;
         int var5 = var1.getBlockX();
         int var6 = var1.getBlockY();
         int var7 = var1.getBlockZ();

         for (int var8 = -var4; var8 <= var4; var8++) {
            for (int var9 = -var4; var9 <= var4; var9++) {
               if (var8 != 0 || var9 != 0) {
                  int var10 = Math.max(Math.abs(var8), Math.abs(var9));
                  if (var10 < var4 && BlockFaceUtils.isChunkLoadedAtCoords(var3, var5 + var8 >> 4, var7 + var9 >> 4)) {
                     Block var11 = var3.getBlockAt(var5 + var8, var6, var7 + var9);
                     if (this.mZ.isCustomCrop(var11)) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void refreshCropStateLater(Player var1, Block var2) {
      if (isLightweightCrops(var1)) {
         ByteNamedKey var3 = this.mZ.getBlockStateKey(var2);
         if (var3 != null) {
            SchedulerUtils.runLaterSync(() -> {
               if (this.isActive()) {
                  this.mZ.sendCropState(var1, var2.getLocation(), var3);
               }
            }, 3L);
         }
      }
   }

   @EventHandler
   private void onArmSwing(PlayerAnimationEvent var1) {
      if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
         Player var2 = var1.getPlayer();
         PlayerStepState var3 = this.nb.getState(var2);
         if (var3 != null) {
            var3.extendHitboxShort();
            var3.refreshContinuousTimer();
         }

         this.tryBreakTargetedCrop(var2);
      }
   }

   void tickPlayerState(Player var1, PlayerStepState var2) {
      if (var2.isContinuousActive()) {
         this.tryBreakTargetedCrop(var1);
      }
   }

   private void tryBreakTargetedCrop(Player var1) {
      if (var1.getGameMode() != GameMode.CREATIVE || !CustomBlockListener.dn.method728(var1)) {
         if (!CustomBlockListener.dn.method718(var1)) {
            if (!CustomBlockListener.dn.method726(var1)) {
               if (!this.isOnActionCooldown(var1)) {
                  BlockDisplayRegistry var2 = this.mZ.getDisplayRegistry(var1);
                  if (var2 != null) {
                     BlockDisplayEntry var3 = var2.raycastEntry(var1);
                     if (var3 != null && var3.mr instanceof ItemDisplay var4) {
                        this.breakCropAt(var4.getLocation().getBlock(), var3, var1, true);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean breakCropAt(Block var1, BlockDisplayEntry var2, Player var3, boolean var4) {
      ByteNamedKey var5 = this.mZ.getBlockStateKey(var1);
      if (var5 == null) {
         return false;
      }

      BlockSoundProfile var6 = (BlockSoundProfile)ContentManager.method8084().au.get(var5.namespacedId());
      if (var6 == null) {
         return false;
      }

      if (var4 && !BlockFaceUtils.simulateBreak(var1.getRelative(BlockFace.DOWN), var3)) {
         return false;
      }

      PlayerStepState var7 = this.nb.getState(var3);
      if (var7 != null) {
         var7.extendHitboxLong();
         var7.startContinuousHarvest();
      }

      if (var2 != null) {
         BoundingBox var8 = var2.getBounds().toBoundingBox();
         ItemStack var9 = var2.mr.getItemStack();
         SchedulerUtils.runSync(() -> BlockDropHelper.spawnItemParticlesInBox(var1, var8, var9));
      }

      this.mZ.removeBlockStateAt(var1);
      if (var2 != null) {
         BlockDisplayRegistry var10 = this.mZ.getDisplayRegistry(var3);
         if (var10 != null) {
            var10.releaseByUuid(var2.mr.getUniqueId());
         }
      }

      if (var7 != null) {
         var7.hideDisplays();
      }

      var6.handleInteraction(var3, var5.age(), var1.getLocation(), BlockInteractionKind.BREAK, true);
      if (isLightweightCrops(var3)) {
         var3.sendBlockChange(var1.getLocation(), var1.getBlockData());
      }

      this.markActionTime(var3);
      return true;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onLiquidFlow(BlockFromToEvent var1) {
      if (var1.getBlock().getType() == Material.WATER) {
         ByteNamedKey var2 = this.mZ.getBlockStateKey(var1.getToBlock());
         if (var2 != null) {
            this.mZ.removeBlockStateAt(var1.getToBlock());
            BlockSoundProfile var3 = (BlockSoundProfile)ContentManager.method8084().au.get(var2.namespacedId());
            var3.handleInteraction(null, var2.age(), var1.getToBlock().getLocation(), BlockInteractionKind.WATER, true);
         }
      } else if (var1.getBlock().getType() == Material.LAVA) {
         ByteNamedKey var4 = this.mZ.getBlockStateKey(var1.getToBlock());
         if (var4 != null) {
            this.mZ.removeBlockStateAt(var1.getToBlock());
            BlockSoundProfile var5 = (BlockSoundProfile)ContentManager.method8084().au.get(var4.namespacedId());
            var5.handleInteraction(null, var4.age(), var1.getToBlock().getLocation(), BlockInteractionKind.LAVA, true);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPistonExtend(BlockPistonExtendEvent var1) {
      Block var2 = null;
      int var3 = Integer.MIN_VALUE;

      for (Block var5 : var1.getBlocks()) {
         int var6 = 0;
         switch (SwitchMap87.nq[var1.getDirection().ordinal()]) {
            case 1:
               var6 = var5.getY();
               break;
            case 2:
               var6 = -var5.getY();
               break;
            case 3:
               var6 = -var5.getZ();
               break;
            case 4:
               var6 = var5.getZ();
               break;
            case 5:
               var6 = var5.getX();
               break;
            case 6:
               var6 = -var5.getX();
         }

         if (var6 > var3) {
            var3 = var6;
            var2 = var5;
         }
      }

      var2 = Objects.requireNonNullElseGet(var2, var1::getBlock).getRelative(var1.getDirection());
      if (this.mZ.isCustomCrop(var2)) {
         ByteNamedKey var11 = this.mZ.getBlockStateKey(var2);
         assert var11 != null;
         this.mZ.removeBlockStateAt(var2);
         BlockSoundProfile var12 = (BlockSoundProfile)ContentManager.method8084().au.get(var11.namespacedId());
         var12.handleInteraction(null, var11.age(), var2.getLocation(), BlockInteractionKind.BREAK, true);

         for (Entry var7 : mX.entrySet()) {
            Player var8 = (Player)var7.getKey();
            Boolean var9 = (Boolean)var7.getValue();
            if (var9) {
               this.mZ.sendCropState(var8, var2.getLocation(), var11);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   public void onFertilize(BlockFertilizeEvent var1) {
      if (var1.getPlayer() != null && this.mZ.isCustomCrop(var1.getBlock().getRelative(CustomBlockListener.dn.method714(var1.getPlayer())))) {
         var1.setCancelled(true);
      }

      List var2 = var1.getBlocks()
         .stream()
         .filter(
            var1x -> this.mZ.isCustomCrop(var1x.getBlock())
               || (var1x.getType() == Material.TALL_GRASS || var1x.getType() == Material.LARGE_FERN)
                  && (this.mZ.isCustomCrop(var1x.getBlock().getRelative(BlockFace.UP)) || this.mZ.isCustomCrop(var1x.getBlock().getRelative(BlockFace.DOWN)))
         )
         .toList();
      if (!var2.isEmpty()) {
         var1.getBlocks().removeAll(var2);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   public void onPlayerInteractSecondary(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.PHYSICAL) {
         if (var1.getClickedBlock() != null && var1.getClickedBlock().getType() == Material.FARMLAND) {
            Block var2 = var1.getClickedBlock();
            Block var3 = var2.getRelative(0, 1, 0);
            if (this.mZ.isCustomCrop(var3)) {
               ByteNamedKey var4 = this.mZ.getBlockStateKey(var3);
               assert var4 != null;
               BlockSoundProfile var5 = (BlockSoundProfile)ContentManager.method8084().au.get(var4.namespacedId());
               this.mZ.removeBlockStateAt(var3);
               var5.handleInteraction(null, var4.age(), var3.getLocation(), BlockInteractionKind.TRAMPLE, true);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   public void onBlockFade(BlockFadeEvent var1) {
      if (var1.getBlock().getType() == Material.FARMLAND) {
         this.removeCropAboveFarmland(var1.getBlock());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   public void onEntityExplode(EntityExplodeEvent var1) {
      for (Block var3 : var1.blockList()) {
         this.removeCropAboveFarmland(var3);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   public void onBlockExplode(BlockExplodeEvent var1) {
      for (Block var3 : var1.blockList()) {
         this.removeCropAboveFarmland(var3);
      }
   }

   private void removeCropAboveFarmland(Block var1) {
      Block var2 = var1.getRelative(0, 1, 0);
      if (this.mZ.isCustomCrop(var2)) {
         ByteNamedKey var3 = this.mZ.getBlockStateKey(var2);
         assert var3 != null;
         BlockSoundProfile var4 = (BlockSoundProfile)ContentManager.method8084().au.get(var3.namespacedId());
         this.mZ.removeBlockStateAt(var2);
         var4.handleInteraction(null, var3.age(), var2.getLocation(), BlockInteractionKind.FARMLAND_DRY, true);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onCanBuild(BlockCanBuildEvent var1) {
      if (this.mZ.isCustomCrop(var1.getBlock())) {
         var1.setBuildable(false);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onCustomBlockPlace(CustomBlockPlaceEvent var1) {
      if (this.mZ.isCustomCrop(var1.getBlock())) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onBlockBreak(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (this.isOnActionCooldown(var1.getPlayer())) {
               var1.setCancelled(true);
               return;
            }

            BlockDisplayRegistry var2 = this.mZ.getDisplayRegistry(var1.getPlayer());
            if (var2 == null) {
               return;
            }

            BlockDisplayEntry var3 = var2.raycastEntry(var1.getPlayer());
            if (var3 != null) {
               Block var4 = var1.getBlock();
               if (var4.getType().getHardness() > 0.0F) {
                  var1.setCancelled(true);
                  if (var1.getPlayer().getGameMode() == GameMode.CREATIVE) {
                     this.breakCropAt(var3.getBlock(), var3, var1.getPlayer(), false);
                  }

                  return;
               }
            }
         }

         Block var5 = var1.getBlock().getRelative(BlockFace.UP);
         this.breakCropAt(var5, null, var1.getPlayer(), false);
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent var1) {
      this.nh.remove(var1.getPlayer().getUniqueId());
      this.ni.keySet().removeIf(var1x -> var1x.playerId().equals(var1.getPlayer().getUniqueId()));
      mX.remove(var1.getPlayer());
      mY.remove(var1.getPlayer());
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent var1) {
      this.nh.put(var1.getPlayer().getUniqueId(), var1.getPlayer().getWorld().getUID());
      isLightweightCrops(var1.getPlayer());
   }

   @EventHandler
   public void onWorldChange(PlayerChangedWorldEvent var1) {
      UUID var2 = var1.getPlayer().getUniqueId();
      this.nh.put(var2, var1.getPlayer().getWorld().getUID());
      this.ni.keySet().removeIf(var1x -> var1x.playerId().equals(var2));
   }
}
