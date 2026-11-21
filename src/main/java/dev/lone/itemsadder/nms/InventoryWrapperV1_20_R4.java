package dev.lone.itemsadder.nms;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftHumanEntity;
import org.bukkit.inventory.InventoryHolder;

class InventoryWrapperV1_20_R4 implements IInventory {
   final InventorySubcontainer bcb;
   final List bcc;

   InventoryWrapperV1_20_R4(InventoryAdapterV1_20_R4 var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bcd = var2;
      this.bce = var3;
      this.bcf = var4;
      this.bcb = new InventorySubcontainer(54, this.bcd);
      this.bcc = new InventoryViewListV1_20_R4(this);
   }

   public int method4478() {
      return 54;
   }

   public List getContents() {
      return this.bcc;
   }

   public ItemStack method4479(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bce.applyAsInt(slot);
         if (var2 < 0) {
            return this.bcb.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bcf.get(var2);
         if (var3 != null && !var3.e()) {
            return var3;
         }

         this.bcf.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4480(int slot, ItemStack item) {
      int var3 = this.bce.applyAsInt(slot);
      if (var3 < 0) {
         this.bcb.a(slot, item);
      } else {
         item.f(this.e_(item));
         if (item.e()) {
            this.bcf.remove(var3);
         } else {
            this.bcf.put(var3, item);
         }
      }

      this.method4485();
   }

   public ItemStack method4481(int slot, int amount) {
      ItemStack var3 = this.method4479(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.e()) {
         this.method4482(slot);
      }

      this.method4485();
      return var4;
   }

   public ItemStack method4482(int slot) {
      int var2 = this.bce.applyAsInt(slot);
      if (var2 < 0) {
         return this.bcb.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bcf.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4483() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4479(var1).e()) {
            return false;
         }
      }

      return true;
   }

   public void method4484() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4480(var1, ItemStack.l);
      }
   }

   public void method4485() {
      this.bcb.e();
   }

   public boolean method4486(EntityHuman player) {
      return true;
   }

   public int ah_() {
      return this.bcb.ah_();
   }

   public void setMaxStackSize(int size) {
      this.bcb.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bcb.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bcb.onClose(player);
   }

   public List getViewers() {
      return this.bcb.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bcd;
   }

   public Location getLocation() {
      return null;
   }
}
