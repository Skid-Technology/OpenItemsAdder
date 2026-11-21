package dev.lone.itemsadder.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public final class CustomInventoryListener implements Listener {
   private final Plugin bQg;

   public CustomInventoryListener(Plugin var1) {
      this.bQg = var1;
   }

   @EventHandler
   public void method7639(InventoryClickEvent var1) {
      if (var1.getInventory().getHolder() instanceof CustomInventoryHolder && var1.getClickedInventory() != null) {
         CustomInventoryHolder var2 = (CustomInventoryHolder)var1.getInventory().getHolder();
         boolean var3 = var1.isCancelled();
         var1.setCancelled(true);
         var2.method2687(var1);
         if (!var3 && !var1.isCancelled()) {
            var1.setCancelled(false);
         }
      }
   }

   @EventHandler
   public void method7640(InventoryDragEvent var1) {
      if (var1.getInventory().getHolder() instanceof CustomInventoryHolder) {
         CustomInventoryHolder var2 = (CustomInventoryHolder)var1.getInventory().getHolder();
         boolean var3 = var1.isCancelled();
         var1.setCancelled(true);
         var2.method2688(var1);
         if (!var3 && !var1.isCancelled()) {
            var1.setCancelled(false);
         }
      }
   }

   @EventHandler
   public void method7641(InventoryOpenEvent var1) {
      if (var1.getInventory().getHolder() instanceof CustomInventoryHolder) {
         CustomInventoryHolder var2 = (CustomInventoryHolder)var1.getInventory().getHolder();
         var2.method2685(var1);
      }
   }

   @EventHandler
   public void method7642(InventoryCloseEvent var1) {
      if (var1.getInventory().getHolder() instanceof CustomInventoryHolder) {
         CustomInventoryHolder var2 = (CustomInventoryHolder)var1.getInventory().getHolder();
         if (var2.method2686(var1)) {
            Bukkit.getScheduler().runTask(this.bQg, () -> var2.open((Player)var1.getPlayer()));
         }
      }
   }

   @EventHandler
   public void method7643(PluginDisableEvent var1) {
      if (var1.getPlugin() == this.bQg) {
         GuiEventGuard.LF();
         GuiEventGuard.aeb().set(false);
      }
   }
}
