package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.block.CustomBlockDefinition;

public class BlockToolDropRule extends ToolDropRule {
   final CustomBlockDefinition atx;

   public BlockToolDropRule(CustomBlockDefinition var1) {
      this.atx = var1;
   }

   public CustomBlockDefinition Bu() {
      return this.atx;
   }
}
