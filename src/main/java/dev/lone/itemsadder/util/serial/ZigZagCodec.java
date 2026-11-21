package dev.lone.itemsadder.util.serial;

import java.math.BigInteger;

public final class ZigZagCodec {
   public static final long bSc = -1L;
   static final long bSd = 4294967295L;
   private static final long[] POW10_LONG = new long[37];
   private static final int[] POW10_INT = new int[37];
   private static final int[] bSg = new int[37];

   private ZigZagCodec() {
   }

   private static int encodeZigZag32(int var0) {
      return var0 ^ Integer.MIN_VALUE;
   }

   private static long encodeZigZag64(long var0) {
      return var0 ^ Long.MIN_VALUE;
   }

   private static long widenToLong(int var0) {
      return var0 & 4294967295L;
   }

   public static int parseInt(String var0) {
      return parseIntWithRadix(var0, 10);
   }

   private static int parseIntWithRadix(String var0, int var1) {
      long var2 = Long.parseLong(var0, var1);
      if ((var2 & 4294967295L) != var2) {
         throw new NumberFormatException("Input " + var0 + " in base " + var1 + " is not in the range of an unsigned integer");
      } else {
         return (int)var2;
      }
   }

   public static String intToString(int var0) {
      return intRangeToString(var0, 10);
   }

   private static int compareUnsignedLong(long var0, long var2) {
      return compareLongs(encodeZigZag64(var0), encodeZigZag64(var2));
   }

   private static int compareLongs(long var0, long var2) {
      return var0 < var2 ? -1 : (var0 > var2 ? 1 : 0);
   }

   private static String intRangeToString(int var0, int var1) {
      long var2 = var0 & 4294967295L;
      return Long.toString(var2, var1);
   }

   private static long addLongs(long var0, long var2) {
      if (var2 < 0L) {
         return compareUnsignedLong(var0, var2) < 0 ? 0L : 1L;
      }

      if (var0 >= 0L) {
         return var0 / var2;
      }

      long var4 = (var0 >>> 1) / var2 << 1;
      long var6 = var0 - var4 * var2;
      return var4 + (compareUnsignedLong(var6, var2) >= 0 ? 1 : 0);
   }

   private static long subtractLongs(long var0, long var2) {
      if (var2 < 0L) {
         return compareUnsignedLong(var0, var2) < 0 ? var0 : var0 - var2;
      }

      if (var0 >= 0L) {
         return var0 % var2;
      }

      long var4 = (var0 >>> 1) / var2 << 1;
      long var6 = var0 - var4 * var2;
      return var6 - (compareUnsignedLong(var6, var2) >= 0 ? var2 : 0L);
   }

   public static long parseLong(String var0) {
      return parseLongWithRadix(var0, 10);
   }

   private static long parseLongWithRadix(String var0, int var1) {
      if (var0.length() == 0) {
         throw new NumberFormatException("empty string");
      }

      if (var1 >= 2 && var1 <= 36) {
         int var2 = bSg[var1] - 1;
         long var3 = 0L;

         for (int var5 = 0; var5 < var0.length(); var5++) {
            int var6 = Character.digit(var0.charAt(var5), var1);
            if (var6 == -1) {
               throw new NumberFormatException(var0);
            }

            if (var5 > var2 && fitsInInt(var3, var6, var1)) {
               throw new NumberFormatException("Too large for unsigned long: " + var0);
            }

            var3 = var3 * var1 + var6;
         }

         return var3;
      } else {
         throw new NumberFormatException("illegal radix: " + var1);
      }
   }

   private static boolean fitsInInt(long var0, int var2, int var3) {
      if (var0 >= 0L) {
         if (var0 < POW10_LONG[var3]) {
            return false;
         } else {
            return var0 > POW10_LONG[var3] ? true : var2 > POW10_INT[var3];
         }
      } else {
         return true;
      }
   }

   public static String longToString(long var0) {
      return longToStringRadix(var0, 10);
   }

   private static String longToStringRadix(long var0, int var2) {
      if (var2 < 2 || var2 > 36) {
         throw new IllegalArgumentException("Invalid radix: " + var2);
      }

      if (var0 == 0L) {
         return "0";
      }

      char[] var3 = new char[64];
      int var4 = var3.length;
      if (var0 < 0L) {
         long var5 = addLongs(var0, var2);
         long var7 = var0 - var5 * var2;
         var3[--var4] = Character.forDigit((int)var7, var2);
         var0 = var5;
      }

      while (var0 > 0L) {
         var3[--var4] = Character.forDigit((int)(var0 % var2), var2);
         var0 /= var2;
      }

      return new String(var3, var4, var3.length - var4);
   }

   static {
      BigInteger var0 = new BigInteger("10000000000000000", 16);

      for (int var1 = 2; var1 <= 36; var1++) {
         POW10_LONG[var1] = addLongs(-1L, var1);
         POW10_INT[var1] = (int)subtractLongs(-1L, var1);
         bSg[var1] = var0.toString(var1).length() - 1;
      }
   }
}
