package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R1 extends PathfinderGoal {
   private int ben;
   private final RandomSource beN;

   RandomStrollGoalV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, EntityCreature var2, int var3, int var4) {
      this.beO = var2;
      this.beP = var3;
      this.beQ = var4;
      this.ben = 0;
      this.beN = this.beO.dR();
   }

   public boolean method4894() {
      return this.beO.ai >= this.ben;
   }

   public void method4895() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.beO.ai + this.beN.a(this.beP - this.beQ + 1) + this.beQ;
   }

   private void OZ() {
      if (!this.beO.dO().x_() && this.beO.bE()) {
         double var1 = this.beO.dt() + (this.beN.j() - 0.5) * 32.0;
         double var3 = this.beO.dv() + (this.beN.a(16) - 8);
         double var5 = this.beO.dz() + (this.beN.j() - 0.5) * 32.0;
         this.beO.b(var1, var3, var5, true);
      }
   }
}
