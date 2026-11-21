package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterD extends PathfinderGoal {
   final String bgn;
   final CustomGoal bgo;

   CustomGoalAdapterD(String var1, CustomGoal var2) {
      this.bgn = var1;
      this.bgo = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method4992() {
      return this.bgo.zi();
   }

   public boolean method4993() {
      return this.bgo.zj();
   }

   public void method4994() {
      this.bgo.start();
   }

   public void method4995() {
      this.bgo.stop();
   }

   public void method4996() {
      this.bgo.tick();
   }

   public boolean V_() {
      return true;
   }
}
