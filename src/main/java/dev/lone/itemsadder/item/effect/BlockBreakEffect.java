package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakEffect extends ItemEffect {
   public final double aja;
   public final int ajb;
   public final int ajc;

   public BlockBreakEffect(double var1, int var3, int var4) {
      this.aja = var1;
      this.ajb = var3;
      this.ajc = var4;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      double var5 = Math.random();
      if (var5 * 100.0 > this.aja) {
         return EffectOutcome.success();
      }

      ((ExperienceOrb)var4.getWorld().spawn(var4.getEyeLocation(), ExperienceOrb.class)).setExperience(Utils.randomInt(this.ajb, this.ajc));
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnBlockBreak(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Block var4 = var1.getBlock();
      Player var5 = var1.getPlayer();
      return this.breakBlock(var5, var4);
   }

   @Override
   public EffectOutcome applyOnCustomBlockAction(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Block var4 = var1.getBlock();
      Player var5 = var1.getPlayer();
      return this.breakBlock(var5, var4);
   }

   private EffectOutcome breakBlock(Player var1, Block var2) {
      if (var2.getType().getHardness() == 0.0F) {
         return EffectOutcome.success();
      }

      if (var2.getType() != Material.CHEST && var2.getType() != Material.TRAPPED_CHEST && var2.getType() != Material.ENDER_CHEST) {
         double var3 = Math.random();
         if (var3 * 100.0 > this.aja) {
            return EffectOutcome.success();
         }

         ((ExperienceOrb)var1.getWorld().spawn(var2.getLocation(), ExperienceOrb.class)).setExperience(Utils.randomInt(this.ajb, this.ajc));
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
