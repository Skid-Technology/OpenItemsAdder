package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.api.CustomRecipe;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import lombok.Generated;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCustomRecipe implements CustomRecipe {
   @Nullable
   public YamlConfig vp;
   public String aBm;
   protected NamespacedKey aBn;
   String YK;
   protected ItemStack aBo;
   @Nullable
   protected CustomItemDefinition aBp;
   public int aBq;
   @Nullable
   public Object api;
   private Recipe aBr;

   public AbstractCustomRecipe(@Nullable YamlConfig var1, String var2, String var3, ItemStack var4) {
      this.vp = var1;
      this.aBm = var2;
      this.YK = var3;
      this.aBo = ItemStackUtils.aF(var4);
      if (var4 instanceof CustomItemDefinition var5) {
         this.aBp = var5;
      }

      if (ContentManager.method8084().isCustomItem(var4)) {
         this.aBp = ContentManager.method8084().method8149(var4);
      }
   }

   public AbstractCustomRecipe(ItemStack var1) {
      this.YK = "";
      this.aBo = ItemStackUtils.aF(var1);
   }

   public ItemStack JR() {
      return this.aBo;
   }

   @Nullable
   public CustomItemDefinition JS() {
      return this.aBp;
   }

   @Override
   public ItemStack getResultItemStackCopy() {
      return this.aBo instanceof CustomItemDefinition ? ((CustomItemDefinition)this.aBo).Ds() : ItemStackUtils.aF(this.aBo);
   }

   @Override
   public NamespacedKey getNamespacedKey() {
      return this.aBn;
   }

   public void method1582(NamespacedKey var1) {
      this.aBn = var1;
   }

   public void method1583(Recipe var1) {
      this.aBr = var1;
      RecipeRegistry.method2236(var1);
      this.api = RecipeManagerHolder.Qb().method595(this.aBn);
   }

   public Recipe JT() {
      return this.aBr;
   }

   public abstract boolean JU();

   public boolean JV() {
      return this instanceof CraftingRecipe || this instanceof CookingRecipe;
   }

   public CraftingBookCategory JW() {
      CustomItemDefinition var1 = this.JS();
      if (var1 != null) {
         if (var1 instanceof CustomBlockDefinition) {
            return CraftingBookCategory.BUILDING;
         }

         if (var1.aoh.hasAction("furniture")) {
            return CraftingBookCategory.BUILDING;
         }

         if (var1.aoh.hasAction("hat")) {
            return CraftingBookCategory.EQUIPMENT;
         }

         if (var1.aoh.hasAction("gun")) {
            return CraftingBookCategory.EQUIPMENT;
         }
      }

      if (ItemStackUtils.ap(this.JR()) || ItemStackUtils.ac(this.JR().getType())) {
         return CraftingBookCategory.EQUIPMENT;
      } else {
         return this.JR().getType().isBlock() ? CraftingBookCategory.BUILDING : CraftingBookCategory.MISC;
      }
   }

   public String DJ() {
      return this.vp.DJ();
   }

   @Generated
   @Override
   public String getPermissionSuffix() {
      return this.YK;
   }
}
