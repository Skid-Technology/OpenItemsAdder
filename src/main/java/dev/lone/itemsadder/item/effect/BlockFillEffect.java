package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.FloatVector3Base;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class BlockFillEffect extends ItemEffect {
   final List akc;

   public BlockFillEffect(BlockPairSpecOffset var1) {
      this.akc = List.of(var1);
   }

   public BlockFillEffect(BlockMatcherSpec var1, BlockMatcherSpec var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      this(List.of(var1), var2, var3, var4, var5, var6, var7, var8);
   }

   public BlockFillEffect(List var1, BlockMatcherSpec var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      FloatVector3Base var9 = new FloatVector3Base(Math.max(var3 - 1, 1), Math.max(var4 - 1, 1), Math.max(var5 - 1, 1));
      this.akc = new ArrayList(var1.size());

      for (BlockMatcherSpec var11 : var1) {
         this.akc.add(new BlockPairSpecOffset(var11, var2, var9, var6, var7, var8));
      }
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.fillBlocks(var4, var3, var4.getLocation().getBlock());
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      return this.fillBlocks(var5, var4, var2);
   }

   private EffectOutcome fillBlocks(Player var1, ItemStack var2, Block var3) {
      if (var3 == null) {
         return EffectOutcome.success();
      }

      if (var2 != null && var2.getType() != Material.AIR && !ItemStackUtils.method7242(var2) && !CustomItemDefinition.method8820(var2)) {
         Predicate var4 = var1x -> BlockFaceUtils.simulatePlace(var1x, var1);
         boolean var5 = this.canFillBlockDup2(var3, var2x -> this.canFillBlock(var2x, var4));
         BlockPairSpecOffset var6 = (BlockPairSpecOffset)this.akc.get(0);
         if (var6.akU > 0 && var5) {
            CustomItemDefinition.method8826(var2, var6.akU, var1);
         }

         if (var6.akV > 0) {
            ItemStackUtils.method7231(var2, var6.akV);
         }

         return var5 ? EffectOutcome.failure() : EffectOutcome.success();
      } else {
         return EffectOutcome.success();
      }
   }

   boolean canFillBlock(Block var1, Predicate var2) {
      for (BlockPairSpecOffset var4 : this.akc) {
         if (var4.akS.matches(var1)) {
            if (!var2.test(var1)) {
               return false;
            }

            var4.applyToBlock(var1);
            return true;
         }
      }

      return false;
   }

   boolean canFillBlockDup2(Block var1, Predicate var2) {
      FloatVector3Base var3 = ((BlockPairSpecOffset)this.akc.get(0)).akZ;
      boolean var4 = false;

      for (int var5 = -((int)var3.x); var5 <= var3.x; var5++) {
         for (int var6 = -((int)var3.y); var6 <= var3.y; var6++) {
            for (int var7 = -((int)var3.z); var7 <= var3.z; var7++) {
               if (var2.test(var1.getRelative(var5, var6, var7))) {
                  var4 = true;
               }
            }
         }
      }

      return var4;
   }
}
