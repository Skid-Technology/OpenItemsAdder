package dev.lone.itemsadder.util;

public class PersistentCounterExt extends PersistentCounter {
   int count;

   public int incrementAndGet() {
      this.count++;
      return this.count;
   }

   public void increment() {
      this.count++;
   }

   public int getCount() {
      return this.count;
   }
}
