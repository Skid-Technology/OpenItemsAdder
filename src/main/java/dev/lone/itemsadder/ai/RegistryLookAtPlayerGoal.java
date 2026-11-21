package dev.lone.itemsadder.ai;

import java.util.function.Predicate;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;

class RegistryLookAtPlayerGoal extends PathfinderGoalLookAtPlayer {
   RegistryLookAtPlayerGoal(MobGoalRegistry var1, EntityInsentient var2, Class var3, float var4, float var5, boolean var6, Predicate var7) {
      super(var2, var3, var4, var5, var6);
      this.bdY = var7;
      this.g.a(this.bdY);
   }
}
