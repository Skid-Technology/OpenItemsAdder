package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class PotionRemoveEffect extends ItemEffect {
   public final PotionEffectType ajV;

   public PotionRemoveEffect(PotionEffectType var1) {
      this.ajV = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (var4.hasPotionEffect(this.ajV)) {
         var4.removePotionEffect(this.ajV);
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
