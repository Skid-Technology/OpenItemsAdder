package dev.lone.itemsadder.util;

import java.awt.image.BufferedImage;

public class ImagePixelUtils {
   public static int[] toArgbArray(BufferedImage var0) {
      int var1 = var0.getWidth();
      int var2 = var0.getHeight();
      int[] var3 = new int[var1 * var2];

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            var3[var4 * var1 + var5] = var0.getRGB(var5, var4);
         }
      }

      return var3;
   }

   public static boolean hasTransparentMarker(BufferedImage var0) {
      if (var0 == null) {
         return false;
      }

      int var1 = var0.getWidth();
      int var2 = var0.getHeight();
      if (var1 == 64 && var2 == 32) {
         return false;
      }

      if (var1 % 64 == 0 && var2 % 64 == 0) {
         int var3 = Math.max(1, var1 / 64);
         int var4 = 54 * var3;
         int var5 = 20 * var3;
         if (var4 < var1 && var5 < var2) {
            int var6 = var0.getRGB(var4, var5);
            int var7 = var6 >>> 24 & 0xFF;
            return var7 == 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
