package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RandomStrollGoalGenericDup3 extends Goal {
   private int ben;
   private final RandomSource bjR;

   RandomStrollGoalGenericDup3(MobGoalFactoryImplGenericDup3 var1, PathfinderMob var2, int var3, int var4) {
      this.bjS = var2;
      this.bjT = var3;
      this.bjU = var4;
      this.ben = 0;
      this.bjR = this.bjS.getRandom();
   }

   public boolean canUse() {
      return this.bjS.tickCount >= this.ben;
   }

   public void start() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bjS.tickCount + this.bjR.nextInt(this.bjT - this.bjU + 1) + this.bjU;
   }

   private void OZ() {
      if (!this.bjS.level().isClientSide() && this.bjS.isAlive()) {
         double var1 = this.bjS.getX() + (this.bjR.nextDouble() - 0.5) * 32.0;
         double var3 = this.bjS.getY() + (this.bjR.nextInt(16) - 8);
         double var5 = this.bjS.getZ() + (this.bjR.nextDouble() - 0.5) * 32.0;
         this.bjS.randomTeleport(var1, var3, var5, true);
      }
   }
}
