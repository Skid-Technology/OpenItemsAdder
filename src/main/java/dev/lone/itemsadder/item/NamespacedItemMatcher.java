package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import org.bukkit.inventory.ItemStack;

public class NamespacedItemMatcher extends ItemMatcher {
   String namespacedId;

   public NamespacedItemMatcher(String var1) {
      this.namespacedId = var1;
   }

   @Override
   public boolean matches(ItemStack var1) {
      if (var1 != null && !var1.getType().isAir()) {
         CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
         return var2 == null ? false : var2.getNamespacedID().equals(this.namespacedId);
      } else {
         return false;
      }
   }
}
