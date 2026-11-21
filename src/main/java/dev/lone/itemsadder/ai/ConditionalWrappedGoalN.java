package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public final class ConditionalWrappedGoalN extends WrappedGoal {
   private final BooleanSupplier bku;

   ConditionalWrappedGoalN(int var1, Goal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bku = var3;
   }

   public boolean canUse() {
      return this.bku.getAsBoolean() && super.canUse();
   }

   public boolean canContinueToUse() {
      return this.bku.getAsBoolean() && super.canContinueToUse();
   }
}
