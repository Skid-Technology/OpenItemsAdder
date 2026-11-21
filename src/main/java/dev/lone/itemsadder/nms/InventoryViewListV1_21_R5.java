package dev.lone.itemsadder.nms;

import java.util.AbstractList;
import net.minecraft.world.item.ItemStack;

class InventoryViewListV1_21_R5 extends AbstractList {
   InventoryViewListV1_21_R5(InventoryWrapperV1_21_R5 var1) {
      this.bdb = var1;
   }

   @Override
   public int size() {
      return 54;
   }

   public ItemStack cD(int var1) {
      return this.bdb.method4542(var1);
   }

   public ItemStack method332(int var1, ItemStack var2) {
      ItemStack var3 = this.bdb.method4542(var1);
      this.bdb.method4543(var1, var2);
      return var3;
   }
}
