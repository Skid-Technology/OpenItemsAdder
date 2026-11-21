package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.internal.SwitchMap5;
import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CookingRecipe extends AbstractCustomRecipe {
   final RecipeIngredient aBK;
   final float aBL;
   final int aBM;
   Material aBN;

   public CookingRecipe(YamlConfig var1, String var2, String var3, ItemStack var4, RecipeIngredient var5, float var6, int var7) {
      super(var1, var2, var3, var4);
      this.aBK = var5;
      this.aBL = var6;
      this.aBM = var7;
      this.aBN = Material.FURNACE;
      this.aBq = 1;
   }

   @Override
   public boolean JU() {
      return this.aBK != null && this.aBK.aBI.getType() != Material.AIR;
   }

   public void method1588(org.bukkit.inventory.CookingRecipe var1) {
      if (this.JS() != null) {
         var1.setGroup(this.JS().aok);
      } else {
         var1.setGroup(this.JR().getType().toString());
      }
   }

   public RecipeIngredient Kg() {
      return this.aBK;
   }

   public float Kh() {
      return this.aBL;
   }

   public int Ki() {
      return this.aBM;
   }

   public boolean method1589(ItemStack var1) {
      if (this.aBK.aBJ) {
         return CustomItemDefinition.isCustomItem(var1) ? CustomItemDefinition.method8780(this.aBK.getItemStack(), var1) : false;
      }

      try {
         if (CustomItemDefinition.isCustomItem(var1)) {
            return false;
         }

         if ((this.aBK.getItemStack() != null || this.aBK.getItemStack().getType() != Material.AIR) && var1 == null) {
            return false;
         }

         if (this.aBK.getItemStack().getType() != var1.getType()) {
            return false;
         }
      } catch (Exception var3) {
      }

      return true;
   }

   public Material Kj() {
      return this.aBN;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void method1590(Material var1) {
      switch (SwitchMap5.aBO[var1.ordinal()]) {
         case 1:
         case 2:
         case 3:
            this.aBN = var1;
            return;
         default:
            throw new IllegalArgumentException("Invalid furnace type: " + var1);
      }
   }

   public boolean aK(Block var1) {
      return this.aBN.equals(var1.getType());
   }
}
