package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobInventoryHelper;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import dev.lone.itemsadder.nms.MobGoalService;
import dev.lone.itemsadder.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class MobGoalEventListener implements Listener {
   private static final Map aal = new ConcurrentHashMap();

   static void method9599(Mob var0) {
      aal.put(var0.getUniqueId(), new WeakReference<>(var0));
   }

   static void method9600(Mob var0) {
      aal.remove(var0.getUniqueId());
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void method9601(EntityDamageEvent var1) {
      if (var1 instanceof EntityDamageByEntityEvent var2 && var2.getDamager() instanceof Projectile var3) {
         Double var8 = (Double)var3.getPersistentDataContainer().get(MobPersistentKeys.aaM, PersistentDataType.DOUBLE);
         if (var8 != null && Double.isFinite(var8) && var8 >= 0.0 && var8 <= 2048.0) {
            var1.setDamage(var8);
         }
      }

      if (var1.getEntity() instanceof Mob var5) {
         double[] var7 = (double[])var5.getPersistentDataContainer().get(MobPersistentKeys.aaI, MobPersistentKeys.aaE);
         if (var7 != null
            && var7.length == 2
            && Double.isFinite(var7[0])
            && var7[0] >= 0.0
            && var7[0] <= 4.0
            && var7[1] >= var5.getTicksLived()
            && var7[1] <= var5.getTicksLived() + 5) {
            var1.setDamage(Math.min(Double.MAX_VALUE, var1.getDamage() * var7[0]));
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void method9602(EntityDamageByEntityEvent var1) {
      if (var1.getEntity() instanceof LivingEntity var2 && var1.getDamager() instanceof Projectile var3) {
         Double var10 = (Double)var3.getPersistentDataContainer().get(MobPersistentKeys.aaM, PersistentDataType.DOUBLE);
         if (!(var1.getFinalDamage() <= 0.0) || var10 != null && var10 == 0.0) {
            String var5 = (String)var3.getPersistentDataContainer().get(MobPersistentKeys.aaN, PersistentDataType.STRING);
            if (var5 != null) {
               for (PotionEffect var7 : bK(var5)) {
                  var2.addPotionEffect(var7);
               }
            }

            Double var11 = (Double)var3.getPersistentDataContainer().get(MobPersistentKeys.keyOf("projectile_knockback"), PersistentDataType.DOUBLE);
            if (var11 != null
               && Double.isFinite(var11)
               && var11 > 0.0
               && var11 <= 8.0
               && var3.getShooter() instanceof Mob var12
               && var12.getWorld() == var2.getWorld()) {
               MobPersistentKeys.method9620(var12, var2, var11);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void method9603(ProjectileHitEvent var1) {
      Projectile var2 = var1.getEntity();
      if ((var2.getType() == EntityType.SNOWBALL || var2.getType() == EntityType.EGG)
         && var1.getHitEntity() instanceof LivingEntity var3
         && var2.getShooter() instanceof Mob var4) {
         Double var8 = (Double)var2.getPersistentDataContainer().get(MobPersistentKeys.aaM, PersistentDataType.DOUBLE);
         if (var8 != null && Double.isFinite(var8) && !(var8 < 0.0) && !(var8 > 2048.0)) {
            var1.setCancelled(true);
            if (var8 > 0.0) {
               var3.damage(var8, var2);
            } else {
               EntityDamageByEntityEvent var6 = new EntityDamageByEntityEvent(var2, var3, DamageCause.PROJECTILE, 0.0);
               Bukkit.getPluginManager().callEvent(var6);
            }

            var2.remove();
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGH)
   public void method9604(EntityDeathEvent var1) {
      if (var1.getEntity() instanceof Mob var2) {
         method9600(var2);

         try {
            if (MobInventoryHelper.hasInventory(var2)) {
               var1.getDrops().addAll(MobInventoryHelper.getMobItems(var2));
            }
         } catch (RuntimeException var9) {
            Log.error("Could not release behaviour cargo for " + var2.getUniqueId() + ": " + var9);
         }

         String var10 = (String)var2.getPersistentDataContainer().get(MobPersistentKeys.aaJ, PersistentDataType.STRING);
         if (var10 != null) {
            try {
               Entity var4 = Bukkit.getEntity(UUID.fromString(var10));
               if (var4 == null) {
                  return;
               }

               NamespacedKey var5 = MobPersistentKeys.keyOf("summon_slots");
               String var6 = (String)var4.getPersistentDataContainer().getOrDefault(var5, PersistentDataType.STRING, "");
               String var7 = String.join("\n", Arrays.stream(var6.split("\n")).filter(var1x -> !var1x.startsWith(var2.getUniqueId() + ":")).toList());
               var4.getPersistentDataContainer().set(var5, PersistentDataType.STRING, var7);
            } catch (IllegalArgumentException var8) {
               Log.warn("Invalid behaviour summon owner on " + var2.getUniqueId());
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void method9605(GenericGameEvent var1) {
      if (!var1.isAsynchronous()) {
         Location var2 = var1.getLocation();
         double var3 = Math.min(32, Math.max(0, var1.getRadius()));

         for (Entry var6 : aal.entrySet()) {
            Mob var7 = (Mob)((WeakReference)var6.getValue()).get();
            if (var7 == null) {
               aal.remove(var6.getKey(), var6.getValue());
            } else if (var7.getWorld() == var2.getWorld()
               && var7.isValid()
               && var1.getEntity() != var7
               && (var1.getEntity() == null || !MobPersistentKeys.method9615(var7, var1.getEntity()))) {
               PersistentDataContainer var8 = var7.getPersistentDataContainer();
               double[] var9 = (double[])var8.get(MobPersistentKeys.aaG, MobPersistentKeys.aaE);
               if (var9 != null
                  && var9.length == 4
                  && !(var9[0] <= 0.0)
                  && !(var9[0] > 32.0)
                  && !(var2.distanceSquared(var7.getLocation()) > Math.pow(Math.min(var3, var9[0]), 2.0))) {
                  String var10 = (String)var8.getOrDefault(MobPersistentKeys.keyOf("hearing_events"), PersistentDataType.STRING, "");
                  if (var10.isEmpty() || !Arrays.stream(var10.split("\n")).noneMatch(var1.getEvent().getKey().toString()::equals)) {
                     double[] var11 = (double[])var8.get(MobPersistentKeys.aaH, MobPersistentKeys.aaE);
                     double var12 = Math.min(1000.0, var9[2] + (var11 != null && var11.length == 5 && var11[3] >= var7.getTicksLived() ? var11[4] : 0.0));
                     var8.set(
                        MobPersistentKeys.aaH,
                        MobPersistentKeys.aaE,
                        new double[]{var2.getX(), var2.getY(), var2.getZ(), var7.getTicksLived() + var9[1], var12}
                     );
                     var8.set(MobPersistentKeys.keyOf("disturbance_world"), PersistentDataType.STRING, var7.getWorld().getUID().toString());
                     if (var1.getEntity() != null) {
                        var8.set(MobPersistentKeys.keyOf("disturbance_source"), PersistentDataType.STRING, var1.getEntity().getUniqueId().toString());
                     } else {
                        var8.remove(MobPersistentKeys.keyOf("disturbance_source"));
                     }
                  }
               }
            }
         }
      }
   }

   public static boolean method9606(Mob var0) {
      Long var1 = (Long)var0.getPersistentDataContainer().get(MobPersistentKeys.aaK, PersistentDataType.LONG);
      if (var1 == null) {
         return true;
      }

      long var2 = var1 - var0.getWorld().getFullTime();
      if (var2 <= 0L) {
         if (MobInventoryHelper.hasInventory(var0)) {
            MobInventoryHelper.clearInventory(var0);
         }

         var0.remove();
         return false;
      } else {
         MemoryConfiguration var4 = new MemoryConfiguration();
         var4.set("duration", (int)Math.min(72000L, var2));
         var4.set("cooldown", 0);
         var4.set("interval", 20);
         MobGoalSettings var5 = MobGoalSettings.parse("timed_despawn", var4, "summon");
         MobGoalService.OV().method4594(var0, true, 0, "summon_lifetime", var2x -> AbstractMobGoal.method4568(var0, var5, var2x));
         return true;
      }
   }

   public static String method9607(List var0) {
      return String.join(";", var0.stream().map(var0x -> var0x.getType().getKey() + "," + var0x.getDuration() + "," + var0x.getAmplifier()).toList());
   }

   static List bK(String var0) {
      if (var0.length() > 16000) {
         return List.of();
      }

      ArrayList var1 = new ArrayList();

      for (String var5 : var0.split(";")) {
         String[] var6 = var5.split(",");
         if (var6.length == 3 && var1.size() < 128) {
            try {
               NamespacedKey var7 = NamespacedKey.fromString(var6[0]);
               PotionEffectType var8 = var7 == null ? null : PotionEffectType.getByKey(var7);
               int var9 = Integer.parseInt(var6[1]);
               int var10 = Integer.parseInt(var6[2]);
               if (var8 != null && var9 > 0 && var9 <= 72000 && var10 >= 0 && var10 <= 4) {
                  var1.add(new PotionEffect(var8, var9, var10));
               }
            } catch (IllegalArgumentException var11) {
            }
         }
      }

      return List.copyOf(var1);
   }
}
