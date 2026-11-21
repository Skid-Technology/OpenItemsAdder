package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup2;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GotoTargetGoalGenericDup2 extends MoveToBlockGoal {
   GotoTargetGoalGenericDup2(MobGoalFactoryImplGenericDup2 var1, PathfinderMob var2, double var3, int var5, Predicate var6) {
      super(var2, var3, var5);
      this.bjn = var6;
   }

   protected boolean isValidTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos) {
      BlockState var3 = levelReader.getBlockState(blockPos);
      if (var3.isAir()) {
         return false;
      }

      org.bukkit.block.BlockState var4 = var3.asBlockData().createBlockState();
      return this.bjn == null || this.bjn.test(var4);
   }
}
