package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.inventory.ItemStack;

public class CustomItemOutput extends RecipeOutput {
   private final CustomItemDefinition aCq;

   public CustomItemOutput(CustomItemDefinition var1) {
      this.aCq = var1;
   }

   public CustomItemDefinition Fp() {
      return this.aCq;
   }

   @Override
   public ItemStack Kv() {
      return this.aCq.Ds();
   }
}
