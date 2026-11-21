package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_20_R4 extends AbstractList {
   InventoryViewListV1_20_R4(InventoryWrapperV1_20_R4 var1) {
      this.bcg = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcg.method4479(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcg.method4479(var1);
      this.bcg.method4480(var1, var2);
      return var3;
   }
}
