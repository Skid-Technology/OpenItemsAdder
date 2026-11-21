package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.text.PendingMessageStore;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

class GuiInventoryListener implements Listener {
   @EventHandler(priority = EventPriority.LOWEST)
   public void method2428(InventoryClickEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         var2.aIo.method2409(var2);
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void method2429(InventoryDragEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         var2.aIo.method2409(var2);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void method2430(InventoryClickEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         var2.method2440(var1);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void method2431(InventoryDragEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         if (var1.getRawSlots().stream().anyMatch(var1x -> var1x < 54 && !var2.bZ(var1x))) {
            var1.setCancelled(true);
         } else {
            BiConsumer var5 = var2.aIo.aIl;
            if (var5 != null) {
               TreeMap var4 = new TreeMap();
               var1.getNewItems().forEach((var2x, var3) -> {
                  if (var2.bZ(var2x)) {
                     var4.put((var2.aIw + var2x / 9) * 8 + var2x % 9, var3.clone());
                  }
               });
               if (!var2.aIo.method2408("drag", () -> var5.accept(Collections.unmodifiableMap(var4), var1))) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void method2432(InventoryOpenEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         PendingMessageStore.method2753(var2.aIp, var2.aIo.aIc.MG());
      }
   }

   @EventHandler
   public void method2433(InventoryCloseEvent var1) {
      if (var1.getInventory().getHolder() instanceof GuiSession var2) {
         var2.method2439();
      }
   }

   @EventHandler
   public void method2434(PluginDisableEvent var1) {
      for (GuiSessionManager var3 : List.copyOf(GuiSessionManager.aHZ)) {
         if (var3.aIb == var1.getPlugin()) {
            var3.LH();
         }
      }
   }

   @EventHandler
   public void method2435(PlayerQuitEvent var1) {
      for (GuiSessionManager var3 : List.copyOf(GuiSessionManager.aHZ)) {
         GuiSession var4 = (GuiSession)var3.aIg.get(var1.getPlayer().getUniqueId());
         if (var4 != null) {
            var4.method2439();
         }

         var3.aIh.remove(var1.getPlayer().getUniqueId());
         var3.LI();
      }
   }
}
