package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.config.YamlConfig;
import org.bukkit.inventory.ItemStack;

public class AnvilRecipe extends AbstractCustomRecipe {
   public ItemStack aBE;
   public ItemStack aBF;

   public AnvilRecipe(YamlConfig var1, String var2, String var3, ItemStack var4, ItemStack var5) {
      super(var1, var2, var3, var4);
      this.aBE = var4;
      this.aBF = var5;
      this.aBq = 2;
   }

   public ItemStack Kd() {
      return this.aBE;
   }

   public ItemStack Ke() {
      return this.aBF;
   }

   public boolean method1584(ItemStack var1) {
      return this.aBF.isSimilar(var1);
   }

   @Override
   public boolean JU() {
      return this.aBE != null && this.aBF != null;
   }
}
