package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RandomStrollGoalV1_21_R3 extends PathfinderGoal {
   private int ben;
   private final RandomSource bgF;

   RandomStrollGoalV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, EntityCreature var2, int var3, int var4) {
      this.bgG = var2;
      this.bgH = var3;
      this.bgI = var4;
      this.ben = 0;
      this.bgF = this.bgG.dY();
   }

   public boolean method5008() {
      return this.bgG.af >= this.ben;
   }

   public void method5009() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bgG.af + this.bgF.a(this.bgH - this.bgI + 1) + this.bgI;
   }

   private void OZ() {
      if (!this.bgG.dV().B_() && this.bgG.bL()) {
         double var1 = this.bgG.dA() + (this.bgF.j() - 0.5) * 32.0;
         double var3 = this.bgG.dC() + (this.bgF.a(16) - 8);
         double var5 = this.bgG.dG() + (this.bgF.j() - 0.5) * 32.0;
         this.bgG.b(var1, var3, var5, true);
      }
   }
}
