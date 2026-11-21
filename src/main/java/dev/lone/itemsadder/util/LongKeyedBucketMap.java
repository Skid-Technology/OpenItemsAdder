package dev.lone.itemsadder.util;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.Arrays;

public final class LongKeyedBucketMap {
   private final Long2ObjectOpenHashMap stagesByKey = new Long2ObjectOpenHashMap();
   private volatile LongKeyedBuckets snapshot = LongKeyedBuckets.EMPTY;

   synchronized boolean updateStage(long var1, int var3, int var4) {
      Int2IntOpenHashMap var5 = (Int2IntOpenHashMap)this.stagesByKey.get(var1);
      int var6 = maxStage(var5);
      if (var4 < 0) {
         if (var5 == null) {
            return false;
         }

         var5.remove(var3);
         if (var5.isEmpty()) {
            this.stagesByKey.remove(var1);
         }
      } else {
         if (var5 == null) {
            var5 = new Int2IntOpenHashMap();
            this.stagesByKey.put(var1, var5);
         }

         var5.put(var3, Math.min(var4, 9));
      }

      int var7 = maxStage((Int2IntOpenHashMap)this.stagesByKey.get(var1));
      if (var6 != var7) {
         this.snapshot = this.rebuildSnapshot();
      }

      return var6 != var7;
   }

   int lookupStage(long var1) {
      return this.snapshot.stageFor(var1);
   }

   private static int maxStage(Int2IntOpenHashMap var0) {
      if (var0 == null) {
         return -1;
      }

      int var1 = -1;
      IntIterator var2 = var0.values().iterator();

      while (var2.hasNext()) {
         int var3 = (Integer)var2.next();
         var1 = Math.max(var1, var3);
      }

      return var1;
   }

   synchronized boolean isEmpty() {
      return this.stagesByKey.isEmpty();
   }

   LongKeyedBuckets getSnapshot() {
      return this.snapshot;
   }

   private LongKeyedBuckets rebuildSnapshot() {
      if (this.stagesByKey.isEmpty()) {
         return LongKeyedBuckets.EMPTY;
      }

      long[] var1 = new long[this.stagesByKey.size()];
      int var2 = 0;
      LongIterator var3 = this.stagesByKey.keySet().iterator();

      while (var3.hasNext()) {
         long var4 = (Long)var3.next();
         var1[var2++] = var4;
      }

      Arrays.sort(var1);
      byte[] var7 = new byte[var1.length];

      for (int var6 = 0; var6 < var1.length; var6++) {
         var7[var6] = (byte)maxStage((Int2IntOpenHashMap)this.stagesByKey.get(var1[var6]));
      }

      return new LongKeyedBuckets(var1, var7);
   }
}
