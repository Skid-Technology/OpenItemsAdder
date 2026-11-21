package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.core.ContentManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;

public class CrafterCraftListenerL implements Listener {
   @EventHandler
   private void method5371(CrafterCraftEvent var1) {
      String var2 = var1.getRecipe().getKey().toString();
      if (var2.startsWith(AbstractRecipeManager.boe) || ContentManager.method8084().bp.dV(var2) != null) {
         var1.setCancelled(true);
      }
   }
}
