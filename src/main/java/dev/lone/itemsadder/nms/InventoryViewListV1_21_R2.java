package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R2 extends AbstractList {
   InventoryViewListV1_21_R2(InventoryWrapperV1_21_R2 var1) {
      this.bcs = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcs.method4497(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcs.method4497(var1);
      this.bcs.method4498(var1, var2);
      return var3;
   }
}
