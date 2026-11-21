package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public final class ConditionalWrappedGoalL extends WrappedGoal {
   private final BooleanSupplier bjy;

   ConditionalWrappedGoalL(int var1, Goal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bjy = var3;
   }

   public boolean canUse() {
      return this.bjy.getAsBoolean() && super.canUse();
   }

   public boolean canContinueToUse() {
      return this.bjy.getAsBoolean() && super.canContinueToUse();
   }
}
