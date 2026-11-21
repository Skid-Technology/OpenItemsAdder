package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R2 implements IInventory {
   final InventorySubcontainer bcn;
   final List bco;

   InventoryWrapperV1_21_R2(InventoryAdapterV1_21_R2 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcp = var2;
      this.bcq = var3;
      this.bcr = var4;
      this.bcn = new InventorySubcontainer(54, this.bcp);
      this.bco = new InventoryViewListV1_21_R2(this);
   }

   public int method4496() {
      return 54;
   }

   public List getContents() {
      return this.bco;
   }

   public ItemStack method4497(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcq.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcn.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcr.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bcr.remove(var2);
         return ItemStack.j;
      } else {
         return ItemStack.j;
      }
   }

   public void method4498(int slot, ItemStack item) {
      int var3 = this.bcq.applyAsInt(slot);
      if (var3 < 0) {
         this.bcn.a(slot, item);
      } else {
         item.f(this.e_(item));
         if (item.f()) {
            this.bcr.remove(var3);
         } else {
            this.bcr.put(var3, item);
         }
      }

      this.method4503();
   }

   public ItemStack method4499(int slot, int amount) {
      ItemStack var3 = this.method4497(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4500(slot);
      }

      this.method4503();
      return var4;
   }

   public ItemStack method4500(int slot) {
      int var2 = this.bcq.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcn.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcr.remove(var2);
      return var3 == null ? ItemStack.j : var3;
   }

   public boolean method4501() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4497(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4502() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4498(var1, ItemStack.j);
      }
   }

   public void method4503() {
      this.bcn.e();
   }

   public boolean method4504(EntityHuman player) {
      return true;
   }

   public int ao_() {
      return this.bcn.ao_();
   }

   public void setMaxStackSize(int size) {
      this.bcn.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcn.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcn.onClose(player);
   }

   public List getViewers() {
      return this.bcn.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcp;
   }

   public Location getLocation() {
      return null;
   }
}
