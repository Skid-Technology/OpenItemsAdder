package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalGotoTarget;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

class GotoTargetGoalV1_21_R4 extends PathfinderGoalGotoTarget {
   GotoTargetGoalV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, EntityCreature var2, double var3, int var5, Predicate var6) {
      super(var2, var3, var5);
      this.bgX = var6;
   }

   protected boolean method5031(@NotNull IWorldReader levelReader, @NotNull BlockPosition blockPos) {
      IBlockData var3 = levelReader.a_(blockPos);
      if (var3.l()) {
         return false;
      }

      BlockState var4 = var3.createCraftBlockData().createBlockState();
      return this.bgX == null || this.bgX.test(var4);
   }
}
