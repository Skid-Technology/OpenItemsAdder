package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;

final class MobGoalFollowEntity extends AbstractMobGoal {
   private LivingEntity aaY;
   private List aaZ = List.of();
   private double aba;

   MobGoalFollowEntity(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
      if (var2.has("group")) {
         var1.getPersistentDataContainer().set(MobPersistentKeys.aaF, PersistentDataType.STRING, var2.getString("group"));
      }
   }

   @Override
   public EnumSet yW() {
      return this.aan.zq().equals("share_target") ? EnumSet.noneOf(GoalFlag.class) : super.yW();
   }

   @Override
   protected boolean yX() {
      if (this.aan.zq().equals("orbit_target")) {
         this.aaY = this.aam.getTarget();
         return MobPersistentKeys.method9613(this.aam, this.aaY, this.aan.getDouble("range"));
      }

      this.aaZ = MobPersistentKeys.method9616(this.aam, this.aan.getDouble("range"), List.of())
         .stream()
         .filter(var1 -> var1 instanceof Mob && MobPersistentKeys.method9615(this.aam, var1))
         .map(var0 -> (Mob)var0)
         .limit(this.aan.getInt("max_members") - 1L)
         .toList();
      if (this.aan.zq().equals("share_target")) {
         return !this.aaZ.isEmpty()
            && MobPersistentKeys.method9613(this.aam, this.aam.getTarget(), this.aan.getDouble("range"))
            && !MobPersistentKeys.method9615(this.aam, this.aam.getTarget());
      }

      if (this.aam.getTarget() != null) {
         return false;
      }

      this.aaY = this.aaZ
         .stream()
         .filter(var1 -> var1.getUniqueId().compareTo(this.aam.getUniqueId()) < 0)
         .min(Comparator.comparing(Entity::getUniqueId))
         .orElse(null);
      return this.aaY != null
         && this.aaY.getLocation().distanceSquared(this.aam.getLocation()) > this.aan.getDouble("stop_distance") * this.aan.getDouble("stop_distance");
   }

   @Override
   protected boolean yZ() {
      return this.aan.zq().equals("share_target")
         || MobPersistentKeys.method9612(this.aaY)
            && this.aaY.getWorld() == this.aam.getWorld()
            && this.aaY.getLocation().distanceSquared(this.aam.getLocation()) <= this.aan.getDouble("range") * this.aan.getDouble("range");
   }

   @Override
   protected void zb() {
      if (!this.aan.zq().equals("share_target")) {
         if (this.aan.zq().equals("follow_group")) {
            if (this.aam.getTarget() == null
               && !(this.aam.getLocation().distanceSquared(this.aaY.getLocation()) <= this.aan.getDouble("stop_distance") * this.aan.getDouble("stop_distance"))
               )
             {
               this.method4571(this.aaY.getLocation());
            } else {
               this.aap = true;
            }
         } else if (this.ms % this.aan.getInt("repath_interval") == 0) {
            this.aba = this.aba + Math.toRadians(this.aan.getDouble("angular_speed")) * (this.aan.getBoolean("clockwise") ? 1 : -1);
            Location var4 = this.aaY.getLocation();
            this.method4571(var4.add(Math.cos(this.aba) * this.aan.getDouble("orbit_radius"), 0.0, Math.sin(this.aba) * this.aan.getDouble("orbit_radius")));
         }
      } else {
         LivingEntity var1 = this.aam.getTarget();
         if (MobPersistentKeys.method9613(this.aam, var1, this.aan.getDouble("range")) && !MobPersistentKeys.method9615(this.aam, var1)) {
            for (Mob var3 : this.aaZ) {
               if (MobPersistentKeys.method9612(var3)
                  && var3.getWorld() == this.aam.getWorld()
                  && MobPersistentKeys.method9615(this.aam, var3)
                  && (this.aan.getBoolean("overwrite") || var3.getTarget() == null)
                  && MobPersistentKeys.method9613(var3, var1, this.aan.getDouble("range"))) {
                  MobPersistentKeys.method9617(var3, var1);
               }
            }
         }

         this.aap = true;
      }
   }
}
