package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterH extends PathfinderGoal {
   final String bif;
   final CustomGoal big;

   CustomGoalAdapterH(String var1, CustomGoal var2) {
      this.bif = var1;
      this.big = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method5092() {
      return this.big.zi();
   }

   public boolean method5093() {
      return this.big.zj();
   }

   public void method5094() {
      this.big.start();
   }

   public void method5095() {
      this.big.stop();
   }

   public void method5096() {
      this.big.tick();
   }

   public boolean X_() {
      return true;
   }
}
