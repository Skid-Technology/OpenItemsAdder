package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R4 implements IInventory {
   final InventorySubcontainer bcP;
   final List bcQ;

   InventoryWrapperV1_21_R4(ContainerClickAdapterV1_21_R4 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcR = var2;
      this.bcS = var3;
      this.bcT = var4;
      this.bcP = new InventorySubcontainer(54, this.bcR);
      this.bcQ = new InventoryViewListV1_21_R4(this);
   }

   public int method4532() {
      return 54;
   }

   public List getContents() {
      return this.bcQ;
   }

   public ItemStack method4533(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcS.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcP.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcT.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bcT.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4534(int slot, ItemStack item) {
      int var3 = this.bcS.applyAsInt(slot);
      if (var3 < 0) {
         this.bcP.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.f()) {
            this.bcT.remove(var3);
         } else {
            this.bcT.put(var3, item);
         }
      }

      this.method4539();
   }

   public ItemStack method4535(int slot, int amount) {
      ItemStack var3 = this.method4533(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4536(slot);
      }

      this.method4539();
      return var4;
   }

   public ItemStack method4536(int slot) {
      int var2 = this.bcS.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcP.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcT.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4537() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4533(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4538() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4534(var1, ItemStack.l);
      }
   }

   public void method4539() {
      this.bcP.e();
   }

   public boolean method4540(EntityHuman player) {
      return true;
   }

   public int ak_() {
      return this.bcP.ak_();
   }

   public void setMaxStackSize(int size) {
      this.bcP.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcP.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcP.onClose(player);
   }

   public List getViewers() {
      return this.bcP.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcR;
   }

   public Location getLocation() {
      return null;
   }
}
