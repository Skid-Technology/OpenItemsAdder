package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ArrowDisplayEffect extends ItemEffect {
   private static final int akl = 200;
   private static final List akm = new ArrayList();
   private static final ArrowHitListener akn = new ArrowHitListener();
   private static BukkitTask ako;
   private static boolean akp;
   private final ItemStack akq;
   private final int akr;
   private final double aks;
   private final double akt;
   private final boolean aku;

   public ArrowDisplayEffect(ItemStack var1, int var2, double var3, double var5, boolean var7) {
      this.akq = var1;
      this.akr = var2;
      this.aks = var3;
      this.akt = var5;
      this.aku = var7;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (!ItemStackUtils.aE(var3) && !ItemStackUtils.method7242(var3) && !CustomItemDefinition.method8820(var3)) {
         Location var5 = ParticleEffect.getParticleLocation(var4, var3);
         Location var6 = ParticleEffect.getParticleLocationAt(var4, this.akr);
         Vector var7 = var6.toVector().subtract(var5.toVector());
         if (var7.lengthSquared() < 1.0E-6) {
            return EffectOutcome.success();
         }

         Location var8 = var5.clone().add(var7.clone().multiply(0.5));
         double var9 = var5.distance(var6) / 2.0 + 32.0;
         ArrayList var11 = new ArrayList();

         for (Entity var13 : var5.getWorld().getNearbyEntities(var8, var9, var9, var9)) {
            if (var13 instanceof Player var14) {
               var11.add(var14);
            }
         }

         if (var11.isEmpty()) {
            return EffectOutcome.success();
         }

         var7.normalize().multiply(this.aks);
         Arrow var16 = (Arrow)var5.getWorld().spawn(var5, Arrow.class, var3x -> {
            var3x.setVisibleByDefault(false);
            var3x.setPersistent(false);
            var3x.setShooter(var4);
            var3x.setPickupStatus(PickupStatus.DISALLOWED);
            var3x.setDamage(this.akt);
            var3x.setGravity(this.aku);
            var3x.setVelocity(var7);
         });
         if (!var16.isValid()) {
            return EffectOutcome.success();
         }

         if (!akp) {
            Bukkit.getPluginManager().registerEvents(akn, Main.getInstance());
            akp = true;
         }

         ItemDisplay var17 = NativePacketsHolder.PD().spawnItemDisplay(var16.getLocation(), var1x -> {
            var1x.setItemStack(this.akq);
            var1x.setItemDisplayTransform(ItemDisplayTransform.GROUND);
            var1x.setTeleportDuration(1);
            var1x.setDisplayWidth(1.0F);
            var1x.setDisplayHeight(1.0F);
            var1x.setViewRange(2.0F);
         });

         for (Player var15 : var11) {
            NativePacketsHolder.PD().mountFor(var17, var15);
         }

         akm.add(new ArrowDisplayState(var16, var17, var11, var5, var5.distanceSquared(var6), this.akt));
         if (ako == null) {
            ako = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), ArrowDisplayEffect::tick, 1L, 1L);
         }

         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }

   private static void tick() {
      Iterator var0 = akm.iterator();

      while (var0.hasNext()) {
         if (!((ArrowDisplayState)var0.next()).isExpired()) {
            var0.remove();
         }
      }

      if (akm.isEmpty()) {
         ako.cancel();
         ako = null;
      }
   }

   public static void es() {
      if (akp) {
         HandlerList.unregisterAll(akn);
         akp = false;
      }

      if (ako != null) {
         ako.cancel();
         ako = null;
      }

      for (ArrowDisplayState var1 : akm) {
         var1.destroy();
      }

      akm.clear();
   }
}
