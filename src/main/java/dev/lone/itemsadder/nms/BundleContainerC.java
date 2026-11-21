package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class BundleContainerC implements Container {
   final SimpleContainer bdr;
   final List bds;

   BundleContainerC(ContainerClickAdapterImplC var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bdt = var2;
      this.bdu = var3;
      this.bdv = var4;
      this.bdr = new SimpleContainer(54, this.bdt);
      this.bds = new BundleContainerViewC(this);
   }

   public int getContainerSize() {
      return 54;
   }

   public List getContents() {
      return this.bds;
   }

   public ItemStack getItem(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bdu.applyAsInt(slot);
         if (var2 < 0) {
            return this.bdr.getItem(slot);
         }

         ItemStack var3 = (ItemStack)this.bdv.get(var2);
         if (var3 != null && !var3.isEmpty()) {
            return var3;
         }

         this.bdv.remove(var2);
         return ItemStack.EMPTY;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void setItem(int slot, ItemStack item) {
      int var3 = this.bdu.applyAsInt(slot);
      if (var3 < 0) {
         this.bdr.setItem(slot, item);
      } else {
         item.limitSize(this.getMaxStackSize(item));
         if (item.isEmpty()) {
            this.bdv.remove(var3);
         } else {
            this.bdv.put(var3, item);
         }
      }

      this.setChanged();
   }

   public ItemStack removeItem(int slot, int amount) {
      ItemStack var3 = this.getItem(slot);
      ItemStack var4 = var3.split(amount);
      if (var3.isEmpty()) {
         this.removeItemNoUpdate(slot);
      }

      this.setChanged();
      return var4;
   }

   public ItemStack removeItemNoUpdate(int slot) {
      int var2 = this.bdu.applyAsInt(slot);
      if (var2 < 0) {
         return this.bdr.removeItemNoUpdate(slot);
      }

      ItemStack var3 = (ItemStack)this.bdv.remove(var2);
      return var3 == null ? ItemStack.EMPTY : var3;
   }

   public boolean isEmpty() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.getItem(var1).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void clearContent() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.setItem(var1, ItemStack.EMPTY);
      }
   }

   public void setChanged() {
      this.bdr.setChanged();
   }

   public boolean stillValid(Player player) {
      return true;
   }

   public int getMaxStackSize() {
      return this.bdr.getMaxStackSize();
   }

   public void setMaxStackSize(int size) {
      this.bdr.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bdr.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bdr.onClose(player);
   }

   public List getViewers() {
      return this.bdr.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bdt;
   }

   public Location getLocation() {
      return null;
   }
}
