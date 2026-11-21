package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerLookPacketView;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.Events.FurniturePreSitEvent;
import dev.lone.itemsadder.api.Events.FurnitureSitEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemFlagAction;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PersistentDataHelper;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MountTrackingListener implements IaListener {
   private static MountTrackingListener cN;
   public static final NamespacedKey cO = new NamespacedKey("itemsadder", "sit_manager_seat");
   public static final NamespacedKey cP = new NamespacedKey("itemsadder", "sit_manager_original_location");
   private final Map cQ = new ConcurrentHashMap();
   private final Map cR = new ConcurrentHashMap();
   private final Map cS = new ConcurrentHashMap();
   private final Set cT = ConcurrentHashMap.newKeySet();
   @Nullable
   private PacketListenerHandle cU;
   private volatile boolean cV;
   private volatile long cW;

   MountTrackingListener() {
      cN = this;
      EventRegistrar.registerIfAbsent(this, Main.getPlugin());
      this.bf();
   }

   private synchronized void bf() {
      if (this.cU != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.cU);
      }

      this.cV = true;
      long var1 = ++this.cW;
      this.cU = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.PLAYER_LOOK),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var3 -> this.method1117(var3, (PlayerLookPacketView)var3.view(PlayerLookPacketView.class), var1)
         );
   }

   private void method1117(PacketEvent var1, PlayerLookPacketView var2, long var3) {
      UUID var5 = NativePacketsHolder.PE().method1883(var1);
      if (this.cV && this.cW == var3 && var5 != null && this.cT.contains(var5)) {
         TimedEntityRef var6 = new TimedEntityRef(var5, var1.connection().id(), var2.yaw(), var3);
         var1.sync(
            (var2x, var3x) -> {
               if (this.cV
                  && this.cW == var6.expiresAt()
                  && var3x.player() instanceof Player var4
                  && var4 == NativePacketsHolder.PE().method1885(var6.entityId(), var6.createdAt())
                  && var4.isOnline()) {
                  Entity var9 = (Entity)this.cQ.get(var4);
                  Entity var6x = (Entity)this.cR.get(var4);
                  ItemFlagAction var7 = (ItemFlagAction)this.cS.get(var4);
                  SchedulerAccess var8 = NativePacketsHolder.PE().IP();
                  if (var9 instanceof ArmorStand
                     && var6x != null
                     && var7 != null
                     && var7.AN()
                     && var4.getVehicle() == var9
                     && var8.ownsEntity(var9)
                     && var8.ownsEntity(var6x)
                     && var9.isValid()
                     && var6x.isValid()
                     && var9.getWorld() == var4.getWorld()
                     && var6x.getWorld() == var4.getWorld()) {
                     this.method1118(var6);
                  }
               }
            }
         );
      }
   }

   private void method1118(TimedEntityRef var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.entityId(), var1.createdAt());
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      if (this.cV && this.cW == var1.expiresAt() && var2 != null && var3.ownsEntity(var2)) {
         Entity var4 = (Entity)this.cQ.get(var2);
         Entity var5 = (Entity)this.cR.get(var2);
         ItemFlagAction var6 = (ItemFlagAction)this.cS.get(var2);
         if (var4 instanceof ArmorStand
            && var5 != null
            && var6 != null
            && var3.ownsEntity(var4)
            && var3.ownsEntity(var5)
            && var2.isOnline()
            && this.hasMount(var2)
            && var2.getVehicle() == var4
            && var4.isValid()
            && var5.isValid()
            && var4.getWorld() == var2.getWorld()
            && var5.getWorld() == var2.getWorld()
            && var6.AN()) {
            ProjectileTypeHelper.getAdapter().method3234(var4, var1.distance());
         }
      }
   }

   private synchronized void bg() {
      this.cV = false;
      this.cW++;
      if (this.cU != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.cU);
         this.cU = null;
      }
   }

   @Override
   public void unregister() {
      this.bg();
      IaListener.super.unregister();
      this.cQ.clear();
      this.cR.clear();
      this.cS.clear();
      this.cT.clear();
      if (cN == this) {
         cN = null;
      }
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      if (var2.isInsideVehicle()) {
         Entity var3 = var2.getVehicle();
         if (var3 instanceof ArmorStand && var3.getPersistentDataContainer().has(cO, PersistentDataType.BYTE)) {
            this.cQ.put(var2, var3);
         }
      }
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      Player var2 = var1.getPlayer();
      if (this.cQ.containsKey(var2)) {
         this.method1124(var2);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onMount(EntityMountEvent var1) {
      if (var1.isCancelled()) {
         if (!(var1.getEntity() instanceof Player var2)) {
            return;
         }

         if (!ProtectionPlugins.hasWorldGuard) {
            return;
         }

         Entity var4 = var1.getMount();
         if (!this.cQ.containsKey(var2)) {
            return;
         }

         if (ProtectionPlugins.canSitOnFurniture(var2, var4)) {
            var1.setCancelled(false);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onDismount(EntityDismountEvent var1) {
      if (var1.getEntity() instanceof Player var2) {
         if (this.cQ.containsKey(var2)) {
            this.method1124(var2);
         }
      }
   }

   public static MountTrackingListener getInstance() {
      if (cN == null) {
         cN = new MountTrackingListener();
      }

      return cN;
   }

   public static void aM() {
      if (cN != null) {
         cN.unregister();
      }
   }

   public static void bi() {
      if (cN != null) {
         cN.bg();
      }
   }

   public static void bj() {
      if (cN != null) {
         cN.bf();
      }
   }

   public boolean tryMount(Player var1, Entity var2, @Nullable Block var3, double var4, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10) {
      return this.tryMountAt(var1, var2, var3, var4, var7 ? -0.9875 : -2.185, var6, var7, var8, var9, var10);
   }

   public boolean tryMountAt(
      Player var1, Entity var2, @Nullable Block var3, double var4, double var6, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12
   ) {
      Location var13;
      if (var3 != null) {
         var13 = var3.getLocation();
         var13.add(0.5, 0.0, 0.5);
      } else {
         var13 = var2.getLocation().clone();
      }

      var13.setYaw(var1.getLocation().getYaw());
      var13.setPitch(var1.getLocation().getPitch());
      if (ProjectileTypeHelper.isAllowedEntityType(var2.getType())) {
         var13.setPitch(0.0F);
         ItemFrame var14 = (ItemFrame)var2;
         BlockFace var15 = BlockFaceUtils.rotationToFace(var14.getRotation());
         if (!var11) {
            var15 = var15.getOppositeFace();
         }

         var13.setYaw(BlockFaceUtils.faceToRotationDegrees(var15));
      } else if (var11) {
         var13.setYaw(var2.getLocation().getYaw() + 180.0F);
      }

      if (!BlockFaceUtils.entitiesOfTypeNear(var13, 0.5, EntityType.PLAYER).isEmpty()) {
         return false;
      }

      ItemStack var18 = FurnitureItemEntity.A(var2);
      CustomItemDefinition var19 = ContentManager.method8084().method8149(var18);
      if (var19 != null) {
         ItemFlagAction var17 = (ItemFlagAction)var19.aoh.getAction("furniture_sit");
         FurnitureItemEntity var16 = (FurnitureItemEntity)var19.aoh.getAction("furniture");
         if (!EventRegistrar.callEvent(new FurniturePreSitEvent(var1, var2, var16))) {
            return false;
         }

         this.cS.put(var1, var17);
         PersistentDataHelper.setLocation(var1, cP, var1.getLocation());
         if (var12) {
            Location var20 = var13.clone();
            var20.add(0.0, 0.5, 0.0);
            var1.teleport(var20);
         }

         ArmorStand var21 = (ArmorStand)var13.getWorld().spawn(var13.add(0.0, var6 + var4, 0.0), ArmorStand.class, var3x -> {
            var3x.setVisible(false);
            var3x.setAI(false);
            var3x.setBasePlate(false);
            var3x.setCanPickupItems(false);
            var3x.setRemoveWhenFarAway(true);
            var3x.setInvulnerable(false);
            var3x.setSilent(true);
            var3x.setGravity(var8);
            var3x.setSmall(var9);
            var3x.setCollidable(var10);
            var3x.getPersistentDataContainer().set(cO, PersistentDataType.BYTE, (byte)1);
            var3x.setMaxHealth(0.5);
         });
         if (!this.cQ.containsKey(var1)) {
            this.cQ.put(var1, var21);
         }

         this.cR.put(var1, var2);
         if (((ItemFlagAction)this.cS.get(var1)).AN()) {
            this.cT.add(var1.getUniqueId());
         }

         if (var12) {
            SchedulerUtils.runLaterSync(() -> var21.addPassenger(var1), 5L);
         } else {
            var21.addPassenger(var1);
         }

         EventRegistrar.callEvent(new FurnitureSitEvent(var1, var2, var16));
         return true;
      } else {
         return false;
      }
   }

   public void method1124(Player var1) {
      this.cT.remove(var1.getUniqueId());
      if (this.cQ.containsKey(var1)) {
         ((Entity)this.cQ.get(var1)).remove();
      }

      this.cR.remove(var1);
      this.cS.remove(var1);
      this.cQ.remove(var1);
      Optional.ofNullable(PersistentDataHelper.getLocation(var1, cP))
         .ifPresent(
            var1x -> {
               if (var1x.getWorld() == var1.getWorld()) {
                  Location var2 = var1x.clone();
                  double var3 = var1x.distance(var1.getLocation());
                  if (var3 > 2.5) {
                     double var5 = 2.5 / var3;
                     var2.add(
                        (var1.getLocation().getX() - var1x.getX()) * var5,
                        (var1.getLocation().getY() - var1x.getY()) * var5,
                        (var1.getLocation().getZ() - var1x.getZ()) * var5
                     );
                  }

                  if (BlockFaceUtils.hasBlockAbove(var1)) {
                     Log.devLog("Teleporting player to raycast hit location");
                     var1.teleport(var1x);
                  }
               }

               PersistentDataHelper.removeKey(var1, cP);
            }
         );
   }

   public void method1125(Entity var1) {
      for (Entry var3 : this.cR.entrySet()) {
         if (((Entity)var3.getValue()).getUniqueId().equals(var1.getUniqueId())) {
            this.method1124((Player)var3.getKey());
            return;
         }
      }
   }

   public boolean hasMount(@NotNull Player var1) {
      return this.cQ.containsKey(var1);
   }
}
