package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public final class ConditionalWrappedGoalM extends WrappedGoal {
   private final BooleanSupplier bjW;

   ConditionalWrappedGoalM(int var1, Goal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bjW = var3;
   }

   public boolean canUse() {
      return this.bjW.getAsBoolean() && super.canUse();
   }

   public boolean canContinueToUse() {
      return this.bjW.getAsBoolean() && super.canContinueToUse();
   }
}
