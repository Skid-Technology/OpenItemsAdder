package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class BundleContainerViewE extends AbstractList {
   BundleContainerViewE(BundleContainerE var1) {
      this.bdK = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdK.getItem(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdK.getItem(var1);
      this.bdK.setItem(var1, var2);
      return var3;
   }
}
