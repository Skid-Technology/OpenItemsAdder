package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

public class RecipeIngredient {
   final RecipeChoice aBH;
   final ItemStack aBI;
   final boolean aBJ;

   public RecipeIngredient(Object var1) {
      if (var1 instanceof Material) {
         this.aBH = new MaterialChoice((Material)var1);
         this.aBI = new ItemStack((Material)var1);
      } else {
         ItemStack var2 = ItemStackUtils.aF((ItemStack)var1);
         this.aBH = new ExactChoice(var2);
         this.aBI = var2;
      }

      this.aBJ = CustomItemDefinition.isCustomItem(this.aBI);
   }

   public RecipeChoice Kf() {
      return this.aBH;
   }

   public ItemStack getItemStack() {
      return this.aBI;
   }
}
