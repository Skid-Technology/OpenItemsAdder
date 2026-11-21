package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalF extends PathfinderGoalWrapped {
   private final BooleanSupplier bgK;

   ConditionalWrappedGoalF(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bgK = var3;
   }

   public boolean method5013() {
      return this.bgK.getAsBoolean() && super.b();
   }

   public boolean method5014() {
      return this.bgK.getAsBoolean() && super.c();
   }
}
