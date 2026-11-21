package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

public final class CustomGoalAdapterC extends PathfinderGoal {
   final String bfP;
   final CustomGoal bfQ;

   CustomGoalAdapterC(String var1, CustomGoal var2) {
      this.bfP = var1;
      this.bfQ = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method4969() {
      return this.bfQ.zi();
   }

   public boolean method4970() {
      return this.bfQ.zj();
   }

   public void method4971() {
      this.bfQ.start();
   }

   public void method4972() {
      this.bfQ.stop();
   }

   public void method4973() {
      this.bfQ.tick();
   }

   public boolean X_() {
      return true;
   }
}
