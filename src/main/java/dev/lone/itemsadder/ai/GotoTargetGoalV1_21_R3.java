package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalGotoTarget;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

class GotoTargetGoalV1_21_R3 extends PathfinderGoalGotoTarget {
   GotoTargetGoalV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, EntityCreature var2, double var3, int var5, Predicate var6) {
      super(var2, var3, var5);
      this.bgz = var6;
   }

   protected boolean method5000(@NotNull IWorldReader levelReader, @NotNull BlockPosition blockPos) {
      IBlockData var3 = levelReader.a_(blockPos);
      if (var3.l()) {
         return false;
      }

      BlockState var4 = var3.createCraftBlockData().createBlockState();
      return this.bgz == null || this.bgz.test(var4);
   }
}
