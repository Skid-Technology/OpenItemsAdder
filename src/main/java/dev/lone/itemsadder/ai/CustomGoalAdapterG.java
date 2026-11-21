package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterG extends PathfinderGoal {
   final String bhH;
   final CustomGoal bhI;

   CustomGoalAdapterG(String var1, CustomGoal var2) {
      this.bhH = var1;
      this.bhI = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method5069() {
      return this.bhI.zi();
   }

   public boolean method5070() {
      return this.bhI.zj();
   }

   public void method5071() {
      this.bhI.start();
   }

   public void method5072() {
      this.bhI.stop();
   }

   public void method5073() {
      this.bhI.tick();
   }

   public boolean X_() {
      return true;
   }
}
