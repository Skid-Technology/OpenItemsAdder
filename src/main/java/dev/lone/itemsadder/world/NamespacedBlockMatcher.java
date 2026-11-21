package dev.lone.itemsadder.world;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.block.BlockState;

public class NamespacedBlockMatcher extends BlockStateMatcher {
   String namespacedId;

   public NamespacedBlockMatcher(String var1) {
      this.namespacedId = var1;
   }

   @Override
   public boolean matches(BlockState var1) {
      CustomBlock var2 = CustomBlock.byAlreadyPlaced(var1.getBlock());
      return var2 == null ? false : var2.getNamespacedID().equals(this.namespacedId);
   }
}
