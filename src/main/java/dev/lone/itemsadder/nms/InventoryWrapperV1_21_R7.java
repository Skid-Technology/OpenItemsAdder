package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R7 implements IInventory {
   final InventorySubcontainer bcB;
   final List bcC;

   InventoryWrapperV1_21_R7(ContainerClickAdapterV1_21_R7 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcD = var2;
      this.bcE = var3;
      this.bcF = var4;
      this.bcB = new InventorySubcontainer(54, this.bcD);
      this.bcC = new InventoryViewListV1_21_R7(this);
   }

   public int method4514() {
      return 54;
   }

   public List getContents() {
      return this.bcC;
   }

   public ItemStack method4515(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcE.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcB.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcF.get(var2);
         if (var3 != null && !var3.isEmpty()) {
            return var3;
         }

         this.bcF.remove(var2);
         return ItemStack.EMPTY;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void method4516(int slot, ItemStack item) {
      int var3 = this.bcE.applyAsInt(slot);
      if (var3 < 0) {
         this.bcB.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.isEmpty()) {
            this.bcF.remove(var3);
         } else {
            this.bcF.put(var3, item);
         }
      }

      this.method4521();
   }

   public ItemStack method4517(int slot, int amount) {
      ItemStack var3 = this.method4515(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.isEmpty()) {
         this.method4518(slot);
      }

      this.method4521();
      return var4;
   }

   public ItemStack method4518(int slot) {
      int var2 = this.bcE.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcB.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcF.remove(var2);
      return var3 == null ? ItemStack.EMPTY : var3;
   }

   public boolean method4519() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4515(var1).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void method4520() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4516(var1, ItemStack.EMPTY);
      }
   }

   public void method4521() {
      this.bcB.e();
   }

   public boolean method4522(EntityHuman player) {
      return true;
   }

   public int ap_() {
      return this.bcB.ap_();
   }

   public void setMaxStackSize(int size) {
      this.bcB.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcB.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcB.onClose(player);
   }

   public List getViewers() {
      return this.bcB.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcD;
   }

   public Location getLocation() {
      return null;
   }
}
