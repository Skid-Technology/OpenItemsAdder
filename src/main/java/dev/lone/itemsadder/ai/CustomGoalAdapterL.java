package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;

public final class CustomGoalAdapterL extends Goal {
   final String bjX;
   final CustomGoal bjY;

   CustomGoalAdapterL(String var1, CustomGoal var2) {
      this.bjX = var1;
      this.bjY = var2;
      EnumSet var3 = EnumSet.noneOf(Flag.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Flag.valueOf(var5.name()));
      }

      this.setFlags(var3);
   }

   public boolean canUse() {
      return this.bjY.zi();
   }

   public boolean canContinueToUse() {
      return this.bjY.zj();
   }

   public void start() {
      this.bjY.start();
   }

   public void stop() {
      this.bjY.stop();
   }

   public void tick() {
      this.bjY.tick();
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }
}
