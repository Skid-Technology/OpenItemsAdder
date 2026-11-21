package dev.lone.itemsadder.item;

import java.util.Random;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemVariantHolder {
   ItemStack itemStack;
   @Nullable
   final CustomItemDefinition aqQ;
   final int aqR;
   final int aqS;
   final float aqT;
   final boolean aqU;
   final boolean aqV;

   public ItemVariantHolder(int var1, int var2, float var3, boolean var4) {
      this.aqR = var1;
      this.aqS = var2;
      this.aqT = var3;
      this.aqU = var4;
      this.aqV = var3 >= 100.0F;
      this.aqQ = null;
   }

   public ItemVariantHolder(@NotNull ItemStack var1, int var2, int var3, float var4, boolean var5) {
      if (var1 instanceof CustomItemDefinition var6) {
         this.aqQ = var6;
         this.itemStack = this.aqQ;
      } else {
         this.aqQ = null;
         this.itemStack = var1;
      }

      this.aqR = var2;
      this.aqS = var3;
      this.aqT = var4;
      this.aqU = var5;
      this.aqV = var4 >= 100.0F;
   }

   public void setItemStack(@NotNull ItemStack var1) {
      this.itemStack = var1;
   }

   @NotNull
   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @NotNull
   public ItemStack buildItem(boolean var1) {
      return this.method10017(null, var1);
   }

   @Nullable
   public CustomItemDefinition getDefinition() {
      return this.aqQ;
   }

   public boolean Fq() {
      return this.aqQ != null;
   }

   public boolean gC() {
      return !this.aqV ? !(Math.random() * 100.0 > this.aqT) : true;
   }

   public ItemStack method10017(@Nullable ItemStack var1, boolean var2) {
      if (var2) {
         ItemStack var3 = this.itemStack.clone();
         var3.setAmount(this.method10019(var1, this.aqR, this.aqS));
         return var3;
      } else {
         return this.itemStack;
      }
   }

   public static int method10018(int var0, int var1, int var2) {
      Random var3 = new Random();
      if (var0 < 1) {
         return var3.nextInt(var2 - var1 + 1) + var1;
      }

      int var4 = var3.nextInt(var0 + 2) - 1;
      if (var4 < 0) {
         var4 = 0;
      }

      return var3.nextInt(var2 - var1 + 2) + var1 * (var4 + 1);
   }

   public int method10019(@Nullable ItemStack var1, int var2, int var3) {
      int var4 = -1;
      if (!this.aqU && var1 != null && var1.getType() != Material.AIR) {
         var4 = ItemStackUtils.method7219(var1, Enchantment.FORTUNE);
      }

      return method10018(var4, var2, var3);
   }

   @Generated
   public int Fr() {
      return this.aqR;
   }

   @Generated
   public int Fs() {
      return this.aqS;
   }

   @Generated
   public float getScale() {
      return this.aqT;
   }

   @Generated
   public boolean Fu() {
      return this.aqV;
   }
}
