package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EnvironmentDetector;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SmithingRecipe extends AbstractCustomRecipe {
   public final SmithingIngredient aCw;
   public final SmithingIngredient aCx;
   public final SmithingIngredient aCy;
   public final SmithingCopyOptions aCz;

   public SmithingRecipe(
      YamlConfig var1,
      String var2,
      String var3,
      ItemStack var4,
      SmithingIngredient var5,
      SmithingIngredient var6,
      SmithingIngredient var7,
      SmithingCopyOptions var8
   ) {
      super(var1, var2, var3, var4);
      this.aCw = var5;
      this.aCx = var6;
      this.aCy = var7;
      this.aCz = var8;
      this.aBq = 1;
   }

   @Override
   public boolean JU() {
      return this.aCx != null && this.aCx.aCu.getType() != Material.AIR && this.aCy != null && this.aCy.aCu.getType() != Material.AIR;
   }

   public ItemStack method1585(ItemStack var1) {
      ItemStack var2 = ItemStackUtils.aF(this.aBo);
      if (this.aCz.aCG) {
         ItemOperationsService.getRegistryBridge().method3430(var1, var2);
      }

      if (EnvironmentDetector.paperAdventure) {
         ItemMeta var3 = var2.getItemMeta();
         ItemMeta var4 = var1.getItemMeta();
         if (this.aCz.aCA && var4.hasDisplayName()) {
            var3.displayName(var4.displayName());
         }

         if (this.aCz.aCB) {
            List var5 = var4.lore();
            if (var5 != null) {
               List var6 = var3.lore();
               if (var6 != null) {
                  var5.addAll(var6);
               }

               var3.lore(var5);
            }
         }

         var2.setItemMeta(var3);
      }

      if (this.aCz.aCC) {
         ItemStackUtils.method7256(var1, var2);
      }

      if (this.aCz.aCD && ItemStackUtils.aG(var1)) {
         int var7 = ItemStackUtils.aw(var1);
         int var8 = ItemStackUtils.method7233(var2);
         int var9 = (int)Math.ceil((double)var7 / ItemStackUtils.method7233(var1) * var8);
         ItemStackUtils.method7235(var2, var9);
      }

      if (this.aCz.aCF) {
         ItemStackUtils.method7257(var1, var2);
      }

      if (this.aCz.aCE) {
         var2.setType(var1.getType());
      }

      return var2;
   }

   public boolean method1586(ItemStack var1, ItemStack var2, ItemStack var3) {
      return this.method1587(this.aCw, var1) && this.method1587(this.aCx, var2) && this.method1587(this.aCy, var3);
   }

   private boolean method1587(SmithingIngredient var1, ItemStack var2) {
      if ((var2 == null || var2.getType() == Material.AIR) && var1.getItem() != null && var1.getItem().getType() != Material.AIR) {
         return false;
      } else {
         boolean var3 = CustomItemDefinition.isCustomItem(var2);
         if (var1.Kw()) {
            return var3 ? var1.dW(CustomItemDefinition.method8750(var2)) : false;
         } else {
            return var3 ? false : var1.getItem().getType() == var2.getType();
         }
      }
   }
}
