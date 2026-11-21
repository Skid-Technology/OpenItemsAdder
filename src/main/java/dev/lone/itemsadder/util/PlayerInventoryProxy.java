package dev.lone.itemsadder.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryProxy {
   public PlayerInventory inventory;
   public int size;
   private final Player player;
   HashMap handles = new HashMap();

   public PlayerInventoryProxy(Player var1) {
      this.inventory = var1.getInventory();
      this.size = this.inventory.getSize();
      this.player = var1;

      for (int var2 = 0; var2 < this.inventory.getSize(); var2++) {
         ItemStack var3 = this.inventory.getItem(var2);
         if (var3 != null) {
            this.handles.put(var2, new ItemSlotHandle(var3));
         }
      }
   }

   public Player getPlayer() {
      return this.player;
   }

   public int getSize() {
      return this.size;
   }

   public ItemStack getItem(int var1) {
      return ((ItemSlotHandle)this.handles.get(var1)).item;
   }

   public int getAmount(int var1) {
      return ((ItemSlotHandle)this.handles.get(var1)).amount;
   }

   public void setAmount(int var1, int var2) {
      ((ItemSlotHandle)this.handles.get(var1)).amount = var2;
   }

   public void clearSlot(int var1) {
      ((ItemSlotHandle)this.handles.get(var1)).decrement();
   }

   public void shrinkSlot(int var1, int var2) {
      ((ItemSlotHandle)this.handles.get(var1)).shrinkBy(var2);
   }

   public boolean hasItemAt(int var1) {
      ItemSlotHandle var2 = (ItemSlotHandle)this.handles.get(var1);
      return var2 == null ? true : var2.empty;
   }

   @Deprecated
   public ItemStack getItemWithAmount(int var1, int var2) {
      ItemStack var3 = this.getItem(var1);
      var3.setAmount(var2);
      return var3;
   }

   public void applyChanges() {
      this.handles.forEach((var0, var1) -> var1.applyToItem());
   }

   public void setCraftingMatrix(CraftingInventory var1, ItemStack[] var2) {
      ItemStack[] var3 = Arrays.stream(this.inventory.getContents()).map(var0 -> var0 == null ? null : var0.clone()).toArray(ItemStack[]::new);
      ItemStack[] var4 = var1.getMatrix();
      ItemStack var5 = var1.getResult();
      ItemStack var6 = var5 == null ? null : var5.clone();

      try {
         this.applyChanges();

         for (int var7 = 0; var7 < var2.length; var7++) {
            if (!Objects.equals(var4[var7], var2[var7])) {
               var1.setItem(var7 + 1, var2[var7]);
            }
         }
      } catch (RuntimeException | Error var8) {
         this.inventory.setContents(var3);
         var1.setMatrix(var4);
         var1.setResult(var6);
         throw var8;
      }
   }

   @Nullable
   public ItemSlotHandle getSlotHandle(int var1) {
      return (ItemSlotHandle)this.handles.get(var1);
   }
}
