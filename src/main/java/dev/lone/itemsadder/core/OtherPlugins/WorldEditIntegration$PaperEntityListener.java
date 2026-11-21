package dev.lone.itemsadder.Core.OtherPlugins;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

final class WorldEditIntegration$PaperEntityListener implements Listener {
   private WorldEditIntegration$PaperEntityListener(WorldEditIntegration var1) {
      this.this$0 = var1;
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onEntityAdd(EntityAddToWorldEvent event) {
      this.this$0.onEntityAdd(event.getEntity());
   }
}
