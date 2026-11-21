package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterE extends PathfinderGoal {
   final String bgL;
   final CustomGoal bgM;

   CustomGoalAdapterE(String var1, CustomGoal var2) {
      this.bgL = var1;
      this.bgM = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method5023() {
      return this.bgM.zi();
   }

   public boolean method5024() {
      return this.bgM.zj();
   }

   public void method5025() {
      this.bgM.start();
   }

   public void method5026() {
      this.bgM.stop();
   }

   public void method5027() {
      this.bgM.tick();
   }

   public boolean V_() {
      return true;
   }
}
