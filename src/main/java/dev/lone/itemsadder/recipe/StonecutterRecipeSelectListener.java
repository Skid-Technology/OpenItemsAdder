package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.CustomItemDefinition;
import io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StonecutterRecipeSelectListener implements Listener {
   @EventHandler(ignoreCancelled = true)
   private void method5374(PlayerStonecutterRecipeSelectEvent var1) {
      if (CustomItemDefinition.method8748(var1.getStonecutterInventory().getItem(0))) {
         var1.setCancelled(true);
      }
   }
}
