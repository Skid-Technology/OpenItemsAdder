package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;

public final class CustomGoalAdapterM extends Goal {
   final String bkv;
   final CustomGoal bkw;

   CustomGoalAdapterM(String var1, CustomGoal var2) {
      this.bkv = var1;
      this.bkw = var2;
      EnumSet var3 = EnumSet.noneOf(Flag.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Flag.valueOf(var5.name()));
      }

      this.setFlags(var3);
   }

   public boolean canUse() {
      return this.bkw.zi();
   }

   public boolean canContinueToUse() {
      return this.bkw.zj();
   }

   public void start() {
      this.bkw.start();
   }

   public void stop() {
      this.bkw.stop();
   }

   public void tick() {
      this.bkw.tick();
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }
}
