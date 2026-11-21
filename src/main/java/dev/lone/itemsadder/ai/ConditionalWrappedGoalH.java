package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalH extends PathfinderGoalWrapped {
   private final BooleanSupplier bhG;

   ConditionalWrappedGoalH(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bhG = var3;
   }

   public boolean method5067() {
      return this.bhG.getAsBoolean() && super.b();
   }

   public boolean method5068() {
      return this.bhG.getAsBoolean() && super.c();
   }
}
