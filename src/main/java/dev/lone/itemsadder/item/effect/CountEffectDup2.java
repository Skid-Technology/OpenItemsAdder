package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class CountEffectDup2 extends ItemEffect {
   public int qn = 1;

   public CountEffectDup2(int var1) {
      this.qn = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      int var5 = ItemStackUtils.aw(var3);
      CustomItemDefinition.method8827(var3, this.qn, var4, false, true);
      return ItemStackUtils.aw(var3) == var5 ? EffectOutcome.successWith(var3) : EffectOutcome.failure();
   }
}
