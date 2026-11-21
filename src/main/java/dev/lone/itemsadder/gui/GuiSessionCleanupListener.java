package dev.lone.itemsadder.gui;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

class GuiSessionCleanupListener implements Listener {
   @EventHandler
   public void method2473(PluginDisableEvent var1) {
      ScrollableGuiAssets.method6619(var1.getPlugin());

      for (GuiSessionEntry var3 : List.copyOf(GuiSessionRegistry.aIy.values())) {
         if (var3.aIE.aIA == var1.getPlugin()) {
            var3.aIE.close(var3.aIF);
         }
      }
   }

   @EventHandler
   public void method2474(PlayerQuitEvent var1) {
      GuiSessionRegistry.aIy.remove(var1.getPlayer().getUniqueId());
   }
}
