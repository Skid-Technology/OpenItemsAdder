package dev.lone.itemsadder.item;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemHandlerCallback {
   void execute(Object var1, CustomItemDefinition var2, ItemStack var3);
}
