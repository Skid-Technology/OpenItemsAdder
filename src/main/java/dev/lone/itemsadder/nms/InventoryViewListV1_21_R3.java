package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R3 extends AbstractList {
   InventoryViewListV1_21_R3(InventoryWrapperV1_21_R3 var1) {
      this.bcN = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bcN.method4524(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bcN.method4524(var1);
      this.bcN.method4525(var1, var2);
      return var3;
   }
}
