package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.SoundUtils;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class TextEffect extends ItemEffect {
   public final String akH;

   public TextEffect(String var1) {
      this.akH = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      SoundUtils.playSound(var4.getLocation(), SoundCategory.MASTER, this.akH);
      return EffectOutcome.failure();
   }
}
