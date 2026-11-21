package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalE extends PathfinderGoalWrapped {
   private final BooleanSupplier bgm;

   ConditionalWrappedGoalE(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bgm = var3;
   }

   public boolean method4990() {
      return this.bgm.getAsBoolean() && super.b();
   }

   public boolean method4991() {
      return this.bgm.getAsBoolean() && super.c();
   }
}
