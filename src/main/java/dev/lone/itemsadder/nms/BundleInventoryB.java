package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class BundleInventoryB implements IInventory {
   final InventorySubcontainer bdk;
   final List bdl;

   BundleInventoryB(ContainerClickAdapterImplB var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bdm = var2;
      this.bdn = var3;
      this.bdo = var4;
      this.bdk = new InventorySubcontainer(54, this.bdm);
      this.bdl = new BundleInventoryViewB(this);
   }

   public int method4559() {
      return 54;
   }

   public List getContents() {
      return this.bdl;
   }

   public ItemStack method4560(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bdn.applyAsInt(slot);
         if (var2 < 0) {
            return this.bdk.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bdo.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bdo.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4561(int slot, ItemStack item) {
      int var3 = this.bdn.applyAsInt(slot);
      if (var3 < 0) {
         this.bdk.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.f()) {
            this.bdo.remove(var3);
         } else {
            this.bdo.put(var3, item);
         }
      }

      this.method4566();
   }

   public ItemStack method4562(int slot, int amount) {
      ItemStack var3 = this.method4560(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4563(slot);
      }

      this.method4566();
      return var4;
   }

   public ItemStack method4563(int slot) {
      int var2 = this.bdn.applyAsInt(slot);
      if (var2 < 0) {
         return this.bdk.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bdo.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4564() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4560(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4565() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4561(var1, ItemStack.l);
      }
   }

   public void method4566() {
      this.bdk.e();
   }

   public boolean method4567(EntityHuman player) {
      return true;
   }

   public int an_() {
      return this.bdk.an_();
   }

   public void setMaxStackSize(int size) {
      this.bdk.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bdk.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bdk.onClose(player);
   }

   public List getViewers() {
      return this.bdk.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bdm;
   }

   public Location getLocation() {
      return null;
   }
}
