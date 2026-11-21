package dev.lone.itemsadder.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

class MobRangeFilter {
   private final Set Zj;
   private final double Zk;
   private final int Zl;
   private final int Zm;
   private final Boolean Zn;

   private MobRangeFilter(Set var1, double var2, int var4, int var5, Boolean var6) {
      this.Zj = var1;
      this.Zk = var2;
      this.Zl = var4;
      this.Zm = var5;
      this.Zn = var6;
   }

   static MobRangeFilter parse(ConfigurationSection var0) {
      if (var0 != null && Set.of("entities", "range", "min_count", "max_count", "baby").containsAll(var0.getKeys(false))) {
         if (var0.get("entities") instanceof List var2 && !var2.isEmpty() && var2.size() <= 64) {
            HashSet var3 = new HashSet();

            for (Object var5 : var2) {
               if (!(var5 instanceof String var6)) {
                  throw new IllegalArgumentException("invalid nearby entity");
               }

               EntityType var7 = EntityType.valueOf(var6.toUpperCase(Locale.ROOT));
               if (!var7.isAlive()) {
                  throw new IllegalArgumentException("nearby entity must be living");
               }

               var3.add(var7);
            }

            double var9 = MobFilterPredicate.method9528("nearby.range", var0.get("range", 8), 1.0, 32.0, false);
            int var10 = (int)MobFilterPredicate.method9528("nearby.min_count", var0.get("min_count", 1), 0.0, 64.0, true);
            int var11 = (int)MobFilterPredicate.method9528("nearby.max_count", var0.get("max_count", 64), 0.0, 64.0, true);
            if (var10 > var11) {
               throw new IllegalArgumentException("nearby min_count exceeds max_count");
            } else {
               Object var8 = var0.get("baby");
               if (var8 != null && !(var8 instanceof Boolean)) {
                  throw new IllegalArgumentException("nearby.baby must be true or false");
               } else {
                  return new MobRangeFilter(Set.copyOf(var3), var9, var10, var11, (Boolean)var8);
               }
            }
         } else {
            throw new IllegalArgumentException("nearby.entities must be a nonempty list (max64)");
         }
      } else {
         throw new IllegalArgumentException("invalid nearby condition mapping");
      }
   }

   boolean matches(Mob var1) {
      int var2 = 0;

      for (Entity var4 : var1.getNearbyEntities(this.Zk, this.Zk, this.Zk)) {
         if (var4 instanceof LivingEntity
            && this.Zj.contains(var4.getType())
            && var4.getLocation().distanceSquared(var1.getLocation()) <= this.Zk * this.Zk
            && (this.Zn == null || (var4 instanceof Ageable var5 && !var5.isAdult()) == this.Zn)) {
            if (++var2 > this.Zm) {
               return false;
            }
         }
      }

      return var2 >= this.Zl;
   }

   public Set types() {
      return this.Zj;
   }

   public double range() {
      return this.Zk;
   }

   public int minCount() {
      return this.Zl;
   }

   public int maxCount() {
      return this.Zm;
   }

   public Boolean xX() {
      return this.Zn;
   }
}
