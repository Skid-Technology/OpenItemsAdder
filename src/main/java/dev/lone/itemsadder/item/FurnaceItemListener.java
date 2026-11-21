package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FurnaceItemListener implements IaListener {
   private final ContentManager aeV;

   public FurnaceItemListener(Plugin var1, ContentManager var2) {
      this.aeV = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(InventoryClickEvent.class, this::method681));
   }

   @EventHandler(ignoreCancelled = true)
   private void method680(FurnaceBurnEvent var1) {
      CustomItemDefinition var2 = this.aeV.method8149(var1.getFuel());
      if (var2 != null) {
         ItemBlockAction var3 = (ItemBlockAction)var2.aoh.getAction("fuel");
         if (var3 != null) {
            if (var3.method335(var1.getBlock())) {
               var1.setBurnTime(var3.adE);
            } else {
               Furnace var4 = (Furnace)var1.getBlock().getState();
               var1.setCancelled(true);
               var1.setBurning(false);
               var4.getInventory().setFuel(null);
               if (var4.getInventory().getViewers().isEmpty()) {
                  if (var4.getInventory().getResult() != null && var4.getInventory().getResult().getType() != Material.AIR) {
                     LocationScanner.dropItem(var1.getBlock().getLocation(), var1.getFuel());
                  } else {
                     var4.getInventory().setResult(var1.getFuel());
                  }
               } else {
                  LocationScanner.dropItem(var1.getBlock().getLocation(), var1.getFuel());
               }
            }
         }
      }
   }

   @ItemEventHandler
   private void method681(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getInventory() instanceof FurnaceInventory) {
         if (!var2.aoh.hasAction("fuel")) {
            return;
         }

         ItemBlockAction var4 = (ItemBlockAction)var2.aoh.getAction("fuel");
         if (var4 == null) {
            return;
         }

         if (!var4.method335(var1.getInventory().getLocation().getBlock())) {
            if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
               var1.setCancelled(true);
               return;
            }

            if (var1.getSlotType() == SlotType.FUEL) {
               var1.setCancelled(true);
               return;
            }
         }
      }
   }
}
