package dev.lone.itemsadder.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackPair {
   public final ItemStack itemStack;
   public final ItemStack aie;
   public final int aif;

   public ItemStackPair(ItemStack var1, @Nullable ItemStack var2, int var3) {
      this.itemStack = var1;
      this.aie = var2;
      this.aif = var3;
   }
}
