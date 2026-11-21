package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalG extends PathfinderGoalWrapped {
   private final BooleanSupplier bhi;

   ConditionalWrappedGoalG(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bhi = var3;
   }

   public boolean method5044() {
      return this.bhi.getAsBoolean() && super.b();
   }

   public boolean method5045() {
      return this.bhi.getAsBoolean() && super.c();
   }
}
