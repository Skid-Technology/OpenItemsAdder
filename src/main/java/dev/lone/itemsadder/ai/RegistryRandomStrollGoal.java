package dev.lone.itemsadder.ai;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

class RegistryRandomStrollGoal extends PathfinderGoal {
   private int ben;
   private final RandomSource beo;

   RegistryRandomStrollGoal(MobGoalRegistry var1, EntityCreature var2, int var3, int var4) {
      this.bep = var2;
      this.beq = var3;
      this.ber = var4;
      this.ben = 0;
      this.beo = this.bep.el();
   }

   public boolean method4864() {
      return this.bep.ai >= this.ben;
   }

   public void method4865() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bep.ai + this.beo.a(this.beq - this.ber + 1) + this.ber;
   }

   private void OZ() {
      if (!this.bep.dP().x_() && this.bep.bD()) {
         double var1 = this.bep.du() + (this.beo.nextDouble() - 0.5) * 32.0;
         double var3 = this.bep.dw() + (this.beo.a(16) - 8);
         double var5 = this.bep.dA() + (this.beo.nextDouble() - 0.5) * 32.0;
         this.bep.b(var1, var3, var5, true);
      }
   }
}
