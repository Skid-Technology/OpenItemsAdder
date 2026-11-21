package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup4;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RandomStrollGoalGenericDup4 extends Goal {
   private int ben;
   private final RandomSource bkp;

   RandomStrollGoalGenericDup4(MobGoalFactoryImplGenericDup4 var1, PathfinderMob var2, int var3, int var4) {
      this.bkq = var2;
      this.bkr = var3;
      this.bks = var4;
      this.ben = 0;
      this.bkp = this.bkq.getRandom();
   }

   public boolean canUse() {
      return this.bkq.tickCount >= this.ben;
   }

   public void start() {
      this.OZ();
      this.OY();
   }

   private void OY() {
      this.ben = this.bkq.tickCount + this.bkp.nextInt(this.bkr - this.bks + 1) + this.bks;
   }

   private void OZ() {
      if (!this.bkq.level().isClientSide() && this.bkq.isAlive()) {
         double var1 = this.bkq.getX() + (this.bkp.nextDouble() - 0.5) * 32.0;
         double var3 = this.bkq.getY() + (this.bkp.nextInt(16) - 8);
         double var5 = this.bkq.getZ() + (this.bkp.nextDouble() - 0.5) * 32.0;
         this.bkq.randomTeleport(var1, var3, var5, true, BlockTags.DANGEROUS_FOR_TELEPORTATION);
      }
   }
}
