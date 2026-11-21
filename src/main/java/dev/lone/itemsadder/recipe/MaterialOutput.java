package dev.lone.itemsadder.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialOutput extends RecipeOutput {
   public final Material aCs;

   public MaterialOutput(Material var1) {
      this.aCs = var1;
   }

   @Override
   public ItemStack Kv() {
      return new ItemStack(this.aCs);
   }
}
