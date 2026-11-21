package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;

public final class CustomGoalAdapterJ extends Goal {
   final String bjb;
   final CustomGoal bjc;

   CustomGoalAdapterJ(String var1, CustomGoal var2) {
      this.bjb = var1;
      this.bjc = var2;
      EnumSet var3 = EnumSet.noneOf(Flag.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Flag.valueOf(var5.name()));
      }

      this.setFlags(var3);
   }

   public boolean canUse() {
      return this.bjc.zi();
   }

   public boolean canContinueToUse() {
      return this.bjc.zj();
   }

   public void start() {
      this.bjc.start();
   }

   public void stop() {
      this.bjc.stop();
   }

   public void tick() {
      this.bjc.tick();
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }
}
