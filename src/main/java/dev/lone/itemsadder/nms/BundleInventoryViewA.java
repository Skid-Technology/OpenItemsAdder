package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleInventoryViewA extends AbstractList {
   BundleInventoryViewA(BundleInventoryA var1) {
      this.bdi = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdi.method4551(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdi.method4551(var1);
      this.bdi.method4552(var1, var2);
      return var3;
   }
}
