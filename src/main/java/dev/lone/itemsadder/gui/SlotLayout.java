package dev.lone.itemsadder.gui;

public final class SlotLayout {
   static boolean cd(int var0) {
      return var0 > 34;
   }

   static int method2584(int var0, int var1, int var2) {
      if (var2 > 1 && var2 < 36) {
         if (cd(var0)) {
            if (var2 % 9 == 8) {
               return -1;
            }

            int var11 = var1 * 8 + var2 / 9 * 8 + var2 % 9 - 2;
            return var11 < var0 ? var11 : -1;
         } else {
            int var3 = var0 < 9 ? 1 : (var0 <= 18 ? 2 : (var0 <= 25 ? 3 : 4));
            int var4 = var3 <= 2 ? 1 : 0;
            int var5 = 0;

            for (int var6 = 0; var6 < var3; var6++) {
               int var7 = var4 + var6;
               int var8 = var7 == 0 ? 7 : 9;
               int var9 = Math.min(var8, (var0 - var5 + var3 - var6 - 1) / (var3 - var6));
               int var10 = Math.max(var7 == 0 ? 2 : 0, (9 - var9) / 2);
               if (var2 / 9 == var7 && var2 % 9 >= var10 && var2 % 9 < var10 + var9) {
                  return var5 + var2 % 9 - var10;
               }

               var5 += var9;
            }

            return -1;
         }
      } else {
         return -1;
      }
   }
}
