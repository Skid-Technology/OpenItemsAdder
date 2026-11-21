package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R4 extends PathfinderGoal {
   private int ben;
   private final RandomSource bhd;

   RandomStrollGoalV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, EntityCreature var2, int var3, int var4) {
      this.bhe = var2;
      this.bhf = var3;
      this.bhg = var4;
      this.ben = 0;
      this.bhd = this.bhe.dY();
   }

   public boolean method5039() {
      return this.bhe.af >= this.ben;
   }

   public void method5040() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bhe.af + this.bhd.a(this.bhf - this.bhg + 1) + this.bhg;
   }

   private void OZ() {
      if (!this.bhe.dV().A_() && this.bhe.bJ()) {
         double var1 = this.bhe.dA() + (this.bhd.j() - 0.5) * 32.0;
         double var3 = this.bhe.dC() + (this.bhd.a(16) - 8);
         double var5 = this.bhe.dG() + (this.bhd.j() - 0.5) * 32.0;
         this.bhe.b(var1, var3, var5, true);
      }
   }
}
