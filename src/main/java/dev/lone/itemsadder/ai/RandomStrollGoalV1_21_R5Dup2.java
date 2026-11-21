package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup2;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R5Dup2 extends PathfinderGoal {
   private int ben;
   private final RandomSource bhZ;

   RandomStrollGoalV1_21_R5Dup2(MobGoalFactoryImplV1_21_R5Dup2 var1, EntityCreature var2, int var3, int var4) {
      this.bia = var2;
      this.bib = var3;
      this.bic = var4;
      this.ben = 0;
      this.bhZ = this.bia.ec();
   }

   public boolean method5085() {
      return this.bia.as >= this.ben;
   }

   public void method5086() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bia.as + this.bhZ.a(this.bib - this.bic + 1) + this.bic;
   }

   private void OZ() {
      if (!this.bia.ai().B_() && this.bia.bO()) {
         double var1 = this.bia.dC() + (this.bhZ.j() - 0.5) * 32.0;
         double var3 = this.bia.dE() + (this.bhZ.a(16) - 8);
         double var5 = this.bia.dI() + (this.bhZ.j() - 0.5) * 32.0;
         this.bia.b(var1, var3, var5, true);
      }
   }
}
