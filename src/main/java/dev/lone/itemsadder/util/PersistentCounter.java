package dev.lone.itemsadder.util;

public class PersistentCounter {
   long startTime = System.nanoTime();
   long lastLap = System.nanoTime();
   long savedMillis;

   public void init() {
      this.startTime = System.nanoTime();
      this.lastLap = System.nanoTime();
   }

   public long lapFromStart() {
      long var1 = this.millisSince(this.startTime);
      this.lastLap = System.nanoTime();
      return var1;
   }

   public long lapFromLast() {
      long var1 = this.millisSince(this.lastLap);
      this.lastLap = System.nanoTime();
      return var1;
   }

   long millisSince(long var1) {
      return (System.nanoTime() - var1) / 1000000L;
   }

   public static long now() {
      return System.nanoTime();
   }

   public void save() {
      this.savedMillis = this.lapFromStart();
   }

   public long getSaved() {
      return this.savedMillis;
   }
}
