package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AnvilRenameListener implements IaListener {
   public AnvilRenameListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(priority = EventPriority.HIGH)
   private void method830(PrepareAnvilEvent var1) {
      ItemStack var2 = var1.getInventory().getItem(1);
      if (var2 != null && var2.getType() != Material.AIR) {
         ItemStack var3 = var1.getInventory().getItem(0);
         CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
         if (var4 != null) {
            if (var1.getInventory().getRepairCost() > 1) {
               var1.getInventory().setRepairCost(var1.getInventory().getRepairCost() - 1);
            }

            ItemStack var5 = var1.getResult();
            CustomItemDefinition var6 = ContentManager.method8084().method8149(var5);
            if (var6 == null) {
               return;
            }

            Component var7 = ItemStackUtils.as(var5);
            Component var8 = ItemStackUtils.as(var6);
            if (var8 == null || var7 == null) {
               return;
            }

            if (var8.equals(var7)) {
               ItemStackUtils.method7223(var5, var8);
            }
         }
      }
   }
}
