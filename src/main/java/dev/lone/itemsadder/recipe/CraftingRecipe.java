package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.effect.SoundCategoryEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;

public class CraftingRecipe extends AbstractCustomRecipe {
   List aBY;
   private String @Nullable [] aBZ;
   public String @Nullable [] aCa;
   public boolean aCb;
   HashMap aCc;
   HashMap aCd;
   HashMap aCe;
   SoundCategoryEffect aCf;
   public boolean aCg;

   public CraftingRecipe(
      YamlConfig var1, String var2, String var3, HashMap var4, HashMap var5, HashMap var6, SoundCategoryEffect var7, ItemStack var8, String[] var9, List var10
   ) {
      super(var1, var2, var3, var8);
      this.method1591(var4, var5, var6, var7, var9, var10);
   }

   public CraftingRecipe(
      YamlConfig var1, String var2, String var3, HashMap var4, HashMap var5, HashMap var6, SoundCategoryEffect var7, ItemStack var8, List var9
   ) {
      super(var1, var2, var3, var8);
      this.method1591(var4, var5, var6, var7, null, var9);
   }

   public void method1591(HashMap var1, HashMap var2, HashMap var3, SoundCategoryEffect var4, @Nullable String[] var5, List var6) {
      this.aCd = new HashMap();
      this.aCe = new HashMap();
      this.aCc = var1;
      this.aCf = var4;
      this.aBY = var6;
      if (var5 != null) {
         method1596(var5);
         this.aBZ = var5;
         String[] var7 = ShapeUtil.method2279(var5);
         if (var7 != null) {
            this.aCb = true;
            this.aBZ = var7;
            this.aCa = this.aBZ;
         }

         this.aCa = ShapeUtil.method2277(var5);
      } else {
         this.aCg = true;
         this.aBZ = null;
         this.aCb = false;
         this.aBq = var6.size();
      }

      var2.forEach(
         (var1x, var2x) -> this.aCd
            .put(var1x, new RecipeResult(var2x, CustomItemDefinition.isCustomItem(var1x), ContentManager.method8084().method8149(var1x)))
      );
      var3.forEach(
         (var1x, var2x) -> this.aCe
            .put(var1x, new RecipeResult(var2x, CustomItemDefinition.isCustomItem(var1x), ContentManager.method8084().method8149(var1x)))
      );
   }

   public CraftingRecipe(ItemStack var1) {
      super(var1);
      this.aBZ = new String[]{"xxx", "xxx", "xxx"};
      this.aBY = new ArrayList();
   }

   public List Kn() {
      return this.aBY;
   }

   public ShapedIngredient method1592(Character var1) {
      for (ShapedIngredient var3 : this.aBY) {
         if (var3.Kk().compareTo(var1) == 0) {
            return var3;
         }
      }

      return null;
   }

   public ItemStack method1593(Character var1) {
      ShapedIngredient var2 = this.method1592(var1);
      return var2 != null ? var2.getItem() : new ItemStack(Material.AIR);
   }

   public String @Nullable [] Ko() {
      return this.aBZ;
   }

   public HashMap Kp() {
      return this.aCc;
   }

   public HashMap Kq() {
      return this.aCd;
   }

   public HashMap Kr() {
      return this.aCe;
   }

   public SoundCategoryEffect Ks() {
      return this.aCf;
   }

   @Override
   public void method1582(NamespacedKey var1) {
      this.aBn = var1;
   }

   @Override
   public boolean JU() {
      return !this.aBY.isEmpty();
   }

   public void method1594(ShapedRecipe var1) {
      if (this.JS() != null) {
         var1.setGroup(this.JS().aok);
      } else {
         var1.setGroup(this.JR().getType().toString());
      }
   }

   public void method1595(ShapelessRecipe var1) {
      if (this.JS() != null) {
         var1.setGroup(this.JS().aok);
      } else {
         var1.setGroup(this.JR().getType().toString());
      }
   }

   public static void method1596(String[] var0) {
      Arrays.setAll(var0, var1 -> var0[var1].toLowerCase());
   }

   public boolean method1597(CraftingInventory var1) {
      if (!this.aCg) {
         return RecipeManagerHolder.Qb().method604((ShapedRecipe)this.JT(), var1);
      }

      ArrayList var2 = new ArrayList();

      for (ItemStack var6 : var1.getMatrix()) {
         if (var6 != null && !var6.getType().isAir()) {
            var2.add(var6);
         }
      }

      if (var2.size() != this.aBY.size()) {
         return false;
      }

      for (ShapedIngredient var8 : this.aBY) {
         if (var8 != null && var8.Kk() != ' ' && var8.getItem() != null && var8.getItem().getType() != Material.AIR) {
            boolean var9 = false;

            for (int var10 = 0; var10 < var2.size(); var10++) {
               if (var8.method2276((ItemStack)var2.get(var10))) {
                  var2.remove(var10);
                  var9 = true;
                  break;
               }
            }

            if (!var9) {
               return false;
            }
         }
      }

      return var2.isEmpty();
   }

   public Map Kt() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 9; var2++) {
         var1.put(var2, null);
      }

      if (this.aBZ != null) {
         for (int var7 = 0; var7 < this.aBZ.length; var7++) {
            String var3 = this.aBZ[var7];

            for (int var4 = 0; var4 < var3.length(); var4++) {
               char var5 = var3.charAt(var4);
               ShapedIngredient var6 = this.method1592(var5);
               if (var6 != null) {
                  var1.put(var7 * 3 + var4, var6);
               }
            }
         }
      } else {
         for (int var8 = 0; var8 < this.aBY.size(); var8++) {
            var1.put(var8, (ShapedIngredient)this.aBY.get(var8));
         }
      }

      return var1;
   }
}
