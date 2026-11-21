package dev.lone.itemsadder.gui;

import java.util.concurrent.TimeUnit;

public final class VisibilityCooldown {
   static final long aKu = TimeUnit.SECONDS.toNanos(1L);
   private long aKv = System.nanoTime();
   private boolean visible;

   synchronized boolean visible() {
      return this.visible;
   }

   synchronized void method2585(long var1) {
      this.aKv = var1;
      this.visible = false;
   }

   synchronized long method2586(long var1) {
      return Math.max(0L, aKu - (var1 - this.aKv) + 1L);
   }

   synchronized boolean method2587(long var1) {
      if (!this.visible && this.method2586(var1) == 0L) {
         this.visible = true;
         return true;
      } else {
         return false;
      }
   }
}
