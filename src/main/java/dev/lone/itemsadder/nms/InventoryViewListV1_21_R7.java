package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R7 extends AbstractList {
   InventoryViewListV1_21_R7(InventoryWrapperV1_21_R7 var1) {
      this.bcG = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcG.method4515(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcG.method4515(var1);
      this.bcG.method4516(var1, var2);
      return var3;
   }
}
