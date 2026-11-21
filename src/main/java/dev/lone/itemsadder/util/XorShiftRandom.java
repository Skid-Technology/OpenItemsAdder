package dev.lone.itemsadder.util;

public final class XorShiftRandom {
   int initialSeed;
   long state;

   XorShiftRandom(int var1) {
      this.initialSeed = var1 == 0 ? 1831565813 : var1;
   }

   double nextDouble() {
      this.initialSeed = this.initialSeed ^ this.initialSeed << 13;
      this.initialSeed = this.initialSeed ^ this.initialSeed >>> 17;
      this.initialSeed = this.initialSeed ^ this.initialSeed << 5;
      this.state++;
      return Integer.toUnsignedLong(this.initialSeed) / 4.2949673E9F;
   }

   void fillDoubles(double[] var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = this.nextDouble();
      }
   }
}
