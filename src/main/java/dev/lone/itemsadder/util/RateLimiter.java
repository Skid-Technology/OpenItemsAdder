package dev.lone.itemsadder.util;

public final class RateLimiter {
   private static final long WINDOW_NANOS = 400000000L;
   private boolean armed;
   private long armedAtNanos;

   public static boolean isWithinWindow(long var0, long var2) {
      return var2 >= var0 && var2 - var0 <= 400000000L;
   }

   public boolean isOverLimit(long var1) {
      if (this.armed && isWithinWindow(this.armedAtNanos, var1)) {
         this.reset();
         return true;
      } else {
         this.armed = true;
         this.armedAtNanos = var1;
         return false;
      }
   }

   public void reset() {
      this.armed = false;
   }
}
