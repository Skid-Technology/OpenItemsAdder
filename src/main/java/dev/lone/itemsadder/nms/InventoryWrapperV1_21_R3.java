package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R3 implements IInventory {
   final InventorySubcontainer bcI;
   final List bcJ;

   InventoryWrapperV1_21_R3(ContainerClickAdapterV1_21_R3 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcK = var2;
      this.bcL = var3;
      this.bcM = var4;
      this.bcI = new InventorySubcontainer(54, this.bcK);
      this.bcJ = new InventoryViewListV1_21_R3(this);
   }

   public int method4523() {
      return 54;
   }

   public List getContents() {
      return this.bcJ;
   }

   public ItemStack method4524(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcL.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcI.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcM.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bcM.remove(var2);
         return ItemStack.j;
      } else {
         return ItemStack.j;
      }
   }

   public void method4525(int slot, ItemStack item) {
      int var3 = this.bcL.applyAsInt(slot);
      if (var3 < 0) {
         this.bcI.a(slot, item);
      } else {
         item.f(this.e_(item));
         if (item.f()) {
            this.bcM.remove(var3);
         } else {
            this.bcM.put(var3, item);
         }
      }

      this.method4530();
   }

   public ItemStack method4526(int slot, int amount) {
      ItemStack var3 = this.method4524(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4527(slot);
      }

      this.method4530();
      return var4;
   }

   public ItemStack method4527(int slot) {
      int var2 = this.bcL.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcI.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcM.remove(var2);
      return var3 == null ? ItemStack.j : var3;
   }

   public boolean method4528() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4524(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4529() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4525(var1, ItemStack.j);
      }
   }

   public void method4530() {
      this.bcI.e();
   }

   public boolean method4531(EntityHuman player) {
      return true;
   }

   public int an_() {
      return this.bcI.an_();
   }

   public void setMaxStackSize(int size) {
      this.bcI.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcI.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcI.onClose(player);
   }

   public List getViewers() {
      return this.bcI.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcK;
   }

   public Location getLocation() {
      return null;
   }
}
