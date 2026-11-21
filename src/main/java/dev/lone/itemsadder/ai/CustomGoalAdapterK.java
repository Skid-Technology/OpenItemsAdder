package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;

public final class CustomGoalAdapterK extends Goal {
   final String bjz;
   final CustomGoal bjA;

   CustomGoalAdapterK(String var1, CustomGoal var2) {
      this.bjz = var1;
      this.bjA = var2;
      EnumSet var3 = EnumSet.noneOf(Flag.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Flag.valueOf(var5.name()));
      }

      this.setFlags(var3);
   }

   public boolean canUse() {
      return this.bjA.zi();
   }

   public boolean canContinueToUse() {
      return this.bjA.zj();
   }

   public void start() {
      this.bjA.start();
   }

   public void stop() {
      this.bjA.stop();
   }

   public void tick() {
      this.bjA.tick();
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }
}
