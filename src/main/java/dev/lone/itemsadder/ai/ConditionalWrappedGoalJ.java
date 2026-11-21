package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalJ extends PathfinderGoalWrapped {
   private final BooleanSupplier biC;

   ConditionalWrappedGoalJ(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.biC = var3;
   }

   public boolean method5113() {
      return this.biC.getAsBoolean() && super.b();
   }

   public boolean method5114() {
      return this.biC.getAsBoolean() && super.c();
   }
}
