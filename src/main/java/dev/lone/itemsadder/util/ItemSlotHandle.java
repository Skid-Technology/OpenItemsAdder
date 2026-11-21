package dev.lone.itemsadder.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemSlotHandle {
   public final ItemStack item;
   public boolean empty;
   private int amount;

   public ItemSlotHandle(ItemStack var1) {
      this.item = var1;
      if (var1 == null) {
         this.empty = true;
      } else {
         this.empty = var1.getType() == Material.AIR;
         this.amount = var1.getAmount();
      }
   }

   public int getAmount() {
      return this.amount;
   }

   public Material getMaterial() {
      return this.item.getType();
   }

   public void setAmount(int var1) {
      if (var1 <= 0) {
         this.empty = true;
         this.amount = 0;
      } else {
         this.empty = false;
         this.amount = var1;
      }
   }

   public void decrement() {
      this.setAmount(this.amount - 1);
   }

   public void shrinkBy(int var1) {
      this.setAmount(this.amount - var1);
   }

   public void applyToItem() {
      this.item.setAmount(this.amount);
      if (this.empty) {
         this.item.setType(Material.AIR);
      }
   }

   public ItemStack cloneWithAmount(int var1) {
      ItemStack var2 = this.item.clone();
      var2.setAmount(var1);
      return var2;
   }

   public ItemStack takeAmount(int var1) {
      ItemStack var2 = this.item.clone();
      var2.setAmount(var1);
      this.shrinkBy(var1);
      return var2;
   }
}
