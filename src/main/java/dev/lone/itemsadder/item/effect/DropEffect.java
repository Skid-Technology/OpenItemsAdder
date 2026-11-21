package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Core.OtherPlugins.mmoitems.MmoItemsHook;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.PlayerEntityInteractEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DropEffect extends ItemEffect {
   public final ItemStack ajd;
   public final double aje;
   public final int ajf;
   public final int ajg;

   public DropEffect(ItemStack var1, double var2, int var4, int var5) {
      this.ajd = var1;
      this.aje = var2;
      this.ajf = var4;
      this.ajg = var5;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.dropAtLocation(var4.getEyeLocation());
   }

   @Override
   public EffectOutcome applyOnBlockBreak(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Block var4 = var1.getBlock();
      return this.dropAtBlock(var4, true);
   }

   @Override
   public EffectOutcome applyOnEntityInteract(PlayerEntityInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.dropAtBlock(var1.anh.getLocation().getBlock(), false);
   }

   @Override
   public EffectOutcome applyOnCustomBlockAction(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Block var4 = var1.getBlock();
      return this.dropAtBlock(var4, false);
   }

   private EffectOutcome dropAtBlock(Block var1, boolean var2) {
      if (var2 && var1.getType().getHardness() == 0.0F) {
         return EffectOutcome.success();
      } else {
         return var1.getType() != Material.CHEST && var1.getType() != Material.TRAPPED_CHEST && var1.getType() != Material.ENDER_CHEST
            ? this.dropAtLocation(var1.getLocation())
            : EffectOutcome.success();
      }
   }

   private EffectOutcome dropAtLocation(Location var1) {
      if (this.aje != 100.0 && Math.random() * 100.0 > this.aje) {
         return EffectOutcome.success();
      }

      ItemStack var2 = this.ajd.clone();
      MmoItemsHook.injectMmoItem(var2);
      var2.setAmount(Utils.randomInt(this.ajf, this.ajg));
      LocationScanner.dropItem(var1, var2);
      return EffectOutcome.failure();
   }
}
