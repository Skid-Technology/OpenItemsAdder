package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@Deprecated
public class LegacyGrindstoneListener implements Listener {
   @EventHandler(ignoreCancelled = true)
   private void onGrindstoneClick(InventoryClickEvent var1) {
      if (!Main.aEW) {
         if (var1.getInventory().getType() == InventoryType.GRINDSTONE) {
            Inventory var2 = var1.getInventory();
            int var3 = var1.getSlot();
            if (ItemStackUtils.method7217(var1)) {
               if (var3 != 0 && var3 != 1) {
                  GrindstoneListener.method1068(var1, var1x -> var2.setItem(2, var1x));
               } else {
                  SchedulerUtils.runLaterSync(() -> GrindstoneListener.method1068(var1, var1xx -> var2.setItem(2, var1xx)), 5L);
               }
            } else if (var1.getClick() == ClickType.SHIFT_RIGHT || var1.getClick() == ClickType.SHIFT_LEFT) {
               SchedulerUtils.runLaterSync(() -> GrindstoneListener.method1068(var1, var1xx -> var2.setItem(2, var1xx)), 5L);
            }
         }
      }
   }
}
