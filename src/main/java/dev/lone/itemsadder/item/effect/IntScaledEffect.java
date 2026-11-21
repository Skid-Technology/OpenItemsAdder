package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class IntScaledEffect extends ItemEffect {
   public final int ajn;
   public final float ajo;

   public IntScaledEffect(int var1, float var2) {
      this.ajn = var1;
      this.ajo = var2;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      int var5 = var4.getFoodLevel();
      float var6 = var4.getSaturation();
      int var7 = Math.min(var5 + this.ajn, 20);
      float var8 = Math.min(var6 + this.ajo, var7);
      var4.setFoodLevel(var7);
      var4.setSaturation(var8);
      return var5 == var7 && var6 == var8 ? EffectOutcome.success() : EffectOutcome.failure();
   }
}
