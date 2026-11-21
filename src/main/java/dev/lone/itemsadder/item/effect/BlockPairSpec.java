package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.api.ItemsAdder;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import org.bukkit.block.Block;

public class BlockPairSpec {
   public final BlockMatcherSpec akS;
   public final BlockMatcherSpec akT;
   public final int akU;
   public final int akV;
   public final boolean akW;

   public BlockPairSpec(BlockMatcherSpec var1, BlockMatcherSpec var2, int var3, int var4, boolean var5) {
      this.akS = var1;
      this.akT = var2;
      this.akU = var3;
      this.akV = var4;
      this.akW = var5;
   }

   public void applyToBlock(Block var1) {
      if (this.akS.akY instanceof CustomBlockDefinition && ((CustomBlockDefinition)this.akS.akY).getLightLevel() != -1) {
         BlockUtils.clearBlockState(var1);
      }

      if (!this.akT.akX) {
         if (this.akS.akX) {
            ItemsAdder.removeCustomBlock(var1.getLocation());
         }

         var1.setType(this.akT.db, !this.akW);
      } else {
         this.akT.akY.placeBlockNoPhysics(var1);
      }
   }
}
