package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.util.FloatVector3Base;

public class BlockPairSpecOffset extends BlockPairSpec {
   public final FloatVector3Base akZ;

   public BlockPairSpecOffset(BlockMatcherSpec var1, BlockMatcherSpec var2, FloatVector3Base var3, int var4, int var5, boolean var6) {
      super(var1, var2, var4, var5, var6);
      this.akZ = var3;
   }
}
