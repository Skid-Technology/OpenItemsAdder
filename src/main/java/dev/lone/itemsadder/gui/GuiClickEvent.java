package dev.lone.itemsadder.gui;

import java.util.function.Consumer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

class GuiClickEvent extends InventoryClickEvent {
   GuiClickEvent(ScrollableGuiInventory var1, InventoryView var2, SlotType var3, int var4, ClickType var5, InventoryAction var6, int var7, int var8) {
      super(var2, var3, var4, var5, var6, var7);
      this.aLN = var8;
      this.aLO = var1;
   }

   public ItemStack getCurrentItem() {
      ItemStack var1 = (ItemStack)this.aLO.aLJ.get(this.aLN);
      return var1 == null ? null : var1.clone();
   }

   public void setCurrentItem(ItemStack item) {
      this.aLO.method2666(this.aLN, item, (Consumer)this.aLO.aLI.get(this.aLN));
      if (this.aLO.aLM != null) {
         this.aLO.aLM.Mq();
      }
   }
}
