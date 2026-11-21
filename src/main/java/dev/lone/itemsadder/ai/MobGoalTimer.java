package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobInventoryHelper;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import java.util.EnumSet;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

final class MobGoalTimer extends AbstractMobGoal {
   private int abf;
   private int abg;

   MobGoalTimer(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
      if (var2.zq().equals("timed_despawn")) {
         this.abf = Math.min(
            var2.getInt("duration"),
            (Integer)var1.getPersistentDataContainer().getOrDefault(MobPersistentKeys.aaL, PersistentDataType.INTEGER, var2.getInt("duration"))
         );
         this.abg = this.zh();
         var1.getPersistentDataContainer().set(MobPersistentKeys.aaL, PersistentDataType.INTEGER, this.abf);
      }
   }

   @Override
   public EnumSet yW() {
      return this.aan.zq().equals("timed_despawn")
         ? EnumSet.noneOf(GoalFlag.class)
         : (this.aan.getBoolean("movement_lock") ? EnumSet.of(GoalFlag.MOVE, GoalFlag.LOOK, GoalFlag.JUMP) : EnumSet.of(GoalFlag.LOOK));
   }

   @Override
   protected boolean yX() {
      if (!this.aan.zq().equals("timed_despawn")) {
         return true;
      }

      Long var1 = (Long)this.aam.getPersistentDataContainer().get(MobPersistentKeys.aaK, PersistentDataType.LONG);
      if (var1 != null) {
         return this.aam.getWorld().getFullTime() >= var1;
      }

      this.abf = this.abf - Math.max(0, this.zh() - this.abg);
      this.abg = this.zh();
      this.aam.getPersistentDataContainer().set(MobPersistentKeys.aaL, PersistentDataType.INTEGER, Math.max(0, this.abf));
      return this.abf <= 0;
   }

   @Override
   protected void zb() {
      if (this.aan.zq().equals("timed_despawn")) {
         if (this.aan.getBoolean("drop_cargo")) {
            MobInventoryHelper.clearInventory(this.aam);
         }

         this.aam.remove();
         this.aap = true;
      } else {
         if (this.aan.getBoolean("movement_lock")) {
            this.aao.stop();
            this.aam.setVelocity(new Vector(0.0, this.aam.getVelocity().getY(), 0.0));
         }

         this.aam
            .getPersistentDataContainer()
            .set(MobPersistentKeys.aaI, MobPersistentKeys.aaE, new double[]{this.aan.getDouble("damage_multiplier"), this.zh() + 2});
         if (this.ms % 20 == 0) {
            MobPersistentKeys.method9618(this.aam, this.aan.getDouble("heal_amount"));
         }
      }
   }

   @Override
   protected void es() {
      if (this.aan.zq().equals("defensive_state")) {
         this.aam.getPersistentDataContainer().remove(MobPersistentKeys.aaI);
      }
   }
}
