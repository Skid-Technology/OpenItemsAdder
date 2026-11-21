package dev.lone.itemsadder.item;

import dev.lone.itemsadder.event.IaListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class BeaconSlotListener implements IaListener {
   @EventHandler(ignoreCancelled = true)
   private void method742(InventoryClickEvent var1) {
      if (var1.getInventory().getType() == InventoryType.BEACON) {
         if (var1.getSlot() == 0) {
            if (CustomItemDefinition.isCustomItem(var1.getCursor())) {
               var1.setCancelled(true);
            }
         } else if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && CustomItemDefinition.isCustomItem(var1.getCurrentItem())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method823(InventoryDragEvent var1) {
      if (var1.getInventory().getType() == InventoryType.BEACON) {
         if (var1.getRawSlots().contains(0) && (CustomItemDefinition.isCustomItem(var1.getCursor()) || CustomItemDefinition.isCustomItem(var1.getOldCursor()))) {
            var1.setCancelled(true);
         }
      }
   }
}
