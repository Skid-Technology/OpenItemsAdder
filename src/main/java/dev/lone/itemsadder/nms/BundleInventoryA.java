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

class BundleInventoryA implements IInventory {
   final InventorySubcontainer bdd;
   final List bde;

   BundleInventoryA(ContainerClickAdapterImplA var1, InventoryHolder var2, IntUnaryOperator var3, Map var4) {
      this.bdf = var2;
      this.bdg = var3;
      this.bdh = var4;
      this.bdd = new InventorySubcontainer(54, this.bdf);
      this.bde = new BundleInventoryViewA(this);
   }

   public int method4550() {
      return 54;
   }

   public List getContents() {
      return this.bde;
   }

   public ItemStack method4551(int slot) {
      if (slot >= 0 && slot < 54) {
         int var2 = this.bdg.applyAsInt(slot);
         if (var2 < 0) {
            return this.bdd.a(slot);
         }

         ItemStack var3 = (ItemStack)this.bdh.get(var2);
         if (var3 != null && !var3.f()) {
            return var3;
         }

         this.bdh.remove(var2);
         return ItemStack.l;
      } else {
         return ItemStack.l;
      }
   }

   public void method4552(int slot, ItemStack item) {
      int var3 = this.bdg.applyAsInt(slot);
      if (var3 < 0) {
         this.bdd.a(slot, item);
      } else {
         item.f(this.f_(item));
         if (item.f()) {
            this.bdh.remove(var3);
         } else {
            this.bdh.put(var3, item);
         }
      }

      this.method4557();
   }

   public ItemStack method4553(int slot, int amount) {
      ItemStack var3 = this.method4551(slot);
      ItemStack var4 = var3.a(amount);
      if (var3.f()) {
         this.method4554(slot);
      }

      this.method4557();
      return var4;
   }

   public ItemStack method4554(int slot) {
      int var2 = this.bdg.applyAsInt(slot);
      if (var2 < 0) {
         return this.bdd.b(slot);
      }

      ItemStack var3 = (ItemStack)this.bdh.remove(var2);
      return var3 == null ? ItemStack.l : var3;
   }

   public boolean method4555() {
      for (int var1 = 0; var1 < 54; var1++) {
         if (!this.method4551(var1).f()) {
            return false;
         }
      }

      return true;
   }

   public void method4556() {
      for (int var1 = 0; var1 < 54; var1++) {
         this.method4552(var1, ItemStack.l);
      }
   }

   public void method4557() {
      this.bdd.e();
   }

   public boolean method4558(EntityHuman player) {
      return true;
   }

   public int an_() {
      return this.bdd.an_();
   }

   public void setMaxStackSize(int size) {
      this.bdd.setMaxStackSize(size);
   }

   public void onOpen(CraftHumanEntity player) {
      this.bdd.onOpen(player);
   }

   public void onClose(CraftHumanEntity player) {
      this.bdd.onClose(player);
   }

   public List getViewers() {
      return this.bdd.getViewers();
   }

   public InventoryHolder getOwner() {
      return this.bdf;
   }

   public Location getLocation() {
      return null;
   }
}
