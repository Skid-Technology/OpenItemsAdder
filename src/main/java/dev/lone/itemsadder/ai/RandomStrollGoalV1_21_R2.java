package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R2;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R2 extends PathfinderGoal {
   private int ben;
   private final RandomSource bgh;

   RandomStrollGoalV1_21_R2(MobGoalFactoryImplV1_21_R2 var1, EntityCreature var2, int var3, int var4) {
      this.bgi = var2;
      this.bgj = var3;
      this.bgk = var4;
      this.ben = 0;
      this.bgh = this.bgi.dZ();
   }

   public boolean method4985() {
      return this.bgi.af >= this.ben;
   }

   public void method4986() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bgi.af + this.bgh.a(this.bgj - this.bgk + 1) + this.bgk;
   }

   private void OZ() {
      if (!this.bgi.dW().B_() && this.bgi.bL()) {
         double var1 = this.bgi.dB() + (this.bgh.j() - 0.5) * 32.0;
         double var3 = this.bgi.dD() + (this.bgh.a(16) - 8);
         double var5 = this.bgi.dH() + (this.bgh.j() - 0.5) * 32.0;
         this.bgi.b(var1, var3, var5, true);
      }
   }
}
