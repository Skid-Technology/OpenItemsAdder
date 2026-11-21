package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R7;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class RandomStrollGoalV1_21_R7 extends PathfinderGoal {
   private int ben;
   private final RandomSource bfJ;

   RandomStrollGoalV1_21_R7(MobGoalFactoryImplV1_21_R7 var1, EntityCreature var2, int var3, int var4) {
      this.bfK = var2;
      this.bfL = var3;
      this.bfM = var4;
      this.ben = 0;
      this.bfJ = this.bfK.ep();
   }

   public boolean method4962() {
      return this.bfK.at >= this.ben;
   }

   public void method4963() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bfK.at + this.bfJ.a(this.bfL - this.bfM + 1) + this.bfM;
   }

   private void OZ() {
      if (!this.bfK.ao().B_() && this.bfK.cb()) {
         double var1 = this.bfK.dP() + (this.bfJ.nextDouble() - 0.5) * 32.0;
         double var3 = this.bfK.dR() + (this.bfJ.a(16) - 8);
         double var5 = this.bfK.dV() + (this.bfJ.nextDouble() - 0.5) * 32.0;
         this.bfK.b(var1, var3, var5, true);
      }
   }
}
