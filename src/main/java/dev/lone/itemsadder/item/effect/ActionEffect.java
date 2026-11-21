package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ActionEffect extends ItemEffect {
   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      Log.warn("remove_furniture action cannot be used on this event type.");
      return EffectOutcome.successWith(var3);
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyWithPlayer(var1, var1.getPlayer(), var2, var3);
   }

   public EffectOutcome applyOnInteractEntity(PlayerInteractAtEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyWithPlayer(var1, var1.getPlayer(), var2, var3);
   }

   public EffectOutcome applyWithPlayer(Event var1, Player var2, CustomItemDefinition var3, ItemStack var4) {
      Entity var5 = ItemEntityAction.aC(var2);
      if (var5 == null) {
         return this.applyEffect(var1, var3, var4, var2);
      }

      ContentManager.method8084().aV.method564(var5, var2, false, true, false);
      return EffectOutcome.failure();
   }
}
