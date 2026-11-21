package dev.lone.itemsadder.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialItemMatcher extends ItemMatcher {
   Material db;

   public MaterialItemMatcher(Material var1) {
      this.db = var1;
   }

   @Override
   public boolean matches(ItemStack var1) {
      return var1 != null && !var1.getType().isAir() && var1.getType() == this.db;
   }
}
