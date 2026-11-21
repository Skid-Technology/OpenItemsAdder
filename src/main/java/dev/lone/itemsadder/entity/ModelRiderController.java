package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.ComplexFurniturePreSitEvent;
import dev.lone.itemsadder.api.Events.ComplexFurniturePreUnsitEvent;
import dev.lone.itemsadder.api.Events.ComplexFurnitureSitEvent;
import dev.lone.itemsadder.api.Events.ComplexFurnitureUnsitEvent;
import dev.lone.itemsadder.item.ThrowInputState;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LongPair;
import dev.lone.itemsadder.util.PositionRotation;
import dev.lone.itemsadder.util.RateLimiter;
import dev.lone.itemsadder.util.RaycastUtils;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class ModelRiderController {
   public final ModelPartDisplay MY;
   public volatile LivingEntity MZ;
   public LivingEntity xo;
   public boolean Na;
   public final RateLimiter Nb = new RateLimiter();
   private boolean Nc;
   public ArmorStand Nd;
   private ItemDisplay Ne;
   private boolean Nf;
   private boolean Ng;
   private long Nh;
   private long Ni;
   private ThrowInputState Nj;
   private volatile boolean Nk;
   private volatile int Nl = -1;
   @Nullable
   private volatile PositionRotation Nm;
   private final Predicate Nn;

   private Entity getEntity() {
      return (Entity)(this.Ne != null ? this.Ne : this.Nd);
   }

   public ModelRiderController(ModelPartDisplay var1, boolean var2) {
      this(var1, var2, EventRegistrar::callEvent);
   }

   ModelRiderController(ModelPartDisplay var1, boolean var2, Predicate var3) {
      this.MY = var1;
      this.xo = var1.Hm.getBukkitEntity();
      this.Na = var2;
      this.Nc = this.Na;
      this.Nn = var3;
   }

   public boolean isMounted() {
      return this.Nk;
   }

   public CustomEntityImpl getCustomEntity() {
      return this.MY.Hm;
   }

   void teleportToSeat() {
      Location var1 = this.getSeatLocation();
      this.MZ.teleport(var1);
      this.spawnSeatEntity();
      this.sendPassengersUpdate();
   }

   public void mount(LivingEntity var1) {
      this.canMount(var1);
   }

   public boolean canMount(LivingEntity var1) {
      if (!this.testPreSitEvent(var1)) {
         return false;
      }

      if (this.xo != null) {
         boolean var2 = this.MY.Hm.qK.vL || this.MY.Hm.qK.vN.sensitivity() != null;
         this.xo.setCollidable(var2);
         this.Ng = var2 && this.xo.getCollidableExemptions().add(var1.getUniqueId());
         if (this.xo instanceof Mob) {
            ProjectileTypeHelper.getAdapter().method3247((Mob)this.xo, false);
         }

         if (this.MY.Hm.qK.getFloatValue() > 0.0F && (!this.MY.Hm.qK.vL || this.xo.isInWater())) {
            this.MY.Hm.disableGravity(this.xo);
         }
      }

      this.MZ = var1;
      if (this.MY.Hm.qK.vL && this.MY.Hm.qK.getFloatValue() > 0.0F) {
         var1.setRemainingAir(var1.getMaximumAir());
      }

      this.Nb.reset();
      this.resetInputState();
      this.Nl = var1.getEntityId();
      this.Nk = true;

      try {
         this.teleportToSeat();
      } catch (RuntimeException var3) {
         if (this.Ng) {
            this.xo.getCollidableExemptions().remove(var1.getUniqueId());
         }

         this.Ng = false;
         this.Nk = false;
         this.Nl = -1;
         this.MZ = null;
         if (this.xo != null && this.MY.Hm.countMountedRiders() == 0) {
            this.MY.Hm.restoreGravity(this.xo);
         }

         throw var3;
      }

      this.firePostSitEvent(var1);
      return true;
   }

   private void resetInputState() {
      this.Nh = this.Ni = System.nanoTime();
      this.Nj = null;
   }

   public boolean consumeThrowInput(ThrowInputState var1) {
      LongPair var2 = var1.Bn();
      if (var2 != null && var2.second() > this.Ni) {
         this.Ni = var2.second();
         return !this.Nf && var2.first() >= this.Nh;
      } else {
         return false;
      }
   }

   public boolean handleThrowInput(boolean var1, long var2) {
      this.Nj = ThrowInputState.copyWithFlags(this.Nj, false, false, false, false, false, var1, var2);
      return this.consumeThrowInput(this.Nj);
   }

   boolean testPreSitEvent(LivingEntity var1) {
      return !(var1 instanceof Player var2 && !this.Nn.test(new ComplexFurniturePreSitEvent(var2, this.xo, this.MY.Hm)));
   }

   void firePostSitEvent(LivingEntity var1) {
      if (var1 instanceof Player var2) {
         this.Nn.test(new ComplexFurnitureSitEvent(var2, this.xo, this.MY.Hm));
      }
   }

   public void removePassenger() {
      this.tryUnmountChecked(true);
   }

   public void unmount(boolean var1) {
      this.tryUnmountChecked(var1);
   }

   public boolean tryUnmount() {
      return this.tryUnmountChecked(true);
   }

   public boolean tryUnmountChecked(boolean var1) {
      return this.doUnmount(var1, false);
   }

   public boolean doUnmount(boolean var1, boolean var2) {
      if (this.MZ == null) {
         return false;
      }

      if (!var2 && !this.testPreUnsitEvent(this.MZ)) {
         return false;
      }

      LivingEntity var3 = this.MZ;
      this.MZ = null;
      this.Nb.reset();
      this.resetInputState();
      this.Nk = false;
      this.Nl = -1;
      this.sendEmptyPassengersUpdate();
      if (this.xo != null) {
         if (this.Ng) {
            this.xo.getCollidableExemptions().remove(var3.getUniqueId());
         }

         this.Ng = false;
         if (this.MY.Hm.countMountedRiders() == 0) {
            this.xo.setCollidable(true);
            this.MY.Hm.restoreGravity(this.xo);
            if (this.xo instanceof Mob) {
               ProjectileTypeHelper.getAdapter().method3247((Mob)this.xo, true);
            }
         }
      }

      Entity var4 = this.getEntity();
      if (var4 != null) {
         this.Nd = null;
         this.Ne = null;
         if (var1) {
            SchedulerUtils.runOnMainThread(() -> this.ejectSeatEntity(var3, var4));
         } else {
            this.ejectSeatEntity(var3, var4);
         }
      }

      this.Nc = this.Na;
      this.firePostUnsitEvent(var3);
      return true;
   }

   boolean testPreUnsitEvent(LivingEntity var1) {
      return !(var1 instanceof Player var2 && !this.Nn.test(new ComplexFurniturePreUnsitEvent(var2, this.xo, this.MY.Hm)));
   }

   void firePostUnsitEvent(LivingEntity var1) {
      if (var1 instanceof Player var2) {
         this.Nn.test(new ComplexFurnitureUnsitEvent(var2, this.xo, this.MY.Hm));
      }
   }

   private void ejectSeatEntity(LivingEntity var1, Entity var2) {
      Vector var3 = RaycastUtils.raycastBetween(var2.getLocation().clone().add(0.0, 1.0, 0.0), var2.getLocation().clone().add(0.0, -5.0, 0.0));
      var2.eject();
      var2.remove();
      ConnectionRecord var4 = var1 instanceof Player var5 ? NativePacketsHolder.PE().cB(var5) : null;
      Runnable var8 = () -> CustomEntityManager.getInstance().pg.remove(Integer.valueOf(var2.getEntityId()));
      if (var4 != null && var4.PC().isOpen()) {
         SchedulerUtils.runLaterIf(
            () -> var4.PC().send(this, NativePacketsHolder.PE().Pv().removeEntities(new int[]{var2.getEntityId()}), var1xx -> var8.run()),
            1L,
            Main.getPlugin().isEnabled()
         );
      } else {
         var8.run();
      }

      if (var3 != null) {
         Location var6 = var1.getLocation();
         var3.setY((int)var3.getY() + 1);
         Location var7 = var3.toLocation(var1.getWorld());
         var7.setYaw(var6.getYaw());
         var7.setPitch(var6.getPitch());
         var1.teleport(var7);
      }
   }

   public void spawnSeatEntity() {
      if (this.MY.Hm.qK.vn.AN) {
         this.Ne = (ItemDisplay)this.MZ.getWorld().spawn(this.getSeatLocation(), ItemDisplay.class, var0 -> {
            var0.setVisibleByDefault(false);
            var0.setPersistent(false);
            var0.setGravity(false);
            var0.setInvulnerable(true);
            var0.setViewRange(0.0F);
         });
      } else {
         this.Nd = (ArmorStand)this.MZ.getWorld().spawn(this.getSeatLocation(), ArmorStand.class, var0 -> {
            var0.setVisibleByDefault(false);
            var0.setVisible(false);
            var0.setMarker(true);
            var0.setCollidable(false);
            ProjectileTypeHelper.method3217(var0, false);
            var0.setAI(false);
            var0.setRemoveWhenFarAway(true);
            var0.setCustomName("IA_mount");
            var0.setCustomNameVisible(false);
         });
      }

      Entity var1 = this.getEntity();
      CustomEntityManager.getInstance().pg.add(var1.getEntityId());
      var1.addPassenger(this.MZ);
      this.sendSeatPassengerPackets();
   }

   private Location getSeatLocation() {
      Location var1 = this.MY.getPartLocation();
      if (!this.MY.Hm.qK.vn.AN) {
         return var1.clone().add(0.0, this.MY.Hn.Nt()[1] + 1.975 - var1.getY(), 0.0);
      }

      var1 = var1.clone();
      var1.setWorld(this.MY.Hm.sl.getWorld());
      return var1;
   }

   public void sendSeatPassengerPackets() {
      Entity var1 = this.getEntity();
      if (var1 != null) {
         List var2 = this.getCustomEntity().getViewers();

         for (int var3 = 0; var3 < var2.size(); var3++) {
            EntityViewer var4 = (EntityViewer)var2.get(var3);
            NativePacketsHolder.method5250(var4.li, var1.getEntityId());
         }
      }
   }

   public void sendPassengersUpdate() {
      if (this.isMounted()) {
         Object var1 = NativePacketsHolder.PE().Pv().passengers(this.MY.Hn.aQn, new int[]{this.Nl});
         List var2 = this.MY.Hm.getViewers();

         for (int var3 = 0; var3 < var2.size(); var3++) {
            EntityViewer var4 = (EntityViewer)var2.get(var3);
            if (!var4.method8561()) {
               ConnectionRecord var5 = var4.getConnection();
               if (var5 != null) {
                  NativePacketsHolder.PE().method1871(var5, var1);
               }
            }
         }
      }
   }

   public void sendPassengersUpdateDup2() {
      if (this.isMounted()) {
         Object var1 = NativePacketsHolder.PE().Pv().passengers(this.MY.Hn.aQn, new int[]{this.Nl});
         List var2 = this.MY.Hm.getViewers();

         for (int var3 = 0; var3 < var2.size(); var3++) {
            EntityViewer var4 = (EntityViewer)var2.get(var3);
            if (!var4.method8561()) {
               ConnectionRecord var5 = var4.getConnection();
               if (var5 != null) {
                  NativePacketsHolder.PE().method1871(var5, var1);
               }
            }
         }
      }
   }

   void sendEmptyPassengersUpdate() {
      Object var1 = NativePacketsHolder.PE().Pv().passengers(this.MY.Hn.aQn, new int[0]);
      List var2 = this.MY.Hm.getViewers();

      for (int var3 = 0; var3 < var2.size(); var3++) {
         EntityViewer var4 = (EntityViewer)var2.get(var3);
         if (!var4.method8561()) {
            ConnectionRecord var5 = var4.kU();
            if (var5 != null) {
               NativePacketsHolder.PE().method1871(var5, var1);
            }
         }
      }
   }

   public void snapshotSeatPosition() {
      if (this.Nk) {
         Location var1 = this.MY.Hm.getBaseLocation();
         Location var2 = this.MY.Hn.Nv();
         double var3 = this.MY.Hm.qK.vn.AN ? var2.getY() : this.MY.Hn.Nt()[1] + 1.975;
         this.Nm = new PositionRotation(var2.getX(), var3, var2.getZ(), var1.getYaw(), var1.getPitch());
         if (!this.MY.Hm.qK.vn.AN) {
            this.MY.Hn.method2994(var1.getYaw());
         }
      }
   }

   public void refreshSeat() {
      this.snapshotSeatPosition();
   }

   public void tick() {
      PositionRotation var1 = this.Nm;
      Entity var2 = this.getEntity();
      LivingEntity var3 = this.MZ;
      if (this.Nk && var1 != null && var2 != null && var3 != null) {
         ProjectileTypeHelper.getAdapter().method3245(var2, var1.x(), var1.y(), var1.z(), var1.yaw(), var1.pitch(), false);
      }
   }

   public void setLocked(Boolean var1) {
      this.Nf = var1;
   }

   public boolean isLocked() {
      return this.Nf;
   }

   public boolean canControl() {
      return this.Nc;
   }

   public void setCanControl(boolean canControl) {
      this.Nc = canControl;
   }
}
