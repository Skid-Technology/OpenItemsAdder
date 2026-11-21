package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class BlockAreaEffect extends ItemEffect {
   final BlockPairSpec ajW;

   public BlockAreaEffect(BlockPairSpec var1) {
      this.ajW = var1;
   }

   public BlockAreaEffect(BlockMatcherSpec var1, BlockMatcherSpec var2, int var3, int var4, boolean var5) {
      this(new BlockPairSpec(var1, var2, var3, var4, var5));
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.applyBlockAreaAt(var1, var4, var2, var3, var4.getLocation().getBlock());
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      return this.applyBlockAreaAt(var1, var5, var3, var4, var2);
   }

   @Override
   public EffectOutcome applyOnCustomBlockAction(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Block var4 = var1.getBlock();
      Player var5 = var1.getPlayer();
      if (var4 == null) {
         return EffectOutcome.success();
      }

      if (this.ajW.akS.matches(var4)) {
         if (!BlockFaceUtils.simulatePlace(var4, var5)) {
            return EffectOutcome.success();
         }

         var1.setCancelled(true);
         SchedulerUtils.runLaterSync(() -> this.ajW.applyToBlock(var4), 5L);
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }

   private EffectOutcome applyBlockAreaAt(Event var1, Player var2, CustomItemDefinition var3, ItemStack var4, Block var5) {
      if (var5 == null) {
         return EffectOutcome.success();
      }

      if (var4 == null || var4.getType() == Material.AIR || ItemStackUtils.method7242(var4) || CustomItemDefinition.method8820(var4)) {
         return EffectOutcome.success();
      }

      if (!BlockFaceUtils.simulatePlace(var5, var2)) {
         return EffectOutcome.success();
      }

      if (!this.ajW.akS.matches(var5)) {
         return EffectOutcome.success();
      }

      if (!BlockFaceUtils.simulatePlace(var5, var2)) {
         return EffectOutcome.success();
      }

      if (var1 instanceof Cancellable) {
         ((Cancellable)var1).setCancelled(true);
      }

      this.ajW.applyToBlock(var5);
      if (this.ajW.akU > 0) {
         CustomItemDefinition.method8826(var4, this.ajW.akU, var2);
      }

      if (this.ajW.akV > 0) {
         ItemStackUtils.method7231(var4, this.ajW.akV);
      }

      return EffectOutcome.failure();
   }
}
