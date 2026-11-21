package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class RecipeJoinListener implements Listener {
   public RecipeJoinListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void method2272(PlayerJoinEvent var1) {
      RecipeManagerHolder.cO(var1.getPlayer());
   }
}
