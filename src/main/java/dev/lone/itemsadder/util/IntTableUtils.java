package dev.lone.itemsadder.util;

import java.util.Arrays;

public final class IntTableUtils {
   private static final int[] NEIGHBOR_DX = new int[]{1, -1, 0, 0};
   private static final int[] NEIGHBOR_DY = new int[]{0, 0, 1, -1};

   private IntTableUtils() {
   }

   public static void fillTransparentPixels(int[] var0, int var1, int var2) {
      int var3 = Math.multiplyExact(var1, var2);
      if (var1 > 0 && var2 > 0 && var0.length == var3) {
         int[] var4 = new int[var3];
         int[] var5 = new int[var3];
         int[] var6 = new int[var3];
         Arrays.fill(var5, Integer.MAX_VALUE);
         int var7 = 0;
         int var8 = 0;

         for (int var9 = 0; var9 < var1; var9++) {
            for (int var10 = 0; var10 < var2; var10++) {
               int var11 = var9 + var10 * var1;
               if (var0[var11] >>> 24 != 0) {
                  var5[var11] = 0;
                  var4[var11] = var0[var11];
                  var6[var8++] = var11;
               }
            }
         }

         while (var7 < var8) {
            int var16 = var6[var7++];
            int var18 = var16 % var1;
            int var19 = var16 / var1;

            for (int var12 = 0; var12 < 4; var12++) {
               int var13 = var18 + NEIGHBOR_DX[var12];
               int var14 = var19 + NEIGHBOR_DY[var12];
               if (var13 >= 0 && var14 >= 0 && var13 < var1 && var14 < var2) {
                  int var15 = var13 + var14 * var1;
                  if (var5[var15] > var5[var16] + 1) {
                     var5[var15] = var5[var16] + 1;
                     var4[var15] = var4[var16];
                     var6[var8++] = var15;
                  }
               }
            }
         }

         for (int var17 = 0; var17 < var3; var17++) {
            if (var0[var17] >>> 24 == 0) {
               var0[var17] = var4[var17] & 16777215;
            }
         }
      } else {
         throw new IllegalArgumentException("ARGB dimensions");
      }
   }
}
