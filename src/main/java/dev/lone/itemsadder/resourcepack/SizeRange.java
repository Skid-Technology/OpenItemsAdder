package dev.lone.itemsadder.resourcepack;

class SizeRange {
   private final long min;
   private final long max;

   private SizeRange(long var1, long var3) {
      this.min = var1;
      this.max = var3;
   }

   boolean contains(long var1) {
      return var1 >= this.min && var1 <= this.max;
   }
}
