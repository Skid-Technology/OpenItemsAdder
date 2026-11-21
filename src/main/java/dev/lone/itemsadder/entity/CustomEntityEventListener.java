package dev.lone.itemsadder.entity;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.ClientCommandAction;
import beer.devs.nativepackets.api.ClientCommandPacketView;
import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.EntityMetadataPacketView;
import beer.devs.nativepackets.api.HealthPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerInputPacketView;
import beer.devs.nativepackets.api.EntityMetadataFields.LivingEntity;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.MythicMobsHook;
import dev.lone.itemsadder.ai.MobGoalEventListener;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.event.AbstractPlayerTrigger;
import dev.lone.itemsadder.network.EquipmentPacketListener;
import dev.lone.itemsadder.network.InteractPacketListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.PassengersPacketListener;
import dev.lone.itemsadder.network.PlayerInputListener;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.network.ViewerPacketListener;
import dev.lone.itemsadder.network.ViewerSession;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.DoubleVector3Dup2;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PlayerActionBar;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class CustomEntityEventListener implements Listener {
   private InteractPacketListener Uk;
   private PlayerInputListener Ul;
   private PassengersPacketListener Um;
   private EquipmentPacketListener Un;
   private ViewerPacketListener Uo;
   private final Map Up = new HashMap();
   private final Set Uq = new HashSet();
   private final Map Ur = new ConcurrentHashMap();
   private PacketListenerHandle Us;
   private PacketListenerHandle Ut;
   private PacketListenerHandle Uu;
   private PacketListenerHandle Uv;
   private PacketListenerHandle Uw;
   private PacketListenerHandle Ux;

   public void register(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
      Bukkit.getPluginManager().registerEvents(new MobGoalEventListener(), plugin);
      Bukkit.getPluginManager().registerEvents(new EntityLoadListener(), plugin);
      this.Uk = new InteractPacketListener();
      if (Version.isOlderThan(Version.v1_21_3)) {
         this.Ul = new PlayerInputListener();
      }

      this.Um = new PassengersPacketListener();
      this.Uo = new ViewerPacketListener();
      this.Un = new EquipmentPacketListener();
      this.Us = NativePacketsHolder.PE()
         .method1888(
            plugin, PacketSelector.exact(PacketTypes.PLAYER_COMBAT_KILL), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
               ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
               if (var2 != null) {
                  var2.UE.compareAndSet(null, var1x.nativePacket());
                  var1x.cancel();
               }
            }
         );
      this.Ut = NativePacketsHolder.PE()
         .method1888(plugin, PacketSelector.exact(PacketTypes.HEALTH), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
            ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
            if (var2 != null && !(((HealthPacketView)var1x.view(HealthPacketView.class)).health() > 0.0F)) {
               var2.UF.set(var1x.nativePacket());
               var1x.cancel();
            }
         });
      this.Uu = NativePacketsHolder.PE()
         .method1888(
            plugin, PacketSelector.exact(PacketTypes.ENTITY_METADATA), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
               ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
               if (var2 != null) {
                  EntityMetadataPacketView var3 = (EntityMetadataPacketView)var1x.view(EntityMetadataPacketView.class);
                  if (var3.entityId() == var2.UA) {
                     Float var4 = (Float)var3.get(LivingEntity.HEALTH);
                     if (var4 != null && !(var4 > 0.0F)) {
                        var2.UG.add(var1x.nativePacket());
                        var1x.cancel();
                     }
                  }
               }
            }
         );
      this.Uv = NativePacketsHolder.PE()
         .method1888(plugin, PacketSelector.exact(PacketTypes.UNLOAD_CHUNK), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
            ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
            if (var2 != null) {
               var2.UH.add(var1x.nativePacket());
               var1x.cancel();
            }
         });
      this.Uw = NativePacketsHolder.PE()
         .method1888(plugin, PacketSelector.exact(PacketTypes.PLAYER_INPUT), ListenerPriority.NORMAL, true, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
            ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
            if (var2 != null) {
               PlayerInputPacketView var3 = (PlayerInputPacketView)var1x.view(PlayerInputPacketView.class);
               if (var3.forward() != 0.0F || var3.sideways() != 0.0F || var3.jumping() || var3.dismounting()) {
                  var2.UD.set(true);
               }
            }
         });
      this.Ux = NativePacketsHolder.PE()
         .method1888(plugin, PacketSelector.exact(PacketTypes.CLIENT_COMMAND), ListenerPriority.HIGHEST, true, ListenerFailurePolicy.PASS_ORIGINAL, var1x -> {
            ViewerSession var2 = (ViewerSession)this.Ur.get(var1x.connection().id());
            if (var2 != null && ((ClientCommandPacketView)var1x.view(ClientCommandPacketView.class)).action() == ClientCommandAction.PERFORM_RESPAWN) {
               var1x.sync((var2x, var3) -> {
                  if (this.Ur.get(var2.Uz.id()) == var2 && this.Up.get(var2.Uy) == var2.UC && !var2.UC.isRagdollSettled()) {
                     var2x.cancel();
                  }
               });
            }
         });
   }

   public void unregister() {
      this.vM();
   }

   public void method9232(Set var1) {
      if (this.Um != null) {
         this.Um.method1693(var1);
      }
   }

   public CompletionStage vM() {
      for (UUID var2 : List.copyOf(this.Uq)) {
         Player var3 = Bukkit.getPlayer(var2);
         if (var3 != null && var3.isOnline()) {
            SpectatorModeService.getAdapter().cv(var3);
            SpectatorModeService.getAdapter().cw(var3);
         }
      }

      this.Uq.clear();

      for (ViewerSession var7 : List.copyOf(this.Ur.values())) {
         var7.UD.set(false);
         this.method9267(var7);
      }

      if (this.Us != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Us);
      }

      if (this.Ut != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Ut);
      }

      if (this.Uu != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Uu);
      }

      if (this.Uv != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Uv);
      }

      if (this.Uw != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Uw);
      }

      if (this.Ux != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.Ux);
      }

      for (UUID var8 : List.copyOf(this.Up.keySet())) {
         Player var9 = Bukkit.getPlayer(var8);
         if (var9 != null) {
            this.ap(var9);
         } else {
            CustomEntityImpl var4 = (CustomEntityImpl)this.Up.remove(var8);
            if (var4 != null) {
               CustomEntityManager.getInstance().removeWithEffects(var4, false, false);
            }
         }
      }

      if (this.Uk != null) {
         this.Uk.close();
      }

      if (this.Ul != null) {
         this.Ul.close();
      }

      if (this.Un != null) {
         this.Un.close();
      }

      if (this.Uo != null) {
         this.Uo.close();
      }

      return this.Um == null ? CompletableFuture.completedFuture(null) : this.Um.gz();
   }

   private static void method9233(Player var0, int var1) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().method8312(var0);
      if (var2 != null) {
         SchedulerUtils.runNowOrLaterDelayed(var2::syncEquipmentSlots, var1 != 0, var1);
      }
   }

   public static void method9234(ChunkEvent var0, boolean var1) {
      if (!var1 || BlockFaceUtils.isChunkLoaded(var0.getChunk())) {
         for (Entity var5 : var0.getChunk().getEntities()) {
            method9235(var5);
         }
      }
   }

   static void method9235(Entity var0) {
      if (!(var0 instanceof Mob var1 && !MobGoalEventListener.method9606(var1))) {
         if (var0 instanceof org.bukkit.entity.LivingEntity) {
            if (var0 instanceof ArmorStand && Objects.equals(var0.getCustomName(), "IA_mount")) {
               var0.remove();
               return;
            }

            if (var0.getType() == EntityType.PLAYER || var0.getType() == EntityType.ITEM_FRAME || var0.getType() == EntityType.ITEM) {
               return;
            }

            if (CustomEntityManager.getInstance().isCustomEntityLoaded(var0)) {
               return;
            }

            CustomEntityManager.getInstance().wrapEntity((org.bukkit.entity.LivingEntity)var0);
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onDismount(EntityDismountEvent var1) {
      if (var1.getEntity() instanceof org.bukkit.entity.LivingEntity) {
         ModelRiderController var2 = CustomEntityManager.getInstance().getRiderOf((org.bukkit.entity.LivingEntity)var1.getEntity());
         if (var2 != null && var2.isMounted()) {
            var1.setCancelled(true);
         }
      }

      if (!var1.isCancelled() && var1.getEntity() instanceof Player var4) {
         SchedulerUtils.runLaterSync(() -> CustomEntityManager.getInstance().restoreEmote(var4), 1L);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
   private void onEntityDamage(EntityDamageEvent var1) {
      if (!(var1.getEntity() instanceof Player)) {
         CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(var1.getEntity().getUniqueId());
         if (var2 != null) {
            Double var3 = (Double)var2.qK.vS.get(var1.getCause());
            if (var3 != null) {
               double var4 = var1.getDamage() * var3;
               var1.setDamage(Double.isFinite(var4) ? var4 : Double.MAX_VALUE);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onUnleash(EntityUnleashEvent var1) {
      if (var1.getEntity() instanceof Mob var2) {
         SchedulerUtils.runLaterSync(() -> {
            if (var2.isValid() && !var2.isLeashed()) {
               CustomEntityImpl var1x = CustomEntityManager.getInstance().getByUuid(var2.getUniqueId());
               if (var1x != null) {
                  var1x.qK.wn.applyBehaviors(var2);
               }
            }
         }, 1L);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onEntityTeleport(EntityTeleportEvent var1) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(var1.getEntity().getUniqueId());
      if (var2 != null && var2.getModelInstance() != null) {
         var2.getModelInstance().qC();
      }

      if (var1.getEntity() instanceof Mob var3) {
         SchedulerUtils.runLaterSync(() -> {
            if (var3.isValid()) {
               CustomEntityImpl var1x = CustomEntityManager.getInstance().getByUuid(var3.getUniqueId());
               if (var1x != null) {
                  var1x.qK.wn.applyBehaviors(var3);
               }
            }
         }, 1L);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onDamageByEntity(EntityDamageByEntityEvent var1) {
      ModelPartController.onDamageByEntity(var1);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method9241(EntityDamageByEntityEvent var1) {
      ModelPartController.onDamageByEntityDup2(var1);
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(var1.getEntity().getUniqueId());
      if (var2 != null) {
         if (var2.qT && var1.getDamager() instanceof Player && !var2.isHiddenFromPlayer((Player)var1.getDamager())) {
            var1.setCancelled(true);
            return;
         }

         var2.dispatchMobEvent(var1.getEntity(), MobEventType.ATTACKED, var2x -> var2x.method4944(var1, var2));
         if (var2.getBukkitEntity() instanceof Player) {
            PlayerActionBar var3 = CustomEntityManager.getInstance().method8314((Player)var2.getBukkitEntity());
            if (var3 != null && var3.renderer.getCurrentAnimation() != null && var3.renderer.getCurrentAnimation().zH) {
               CustomEntityManager.getInstance().method8327((Player)var2.getBukkitEntity());
            }
         }

         if (var1.getDamager() instanceof Player var5) {
            PlayerActionBar var7 = CustomEntityManager.getInstance().method8314(var5);
            if (var7 != null && var7.renderer.getCurrentAnimation() != null && var7.renderer.getCurrentAnimation().zI) {
               CustomEntityManager.getInstance().method8327(var5);
               return;
            }

            if (CustomEntityManager.getInstance().isRiderOf(var5, var2)) {
               var1.setCancelled(true);
               return;
            }
         }

         var2.playDefaultDamageEffect();
      }

      CustomEntityImpl var6 = CustomEntityManager.getInstance().getByUuid(var1.getDamager().getUniqueId());
      if (var6 != null) {
         var6.dispatchMobEvent(var1.getDamager(), MobEventType.ATTACK, var2x -> var2x.method4944(var1, var6));
         var6.playAnimationDefinition(var6.getAttackAnimation());
         var6.playAttackSound();
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void onEntityDamageHandler(EntityDamageEvent var1) {
      if (!(var1 instanceof EntityDamageByEntityEvent)) {
         if (var1.getEntity().getType() == EntityType.PLAYER) {
            if (var1.getCause() == DamageCause.SUFFOCATION
               && var1.getEntity() instanceof Player
               && CustomEntityManager.getInstance().getRiderOf((org.bukkit.entity.LivingEntity)var1.getEntity()) != null) {
               var1.setCancelled(true);
            }
         } else {
            CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(var1.getEntity().getUniqueId());
            if (var2 != null) {
               var2.dispatchMobEvent(var1.getEntity(), MobEventType.DAMAGED, var2x -> var2x.method4945(var1, var2));
               boolean var3 = var1.getCause().equals(DamageCause.FIRE)
                  || var1.getCause().equals(DamageCause.FIRE_TICK)
                  || var1.getCause().equals(DamageCause.HOT_FLOOR)
                  || var1.getCause().equals(DamageCause.LAVA);
               var2.playDamageEffect(var3);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onPlayerJoin(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      this.aq(var2);
      SchedulerUtils.runLaterSync(() -> {
         if (var2.isOnline()) {
            CustomEntityManager.getInstance().addViewerToWorld(var1.getPlayer());
            CustomEntityManager.getInstance().restoreEmote(var2);
         }
      }, 50L);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.method9266(var1.getPlayer().getUniqueId());
      this.Uq.remove(var1.getPlayer().getUniqueId());
      this.ap(var1.getPlayer());
      CustomEntityManager.getInstance().method8327(var1.getPlayer());
      CustomEntityManager.getInstance().removePassenger(var1.getPlayer());
      CustomEntityManager.getInstance().removeViewerFromAll(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onGameModeChange(PlayerGameModeChangeEvent var1) {
      this.Uq.remove(var1.getPlayer().getUniqueId());
      this.ap(var1.getPlayer());
      CustomEntityManager.getInstance().method8327(var1.getPlayer());
      SchedulerUtils.runLaterSync(() -> CustomEntityManager.getInstance().restoreEmote(var1.getPlayer()), 1L);
   }

   @EventHandler
   private void onWorldChange(PlayerChangedWorldEvent var1) {
      this.ap(var1.getPlayer());
      CustomEntityManager.getInstance().removePassenger(var1.getPlayer());
      CustomEntityManager.getInstance().method8327(var1.getPlayer());
      CustomEntityManager.getInstance().deactivateWorldViewers(var1.getFrom(), var1.getPlayer());
      CustomEntityManager.getInstance().activateWorldViewers(var1.getPlayer());
      SchedulerUtils.runLaterSync(() -> CustomEntityManager.getInstance().restoreEmote(var1.getPlayer()), 1L);
   }

   @EventHandler(ignoreCancelled = true)
   private void onTeleport(PlayerTeleportEvent var1) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByUuid(var1.getPlayer().getUniqueId());
      if (var2 != null && var2.getModelInstance() != null) {
         var2.getModelInstance().qC();
      }

      if (var1.getFrom().getWorld().equals(var1.getTo().getWorld())) {
         if (!PlayerInteractionFacade.method5317(var1)) {
            CustomEntityManager.getInstance().removePassenger(var1.getPlayer());
            CustomEntityManager.getInstance().method8327(var1.getPlayer());
            SchedulerUtils.runLaterSync(() -> CustomEntityManager.getInstance().restoreEmote(var1.getPlayer()), 1L);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onTeleportDup2(PlayerTeleportEvent var1) {
      if (var1.getTo() != null && var1.getFrom().getWorld().equals(var1.getTo().getWorld())) {
         CustomEntityManager.getInstance().teleportViewers(var1.getPlayer(), var1.getTo());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
   private void onMount(EntityMountEvent var1) {
      if (var1.getMount() instanceof Boat) {
         CustomEntityImpl var2 = CustomEntityManager.getInstance().getByEntity(var1.getEntity());
         if (var2 != null && var2.qK.vL && var2.hasRiders()) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onMountDup2(EntityMountEvent var1) {
      if (var1.getEntity() instanceof Player var2) {
         SchedulerUtils.runLaterSync(() -> CustomEntityManager.getInstance().restoreEmote(var2), 1L);
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onPlayerPortal(PlayerPortalEvent var1) {
      ModelRiderController var2 = CustomEntityManager.getInstance().getRiderOf(var1.getPlayer());
      if (var2 != null) {
         var1.setCancelled(true);
      }
   }

   @EventHandler
   private void onEntityPortal(EntityPortalEvent var1) {
      CustomEntityImpl var2 = CustomEntityManager.getInstance().getByEntity(var1.getEntity());
      if (var2 != null) {
         var2.dispatchToManager(var2::hideAllViewers);
      }
   }

   @EventHandler
   private void onEntityPortalExit(EntityPortalExitEvent var1) {
      if (var1.getTo() != null && var1.getEntity() instanceof org.bukkit.entity.LivingEntity) {
         CustomEntityImpl var2 = CustomEntityManager.getInstance().getByEntity(var1.getEntity());
         if (var2 != null) {
            if (!var2.qT) {
               var2.addViewersInWorld(var2.getWorld());
            }

            var2.dispatchToManager(var2::respawnAllViewers);
         }
      }
   }

   @EventHandler
   private void onEntityDeath(EntityDeathEvent var1) {
      org.bukkit.entity.LivingEntity var2 = var1.getEntity();
      if (var2.getType() == EntityType.PLAYER) {
         CustomEntityManager.getInstance().removePassenger(var2);
      } else if (CustomEntityManager.getInstance().isCustomEntityLoaded(var2)) {
         CustomEntityImpl var3 = CustomEntityManager.getInstance().getByUuid(var2.getUniqueId());
         if (var3 != null) {
            var3.dispatchMobEvent(var2, MobEventType.DEATH, var2x -> var2x.method4950(var1, var3));
            var3.markDying(var2.getTicksLived());
            if (!var3.qK.vG) {
               var1.getDrops().clear();
            }

            var3.removeFromManager();
            var3.playDeathSound();
            Runnable var4 = () -> {
               SchedulerUtils.runOnMainThread(() -> EventRegistrar.callEvent(new CustomEntityDeathEvent(var2, var3)));
               CustomEntityManager.getInstance().removeWithEffects(var3, true, true);
            };
            if (!var3.tryRagdollAt(var2, var1.getDamageSource().getSourceLocation(), var4)) {
               if (var3.qK.wd) {
                  var4.run();
               } else {
                  if (var3.qK.we && var3.getDeathAnimation() != null) {
                     var3.refreshFireEffects();
                  }

                  var3.playDeathThen(var4);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onInteractEntity(PlayerInteractEntityEvent var1) {
      Object var2 = var1.getRightClicked();
      org.bukkit.entity.LivingEntity var3 = ModelPartController.asLiving((Entity)var2);
      if (var3 != null) {
         CustomEntityImpl var4 = CustomEntityManager.getInstance().getByUuid(var3.getUniqueId());
         if (var3 == var1.getPlayer()
            || var3.getPassengers().contains(var1.getPlayer())
            || var4 != null && CustomEntityManager.getInstance().isRiderOf(var1.getPlayer(), var4)) {
            var1.setCancelled(true);
            return;
         }

         var2 = var3;
      }

      if (CustomEntityManager.getInstance().isCustomEntityLoaded((Entity)var2)) {
         CustomEntityImpl var6 = CustomEntityManager.getInstance().getByUuid(var2.getUniqueId());
         if (var6 != null) {
            ItemStack var5 = var1.getPlayer().getInventory().getItem(var1.getHand());
            if (var5 != null && var5.getType() == Material.NAME_TAG) {
               SchedulerUtils.runLaterSync(var6::updateNametag, 1L);
            }

            var6.dispatchMobEvent((Entity)var2, MobEventType.INTERACT, var2x -> var2x.method4946(var1, var6));
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onInteract(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_AIR && var1.getHand() != null && var1.getItem() != null && var1.getItem().getType() == Material.NAME_TAG) {
         Player var2 = var1.getPlayer();
         CustomEntityImpl var3 = CustomEntityManager.getInstance().getTargetEntity(var2);
         if (var3 != null && !var3.qQ && !CustomEntityManager.getInstance().isRiderOf(var2, var3)) {
            org.bukkit.entity.LivingEntity var4 = var3.getBukkitEntity();
            if (var4 != null && var4 != var2) {
               PlayerInteractionFacade.getHandler().interactEntityWithSlotDup2(var2, var4, var1.getHand());
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onCombust(EntityCombustEvent var1) {
      if (!(var1 instanceof EntityCombustByBlockEvent) && !(var1 instanceof EntityCombustByEntityEvent)) {
         Entity var2 = var1.getEntity();
         if (ProjectileTypeHelper.method3225(var2)) {
            CustomEntityImpl var3 = CustomEntityManager.getInstance().getByUuid(var2.getUniqueId());
            if (var3 != null && !var3.qK.vF) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onAnimation(PlayerAnimationEvent var1) {
      if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
         Player var2 = var1.getPlayer();
         if (var2.getOpenInventory().getType() == InventoryType.CRAFTING || var2.getOpenInventory().getType() == InventoryType.CREATIVE) {
            if (var2.getGameMode() != GameMode.ADVENTURE && var2.getGameMode() != GameMode.SPECTATOR) {
               if (!CustomBlockListener.dn.method717(var2)) {
                  if (!CustomBlockListener.dn.method726(var2)) {
                     CustomEntityImpl var3 = CustomEntityManager.getInstance().getTargetEntity(var2);
                     if (var3 != null) {
                        if (!CustomEntityManager.getInstance().isRiderOf(var2, var3)) {
                           if (var3.qK.vo == null && var3.getInteractionEntityId() == -1) {
                              PlayerInteractionFacade.getHandler().interactEntity(var2, var3.getBukkitEntity());
                              method9233(var2, 0);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onSneak(PlayerToggleSneakEvent var1) {
      Player var2 = var1.getPlayer();
      PlayerActionBar var3 = CustomEntityManager.getInstance().method8314(var2);
      if (var3 != null) {
         ModelDefinitionData var4 = var3.renderer.getCurrentAnimation();
         if (var4 != null && var4.zG) {
            CustomEntityManager.getInstance().method8329(var3);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onItemHeld(PlayerItemHeldEvent var1) {
      method9233(var1.getPlayer(), 2);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onSwapHand(PlayerSwapHandItemsEvent var1) {
      method9233(var1.getPlayer(), 5);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onDropItem(PlayerDropItemEvent var1) {
      method9233(var1.getPlayer(), 0);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onInventoryClick(InventoryClickEvent var1) {
      if (var1.getWhoClicked() instanceof Player) {
         method9233((Player)var1.getWhoClicked(), 0);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onPlayerDeath(PlayerDeathEvent var1) {
      Player var2 = var1.getEntity();
      this.method9266(var2.getUniqueId());
      CustomEntityManager.getInstance().method8327(var2);
      this.ap(var2);
      if (Settings.get().emotesRagdollOnDeath
         && NativePacketsHolder.aN()
         && ViaVersionIntegration.method4866(var2.getUniqueId()) >= 769
         && CustomEntityManager.getInstance().getSettings("_iainternal:player_v2") != null) {
         Location var3 = var2.getLocation();
         Vector var4 = var2.getVelocity();
         CustomEntityImpl var5 = CustomEntityManager.getInstance()
            .spawnEntityAt("_iainternal:player_v2", AbstractFakeEntity.getSkinUrl(var2), var3, false, true, true);
         if (var5 != null) {
            UUID var6 = var2.getUniqueId();
            this.Up.put(var6, var5);
            if (!var5.startRagdollDetached(var3, var4, () -> this.ap(var2))) {
               this.ap(var2);
            } else {
               if (var2.getGameMode() != GameMode.SPECTATOR) {
                  SpectatorModeService.enterSpectator(var2);
                  this.Uq.add(var6);
               }

               if (Settings.get().emotesDeathScreenDelaySeconds > 0 || Settings.get().emotesAutoRespawn) {
                  try {
                     ConnectionHandle var7 = NativePacketsHolder.PE().cA(var2);
                     ViewerSession var8 = new ViewerSession(var6, var7, var2.getEntityId(), Settings.get().emotesAutoRespawn, var5);
                     this.Ur.put(var7.id(), var8);
                     SchedulerUtils.runLaterSync(() -> this.method9269(var8), Math.max(1L, 20L * Settings.get().emotesDeathScreenDelaySeconds));
                     SchedulerUtils.runLaterSync(() -> this.method9268(var8), 5L);
                  } catch (IllegalStateException var9) {
                  }
               }

               SchedulerUtils.runLaterSync(() -> {
                  if (var2.isOnline() && var2.isDead() && this.Up.get(var6) == var5) {
                     AbstractPlayerTrigger.Pb().method5132(var2, var3.clone().add(0.0, 1.0, 0.0));
                  }
               }, 1L);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onPlayerRespawn(PlayerRespawnEvent var1) {
      Player var2 = var1.getPlayer();
      UUID var3 = var2.getUniqueId();
      this.method9266(var3);
      this.ap(var2);
      if (this.Uq.remove(var3)) {
         SchedulerUtils.runLaterSync(() -> {
            if (var2.isOnline() && !var2.isDead() && !this.Uq.contains(var3)) {
               SpectatorModeService.exitSpectator(var2);
            }
         }, 1L);
      }
   }

   private void method9266(UUID var1) {
      this.Ur.values().removeIf(var1x -> var1x.Uy.equals(var1));
   }

   private void method9267(ViewerSession var1) {
      this.method9270(var1, false);
   }

   private void method9268(ViewerSession var1) {
      if (this.Ur.get(var1.Uz.id()) == var1) {
         Player var2 = Bukkit.getPlayer(var1.Uy);
         if (var2 != null && var2.isOnline() && var2.isDead()) {
            if (this.Up.get(var1.Uy) == var1.UC && !var1.UC.isRagdollSettled()) {
               DoubleVector3Dup2 var3 = var1.UC.getRagdollPosition();
               if (var3 != null) {
                  AbstractPlayerTrigger.Pb().method5133(var2, new Location(var2.getWorld(), var3.getX(), var3.getY(), var3.getZ()));
               }

               SchedulerUtils.runLaterSync(() -> this.method9268(var1), 5L);
            } else {
               this.method9269(var1);
            }
         }
      }
   }

   private void method9269(ViewerSession var1) {
      if (this.Ur.get(var1.Uz.id()) == var1) {
         Player var2 = Bukkit.getPlayer(var1.Uy);
         if (var2 != null) {
            this.ap(var2);
         }

         this.method9270(var1, var1.UD.get());
      }
   }

   private void method9270(ViewerSession var1, boolean var2) {
      if (this.Ur.remove(var1.Uz.id(), var1)) {
         Player var3 = Bukkit.getPlayer(var1.Uy);
         if (var3 != null && var3.isOnline() && var3.isDead()) {
            if (var2 || var1.UB) {
               try {
                  var3.spigot().respawn();
                  return;
               } catch (RuntimeException var7) {
                  Log.warn("Could not auto-respawn " + var3.getName() + "; showing the death screen", var7);
               }
            }

            Object var4;
            while ((var4 = var1.UG.poll()) != null) {
               NativePacketsHolder.PE().method1869(var1.Uz, var4);
            }

            Object var5 = var1.UF.get();
            if (var5 != null) {
               NativePacketsHolder.PE().method1869(var1.Uz, var5);
            }

            Object var6 = var1.UE.get();
            if (var6 != null) {
               NativePacketsHolder.PE().method1869(var1.Uz, var6);
            }

            while ((var6 = var1.UH.poll()) != null) {
               NativePacketsHolder.PE().method1869(var1.Uz, var6);
            }

            if (this.Uq.contains(var1.Uy)) {
               SchedulerUtils.runLaterSync(() -> {
                  if (var3.isOnline() && var3.isDead() && this.Uq.contains(var1.Uy)) {
                     SpectatorModeService.enterSpectator(var3);
                  }
               }, 1L);
            }
         }
      }
   }

   private void ap(Player var1) {
      CustomEntityImpl var2 = (CustomEntityImpl)this.Up.remove(var1.getUniqueId());
      if (var2 != null) {
         AbstractPlayerTrigger.Pb().cy(var1);
         if (var1.isDead() && this.Uq.contains(var1.getUniqueId())) {
            SpectatorModeService.enterSpectator(var1);
         }

         CustomEntityManager.getInstance().removeWithEffects(var2, true, false);
      }
   }

   private void aq(Player var1) {
      if (Settings.get().emotesRagdollOnDeath && ViaVersionIntegration.method4866(var1.getUniqueId()) >= 769) {
         CachedDisplayEntity.fetchTextureAsync(AbstractFakeEntity.getSkinUrl(var1));
      }
   }

   @EventHandler
   private void onItemsAdderLoad(ItemsAdderLoadDataEvent var1) {
      for (ViewerSession var3 : List.copyOf(this.Ur.values())) {
         var3.UD.set(false);
         this.method9267(var3);
      }

      for (UUID var10 : List.copyOf(this.Up.keySet())) {
         Player var4 = Bukkit.getPlayer(var10);
         if (var4 != null) {
            this.ap(var4);
         }
      }

      for (Player var11 : Bukkit.getOnlinePlayers()) {
         this.aq(var11);
      }

      for (World var12 : Bukkit.getWorlds()) {
         for (Entity var5 : var12.getEntities()) {
            if (!(var5 instanceof Mob var6 && !MobGoalEventListener.method9606(var6)) && var5 instanceof org.bukkit.entity.LivingEntity) {
               CustomEntityManager.getInstance().wrapEntity((org.bukkit.entity.LivingEntity)var5);
            }
         }
      }

      if (Main.aFc && Settings.get().customEntitiesEnabled) {
         CitizensHook.reload();
      }

      if (Main.aFh) {
         MythicMobsHook.reload();
      }

      Bukkit.getOnlinePlayers().forEach(var0 -> {
         CustomEntityManager.getInstance().removePassenger(var0);
         CustomEntityManager.getInstance().stopEmote(var0);
         CustomEntityManager.getInstance().method8327(var0);
      });
   }

   @EventHandler
   private void onWorldLoad(WorldLoadEvent var1) {
      for (Entity var3 : var1.getWorld().getEntities()) {
         method9235(var3);
      }
   }

   @EventHandler
   private void onWorldUnload(WorldUnloadEvent var1) {
      CustomEntityManager.getInstance().removeWorldEntities(var1.getWorld());
   }
}
