package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Core.OtherPlugins.mmoitems.MmoItemsHook;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ItemGrantEffect extends ItemEffect {
   public final ItemStack ajp;

   public ItemGrantEffect(ItemStack var1, int var2) {
      this.ajp = var1.clone();
      this.ajp.setAmount(var2);
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      MmoItemsHook.injectMmoItem(this.ajp);
      ItemStackUtils.method7243(var4, this.ajp);
      return EffectOutcome.failure();
   }
}
