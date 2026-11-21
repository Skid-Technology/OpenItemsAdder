package dev.lone.itemsadder.recipe;

import org.bukkit.inventory.ItemStack;

public class ItemStackOutput extends RecipeOutput {
   private final ItemStack aCr;

   public ItemStackOutput(ItemStack var1) {
      this.aCr = var1;
   }

   @Override
   public ItemStack Kv() {
      return this.aCr.clone();
   }
}
