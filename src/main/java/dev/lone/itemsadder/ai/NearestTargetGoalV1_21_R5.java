package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition.a;

class NearestTargetGoalV1_21_R5 extends PathfinderGoalLookAtPlayer {
   NearestTargetGoalV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, EntityInsentient var2, Class var3, float var4, float var5, boolean var6, a var7) {
      super(var2, var3, var4, var5, var6);
      this.bhp = var7;
      this.g.a(this.bhp);
   }
}
