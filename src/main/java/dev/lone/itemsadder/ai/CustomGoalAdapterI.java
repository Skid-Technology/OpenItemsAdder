package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterI extends PathfinderGoal {
   final String biD;
   final CustomGoal biE;

   CustomGoalAdapterI(String var1, CustomGoal var2) {
      this.biD = var1;
      this.biE = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method5115() {
      return this.biE.zi();
   }

   public boolean method5116() {
      return this.biE.zj();
   }

   public void method5117() {
      this.biE.start();
   }

   public void method5118() {
      this.biE.stop();
   }

   public void method5119() {
      this.biE.tick();
   }

   public boolean X_() {
      return true;
   }
}
