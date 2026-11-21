package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.jetbrains.annotations.Nullable;

public class ShapedIngredient {
   final Character aBT;
   final RecipeChoice aBU;
   final ItemStack aBV;
   public final int aBW;
   Integer aBX = null;

   public ShapedIngredient(Character var1, RecipeOutput var2) {
      this(var1, var2, 1);
   }

   public ShapedIngredient(Character var1, RecipeOutput var2, @Deprecated int var3) {
      this.aBT = Character.toLowerCase(var1);
      this.aBV = var2.Kv();
      this.aBV.setAmount(var3);
      switch (var2) {
         case MaterialOutput var6:
            this.aBU = new MaterialChoice(var6.aCs);
            this.aBW = method2273(this.aBV.getType());
            return;
         case ItemStackOutput var7:
            this.aBU = new ExactChoice(this.aBV);
            this.aBW = method2273(this.aBV.getType());
            return;
         case CustomItemOutput var8:
            this.aBU = new ExactChoice(this.aBV);
            this.aBX = var8.Fp().aol;
            this.aBW = this.aBX;
            return;
         default:
            throw new IllegalStateException("Unexpected ingredient type.");
      }
   }

   public static int method2273(@Nullable Material var0) {
      return var0 != null && !var0.isAir() ? InstrumentRegistry.getAdapter().method2798(var0) : -666;
   }

   public static Integer method2274(@Nullable ItemStack var0) {
      if (var0 == null) {
         return method2273(null);
      } else {
         return CustomItemDefinition.isCustomItem(var0) ? CustomItemDefinition.method8751(var0) : method2273(var0.getType());
      }
   }

   public static int method2275(@Nullable ShapedIngredient var0) {
      return var0 == null ? method2273(null) : var0.aBW;
   }

   public RecipeChoice Kf() {
      return this.aBU;
   }

   public Character Kk() {
      return this.aBT;
   }

   public ItemStack getItem() {
      return this.aBV;
   }

   @Nullable
   public Integer Kl() {
      return this.aBX;
   }

   public boolean Eb() {
      return this.aBX != null;
   }

   public boolean method2276(ItemStack var1) {
      return this.Eb() ? CustomItemDefinition.method8784(this.aBW, var1) : this.aBV.getType() == var1.getType() && !CustomItemDefinition.isCustomItem(var1);
   }

   public boolean Km() {
      return this.aBV == null || this.aBV.getType() == Material.AIR;
   }
}
