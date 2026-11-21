package dev.lone.itemsadder.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackPairRecord {
   private final ItemStack kl;
   @Nullable
   private final ItemStack km;
   @Nullable
   private final Float kn;
   @Nullable
   private final Float ko;
   @Nullable
   private final Float kp;

   public ItemStackPairRecord(ItemStack var1, @Nullable ItemStack var2, @Nullable Float var3, @Nullable Float var4, @Nullable Float var5) {
      this.kl = var1;
      this.km = var2;
      this.kn = var3;
      this.ko = var4;
      this.kp = var5;
   }

   public ItemStack dp() {
      return this.kl;
   }

   @Nullable
   public ItemStack dq() {
      return this.km;
   }

   @Nullable
   public Float dt() {
      return this.kn;
   }

   @Nullable
   public Float du() {
      return this.ko;
   }

   @Nullable
   public Float dv() {
      return this.kp;
   }
}
