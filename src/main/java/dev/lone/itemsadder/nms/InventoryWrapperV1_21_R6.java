package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R6 implements IInventory {
   final InventorySubcontainer bcu;
   final List bcv;

   InventoryWrapperV1_21_R6(ContainerClickAdapterV1_21_R6 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcw = var2;
      this.bcx = var3;
      this.bcy = var4;
      this.bcu = new InventorySubcontainer(54, this.bcw);
      this.bcv = new InventoryViewListV1_21_R6(this);
   }

   public int method4505() {
      return 54;
   }

   public List getContents() {
      return this.bcv;
   }

   public ItemStack method4506(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcx.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcu.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcy.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bcy.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4507(int slot, ItemStack item) {
      int var3 = this.bcx.applyAsInt(slot);
      if (var3 < 0) {
         this.bcu.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.f()) {
            this.bcy.remove(var3);
         } else {
            this.bcy.put(var3, item);
         }
      }

      this.method4512();
   }

   public ItemStack method4508(int slot, int amount) {
      ItemStack var3 = this.method4506(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4509(slot);
      }

      this.method4512();
      return var4;
   }

   public ItemStack method4509(int slot) {
      int var2 = this.bcx.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcu.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcy.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4510() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4506(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4511() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4507(var1, ItemStack.l);
      }
   }

   public void method4512() {
      this.bcu.e();
   }

   public boolean method4513(EntityHuman player) {
      return true;
   }

   public int ap_() {
      return this.bcu.ap_();
   }

   public void setMaxStackSize(int size) {
      this.bcu.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcu.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcu.onClose(player);
   }

   public List getViewers() {
      return this.bcu.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcw;
   }

   public Location getLocation() {
      return null;
   }
}
