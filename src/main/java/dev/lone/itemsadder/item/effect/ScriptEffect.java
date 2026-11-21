package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ScriptEffect extends ItemEffect {
   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (var1 instanceof Cancellable) {
         ((Cancellable)var1).setCancelled(true);
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getClickedBlock() != null) {
         var1.setCancelled(true);
         return EffectOutcome.failureWith(var3);
      } else {
         return EffectOutcome.successWith(var3);
      }
   }

   @Override
   public EffectOutcome applyOnPickup(PlayerPickupItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var1.setCancelled(true);
      var1.getItem().setPickupDelay(40);
      return EffectOutcome.failureWith(var3);
   }
}
