package dev.lone.itemsadder.util;

public class IntRange {
   private final int from;
   private final int to;

   public IntRange(int var1, int var2) {
      this.from = var1;
      this.to = var2;
   }

   public boolean contains(int var1) {
      return var1 >= this.from && var1 <= this.to;
   }
}
