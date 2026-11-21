package dev.lone.itemsadder.api;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface CustomRecipe {
   NamespacedKey getNamespacedKey();

   ItemStack getResultItemStackCopy();

   String getPermissionSuffix();
}
