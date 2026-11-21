package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R6 extends AbstractList {
   InventoryViewListV1_21_R6(InventoryWrapperV1_21_R6 var1) {
      this.bcz = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcz.method4506(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcz.method4506(var1);
      this.bcz.method4507(var1, var2);
      return var3;
   }
}
