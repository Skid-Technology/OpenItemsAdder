package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

final class MobGoalBlockSearch extends AbstractMobGoal {
   private final BlockSearchScanner aaS;
   private Location aaT;
   private Block aaU;
   private int aaV = -1;
   private boolean aaW;
   private boolean aaX;

   MobGoalBlockSearch(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
      this.aaS = new BlockSearchScanner(
         (int)var2.getDouble("range"),
         var2.has("vertical_range") ? var2.getInt("vertical_range") : 3,
         var2.has("scan_budget") ? var2.getInt("scan_budget") : 128
      );
      if (var2.zq().equals("investigate_disturbance")) {
         PersistentDataContainer var4 = var1.getPersistentDataContainer();
         var4.set(
            MobPersistentKeys.aaG,
            MobPersistentKeys.aaE,
            new double[]{var2.getDouble("hearing_range"), var2.getInt("memory_ticks"), var2.getDouble("anger_per_event"), var2.getDouble("anger_threshold")}
         );
         var4.set(MobPersistentKeys.keyOf("hearing_events"), PersistentDataType.STRING, String.join("\n", var2.getList("events")));
         MobGoalEventListener.method9599(var1);
      }
   }

   @Override
   protected boolean yX() {
      this.aaV = -1;
      this.aaU = null;
      this.aaT = null;
      if (this.aan.zq().equals("seek_land") && !this.aam.isInWater()) {
         return false;
      }

      if (this.aan.zq().equals("seek_shelter") && this.aam.getLocation().getBlock().getLightFromSky() < 15) {
         return false;
      }

      if (this.aan.zq().equals("investigate_disturbance")) {
         if (this.aam.getTarget() != null) {
            return false;
         }

         double[] var6 = (double[])this.aam.getPersistentDataContainer().get(MobPersistentKeys.aaH, MobPersistentKeys.aaE);
         String var2 = (String)this.aam.getPersistentDataContainer().get(MobPersistentKeys.keyOf("disturbance_world"), PersistentDataType.STRING);
         if (var6 != null && var6.length == 5 && !(var6[3] < this.zh()) && this.aam.getWorld().getUID().toString().equals(var2)) {
            if (var6[4] >= this.aan.getDouble("anger_threshold")) {
               String var3 = (String)this.aam.getPersistentDataContainer().get(MobPersistentKeys.keyOf("disturbance_source"), PersistentDataType.STRING);
               if (var3 != null) {
                  for (LivingEntity var5 : MobPersistentKeys.method9616(this.aam, this.aan.getDouble("hearing_range"), List.of())) {
                     if (var5.getUniqueId().toString().equals(var3) && MobPersistentKeys.method9613(this.aam, var5, this.aan.getDouble("hearing_range"))) {
                        MobPersistentKeys.method9617(this.aam, var5);
                        return false;
                     }
                  }
               }
            }

            this.aaT = new Location(this.aam.getWorld(), var6[0], var6[1], var6[2]);
            return this.aaT.distanceSquared(this.aam.getLocation()) <= this.aan.getDouble("range") * this.aan.getDouble("range");
         } else {
            return false;
         }
      } else {
         this.aaU = this.aaS.method9629(this.aam, this::method4583);
         if (this.aaU == null) {
            return false;
         }

         this.aaT = this.aaU.getLocation().add(0.5, 0.0, 0.5);
         if (this.aan.zq().equals("avoid_blocks")) {
            Vector var1 = this.aam.getLocation().toVector().subtract(this.aaT.toVector()).setY(0);
            if (var1.lengthSquared() < 0.001) {
               var1 = this.aam.getLocation().getDirection().setY(0);
            }

            if (var1.lengthSquared() < 0.001) {
               var1 = new Vector(1, 0, 0);
            }

            this.aaT = this.aam.getLocation().add(var1.normalize().multiply(this.aan.getDouble("avoid_distance")));
         } else if (this.aan.zq().equals("rest_at_block")) {
            this.aaT.add(0.0, this.aan.getBoolean("roost") ? -this.aam.getHeight() : 1.0, 0.0);
         }

         return true;
      }
   }

   private boolean method4583(Block var1) {
      return switch (this.aan.zq()) {
         case "avoid_blocks" -> this.aan.getSet("blocks").contains(var1.getType())
            && var1.getLocation().distanceSquared(this.aam.getLocation()) <= this.aan.getDouble("avoid_distance") * this.aan.getDouble("avoid_distance");
         case "rest_at_block" -> this.aan.getSet("blocks").contains(var1.getType())
            && (this.aan.getBoolean("roost") ? var1.getRelative(0, -1, 0).isPassable() : var1.getRelative(0, 1, 0).isPassable());
         case "seek_land", "seek_shelter" -> var1.isPassable()
            && !var1.isLiquid()
            && var1.getRelative(0, -1, 0).getType().isSolid()
            && (this.aan.zq().equals("seek_land") || var1.getLightFromSky() < 15);
         default -> false;
      };
   }

   @Override
   protected void za() {
      if (!this.aan.zq().equals("rest_at_block")) {
         super.za();
      }
   }

   @Override
   protected boolean yZ() {
      if (this.aaT == null || this.aaT.getWorld() != this.aam.getWorld()) {
         return false;
      } else {
         return this.aan.zq().equals("investigate_disturbance")
            ? this.aam.getTarget() == null
            : this.aaU != null && this.aam.getWorld().isChunkLoaded(this.aaU.getX() >> 4, this.aaU.getZ() >> 4) && this.method4583(this.aaU);
      }
   }

   @Override
   protected void zb() {
      boolean var1 = switch (this.aan.zq()) {
         case "seek_land" -> !this.aam.isInWater();
         case "seek_shelter" -> this.aam.getLocation().getBlock().getLightFromSky() < 15;
         default -> this.method4570(this.aaT);
      };
      if (!var1) {
         this.method4571(this.aaT);
      } else {
         this.aao.stop();
         if (!this.aan.zq().equals("rest_at_block")) {
            this.aap = true;
         } else {
            if (this.aaV < 0) {
               this.aaV = this.ms;
               this.bL("active");
               if (this.aan.getBoolean("roost")) {
                  this.aaX = this.aam.hasGravity();
                  this.aam.setGravity(false);
                  this.aaW = true;
               }
            }

            this.aam.setVelocity(new Vector(0.0, this.aan.getBoolean("roost") ? 0.0 : this.aam.getVelocity().getY(), 0.0));
            if ((this.ms - this.aaV) % 20 == 0) {
               MobPersistentKeys.method9618(this.aam, this.aan.getDouble("heal_amount"));
            }

            if (this.ms - this.aaV >= this.aan.getInt("rest_ticks")) {
               this.aap = true;
            }
         }
      }
   }

   @Override
   protected void es() {
      if (this.aaW) {
         this.aam.setGravity(this.aaX);
         this.aaW = false;
      }
   }
}
