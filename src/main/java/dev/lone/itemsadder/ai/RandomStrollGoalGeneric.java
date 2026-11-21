package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGeneric;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RandomStrollGoalGeneric extends Goal {
   private int ben;
   private final RandomSource biV;

   RandomStrollGoalGeneric(MobGoalFactoryImplGeneric var1, PathfinderMob var2, int var3, int var4) {
      this.biW = var2;
      this.biX = var3;
      this.biY = var4;
      this.ben = 0;
      this.biV = this.biW.getRandom();
   }

   public boolean canUse() {
      return this.biW.tickCount >= this.ben;
   }

   public void start() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.biW.tickCount + this.biV.nextInt(this.biX - this.biY + 1) + this.biY;
   }

   private void OZ() {
      if (!this.biW.level().isClientSide() && this.biW.isAlive()) {
         double var1 = this.biW.getX() + (this.biV.nextDouble() - 0.5) * 32.0;
         double var3 = this.biW.getY() + (this.biV.nextInt(16) - 8);
         double var5 = this.biW.getZ() + (this.biV.nextDouble() - 0.5) * 32.0;
         this.biW.randomTeleport(var1, var3, var5, true);
      }
   }
}
