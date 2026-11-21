package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class CountEffect extends ItemEffect {
   public final int aiW;

   public CountEffect(int var1) {
      this.aiW = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      ItemStackUtils.method7231(var3, this.aiW);
      return EffectOutcome.failure();
   }
}
