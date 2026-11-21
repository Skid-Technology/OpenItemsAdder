package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalB extends PathfinderGoalWrapped {
   private final BooleanSupplier beS;

   ConditionalWrappedGoalB(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.beS = var3;
   }

   public boolean method4899() {
      return this.beS.getAsBoolean() && super.b();
   }

   public boolean method4900() {
      return this.beS.getAsBoolean() && super.c();
   }
}
