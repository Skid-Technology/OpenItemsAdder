package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class ProjectileLaunchEffect extends ItemEffect {
   public final int ajt;

   public ProjectileLaunchEffect(int var1) {
      this.ajt = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var3.setAmount(var3.getAmount() + this.ajt);
      var4.updateInventory();
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnProjectileLaunch(ProjectileLaunchEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return var3 != null && var3.getType() != Material.AIR && var3.getAmount() > 0 ? EffectOutcome.success() : EffectOutcome.success();
   }
}
