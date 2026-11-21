package dev.lone.itemsadder.util;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.ArrayList;
import java.util.Collection;

@Deprecated
public class IntArrayList extends ArrayList {
   public IntArrayList() {
   }

   public IntArrayList(int var1) {
      super(var1);
   }

   public IntArrayList(Collection var1) {
      super(var1);
   }

   @Override
   public int hashCode() {
      int[] var1 = this.toIntArray(false);
      int var2 = 1;

      for (int var6 : var1) {
         var2 = 31 * var2 + var6;
      }

      return 31 * var2 + this.size();
   }

   public int[] toIntArray(boolean var1) {
      Integer[] var2 = this.toArray(new Integer[0]);
      int[] var3 = new int[var2.length];

      for (int var4 = 0; var4 < var2.length; var4++) {
         var3[var4] = var2[var4] == null ? 0 : var2[var4];
      }

      if (var1) {
         IntArrays.quickSort(var3);
      }

      return var3;
   }
}
