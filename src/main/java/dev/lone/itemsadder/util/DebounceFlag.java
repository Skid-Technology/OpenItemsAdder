package dev.lone.itemsadder.util;

public class DebounceFlag {
   public long lastFireTime = System.currentTimeMillis();

   public DebounceFlag() {
   }

   public boolean tryFire() {
      return System.currentTimeMillis() - this.lastFireTime < 50L;
   }
}
