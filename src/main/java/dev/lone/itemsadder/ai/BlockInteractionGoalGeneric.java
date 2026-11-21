package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGeneric;
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

public class BlockInteractionGoalGeneric extends Goal {
   private static final int biQ = 40;
   private static Predicate beh;
   private final Mob biR;
   private final Level biS;
   private int bek;

   BlockInteractionGoalGeneric(MobGoalFactoryImplGeneric var1, org.bukkit.entity.Mob var2, Predicate var3) {
      this.biT = var2;
      this.biU = var3;
      this.biR = ((CraftMob)this.biT).getHandle();
      this.biS = this.biR.level();
      if (this.biU == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.asBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean canUse() {
      if (!((ServerLevel)this.biS).chunkSource.chunkMap.anyPlayerCloseEnoughForSpawning(this.biR.chunkPosition())) {
         return false;
      }

      if (this.biR.getRandom().nextInt(this.biR.isBaby() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPos var1 = this.biR.blockPosition();
      return beh.test(this.biS.getBlockState(var1));
   }

   public void start() {
      this.bek = this.adjustedTickDelay(40);
      this.biS.broadcastEntityEvent(this.biR, (byte)10);
      this.biR.getNavigation().stop();
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
         BlockPos var1 = this.biR.blockPosition();
         net.minecraft.world.level.block.state.BlockState var2 = this.biS.getBlockState(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(
               this.biR, var1, var2.getFluidState().createLegacyBlock(), !(Boolean)getServerLevel(this.biS).getGameRules().get(GameRules.MOB_GRIEFING)
            )) {
               this.biS.destroyBlock(var1, false);
            }

            this.biR.ate();
         } else {
            BlockPos var3 = var1.below();
            if (this.biS.getBlockState(var3).is(Blocks.GRASS_BLOCK)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(
                  this.biR, var3, Blocks.DIRT.defaultBlockState(), !(Boolean)getServerLevel(this.biS).getGameRules().get(GameRules.MOB_GRIEFING)
               )) {
                  this.biS.levelEvent(2001, var3, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                  this.biS.setBlock(var3, Blocks.DIRT.defaultBlockState(), 2);
               }

               this.biR.ate();
            }
         }
      }
   }
}
