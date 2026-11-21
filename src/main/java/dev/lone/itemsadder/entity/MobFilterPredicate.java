package dev.lone.itemsadder.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Vex;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

final class MobFilterPredicate implements Predicate {
   private static final Set Zg = Set.of("min_health", "max_health", "health_below", "health_above");
   private static final Set Zh = Set.of(
      "in_water", "on_ground", "has_target", "on_fire", "rain", "observed_by_player", "has_owner", "tamed", "is_baby", "has_cargo"
   );
   private final Map Zi;

   private MobFilterPredicate(Map var1) {
      this.Zi = Map.copyOf(var1);
   }

   static MobFilterPredicate fromSection(ConfigurationSection var0) {
      ConfigurationSection var1 = var0.getConfigurationSection("conditions");
      if (var1 == null) {
         throw new IllegalArgumentException("conditions must be a mapping");
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (String var4 : var1.getKeys(false)) {
         Object var5 = var1.get(var4);
         String var6 = var4.startsWith("target_") ? var4.substring(7) : var4;
         if (Zg.contains(var6)) {
            var5 = method9528(var4, var5, 0.0, Double.MAX_VALUE, false);
         } else if (Zh.contains(var4)) {
            if (!(var5 instanceof Boolean)) {
               throw new IllegalArgumentException(var4 + " must be true or false");
            }
         } else if (var4.equals("min_target_distance") || var4.equals("max_target_distance")) {
            var5 = method9528(var4, var5, 0.0, 64.0, false);
         } else if (var4.equals("min_light") || var4.equals("max_light")) {
            var5 = method9528(var4, var5, 0.0, 15.0, true);
         } else if (var4.equals("observation_range")) {
            var5 = method9528(var4, var5, 1.0, 32.0, false);
         } else if (var4.equals("time")) {
            if (!"day".equals(var5) && !"night".equals(var5)) {
               throw new IllegalArgumentException("time must be day or night");
            }
         } else if (var4.equals("held_items")) {
            var5 = MobGoalSettings.method134(var5);
         } else if (!var4.equals("has_effects") && !var4.equals("target_has_effects")) {
            if (!var4.equals("nearby")) {
               throw new IllegalArgumentException("unknown condition '" + var4 + "'");
            }

            var5 = MobRangeFilter.parse(var1.getConfigurationSection(var4));
         } else {
            var5 = method9530(var5);
         }

         var2.put(var4, var5);
      }

      method9529(var2, "");
      method9529(var2, "target_");

      for (String[] var8 : List.of(new String[]{"min_target_distance", "max_target_distance"}, new String[]{"min_light", "max_light"})) {
         if (var2.containsKey(var8[0]) && var2.containsKey(var8[1]) && (Double)var2.get(var8[0]) > (Double)var2.get(var8[1])) {
            throw new IllegalArgumentException(var8[0] + " exceeds " + var8[1]);
         }
      }

      if (var2.containsKey("observation_range") && !var2.containsKey("observed_by_player")) {
         throw new IllegalArgumentException("observation_range requires observed_by_player");
      } else {
         return new MobFilterPredicate(var2);
      }
   }

   private static double method9528(String var0, Object var1, double var2, double var4, boolean var6) {
      if (!(
         var1 instanceof Number var7
            && Double.isFinite(var7.doubleValue())
            && !(var7.doubleValue() < var2)
            && !(var7.doubleValue() > var4)
            && (!var6 || var7.doubleValue() == Math.rint(var7.doubleValue()))
      )) {
         throw new IllegalArgumentException(var0 + " must be a finite " + (var6 ? "integer" : "number") + " in [" + var2 + ", " + var4 + "]");
      } else {
         return var7.doubleValue();
      }
   }

   private static void method9529(Map var0, String var1) {
      double var2 = var0.getOrDefault(var1 + "min_health", 0.0);
      double var4 = var0.getOrDefault(var1 + "max_health", Double.POSITIVE_INFINITY);
      double var6 = var0.getOrDefault(var1 + "health_above", Double.NEGATIVE_INFINITY);
      double var8 = var0.getOrDefault(var1 + "health_below", Double.POSITIVE_INFINITY);
      double var10 = Math.max(var2, var6);
      double var12 = Math.min(var4, var8);
      if (var10 > var12 || var10 == var12 && (var6 >= var2 || var8 <= var4 && var0.containsKey(var1 + "health_below"))) {
         throw new IllegalArgumentException(var1 + "health conditions have no matching value");
      }
   }

   private static Set method9530(Object var0) {
      if (var0 instanceof List var1 && !var1.isEmpty() && var1.size() <= 64) {
         HashSet var2 = new HashSet();
         Iterator var3 = var1.iterator();

         while (var3.hasNext()) {
            if (!(var3.next() instanceof String var5) || var5.length() > 128) {
               throw new IllegalArgumentException("invalid effect name");
            }

            PotionEffectType var6 = PotionEffectType.getByName(var5.toUpperCase(Locale.ROOT).replace("MINECRAFT:", ""));
            if (var6 == null) {
               throw new IllegalArgumentException("unknown effect '" + var5 + "'");
            }

            var2.add(var6);
         }

         return Set.copyOf(var2);
      } else {
         throw new IllegalArgumentException("effects condition must be a nonempty list (max64)");
      }
   }

   public boolean matches(Mob var1) {
      LivingEntity var2 = var1.getTarget();

      for (Entry var4 : this.Zi.entrySet()) {
         String var5 = (String)var4.getKey();
         Object var6 = var4.getValue();
         boolean var7;
         if (var5.startsWith("target_") && Zg.contains(var5.substring(7))) {
            var7 = var2 != null && method9532(var5.substring(7), var2.getHealth(), (Double)var6);
         } else if (Zg.contains(var5)) {
            var7 = method9532(var5, var1.getHealth(), (Double)var6);
         } else {
            var7 = switch (var5) {
               case "in_water" -> var1.isInWater() == (Boolean)var6;
               case "on_ground" -> var1.isOnGround() == (Boolean)var6;
               case "has_target" -> var2 != null == (Boolean)var6;
               case "on_fire" -> var1.getFireTicks() > 0 == (Boolean)var6;
               case "rain" -> var1.getWorld().hasStorm() == (Boolean)var6;
               case "time" -> var1.getWorld().getTime() < 12000L == var6.equals("day");
               case "min_light" -> var1.getLocation().getBlock().getLightLevel() >= (Double)var6;
               case "max_light" -> var1.getLocation().getBlock().getLightLevel() <= (Double)var6;
               case "min_target_distance" -> {
                  if (var2 != null
                     && var2.getWorld() == var1.getWorld()
                     && var1.getLocation().distanceSquared(var2.getLocation()) >= (Double)var6 * (Double)var6) {
                     yield true;
                  }

                  yield false;
               }
               case "max_target_distance" -> {
                  if (var2 != null
                     && var2.getWorld() == var1.getWorld()
                     && var1.getLocation().distanceSquared(var2.getLocation()) <= (Double)var6 * (Double)var6) {
                     yield true;
                  }

                  yield false;
               }
               case "observed_by_player" -> this.method9533(var1) == (Boolean)var6;
               case "observation_range" -> true;
               case "has_owner" -> (var1 instanceof Tameable var8 && var8.getOwner() != null || var1 instanceof Vex var9 && var9.getSummoner() != null)
                  == (Boolean)var6;
               case "tamed" -> (var1 instanceof Tameable var10 && var10.isTamed()) == (Boolean)var6;
               case "is_baby" -> (var1 instanceof Ageable var11 && !var11.isAdult()) == (Boolean)var6;
               case "has_cargo" -> MobInventoryHelper.hasInventory(var1) == (Boolean)var6;
               case "held_items" -> {
                  if (var1.getEquipment() == null
                     || !((Predicate)var6).test(var1.getEquipment().getItemInMainHand()) && !((Predicate)var6).test(var1.getEquipment().getItemInOffHand())) {
                     yield false;
                  }

                  yield true;
               }
               case "has_effects" -> ((Set)var6).stream().allMatch(var1::hasPotionEffect);
               case "target_has_effects" -> {
                  if (var2 != null && ((Set)var6).stream().allMatch(var2::hasPotionEffect)) {
                     yield true;
                  }

                  yield false;
               }
               case "nearby" -> ((MobRangeFilter)var6).matches(var1);
               default -> throw new IllegalStateException("Unvalidated goal condition");
            };
         }

         if (!var7) {
            return false;
         }
      }

      return true;
   }

   private static boolean method9532(String var0, double var1, double var3) {
      return switch (var0) {
         case "min_health" -> var1 >= var3;
         case "max_health" -> var1 <= var3;
         case "health_below" -> var1 < var3;
         case "health_above" -> var1 > var3;
         default -> false;
      };
   }

   private boolean method9533(Mob var1) {
      double var2 = this.Zi.getOrDefault("observation_range", 16.0);

      for (Entity var5 : var1.getNearbyEntities(var2, var2, var2)) {
         if (var5 instanceof Player var6 && var6.getGameMode() != GameMode.SPECTATOR) {
            Vector var7 = var1.getEyeLocation().toVector().subtract(var6.getEyeLocation().toVector());
            if (var7.lengthSquared() <= var2 * var2
               && (var7.lengthSquared() < 0.001 || var6.getEyeLocation().getDirection().dot(var7.normalize()) >= 0.97)
               && var6.hasLineOfSight(var1)) {
               return true;
            }
         }
      }

      return false;
   }
}
