package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup2;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RandomStrollGoalGenericDup2 extends Goal {
   private int ben;
   private final RandomSource bjt;

   RandomStrollGoalGenericDup2(MobGoalFactoryImplGenericDup2 var1, PathfinderMob var2, int var3, int var4) {
      this.bju = var2;
      this.bjv = var3;
      this.bjw = var4;
      this.ben = 0;
      this.bjt = this.bju.getRandom();
   }

   public boolean canUse() {
      return this.bju.tickCount >= this.ben;
   }

   public void start() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bju.tickCount + this.bjt.nextInt(this.bjv - this.bjw + 1) + this.bjw;
   }

   private void OZ() {
      if (!this.bju.level().isClientSide() && this.bju.isAlive()) {
         double var1 = this.bju.getX() + (this.bjt.nextDouble() - 0.5) * 32.0;
         double var3 = this.bju.getY() + (this.bjt.nextInt(16) - 8);
         double var5 = this.bju.getZ() + (this.bjt.nextDouble() - 0.5) * 32.0;
         this.bju.randomTeleport(var1, var3, var5, true);
      }
   }
}
