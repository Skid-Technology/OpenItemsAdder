package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.ai.MobGoalEventListener;
import dev.lone.itemsadder.util.DoubleArrayPersistentType;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public final class MobPersistentKeys {
   public static final PersistentDataType aaE = new DoubleArrayPersistentType();
   public static final NamespacedKey aaF = keyOf("group");
   public static final NamespacedKey aaG = keyOf("hearing");
   public static final NamespacedKey aaH = keyOf("disturbance");
   public static final NamespacedKey aaI = keyOf("defence");
   public static final NamespacedKey aaJ = keyOf("summoner");
   public static final NamespacedKey aaK = keyOf("summon_expiry");
   public static final NamespacedKey aaL = keyOf("lifetime");
   public static final NamespacedKey aaM = keyOf("projectile_damage");
   public static final NamespacedKey aaN = keyOf("projectile_effects");

   private MobPersistentKeys() {
   }

   public static NamespacedKey keyOf(String var0) {
      return new NamespacedKey("itemsadder", "behaviour_" + var0);
   }

   public static boolean method9611(Mob var0) {
      return !Boolean.FALSE.equals(var0.getWorld().getGameRuleValue(GameRule.MOB_GRIEFING));
   }

   public static boolean method9612(LivingEntity var0) {
      return var0 != null && var0.isValid() && !var0.isDead();
   }

   public static boolean method9613(Mob var0, LivingEntity var1, double var2) {
      return method9612(var1)
         && var1 != var0
         && var1.getWorld() == var0.getWorld()
         && var0.getLocation().distanceSquared(var1.getLocation()) <= var2 * var2
         && !(var1 instanceof Player var4 && (var4.getGameMode() == GameMode.SPECTATOR || var4.getGameMode() == GameMode.CREATIVE));
   }

   public static String method9614(Entity var0) {
      return (String)var0.getPersistentDataContainer().get(aaF, PersistentDataType.STRING);
   }

   public static boolean method9615(Entity var0, Entity var1) {
      String var2 = method9614(var0);
      return var2 != null && var2.equals(method9614(var1));
   }

   public static List method9616(Mob var0, double var1, List var3) {
      return var0.getNearbyEntities(var1, var1, var1)
         .stream()
         .filter(var0x -> var0x instanceof LivingEntity)
         .map(var0x -> (LivingEntity)var0x)
         .filter(
            var4 -> method9612(var4)
               && var4.getLocation().distanceSquared(var0.getLocation()) <= var1 * var1
               && (var3.isEmpty() || var3.contains(var4.getType().name()))
         )
         .sorted(Comparator.comparingDouble(var1x -> var1x.getLocation().distanceSquared(var0.getLocation())))
         .limit(64L)
         .toList();
   }

   public static void method9617(Mob var0, LivingEntity var1) {
      EntityTargetLivingEntityEvent var2 = new EntityTargetLivingEntityEvent(var0, var1, TargetReason.CUSTOM);
      Bukkit.getPluginManager().callEvent(var2);
      if (!var2.isCancelled() && (var2.getTarget() == null || var2.getTarget().getWorld() == var0.getWorld())) {
         var0.setTarget(var2.getTarget());
      }
   }

   public static void method9618(LivingEntity var0, double var1) {
      if (!(var1 <= 0.0) && method9612(var0)) {
         EntityRegainHealthEvent var3 = new EntityRegainHealthEvent(var0, var1, RegainReason.CUSTOM);
         Bukkit.getPluginManager().callEvent(var3);
         if (!var3.isCancelled() && Double.isFinite(var3.getAmount()) && var3.getAmount() > 0.0) {
            var0.setHealth(Math.min(var0.getMaxHealth(), var0.getHealth() + var3.getAmount()));
         }
      }
   }

   public static boolean method9619(Mob var0, LivingEntity var1, double var2, double var4, List var6) {
      if (!method9613(var0, var1, 64.0)) {
         return false;
      }

      if (var2 > 0.0) {
         EntityDamageEvent var7 = var1.getLastDamageCause();
         var1.damage(var2, var0);
         EntityDamageEvent var8 = var1.getLastDamageCause();
         if (var8 == var7
            || !(var8 instanceof EntityDamageByEntityEvent var9)
            || !var9.getDamager().equals(var0)
            || var8.isCancelled()
            || var8.getFinalDamage() <= 0.0) {
            return false;
         }
      } else {
         EntityDamageByEntityEvent var10 = new EntityDamageByEntityEvent(var0, var1, DamageCause.ENTITY_ATTACK, 0.0);
         Bukkit.getPluginManager().callEvent(var10);
         if (var10.isCancelled()) {
            return false;
         }
      }

      if (method9612(var1)) {
         method9620(var0, var1, var4);

         for (PotionEffect var12 : var6) {
            var1.addPotionEffect(var12);
         }
      }

      return true;
   }

   public static void method9620(Mob var0, LivingEntity var1, double var2) {
      if (!(var2 <= 0.0)) {
         Vector var4 = var1.getLocation().toVector().subtract(var0.getLocation().toVector());
         if (var4.lengthSquared() < 1.0E-6) {
            var4 = var0.getLocation().getDirection();
         }

         var4.normalize().multiply(var2).setY(Math.min(0.6, var2 * 0.2));
         var1.setVelocity(var1.getVelocity().add(var4));
      }
   }

   public static Player method9621(Mob var0) {
      return var0 instanceof Tameable var2 && var2.getOwner() instanceof Player var1 && var1.isOnline() && var1.getWorld() == var0.getWorld() ? var1 : null;
   }

   public static void method9622(Mob var0) {
      MobGoalEventListener.method9600(var0);
      PersistentDataContainer var1 = var0.getPersistentDataContainer();
      var1.remove(aaG);
      var1.remove(aaH);
      var1.remove(aaI);
   }
}
