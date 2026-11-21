package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R1 extends AbstractList {
   InventoryViewListV1_21_R1(InventoryWrapperV1_21_R1 var1) {
      this.bcm = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcm.method4488(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcm.method4488(var1);
      this.bcm.method4489(var1, var2);
      return var3;
   }
}
