package dev.lone.itemsadder.util.serial;

import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;

public final class Base64Helper {
   static final boolean CESU8_COMPAT = Boolean.getBoolean("io.protostuff.cesu8_compat");

   private Base64Helper() {
   }

   public static String encode(byte[] var0) {
      return encodeRange(var0, 0, var0.length);
   }

   public static String encodeRange(byte[] var0, int var1, int var2) {
      try {
         String var3 = new String(var0, var1, var2, "UTF-8");
         if (CESU8_COMPAT && var3.indexOf(65533) != -1) {
            try {
               return decodeModifiedUtf8(var0, var1, var2);
            } catch (UTFDataFormatException var5) {
               return var3;
            }
         } else {
            return var3;
         }
      } catch (UnsupportedEncodingException var6) {
         throw new RuntimeException(var6);
      }
   }

   static String encodeUnsafe(byte[] var0) {
      try {
         return decodeModifiedUtf8(var0, 0, var0.length);
      } catch (UTFDataFormatException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static byte[] toUtf8Bytes(String var0) {
      try {
         return var0.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static String decodeModifiedUtf8(byte[] var0, int var1, int var2) {
      char[] var3 = new char[var2];
      int var4 = 0;
      int var5 = 0;

      while (var4 < var2) {
         int var6 = var0[var1 + var4] & 255;
         if (var6 > 127) {
            break;
         }

         var3[var5++] = (char)var6;
         var4++;
      }

      while (var4 < var2) {
         int var13 = var0[var1 + var4] & 255;
         int var7 = var13 >> 4;
         if (var7 <= 7) {
            var3[var5++] = (char)var13;
            var4++;
         } else if (var7 == 12 || var7 == 13) {
            var4 += 2;
            if (var4 > var2) {
               throw new UTFDataFormatException("Malformed input: Partial character at end");
            }

            byte var8 = var0[var1 + var4 - 1];
            if ((var8 & 192) != 128) {
               throw new UTFDataFormatException("Malformed input around byte " + var4);
            }

            var3[var5++] = (char)((var13 & 31) << 6 | var8 & 63);
         } else if (var7 == 14) {
            var4 += 3;
            if (var4 > var2) {
               throw new UTFDataFormatException("Malformed input: Partial character at end");
            }

            byte var16 = var0[var1 + var4 - 2];
            byte var17 = var0[var1 + var4 - 1];
            if ((var16 & 192) != 128 || (var17 & 192) != 128) {
               throw new UTFDataFormatException("Malformed input around byte " + (var4 - 1));
            }

            var3[var5++] = (char)((var13 & 15) << 12 | (var16 & 63) << 6 | var17 & 63);
         } else {
            var7 = var13 >> 3;
            if (var7 != 30) {
               throw new UTFDataFormatException("Malformed input at byte " + var4);
            }

            var4 += 4;
            if (var4 > var2) {
               throw new UTFDataFormatException("Malformed input: Partial character at end");
            }

            byte var15 = var0[var1 + var4 - 3];
            byte var9 = var0[var1 + var4 - 2];
            byte var10 = var0[var1 + var4 - 1];
            int var11 = (var13 & 7) << 18 | (var15 & 63) << 12 | (var9 & 63) << 6 | var10 & 63;
            var3[var5++] = Utf8Codec.highSurrogate(var11);
            var3[var5++] = Utf8Codec.lowSurrogate(var11);
         }
      }

      return new String(var3, 0, var5);
   }
}
