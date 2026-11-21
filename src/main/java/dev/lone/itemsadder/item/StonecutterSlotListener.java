package dev.lone.itemsadder.item;

import dev.lone.itemsadder.event.IaListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class StonecutterSlotListener implements IaListener {
   @EventHandler(ignoreCancelled = true)
   private void onStonecutterClick(InventoryClickEvent var1) {
      if (var1.getInventory().getType() == InventoryType.STONECUTTER) {
         if (var1.getSlot() == 1) {
            if (CustomItemDefinition.isCustomItem(var1.getInventory().getStorageContents()[0])) {
               var1.getInventory().setItem(1, new ItemStack(Material.AIR));
               var1.getWhoClicked().closeInventory();
            }
         }
      }
   }
}
