package dev.lone.itemsadder.util;

public final class TimeWindow {
   private final long windowSize;
   private long start;
   private long count;

   TimeWindow(long var1) {
      this.windowSize = var1;
   }

   void addSample(long var1) {
      this.start = var1;
      this.count = 0L;
   }

   LongPairDup2 snapshot(long var1) {
      long var3 = Math.max(1L, Math.floorDiv(Math.max(0L, var1 - this.start), this.windowSize) + 1L);
      this.count += var3;
      this.start = this.start + var3 * this.windowSize;
      return new LongPairDup2(this.count, var3);
   }

   long getStart() {
      return this.start;
   }
}
