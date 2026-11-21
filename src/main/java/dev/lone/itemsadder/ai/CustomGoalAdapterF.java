package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterF extends PathfinderGoal {
   final String bhj;
   final CustomGoal bhk;

   CustomGoalAdapterF(String var1, CustomGoal var2) {
      this.bhj = var1;
      this.bhk = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method5046() {
      return this.bhk.zi();
   }

   public boolean method5047() {
      return this.bhk.zj();
   }

   public void method5048() {
      this.bhk.start();
   }

   public void method5049() {
      this.bhk.stop();
   }

   public void method5050() {
      this.bhk.tick();
   }

   public boolean W_() {
      return true;
   }
}
