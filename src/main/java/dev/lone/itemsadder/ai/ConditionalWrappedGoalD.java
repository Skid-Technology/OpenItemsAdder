package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

public final class ConditionalWrappedGoalD extends PathfinderGoalWrapped {
   private final BooleanSupplier bfO;

   ConditionalWrappedGoalD(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bfO = var3;
   }

   public boolean method4967() {
      return this.bfO.getAsBoolean() && super.b();
   }

   public boolean method4968() {
      return this.bfO.getAsBoolean() && super.c();
   }
}
