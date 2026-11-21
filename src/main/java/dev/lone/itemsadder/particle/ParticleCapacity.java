package dev.lone.itemsadder.particle;

import java.util.concurrent.atomic.AtomicInteger;

final class ParticleCapacity {
   private final int ava;
   private final AtomicInteger avb = new AtomicInteger();

   ParticleCapacity(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("Positive capacity required");
      }

      this.ava = var1;
   }

   boolean HV() {
      for (int var1 = this.avb.get(); var1 < this.ava; var1 = this.avb.get()) {
         if (this.avb.compareAndSet(var1, var1 + 1)) {
            return true;
         }
      }

      return false;
   }

   void bP(int var1) {
      if (var1 != 0) {
         for (int var2 = this.avb.get(); var1 >= 0 && var1 <= var2; var2 = this.avb.get()) {
            if (this.avb.compareAndSet(var2, var2 - var1)) {
               return;
            }
         }

         throw new IllegalStateException("Particle capacity released without reservation");
      }
   }

   int HW() {
      return this.avb.get();
   }
}
