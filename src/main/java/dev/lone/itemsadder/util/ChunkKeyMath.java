package dev.lone.itemsadder.util;

public final class ChunkKeyMath {
   private ChunkKeyMath() {
   }

   static boolean isSphereVisibleFrom(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = var10 - var0;
      double var20 = var12 - var2;
      double var22 = var14 - var4;
      double var24 = var18 * var18 + var20 * var20 + var22 * var22;
      return isSphereInViewCone(var18, var20, var22, var6, var8, var24, var16);
   }

   static boolean isSphereInViewCone(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
      if (var10 == 0.0) {
         return true;
      }

      double var14 = var12 * var12;
      if (var2 * var2 >= var14 * var10) {
         return true;
      }

      double var16 = var6 * var6 + var8 * var8;
      if (var16 <= 1.0E-12) {
         return true;
      }

      double var18 = var6 * var0 + var8 * var4;
      double var20 = var0 * var0 + var4 * var4;
      return var18 > 0.0 && var18 * var18 >= var14 * var20 * var16;
   }

   static long packChunkKey(int var0, int var1, int var2) {
      return (var0 & 67108863L) << 38 | (var2 & 67108863L) << 12 | var1 & 4095L;
   }

   static int unpackX(long var0) {
      return (int)(var0 >> 38);
   }

   static int unpackY(long var0) {
      return (int)(var0 << 52 >> 52);
   }

   static int unpackZ(long var0) {
      return (int)(var0 << 26 >> 38);
   }
}
