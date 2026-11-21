package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;

class LookAtPlayerGoalV1_21_R1 extends PathfinderGoalLookAtPlayer {
   LookAtPlayerGoalV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, EntityInsentient var2, Class var3, float var4, float var5, boolean var6, Predicate var7) {
      super(var2, var3, var4, var5, var6);
      this.beB = var7;
      this.g.a(this.beB);
   }
}
