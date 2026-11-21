package dev.lone.itemsadder.ai;

import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalGotoTarget;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

class RegistryGotoTargetGoal extends PathfinderGoalGotoTarget {
   RegistryGotoTargetGoal(MobGoalRegistry var1, EntityCreature var2, double var3, int var5, Predicate var6) {
      super(var2, var3, var5);
      this.bef = var6;
   }

   protected boolean method4856(@NotNull IWorldReader levelReader, @NotNull BlockPosition blockPos) {
      IBlockData var3 = levelReader.a_(blockPos);
      if (var3.i()) {
         return false;
      }

      BlockState var4 = var3.createCraftBlockData().createBlockState();
      return this.bef == null || this.bef.test(var4);
   }
}
