package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleInventoryViewB extends AbstractList {
   BundleInventoryViewB(BundleInventoryB var1) {
      this.bdp = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdp.method4560(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdp.method4560(var1);
      this.bdp.method4561(var1, var2);
      return var3;
   }
}
