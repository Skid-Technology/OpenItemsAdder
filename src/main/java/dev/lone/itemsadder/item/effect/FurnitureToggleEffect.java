package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.ObjectPair;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FurnitureToggleEffect extends ToggleEffect {
   final FurnitureItemEntity ajC;

   public FurnitureToggleEffect(FurnitureItemEntity var1, boolean var2) {
      super(var2);
      this.ajC = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.toggleFurniture(var1, var4, var3, var4.getLocation().getBlock());
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      return this.toggleFurniture(var1, var5, var4, var2);
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getClickedBlock() == null) {
         return EffectOutcome.successWith(var3);
      }

      if (var3 != null && var3.getType() != Material.AIR && !ItemStackUtils.method7242(var3) && !CustomItemDefinition.method8820(var3)) {
         ObjectPair var4 = ItemEntityAction.aD(var1.getPlayer());
         if (var4 != null) {
            return EffectOutcome.success();
         }

         if (BlockFaceUtils.simulatePlace(var1.getClickedBlock(), var1.getPlayer())) {
            if (var1.getPlayer().getGameMode() != GameMode.CREATIVE && this.aiP) {
               ItemStackUtils.au(var3);
            }

            SchedulerUtils.runLaterSync(() -> {
               Block var2x = var1.getClickedBlock().getRelative(var1.getBlockFace());
               this.ajC.method413(this.ajC.aeT.Ds(), var2x.getLocation(), var2x, var1.getHand(), var1.getPlayer(), null, var1.getBlockFace());
            }, 2L);
         }

         return EffectOutcome.failureWith(var3);
      } else {
         return EffectOutcome.successWith(var3);
      }
   }

   private EffectOutcome toggleFurniture(Event var1, Player var2, ItemStack var3, Block var4) {
      ObjectPair var5 = ItemEntityAction.aD(var2);
      if (var5 != null) {
         return EffectOutcome.success();
      }

      if (BlockFaceUtils.simulatePlace(var4, var2)) {
         this.ajC.method413(this.ajC.aeT.Ds(), var4.getLocation(), var4, null, var2, null, null);
         if (var2.getGameMode() != GameMode.CREATIVE && this.aiP) {
            ItemStackUtils.au(var3);
         }

         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
