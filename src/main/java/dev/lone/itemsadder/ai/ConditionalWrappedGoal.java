package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoal extends PathfinderGoalWrapped {
   private final BooleanSupplier beu;

   ConditionalWrappedGoal(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.beu = var3;
   }

   public boolean method4876() {
      return this.beu.getAsBoolean() && super.a();
   }

   public boolean method4877() {
      return this.beu.getAsBoolean() && super.b();
   }
}
