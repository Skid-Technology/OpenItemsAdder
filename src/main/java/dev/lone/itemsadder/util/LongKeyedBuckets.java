package dev.lone.itemsadder.util;

import java.util.Arrays;

public class LongKeyedBuckets {
   private final long[] blockKeys;
   private final byte[] stages;
   private static final LongKeyedBuckets EMPTY = new LongKeyedBuckets(new long[0], new byte[0]);

   private LongKeyedBuckets(long[] var1, byte[] var2) {
      this.blockKeys = var1;
      this.stages = var2;
   }

   private int stageFor(long var1) {
      int var3 = Arrays.binarySearch(this.blockKeys, var1);
      return var3 < 0 ? -1 : this.stages[var3];
   }
}
