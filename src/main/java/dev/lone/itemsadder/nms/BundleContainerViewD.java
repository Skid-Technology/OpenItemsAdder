package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleContainerViewD extends AbstractList {
   BundleContainerViewD(BundleContainerD var1) {
      this.bdD = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdD.getItem(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdD.getItem(var1);
      this.bdD.setItem(var1, var2);
      return var3;
   }
}
