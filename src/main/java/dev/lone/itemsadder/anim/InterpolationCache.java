package dev.lone.itemsadder.anim;

import java.util.Arrays;
import java.util.function.IntFunction;

public final class InterpolationCache {
   private final Object[] aRx = new Object[34];
   private final boolean[] aRy = new boolean[34];
   private final int[] aRz = new int[34];
   private final int[] aRA = new int[34];

   Object method3149(int var1, IntFunction var2) {
      return this.method3150(var1, 127, var2);
   }

   Object method3150(int var1, int var2, IntFunction var3) {
      return this.method3151(var1, var2, 0, var3);
   }

   Object method3151(int var1, int var2, int var3, IntFunction var4) {
      if (var1 >= 0 && var1 < this.aRx.length) {
         int var5 = var1;
         if (!this.aRy[var5] || this.aRz[var5] != var2 || this.aRA[var5] != var3) {
            this.aRx[var5] = var4.apply(var1);
            this.aRy[var5] = true;
            this.aRz[var5] = var2;
            this.aRA[var5] = var3;
         }

         return this.aRx[var5];
      } else {
         throw new IllegalArgumentException("Unsupported display interpolation duration: " + var1);
      }
   }

   void tU() {
      Arrays.fill(this.aRx, null);
      Arrays.fill(this.aRy, false);
   }
}
