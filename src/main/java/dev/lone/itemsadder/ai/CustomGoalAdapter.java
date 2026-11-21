package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapter extends PathfinderGoal {
   final String beT;
   final CustomGoal beU;

   CustomGoalAdapter(String var1, CustomGoal var2) {
      this.beT = var1;
      this.beU = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method4901() {
      return this.beU.zi();
   }

   public boolean method4902() {
      return this.beU.zj();
   }

   public void method4903() {
      this.beU.start();
   }

   public void method4904() {
      this.beU.stop();
   }

   public void method4905() {
      this.beU.tick();
   }

   public boolean V_() {
      return true;
   }
}
