package dev.lone.itemsadder.ai;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;

final class ConditionalWrappedGoalI extends PathfinderGoalWrapped {
   private final BooleanSupplier bie;

   ConditionalWrappedGoalI(int var1, PathfinderGoal var2, BooleanSupplier var3) {
      super(var1, var2);
      this.bie = var3;
   }

   public boolean method5090() {
      return this.bie.getAsBoolean() && super.b();
   }

   public boolean method5091() {
      return this.bie.getAsBoolean() && super.c();
   }
}
