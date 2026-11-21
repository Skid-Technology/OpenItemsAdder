package dev.lone.itemsadder.world;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

public class BlockDataMatcher extends BlockStateMatcher {
   BlockData blockData;

   public BlockDataMatcher(BlockData var1) {
      this.blockData = var1;
   }

   @Override
   public boolean matches(BlockState var1) {
      return this.blockData == null ? false : var1.getType() == this.blockData.getMaterial() && this.blockData.matches(var1.getBlockData());
   }
}
