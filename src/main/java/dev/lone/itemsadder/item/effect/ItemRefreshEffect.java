package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.SoundPlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ItemRefreshEffect extends ItemEffect {
   public final CustomItemDefinition ajO;

   public ItemRefreshEffect(CustomItemDefinition var1) {
      this.ajO = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      SoundPlayer.method7390(Sound.ITEM_TOTEM_USE, var4, SoundCategory.PLAYERS, 20L, () -> PlayerInteractionFacade.method5319(var4, this.ajO));
      return EffectOutcome.failure();
   }
}
