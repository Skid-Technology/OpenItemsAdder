package dev.lone.itemsadder.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class IntArrayOps {
   private IntArrayOps() {
   }

   static int findFirstFreeIndex(Collection var0, int var1, int var2, int... var3) {
      if (var1 > var2) {
         return -1;
      }

      HashSet var4 = new HashSet();

      for (Object var6 : var0) {
         if (var6 instanceof Number var7) {
            var4.add(var7.intValue());
         }
      }

      int var11 = var1;

      while (true) {
         boolean var12 = var4.contains(var11);

         for (int var10 : var3) {
            var12 |= var11 == var10;
         }

         if (!var12) {
            return var11;
         }

         if (var11 == var2) {
            return -1;
         }

         var11++;
      }
   }

   public static void selfTest(String[] var0) {
      assert findFirstFreeIndex(Set.of(1, 2, 4), 1, 5) == 3;
      assert findFirstFreeIndex(Set.of(1, 3), 1, 3, 2) == -1;
      assert findFirstFreeIndex(Set.of(Integer.MAX_VALUE), Integer.MAX_VALUE, Integer.MAX_VALUE) == -1;
      assert findFirstFreeIndex(Set.of(), 2, 1) == -1;
   }
}
