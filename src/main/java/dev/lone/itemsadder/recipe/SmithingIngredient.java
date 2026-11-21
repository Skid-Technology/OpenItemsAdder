package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

public class SmithingIngredient {
   final RecipeChoice aCt;
   final ItemStack aCu;
   String aCv;

   public SmithingIngredient(Object var1) {
      if (var1 instanceof Material) {
         this.aCu = new ItemStack((Material)var1);
         this.aCt = new MaterialChoice((Material)var1);
      } else if (var1 instanceof CustomItemDefinition var2) {
         this.aCu = var2.Ds();
         this.aCt = new ExactChoice(this.aCu);
         this.aCv = var2.aok;
      } else {
         if (!(var1 instanceof ItemStack)) {
            throw new IllegalArgumentException("Invalid ingredient type: " + var1.getClass().getName());
         }

         this.aCu = (ItemStack)var1;
         this.aCt = new ExactChoice((ItemStack)var1);
      }
   }

   public ItemStack getItem() {
      return this.aCu;
   }

   public boolean dW(String var1) {
      return var1.equals(this.aCv);
   }

   public boolean Kw() {
      return this.aCv != null;
   }
}
