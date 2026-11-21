package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R6;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R6 extends PathfinderGoal {
   private int ben;
   private final RandomSource bfl;

   RandomStrollGoalV1_21_R6(MobGoalFactoryImplV1_21_R6 var1, EntityCreature var2, int var3, int var4) {
      this.bfm = var2;
      this.bfn = var3;
      this.bfo = var4;
      this.ben = 0;
      this.bfl = this.bfm.ek();
   }

   public boolean method4917() {
      return this.bfm.at >= this.ben;
   }

   public void method4918() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bfm.at + this.bfl.a(this.bfn - this.bfo + 1) + this.bfo;
   }

   private void OZ() {
      if (!this.bfm.an().D_() && this.bfm.bX()) {
         double var1 = this.bfm.dK() + (this.bfl.j() - 0.5) * 32.0;
         double var3 = this.bfm.dM() + (this.bfl.a(16) - 8);
         double var5 = this.bfm.dQ() + (this.bfl.j() - 0.5) * 32.0;
         this.bfm.b(var1, var3, var5, true);
      }
   }
}
