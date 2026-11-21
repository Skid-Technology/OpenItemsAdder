package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.block.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;

public class CrafterCraftListenerM implements Listener {
   @EventHandler
   private void method5372(CrafterCraftEvent var1) {
      if (var1.getBlock().getState() instanceof Crafter var2) {
         for (int var5 = 0; var5 < 9; var5++) {
            if (CustomItemDefinition.isCustomItem(var2.getInventory().getItem(var5))) {
               var1.setCancelled(true);
               return;
            }
         }
      }

      String var4 = var1.getRecipe().getKey().toString();
      if (var4.startsWith(AbstractRecipeManager.boe) || ContentManager.method8084().bp.dV(var4) != null) {
         var1.setCancelled(true);
      }
   }
}
