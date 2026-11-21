package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup3;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions.Selector;

public class NearestTargetGoalGenericDup3 extends LookAtPlayerGoal {
   NearestTargetGoalGenericDup3(MobGoalFactoryImplGenericDup3 var1, Mob var2, Class var3, float var4, float var5, boolean var6, Selector var7) {
      super(var2, var3, var4, var5, var6);
      this.bjF = var7;
      this.lookAtContext.selector(this.bjF);
   }
}
