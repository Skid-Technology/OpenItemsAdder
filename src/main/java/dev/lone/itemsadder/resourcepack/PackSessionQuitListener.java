package dev.lone.itemsadder.resourcepack;

import java.util.WeakHashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

class PackSessionQuitListener implements Listener {
   PackSessionQuitListener(WeakHashMap var1) {
      this.sessions = var1;
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent var1) {
      this.sessions.remove(var1.getPlayer().getUniqueId());
   }
}
