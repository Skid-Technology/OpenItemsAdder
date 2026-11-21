package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ShapeUtil {
   public static String[] method2277(String[] var0) {
      String[] var1 = new String[var0.length];
      System.arraycopy(var0, 0, var1, 0, var0.length);

      for (int var2 = 0; var2 < var1.length; var2++) {
         String[] var3 = var1[var2].split("");
         if (var3.length == 2) {
            var1[var2] = "x" + var1[var2];
         } else if (var3.length == 1) {
            var1[var2] = "xx" + var1[var2];
         }
      }

      return var1;
   }

   public static int method2278(String[] var0) {
      Integer var1 = null;

      for (String var5 : var0) {
         if (var1 == null) {
            var1 = var5.length();
         } else if (var1 != var5.length()) {
            throw new IllegalArgumentException("Invalid pattern lines length. Each line must have the same length!");
         }
      }

      if (var1 == null) {
         throw new IllegalArgumentException("No line? Invalid pattern!");
      } else {
         return var1;
      }
   }

   @Nullable
   public static String[] method2279(String[] var0) {
      int var1 = Integer.MAX_VALUE;
      int var2 = -1;
      int var3 = Integer.MAX_VALUE;
      int var4 = -1;

      for (int var5 = 0; var5 < var0.length; var5++) {
         for (int var6 = 0; var6 < var0[var5].length(); var6++) {
            char var7 = var0[var5].charAt(var6);
            if (var7 != 'x' && var7 != ' ') {
               var1 = Math.min(var1, var5);
               var2 = Math.max(var2, var5);
               var3 = Math.min(var3, var6);
               var4 = Math.max(var4, var6);
            }
         }
      }

      if (var2 == -1) {
         return null;
      }

      int var9 = var2 - var1 + 1;
      int var10 = var4 - var3 + 1;
      if (var9 == 2 && var10 == 2) {
         String[] var11 = new String[2];

         for (int var8 = 0; var8 < 2; var8++) {
            var11[var8] = var0[var1 + var8].substring(var3, var3 + 2);
         }

         return var11;
      } else {
         return null;
      }
   }

   private static int method2280(ItemStack[] var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 3; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 3; var4++) {
            if (!ItemStackUtils.aE(var0[var4 * 3 + var2])) {
               var3++;
            }
         }

         if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   private static int method2281(ItemStack[] var0) {
      Integer var1 = null;

      for (int var2 = 0; var2 < 3; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 3; var4++) {
            if (!ItemStackUtils.aE(var0[var2 * 3 + var4])) {
               var3++;
            }
         }

         if (var1 == null) {
            var1 = var3;
         } else if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }
}
