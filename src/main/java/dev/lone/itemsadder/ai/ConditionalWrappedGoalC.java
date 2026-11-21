package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalC extends PathfinderGoalWrapped {
   private final BooleanSupplier bfq;

   ConditionalWrappedGoalC(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bfq = var3;
   }

   public boolean method4922() {
      return this.bfq.getAsBoolean() && super.b();
   }

   public boolean method4923() {
      return this.bfq.getAsBoolean() && super.c();
   }
}
