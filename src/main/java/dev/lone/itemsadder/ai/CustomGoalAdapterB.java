package dev.lone.itemsadder.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal.Type;

final class CustomGoalAdapterB extends PathfinderGoal {
   final String bfr;
   final CustomGoal bfs;

   CustomGoalAdapterB(String var1, CustomGoal var2) {
      this.bfr = var1;
      this.bfs = var2;
      EnumSet var3 = EnumSet.noneOf(Type.class);

      for (GoalFlag var5 : var2.yW()) {
         var3.add(Type.valueOf(var5.name()));
      }

      this.a(var3);
   }

   public boolean method4924() {
      return this.bfs.zi();
   }

   public boolean method4925() {
      return this.bfs.zj();
   }

   public void method4926() {
      this.bfs.start();
   }

   public void method4927() {
      this.bfs.stop();
   }

   public void method4928() {
      this.bfs.tick();
   }

   public boolean Y_() {
      return true;
   }
}
