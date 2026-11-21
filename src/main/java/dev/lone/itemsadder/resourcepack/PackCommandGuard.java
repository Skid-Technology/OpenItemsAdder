package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

public class PackCommandGuard implements IaListener {
   private final HostedPackDelivery bJc;

   public PackCommandGuard(Plugin var1, HostedPackDelivery var2) {
      this.bJc = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   public void stop() {
      EventRegistrar.unregisterListener(this);
   }

   @EventHandler
   private void method1054(ServerCommandEvent var1) {
      if (this.bJc.bJa) {
         if (var1.getCommand().equalsIgnoreCase("acceptprivacy")) {
            this.bJc.setPrivacyAccepted(var1.getSender(), true);
            var1.setCancelled(true);
         } else if (var1.getCommand().equalsIgnoreCase("declineprivacy")) {
            this.bJc.setPrivacyAccepted(var1.getSender(), false);
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   private void method1055(PlayerCommandPreprocessEvent var1) {
      if (this.bJc.bJa) {
         if (var1.getMessage().equalsIgnoreCase("/acceptprivacy")) {
            this.bJc.setPrivacyAccepted(var1.getPlayer(), true);
            var1.setCancelled(true);
         } else if (var1.getMessage().equalsIgnoreCase("/declineprivacy")) {
            this.bJc.setPrivacyAccepted(var1.getPlayer(), false);
            var1.setCancelled(true);
         }
      }
   }
}
