package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R5 extends PathfinderGoal {
   private int ben;
   private final RandomSource bhB;

   RandomStrollGoalV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, EntityCreature var2, int var3, int var4) {
      this.bhC = var2;
      this.bhD = var3;
      this.bhE = var4;
      this.ben = 0;
      this.bhB = this.bhC.ec();
   }

   public boolean method5062() {
      return this.bhC.as >= this.ben;
   }

   public void method5063() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bhC.as + this.bhB.a(this.bhD - this.bhE + 1) + this.bhE;
   }

   private void OZ() {
      if (!this.bhC.ai().B_() && this.bhC.bO()) {
         double var1 = this.bhC.dC() + (this.bhB.j() - 0.5) * 32.0;
         double var3 = this.bhC.dE() + (this.bhB.a(16) - 8);
         double var5 = this.bhC.dI() + (this.bhB.j() - 0.5) * 32.0;
         this.bhC.b(var1, var3, var5, true);
      }
   }
}
