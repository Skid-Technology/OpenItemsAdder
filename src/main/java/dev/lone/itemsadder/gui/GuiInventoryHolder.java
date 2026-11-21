package dev.lone.itemsadder.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiInventoryHolder implements InventoryHolder {
   private final CustomInventoryHolder aLH;

   private GuiInventoryHolder(CustomInventoryHolder var1) {
      this.aLH = var1;
   }

   public Inventory getInventory() {
      return this.aLH.getInventory();
   }

   public CustomInventoryHolder ME() {
      return this.aLH;
   }
}
