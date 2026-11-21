package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R5Dup3 extends PathfinderGoal {
   private int ben;
   private final RandomSource bix;

   RandomStrollGoalV1_21_R5Dup3(MobGoalFactoryImplV1_21_R5Dup3 var1, EntityCreature var2, int var3, int var4) {
      this.biy = var2;
      this.biz = var3;
      this.biA = var4;
      this.ben = 0;
      this.bix = this.biy.ec();
   }

   public boolean method5108() {
      return this.biy.as >= this.ben;
   }

   public void method5109() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.biy.as + this.bix.a(this.biz - this.biA + 1) + this.biA;
   }

   private void OZ() {
      if (!this.biy.ai().B_() && this.biy.bO()) {
         double var1 = this.biy.dC() + (this.bix.j() - 0.5) * 32.0;
         double var3 = this.biy.dE() + (this.bix.a(16) - 8);
         double var5 = this.biy.dI() + (this.bix.j() - 0.5) * 32.0;
         this.biy.b(var1, var3, var5, true);
      }
   }
}
