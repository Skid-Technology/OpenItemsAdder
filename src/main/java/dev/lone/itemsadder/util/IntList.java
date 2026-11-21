package dev.lone.itemsadder.util;

import java.util.Collection;

@Deprecated
public class IntList extends IntArrayList {
   public IntList() {
   }

   public IntList(int var1) {
      super(var1);
   }

   public IntList(Collection var1) {
      super(var1);
   }

   @Override
   public int hashCode() {
      int[] var1 = this.toIntArray(true);
      int var2 = 1;

      for (int var6 : var1) {
         var2 = 31 * var2 + var6;
      }

      return 31 * var2 + this.size();
   }
}
