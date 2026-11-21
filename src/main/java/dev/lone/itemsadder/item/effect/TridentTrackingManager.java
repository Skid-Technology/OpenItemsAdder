package dev.lone.itemsadder.item.effect;

import beer.devs.nativepackets.api.AddEntityPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.RemoveEntitiesPacketView;
import beer.devs.nativepackets.api.SynchronizedPacketAccess;
import beer.devs.nativepackets.api.TakeItemEntityPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.EntityPositionRecord;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemSlotMapping;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.SlotIndexRecord;
import dev.lone.itemsadder.item.TridentPickupMode;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.UuidVector3;
import dev.lone.itemsadder.util.UuidVector3Flagged;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class TridentTrackingManager implements Listener {
   private static final int amb = 5;
   private static final int amc = 10;
   private static final int amd = 300;
   private static final NamespacedKey ame = new NamespacedKey(Main.getInstance(), "custom_trident");
   private static final IntVector3 amf = new IntVector3(16, 16, 16);
   private static TridentTrackingManager amg;
   ScheduledExecutorService le = Executors.newScheduledThreadPool(1, var0 -> {
      Thread var1 = new Thread(var0, "ItemsAdder-Trident-Tick");
      var1.setDaemon(true);
      return var1;
   });
   private PacketListenerHandle amh;
   private PacketListenerHandle ami;
   private PacketListenerHandle amj;
   private volatile Object amk;
   private volatile boolean cV;
   private volatile long bG;
   final Map aml = new ConcurrentHashMap();
   final Map amm = new ConcurrentHashMap();

   public static void setEnabled(boolean var0) {
      if (var0) {
         register();
      } else {
         unregister();
      }
   }

   public static void register() {
      if (amg == null) {
         amg = new TridentTrackingManager();
         amg.startTicker();
      }
   }

   public static void unregister() {
      if (amg != null) {
         amg.stopTicker();
         amg = null;
      }
   }

   private void startTicker() {
      this.amk = NativePacketsHolder.PE().Pz().nativeEntityType(EntityType.TRIDENT);
      this.cV = true;
      long var1 = ++this.bG;
      EventRegistrar.registerListener(this, Main.getInstance());
      this.amh = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.ADD_ENTITY),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> this.method9892(var3, (AddEntityPacketView)var3.view(AddEntityPacketView.class), var1)
         );
      this.ami = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.TAKE_ITEM_ENTITY),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> this.method9894(var3, (TakeItemEntityPacketView)var3.view(TakeItemEntityPacketView.class), var1)
         );
      this.amj = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.REMOVE_ENTITIES),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> this.method9895(var3, (RemoveEntitiesPacketView)var3.view(RemoveEntitiesPacketView.class), var1)
         );
      this.le.scheduleAtFixedRate(() -> this.tickTracking(var1), 0L, TimeUnit.MILLISECONDS.toNanos(50L), TimeUnit.NANOSECONDS);
   }

   private void stopTicker() {
      this.cV = false;
      this.bG++;
      this.unregisterHandle(this.amh);
      this.unregisterHandle(this.ami);
      this.unregisterHandle(this.amj);
      this.amh = null;
      this.ami = null;
      this.amj = null;
      this.amk = null;
      EventRegistrar.unregisterListener(this);
      this.le.shutdownNow();
      Set var1 = Collections.newSetFromMap(new IdentityHashMap());
      var1.addAll(this.aml.values());
      var1.addAll(this.amm.values());

      for (TridentProjectile var3 : var1) {
         var3.CU();
         NativePacketsHolder.PD().removeEntity(var3.amD);
      }

      this.aml.clear();
      this.amm.clear();
   }

   private void method9892(PacketEvent var1, AddEntityPacketView var2, long var3) {
      UUID var5 = NativePacketsHolder.PE().method1883(var1);
      if (this.cV && this.bG == var3 && var5 != null && var2.entityType().nativeHandle() == this.amk) {
         EntityPositionRecord var6 = new EntityPositionRecord(
            var5, var1.connection().id(), var2.entityId(), var2.entityUuid(), var2.x(), var2.y(), var2.z(), var3
         );
         var1.sync((var2x, var3x) -> {
            Player var4 = this.method9896(var6.entityId(), var6.timestamp(), var6.sequence(), var3x.player());
            if (var4 != null && ViaVersionIntegration.method4872(var4)) {
               Object var5x = var3x.location(var6.x(), var6.y(), var6.z());
               var2x.syncAt(var5x, (var2xx, var3xx) -> this.method9893(var2xx, var3xx, var6));
            }
         });
      }
   }

   private void method9893(PacketEvent var1, SynchronizedPacketAccess var2, EntityPositionRecord var3) {
      if (this.cV
         && this.bG == var3.sequence()
         && var2.player() instanceof Player var4
         && var4 == NativePacketsHolder.PE().method1885(var3.entityId(), var3.timestamp())) {
         try {
            if (!(var2.entity(var3.index()) instanceof Trident var6 && var6.getUniqueId().equals(var3.worldId()))) {
               return;
            }

            ItemStack var7 = ProjectileTypeHelper.getProjectileItem(var6);
            if (ContentManager.method8084().method8149(var7) == null || !var6.isValid()) {
               return;
            }

            TridentProjectile var8 = this.createProjectile(var6, var7);
            synchronized (var8) {
               NativePacketsHolder.PD().mountFor(var8.amD, var4);
               var8.method9944(new TridentRef(var3.entityId(), var3.timestamp(), var8.jD));
            }

            var1.cancel();
         } catch (Exception var12) {
            Log.warn("Failed to handle ADD_ENTITY packet for Trident: " + var12.getMessage());
         }
      }
   }

   private void method9894(PacketEvent var1, TakeItemEntityPacketView var2, long var3) {
      UUID var5 = NativePacketsHolder.PE().method1883(var1);
      if (this.cV && this.bG == var3 && var5 != null) {
         int var6 = var2.collectedEntityId();
         if (this.amm.containsKey(var6)) {
            SlotIndexRecord var7 = new SlotIndexRecord(var5, var1.connection().id(), var6, var2.collectorEntityId(), var3);
            var1.sync((var2x, var3x) -> {
               Player var4 = this.method9896(var7.bk(), var7.bl(), var7.ai(), var3x.player());
               if (var4 != null && ViaVersionIntegration.method4872(var4) && var4.getEntityId() == var7.Dd()) {
                  TridentProjectile var5x = (TridentProjectile)this.amm.get(var7.Dc());
                  if (var5x != null && var4.getWorld().getUID().equals(var5x.jD)) {
                     var5x.by(var4);
                  }
               }
            });
         }
      }
   }

   private void method9895(PacketEvent var1, RemoveEntitiesPacketView var2, long var3) {
      UUID var5 = NativePacketsHolder.PE().method1883(var1);
      int var6 = var2.entityCount();
      if (this.cV && this.bG == var3 && var5 != null && var6 != 0) {
         int[] var7 = new int[var6];
         boolean var8 = false;

         for (int var9 = 0; var9 < var6; var9++) {
            int var10 = var2.entityId(var9);
            var7[var9] = var10;
            var8 |= this.amm.containsKey(var10);
         }

         if (var8) {
            ItemSlotMapping var11 = new ItemSlotMapping(var5, var1.connection().id(), var7, var3);
            var1.sync((var2x, var3x) -> {
               Player var4 = this.method9896(var11.bk(), var11.bl(), var11.ai(), var3x.player());
               if (var4 != null && ViaVersionIntegration.method4872(var4)) {
                  UUID var5x = var4.getWorld().getUID();
                  TridentRef var6x = new TridentRef(var4.getUniqueId(), var11.bl(), var5x);

                  for (int var10x : var11.Da()) {
                     TridentProjectile var11x = (TridentProjectile)this.amm.get(var10x);
                     if (var11x != null && var5x.equals(var11x.jD)) {
                        AtomicBoolean var12 = var11x.method9943(var6x);
                        if (var12 != null) {
                           this.method9897(new TridentTrackingKey(var6x, var10x, var11x.amG, var11.ai(), var12));
                        }
                     }
                  }
               }
            });
         }
      }
   }

   @Nullable
   private Player method9896(UUID var1, long var2, long var4, Object var6) {
      return this.cV && this.bG == var4 && var6 instanceof Player var7 && var7 == NativePacketsHolder.PE().method1885(var1, var2) && var7.isOnline()
         ? var7
         : null;
   }

   private void method9897(TridentTrackingKey var1) {
      try {
         NativePacketsHolder.PE().IP().globalLater(Main.getInstance(), () -> this.method9898(var1), 5L);
      } catch (RuntimeException | LinkageError var3) {
         this.method9900(var1, false);
         Log.warn("Failed to schedule Trident display removal: " + var3.getMessage());
      }
   }

   private void method9898(TridentTrackingKey var1) {
      if (this.cV && this.bG == var1.ai()) {
         Player var2 = NativePacketsHolder.PE().method1885(var1.ref().bk(), var1.ref().bl());
         if (var2 == null) {
            this.method9900(var1, false);
         } else {
            try {
               NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> this.method9899(var1, var2), () -> this.method9900(var1, false));
            } catch (RuntimeException | LinkageError var4) {
               this.method9900(var1, false);
               Log.warn("Failed to schedule Trident display removal for player: " + var4.getMessage());
            }
         }
      } else {
         this.method9900(var1, false);
      }
   }

   private void method9899(TridentTrackingKey var1, Player var2) {
      TridentProjectile var3 = (TridentProjectile)this.amm.get(var1.jI());
      if (this.cV
         && this.bG == var1.ai()
         && var2 == NativePacketsHolder.PE().method1885(var1.ref().bk(), var1.ref().bl())
         && var2.isOnline()
         && var1.ref().method20().equals(var2.getWorld().getUID())
         && var3 != null
         && var1.CN().equals(var3.amG)) {
         synchronized (var3) {
            if (var3.method9945(var1.ref(), var1.CP())) {
               try {
                  NativePacketsHolder.PD().removeFor(var3.amD, var2);
                  this.method9900(var1, true);
               } catch (RuntimeException | LinkageError var7) {
                  this.method9900(var1, false);
                  Log.warn("Failed to remove Trident display for player: " + var7.getMessage());
               }
            }
         }
      } else {
         this.method9900(var1, false);
      }
   }

   private void method9900(TridentTrackingKey var1, boolean var2) {
      TridentProjectile var3 = (TridentProjectile)this.amm.get(var1.jI());
      if (var3 != null && var1.CN().equals(var3.amG)) {
         var3.method9946(var1.ref(), var1.CP(), var2);
         this.method9918(var3);
      }
   }

   private void tickTracking(long var1) {
      if (this.cV && this.bG == var1) {
         try {
            for (TridentProjectile var4 : this.aml.values()) {
               this.method9902(var4, var1);
            }
         } catch (Throwable var5) {
            var5.printStackTrace();
         }
      }
   }

   private void method9902(TridentProjectile var1, long var2) {
      if (this.isExpired(var1, var2) && var1.CS()) {
         TridentPickupMode var4 = var1.getPickupMode();
         if (var4 == TridentPickupMode.PICKING_UP) {
            UUID var10 = var1.getOwnerId();
            Player var6 = var10 == null ? null : NativePacketsHolder.PE().method1884(var10);
            if (var6 == null) {
               var1.CT();
               this.method9917(var1);
            } else {
               try {
                  NativePacketsHolder.PE().IP().entity(Main.getInstance(), var6, () -> this.method9903(var1, var2, var6), () -> {
                     var1.CT();
                     this.method9917(var1);
                  });
               } catch (RuntimeException | LinkageError var8) {
                  var1.CT();
                  Log.warn("Failed to schedule Trident pickup tick: " + var8.getMessage());
               }
            }
         } else {
            LivingEntity var5 = var4 == TridentPickupMode.LOYAL_PICKING_UP ? var1.amH : null;
            if (var5 != null) {
               try {
                  NativePacketsHolder.PE().IP().entity(Main.getInstance(), var5, () -> this.method9904(var1, var2, var5), var1::CT);
               } catch (RuntimeException | LinkageError var9) {
                  var1.CT();
                  Log.warn("Failed to schedule Trident loyalty tick: " + var9.getMessage());
               }
            } else {
               this.method9905(var1, var2, null, null);
            }
         }
      }
   }

   private void method9903(TridentProjectile var1, long var2, Player var4) {
      if (this.isExpired(var1, var2) && var1.getPickupMode() == TridentPickupMode.PICKING_UP && var4.isOnline()) {
         Location var5 = var4.getLocation().add(0.0, 0.5, 0.0);
         this.method9905(var1, var2, new UuidVector3(var5.getWorld().getUID(), var5.getX(), var5.getY(), var5.getZ()), null);
      } else {
         var1.CT();
      }
   }

   private void method9904(TridentProjectile var1, long var2, LivingEntity var4) {
      if (this.isExpired(var1, var2) && var1.getPickupMode() == TridentPickupMode.LOYAL_PICKING_UP) {
         Location var5 = var4.getLocation();
         this.method9905(var1, var2, null, new UuidVector3Flagged(var4.isDead(), var5.getWorld().getUID(), var5.getX(), var5.getY(), var5.getZ()));
      } else {
         var1.CT();
      }
   }

   private void method9905(TridentProjectile var1, long var2, @Nullable UuidVector3 var4, @Nullable UuidVector3Flagged var5) {
      Runnable var6 = () -> {
         try {
            this.method9906(var1, var2, var4, var5);
         } finally {
            var1.CT();
         }
      };

      try {
         if (NativePacketsHolder.PE().IP().ownsEntity(var1.amE)) {
            var6.run();
            return;
         }

         NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1.amE, var6, () -> {
            var1.CT();
            this.method9917(var1);
         });
      } catch (RuntimeException | LinkageError var8) {
         var1.CT();
         Log.warn("Failed to schedule Trident entity tick: " + var8.getMessage());
      }
   }

   private void method9906(TridentProjectile var1, long var2, @Nullable UuidVector3 var4, @Nullable UuidVector3Flagged var5) {
      if (this.isExpired(var1, var2)) {
         Trident var6 = var1.amE;
         var1.jD = var6.getWorld().getUID();
         TridentPickupMode var7 = var1.getPickupMode();
         if (!var7.CY() && !var6.isValid()) {
            this.method9917(var1);
         } else if (!var6.isOnGround() || var7.CY()) {
            ItemDisplay var8 = var1.amD;
            if (var7 == TridentPickupMode.LOYAL_PICKING_UP) {
               boolean var9 = var5 != null && var5.isFlagged();
               if (var1.getTicks() > 300 || var6.isDead() || var9) {
                  Location var10 = var6.getLocation();
                  if (var5 == null || !var5.getUuid().equals(var10.getWorld().getUID()) || method9907(var10, var5) <= 9.0) {
                     this.method9917(var1);
                     NativePacketsHolder.PD().removeEntity(var8);
                     if (var6.isValid() && !var9) {
                        var6.remove();
                        ItemStack var18 = ProjectileTypeHelper.getProjectileItem(var6);
                        if (var18 == null) {
                           Log.warn(
                              "TridentHandler: ItemStack is null for trident " + var6.getEntityId() + ". This should not happen, please report this issue."
                           );
                           return;
                        } else {
                           LocationScanner.dropItem(var6.getLocation(), var18);
                           return;
                        }
                     } else {
                        return;
                     }
                  }
               }
            }

            if (var7 != TridentPickupMode.PICKING_UP) {
               NativePacketsHolder.PD().teleportEntity(var8, method9915(var6.getLocation()));
            } else if (var4 != null && var4.getUuid().equals(var6.getWorld().getUID())) {
               Location var16 = var8.getLocation();
               Location var17 = new Location(var16.getWorld(), var4.getX(), var4.getY(), var4.getZ(), var16.getYaw(), var16.getPitch());
               double var11 = var16.distance(var17);
               double var13 = Math.min(0.5, var11);
               Location var15 = var16.clone().add(var17.toVector().subtract(var16.toVector()).normalize().multiply(var13));
               var15.setYaw(var17.getYaw());
               var15.setPitch(var17.getPitch());
               NativePacketsHolder.PD().teleportEntity(var8, var15);
               if (var11 <= 1.0) {
                  this.method9917(var1);
                  ItemStackUtils.ah(var6.getLocation());
               }
            }
         }
      }
   }

   private static double method9907(Location var0, UuidVector3Flagged var1) {
      double var2 = var0.getX() - var1.getX();
      double var4 = var0.getY() - var1.getY();
      double var6 = var0.getZ() - var1.getZ();
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   private boolean isExpired(TridentProjectile var1, long var2) {
      return this.cV && this.bG == var2 && this.aml.get(var1.amG) == var1;
   }

   private void unregisterHandle(@Nullable PacketListenerHandle var1) {
      if (var1 != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), var1);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onProjectileLaunch(ProjectileLaunchEvent var1) {
      if (var1.getEntity() instanceof Trident var2) {
         ItemStack var6 = ProjectileTypeHelper.getProjectileItem(var2);
         if (var6 == null) {
            Log.warn("TridentHandler: ItemStack is null for trident " + var2.getEntityId() + ". This should not happen, please report this issue.");
         } else {
            CustomItemDefinition var4 = ContentManager.method8084().method8149(var6);
            if (var4 != null) {
               TridentProjectile var5 = this.createProjectile(var2, var6);
               this.method9912(var5, 0, this.bG);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onProjectileHit(ProjectileHitEvent var1) {
      if (var1.getEntity() instanceof Trident var2) {
         this.method9914(
            var2,
            (var2x, var3) -> {
               ItemDisplay var4 = var2x.amD;
               NativePacketsHolder.PD().teleportEntity(var4, method9915(var2.getLocation()));
               int var5 = ItemStackUtils.aL(ProjectileTypeHelper.getProjectileItem(var2));
               if (var5 > 0) {
                  if (var1.getEntity().getShooter() instanceof Entity) {
                     var2x.CQ();
                  }
               } else {
                  Collection var6 = var1.getEntity()
                     .getWorld()
                     .getNearbyEntities(
                        var1.getEntity().getLocation(),
                        amf.x,
                        amf.y,
                        amf.z,
                        var0x -> var0x instanceof Trident && var0x.getPersistentDataContainer().has(ame, PersistentDataType.STRING)
                     );
                  if (var6.size() > 10) {
                     Log.warn(
                        "Too many custom tridents in the area ("
                           + BlockFaceUtils.formatLocation(var1.getEntity().getLocation(), true)
                           + "). Trident thrown by "
                           + var1.getEntity().getShooter()
                           + ". Dropping the item instead."
                     );
                     var1.getEntity().remove();
                     LocationScanner.dropItem(var1.getEntity().getLocation(), var3);
                  }
               }
            }
         );
      }
   }

   private void method9912(TridentProjectile var1, int var2, long var3) {
      try {
         NativePacketsHolder.PE().IP().globalLater(Main.getInstance(), () -> {
            if (this.isExpired(var1, var3)) {
               try {
                  NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1.amE, () -> {
                     int var5 = var2 + 1;
                     Trident var6xx = var1.amE;
                     if (this.isExpired(var1, var3) && var5 < 20 && !var6xx.isOnGround() && var6xx.isValid()) {
                        NativePacketsHolder.PD().teleportEntity(var1.amD, method9915(var6xx.getLocation()));
                        this.method9912(var1, var5, var3);
                     }
                  }, () -> this.method9917(var1));
               } catch (RuntimeException | LinkageError var6x) {
                  Log.warn("Failed to schedule initial Trident teleport: " + var6x.getMessage());
               }
            }
         }, 1L);
      } catch (RuntimeException | LinkageError var6) {
         Log.warn("Failed to delay initial Trident teleport: " + var6.getMessage());
      }
   }

   private TridentProjectile createProjectile(Trident var1, ItemStack var2) {
      UUID var3 = var1.getUniqueId();
      synchronized (this.aml) {
         TridentProjectile var5 = (TridentProjectile)this.aml.get(var3);
         if (var5 != null) {
            return var5;
         }

         CustomItemDefinition var6 = ContentManager.method8084().method8149(var2);
         if (var6 == null) {
            throw new IllegalArgumentException("CustomItem not found for ItemStack: " + var2);
         }

         var1.getPersistentDataContainer().set(ame, PersistentDataType.STRING, var6.getNamespacedID());
         Location var7 = var1.getLocation();
         if (var1.getShooter() instanceof LivingEntity var8) {
            Vector var14 = var7.getDirection().normalize().multiply(-10);
            var7.add(var14);
            var7.add(0.0, 100.0, 0.0);
         }

         ItemDisplay var13 = NativePacketsHolder.PD().spawnItemDisplay(method9915(var7), var1x -> {
            var1x.setDisplayWidth(2.0F);
            var1x.setTeleportDuration(1);
            var1x.setItemDisplayTransform(ItemDisplayTransform.HEAD);
            var1x.setItemStack(var2);
         });
         TridentProjectile var15 = new TridentProjectile(var1, var13, var1.getShooter() instanceof LivingEntity ? (LivingEntity)var1.getShooter() : null);
         this.aml.put(var3, var15);
         TridentProjectile var10 = this.amm.put(var1.getEntityId(), var15);
         if (var10 != null && var10 != var15) {
            this.method9917(var10);
            NativePacketsHolder.PD().removeEntity(var10.amD);
         }

         return var15;
      }
   }

   private void method9914(Trident var1, BiConsumer var2) {
      ItemStack var3 = ProjectileTypeHelper.getProjectileItem(var1);
      CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
      if (var4 != null) {
         TridentProjectile var5 = (TridentProjectile)this.aml.get(var1.getUniqueId());
         if (var5 != null) {
            var2.accept(var5, var3);
         }
      }
   }

   private static Location method9915(Location var0) {
      float var1 = -var0.getYaw() + 180.0F;
      float var2 = var0.getPitch();
      var0.setPitch(BlockFaceUtils.normalizeYaw(var2));
      var0.setYaw(BlockFaceUtils.normalizeYaw(var1));
      return var0;
   }

   public void trackTrident(Trident var1) {
      TridentProjectile var2 = (TridentProjectile)this.aml.get(var1.getUniqueId());
      if (var2 != null) {
         this.method9917(var2);
      }
   }

   private void method9917(TridentProjectile var1) {
      this.aml.remove(var1.amG, var1);
      if (!var1.CU()) {
         this.method9918(var1);
      } else {
         try {
            NativePacketsHolder.PE().IP().globalLater(Main.getInstance(), () -> this.method9918(var1), 6L);
         } catch (RuntimeException | LinkageError var3) {
            this.method9918(var1);
         }
      }
   }

   private void method9918(TridentProjectile var1) {
      if (var1.amM.get() && !var1.CV()) {
         this.amm.remove(var1.amF, var1);
      }
   }
}
