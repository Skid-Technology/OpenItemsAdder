package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public final class ConditionalWrappedGoalK extends WrappedGoal {
   private final BooleanSupplier bja;

   ConditionalWrappedGoalK(int var1, Goal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bja = var3;
   }

   public boolean canUse() {
      return this.bja.getAsBoolean() && super.canUse();
   }

   public boolean canContinueToUse() {
      return this.bja.getAsBoolean() && super.canContinueToUse();
   }
}
