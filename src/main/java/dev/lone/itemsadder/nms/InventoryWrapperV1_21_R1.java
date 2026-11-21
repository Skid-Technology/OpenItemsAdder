package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_21_R1 implements IInventory {
   final InventorySubcontainer bch;
   final List bci;

   InventoryWrapperV1_21_R1(InventoryAdapterV1_21_R1 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcj = var2;
      this.bck = var3;
      this.bcl = var4;
      this.bch = new InventorySubcontainer(54, this.bcj);
      this.bci = new InventoryViewListV1_21_R1(this);
   }

   public int method4487() {
      return 54;
   }

   public List getContents() {
      return this.bci;
   }

   public ItemStack method4488(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bck.applyAsInt(slot);
         if (var2 < 0) {
            return this.bch.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcl.get(var2);
         if (var3 != null && !var3.e()) {
            return var3;
         }

         this.bcl.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4489(int slot, ItemStack item) {
      int var3 = this.bck.applyAsInt(slot);
      if (var3 < 0) {
         this.bch.a(slot, item);
      } else {
         item.f(this.e_(item));
         if (item.e()) {
            this.bcl.remove(var3);
         } else {
            this.bcl.put(var3, item);
         }
      }

      this.method4494();
   }

   public ItemStack method4490(int slot, int amount) {
      ItemStack var3 = this.method4488(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.e()) {
         this.method4491(slot);
      }

      this.method4494();
      return var4;
   }

   public ItemStack method4491(int slot) {
      int var2 = this.bck.applyAsInt(slot);
      if (var2 < 0) {
         return this.bch.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcl.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4492() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4488(var1).e()) {
            return false;
         }
      }

      return true;
   }

   public void method4493() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4489(var1, ItemStack.l);
      }
   }

   public void method4494() {
      this.bch.e();
   }

   public boolean method4495(EntityHuman player) {
      return true;
   }

   public int al_() {
      return this.bch.al_();
   }

   public void setMaxStackSize(int size) {
      this.bch.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bch.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bch.onClose(player);
   }

   public List getViewers() {
      return this.bch.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcj;
   }

   public Location getLocation() {
      return null;
   }
}
