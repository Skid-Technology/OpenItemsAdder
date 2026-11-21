package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup4;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R7.event.CraftEventFactory;

public class BlockInteractionGoalGenericDup4 extends Goal {
   private static final int bkk = 40;
   private static Predicate beh;
   private final Mob bkl;
   private final Level bkm;
   private int bek;

   BlockInteractionGoalGenericDup4(MobGoalFactoryImplGenericDup4 var1, org.bukkit.entity.Mob var2, Predicate var3) {
      this.bkn = var2;
      this.bko = var3;
      this.bkl = ((CraftMob)this.bkn).getHandle();
      this.bkm = this.bkl.level();
      if (this.bko == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.asBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean canUse() {
      if (!((ServerLevel)this.bkm).getChunkSource().chunkMap.anyPlayerCloseEnoughForSpawning(this.bkl.chunkPosition())) {
         return false;
      }

      if (this.bkl.getRandom().nextInt(this.bkl.isBaby() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPos var1 = this.bkl.blockPosition();
      return beh.test(this.bkm.getBlockState(var1));
   }

   public void start() {
      this.bek = this.adjustedTickDelay(40);
      this.bkm.broadcastEntityEvent(this.bkl, (byte)10);
      this.bkl.getNavigation().stop();
   }

   public void stop() {
      this.bek = 0;
   }

   public boolean canContinueToUse() {
      return this.bek > 0;
   }

   public void tick() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.adjustedTickDelay(4)) {
         BlockPos var1 = this.bkl.blockPosition();
         net.minecraft.world.level.block.state.BlockState var2 = this.bkm.getBlockState(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(
               this.bkl, var1, var2.getFluidState().createLegacyBlock(), !(Boolean)getServerLevel(this.bkm).getGameRules().get(GameRules.MOB_GRIEFING)
            )) {
               this.bkm.destroyBlock(var1, false);
            }

            this.bkl.ate();
         } else {
            BlockPos var3 = var1.below();
            if (this.bkm.getBlockState(var3).is(Blocks.GRASS_BLOCK)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(
                  this.bkl, var3, Blocks.DIRT.defaultBlockState(), !(Boolean)getServerLevel(this.bkm).getGameRules().get(GameRules.MOB_GRIEFING)
               )) {
                  this.bkm.levelEvent(2014, var3, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                  this.bkm.setBlock(var3, Blocks.DIRT.defaultBlockState(), 2);
               }

               this.bkl.ate();
            }
         }
      }
   }
}
