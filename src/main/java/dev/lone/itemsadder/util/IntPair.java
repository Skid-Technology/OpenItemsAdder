package dev.lone.itemsadder.util;

public class IntPair {
   private final int first;
   private final int second;

   private IntPair(int var1, int var2) {
      this.first = var1;
      this.second = var2;
   }

   public int getFirst() {
      return this.first;
   }

   public int getSecond() {
      return this.second;
   }
}
