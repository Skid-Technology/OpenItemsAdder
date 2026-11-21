package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleContainerViewC extends AbstractList {
   BundleContainerViewC(BundleContainerC var1) {
      this.bdw = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdw.getItem(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdw.getItem(var1);
      this.bdw.setItem(var1, var2);
      return var3;
   }
}
