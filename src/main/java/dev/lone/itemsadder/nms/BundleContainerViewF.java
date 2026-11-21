package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleContainerViewF extends AbstractList {
   BundleContainerViewF(BundleContainerF var1) {
      this.bdR = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdR.getItem(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdR.getItem(var1);
      this.bdR.setItem(var1, var2);
      return var3;
   }
}
