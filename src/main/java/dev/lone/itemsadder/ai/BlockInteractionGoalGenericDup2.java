package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup2;
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

public class BlockInteractionGoalGenericDup2 extends Goal {
   private static final int bjo = 40;
   private static Predicate beh;
   private final Mob bjp;
   private final Level bjq;
   private int bek;

   BlockInteractionGoalGenericDup2(MobGoalFactoryImplGenericDup2 var1, org.bukkit.entity.Mob var2, Predicate var3) {
      this.bjr = var2;
      this.bjs = var3;
      this.bjp = ((CraftMob)this.bjr).getHandle();
      this.bjq = this.bjp.level();
      if (this.bjs == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.asBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean canUse() {
      if (!((ServerLevel)this.bjq).chunkSource.chunkMap.anyPlayerCloseEnoughForSpawning(this.bjp.chunkPosition())) {
         return false;
      }

      if (this.bjp.getRandom().nextInt(this.bjp.isBaby() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPos var1 = this.bjp.blockPosition();
      return beh.test(this.bjq.getBlockState(var1));
   }

   public void start() {
      this.bek = this.adjustedTickDelay(40);
      this.bjq.broadcastEntityEvent(this.bjp, (byte)10);
      this.bjp.getNavigation().stop();
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
         BlockPos var1 = this.bjp.blockPosition();
         net.minecraft.world.level.block.state.BlockState var2 = this.bjq.getBlockState(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(
               this.bjp, var1, var2.getFluidState().createLegacyBlock(), !(Boolean)getServerLevel(this.bjq).getGameRules().get(GameRules.MOB_GRIEFING)
            )) {
               this.bjq.destroyBlock(var1, false);
            }

            this.bjp.ate();
         } else {
            BlockPos var3 = var1.below();
            if (this.bjq.getBlockState(var3).is(Blocks.GRASS_BLOCK)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(
                  this.bjp, var3, Blocks.DIRT.defaultBlockState(), !(Boolean)getServerLevel(this.bjq).getGameRules().get(GameRules.MOB_GRIEFING)
               )) {
                  this.bjq.levelEvent(2001, var3, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                  this.bjq.setBlock(var3, Blocks.DIRT.defaultBlockState(), 2);
               }

               this.bjp.ate();
            }
         }
      }
   }
}
