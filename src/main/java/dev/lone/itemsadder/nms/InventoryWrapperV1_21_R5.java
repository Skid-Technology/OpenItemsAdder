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

class InventoryWrapperV1_21_R5 implements IInventory {
   final InventorySubcontainer bcW;
   final List bcX;

   InventoryWrapperV1_21_R5(ContainerClickAdapterV1_21_R5 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcY = var2;
      this.bcZ = var3;
      this.bda = var4;
      this.bcW = new InventorySubcontainer(54, this.bcY);
      this.bcX = new InventoryViewListV1_21_R5(this);
   }

   public int method4541() {
      return 54;
   }

   public List getContents() {
      return this.bcX;
   }

   public ItemStack method4542(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bcZ.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcW.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bda.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bda.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4543(int slot, ItemStack item) {
      int var3 = this.bcZ.applyAsInt(slot);
      if (var3 < 0) {
         this.bcW.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.f()) {
            this.bda.remove(var3);
         } else {
            this.bda.put(var3, item);
         }
      }

      this.method4548();
   }

   public ItemStack method4544(int slot, int amount) {
      ItemStack var3 = this.method4542(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4545(slot);
      }

      this.method4548();
      return var4;
   }

   public ItemStack method4545(int slot) {
      int var2 = this.bcZ.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcW.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bda.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4546() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4542(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4547() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4543(var1, ItemStack.l);
      }
   }

   public void method4548() {
      this.bcW.e();
   }

   public boolean method4549(EntityHuman player) {
      return true;
   }

   public int an_() {
      return this.bcW.an_();
   }

   public void setMaxStackSize(int size) {
      this.bcW.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcW.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcW.onClose(player);
   }

   public List getViewers() {
      return this.bcW.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcY;
   }

   public Location getLocation() {
      return null;
   }
}
