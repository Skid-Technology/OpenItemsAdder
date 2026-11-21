package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.BlockEffectsService;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BlockClickToggleEffect extends ToggleEffect {
   final CustomBlockDefinition aki;
   final Material akj;
   private final ClickMode akk;

   public BlockClickToggleEffect(CustomBlockDefinition var1, ClickMode var2, boolean var3) {
      super(var3);
      this.aki = var1;
      this.akk = var2;
      this.akj = null;
   }

   public BlockClickToggleEffect(Material var1, ClickMode var2, boolean var3) {
      super(var3);
      this.akj = var1;
      this.akk = var2;
      this.aki = null;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.applyBlockClick(var1, var4, var3, var4.getLocation().getBlock());
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      return this.applyBlockClick(var1, var5, var4, var2);
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getClickedBlock() == null) {
         return EffectOutcome.successWith(var3);
      } else if (var3 != null && var3.getType() != Material.AIR && !ItemStackUtils.method7242(var3) && !CustomItemDefinition.method8820(var3)) {
         SchedulerUtils.runLaterSync(() -> {
            Player var3x = var1.getPlayer();
            Block var4;
            if (this.akk == ClickMode.CLICKED) {
               var4 = var1.getClickedBlock();
            } else {
               var4 = var1.getClickedBlock().getRelative(var1.getBlockFace());
            }

            boolean var5 = false;
            if (this.aki != null) {
               var5 = ContentManager.method8084().aX.method1435(var1, this.aki, this.aki.Ds(), false, false);
            } else if (this.akj != null && BlockFaceUtils.simulatePlace(var4, var3x)) {
               var4.setType(this.akj);
               BlockEffectsService.playBlockSound(var4);
               var5 = true;
            }

            if (var5 && this.aiP) {
               ItemStackUtils.au(var3);
            }
         }, 2L);
         return EffectOutcome.failureWith(var3);
      } else {
         return EffectOutcome.successWith(var3);
      }
   }

   private EffectOutcome applyBlockClick(Event var1, Player var2, ItemStack var3, Block var4) {
      if (var3 != null && var3.getType() != Material.AIR && !ItemStackUtils.method7242(var3) && !CustomItemDefinition.method8820(var3)) {
         Block var5 = PlayerInteractionFacade.cL(var2);
         if (BlockFaceUtils.simulatePlace(var5, var2)) {
            boolean var6 = false;
            if (this.aki != null) {
               var6 = this.aki.placeBlockNoPhysics(var5);
               if (var6) {
                  this.aki.acY.apR.play(var5.getLocation(), SoundCategory.BLOCKS);
               }
            } else if (this.akj != null) {
               var5.setType(this.akj);
               BlockEffectsService.playBlockSound(var5);
               var6 = true;
            }

            if (var6) {
               PlayerInteractionFacade.method5310(var2, EquipmentSlot.HAND);
               if (this.aiP) {
                  ItemStackUtils.au(var3);
               }

               return EffectOutcome.failure();
            }
         }

         return EffectOutcome.success();
      } else {
         return EffectOutcome.success();
      }
   }
}
