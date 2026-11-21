package dev.lone.itemsadder.util;

import dev.lone.itemsadder.util.serial.SerialByteSlice;
import dev.lone.itemsadder.util.serial.SerialBytes;
import dev.lone.itemsadder.util.serial.SerialWriterBase;

public final class Base64Codec {
   static final byte PAD = 61;
   static final byte[] encodeTable = new byte[]{
      65,
      66,
      67,
      68,
      69,
      70,
      71,
      72,
      73,
      74,
      75,
      76,
      77,
      78,
      79,
      80,
      81,
      82,
      83,
      84,
      85,
      86,
      87,
      88,
      89,
      90,
      97,
      98,
      99,
      100,
      101,
      102,
      103,
      104,
      105,
      106,
      107,
      108,
      109,
      110,
      111,
      112,
      113,
      114,
      115,
      116,
      117,
      118,
      119,
      120,
      121,
      122,
      48,
      49,
      50,
      51,
      52,
      53,
      54,
      55,
      56,
      57,
      43,
      47
   };
   static final byte[] decodeTable = new byte[256];

   private Base64Codec() {
   }

   public static byte[] encode(byte[] var0) {
      return encode(var0, 0, var0.length);
   }

   public static byte[] encode(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[(var2 + 2) / 3 * 4];
      encodeBlock(var0, var1, var2, var3, 0);
      return var3;
   }

   public static char[] encodeToChars(byte[] var0) {
      return encodeToChars(var0, 0, var0.length);
   }

   public static char[] encodeToChars(byte[] var0, int var1, int var2) {
      char[] var3 = new char[(var2 + 2) / 3 * 4];
      encodeBlockToChars(var0, var1, var2, var3, 0);
      return var3;
   }

   private static void encodeBlock(byte[] var0, int var1, int var2, byte[] var3, int var4) {
      int var8 = var2 % 3;
      int var9 = var1 + (var2 - var8);

      while (var1 < var9) {
         byte var5 = var0[var1++];
         byte var6 = var0[var1++];
         byte var7 = var0[var1++];
         var3[var4++] = encodeTable[var5 >>> 2 & 63];
         var3[var4++] = encodeTable[var5 << 4 & 63 | var6 >>> 4 & 15];
         var3[var4++] = encodeTable[var6 << 2 & 63 | var7 >>> 6 & 3];
         var3[var4++] = encodeTable[var7 & 63];
      }

      switch (var8) {
         case 0:
            break;
         case 1:
            byte var27 = var0[var1++];
            var3[var4++] = encodeTable[var27 >>> 2 & 63];
            var3[var4++] = encodeTable[var27 << 4 & 63];
            var3[var4++] = 61;
            var3[var4++] = 61;
            break;
         case 2:
            byte var26 = var0[var1++];
            byte var28 = var0[var1++];
            var3[var4++] = encodeTable[var26 >>> 2 & 63];
            var3[var4++] = encodeTable[var26 << 4 & 63 | var28 >>> 4 & 15];
            var3[var4++] = encodeTable[var28 << 2 & 63];
            var3[var4++] = 61;
            break;
         default:
            throw new IllegalStateException("should not happen");
      }
   }

   private static void encodeBlockToChars(byte[] var0, int var1, int var2, char[] var3, int var4) {
      int var8 = var2 % 3;
      int var9 = var1 + (var2 - var8);

      while (var1 < var9) {
         byte var5 = var0[var1++];
         byte var6 = var0[var1++];
         byte var7 = var0[var1++];
         var3[var4++] = (char)encodeTable[var5 >>> 2 & 63];
         var3[var4++] = (char)encodeTable[var5 << 4 & 63 | var6 >>> 4 & 15];
         var3[var4++] = (char)encodeTable[var6 << 2 & 63 | var7 >>> 6 & 3];
         var3[var4++] = (char)encodeTable[var7 & 63];
      }

      switch (var8) {
         case 0:
            break;
         case 1:
            byte var27 = var0[var1++];
            var3[var4++] = (char)encodeTable[var27 >>> 2 & 63];
            var3[var4++] = (char)encodeTable[var27 << 4 & 63];
            var3[var4++] = '=';
            var3[var4++] = '=';
            break;
         case 2:
            byte var26 = var0[var1++];
            byte var28 = var0[var1++];
            var3[var4++] = (char)encodeTable[var26 >>> 2 & 63];
            var3[var4++] = (char)encodeTable[var26 << 4 & 63 | var28 >>> 4 & 15];
            var3[var4++] = (char)encodeTable[var28 << 2 & 63];
            var3[var4++] = '=';
            break;
         default:
            throw new IllegalStateException("should not happen");
      }
   }

   public static SerialByteSlice encodeIntoSlice(byte[] var0, int var1, int var2, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = (var2 + 2) / 3 * 4;
      var3.written += var5;
      int var6 = var4.bytes.length - var4.offset;
      if (var5 <= var6) {
         encodeBlock(var0, var1, var2, var4.bytes, var4.offset);
         var4.offset += var5;
         return var4;
      }

      int var7 = var6 / 4;
      if (var7 == 0) {
         if (var5 > var3.sliceSize) {
            byte[] var20 = new byte[var5];
            encodeBlock(var0, var1, var2, var20, 0);
            return new SerialByteSlice(var3.sliceSize, new SerialByteSlice(var20, 0, var5, var4));
         } else {
            byte[] var19 = new byte[var3.sliceSize];
            encodeBlock(var0, var1, var2, var19, 0);
            return new SerialByteSlice(var19, 0, var5, var4);
         }
      } else {
         int var8 = var1;
         byte[] var9 = var4.bytes;
         int var10 = var4.offset;

         while (var7-- > 0) {
            byte var11 = var0[var1++];
            byte var12 = var0[var1++];
            byte var13 = var0[var1++];
            var9[var10++] = encodeTable[var11 >>> 2 & 63];
            var9[var10++] = encodeTable[var11 << 4 & 63 | var12 >>> 4 & 15];
            var9[var10++] = encodeTable[var12 << 2 & 63 | var13 >>> 6 & 3];
            var9[var10++] = encodeTable[var13 & 63];
         }

         var2 -= var1 - var8;
         var5 -= var10 - var4.offset;
         var4.offset = var10;
         if (var5 > var3.sliceSize) {
            byte[] var24 = new byte[var5];
            encodeBlock(var0, var1, var2, var24, 0);
            return new SerialByteSlice(var3.sliceSize, new SerialByteSlice(var24, 0, var5, var4));
         } else {
            byte[] var14 = new byte[var3.sliceSize];
            encodeBlock(var0, var1, var2, var14, 0);
            return new SerialByteSlice(var14, 0, var5, var4);
         }
      }
   }

   public static SerialByteSlice encodeIntoStreamingSlice(byte[] var0, int var1, int var2, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = (var2 + 2) / 3 * 4;
      var3.written += var5;
      int var6 = var4.bytes.length - var4.offset;
      if (var5 > var6) {
         byte[] var7 = var4.bytes;
         int var8 = var4.offset;
         int var9 = var2 % 3;
         int var10 = var6 / 4;

         for (int var14 = var1 + (var2 - var9); var1 < var14; var10--) {
            if (var10 == 0) {
               var8 = var3.writeChunk(var7, var4.start, var8 - var4.start);
               var10 = (var7.length - var8) / 4;
            }

            byte var11 = var0[var1++];
            byte var12 = var0[var1++];
            byte var13 = var0[var1++];
            var7[var8++] = encodeTable[var11 >>> 2 & 63];
            var7[var8++] = encodeTable[var11 << 4 & 63 | var12 >>> 4 & 15];
            var7[var8++] = encodeTable[var12 << 2 & 63 | var13 >>> 6 & 3];
            var7[var8++] = encodeTable[var13 & 63];
         }

         switch (var9) {
            case 0:
               break;
            case 1:
               if (var10 == 0) {
                  var8 = var3.writeChunk(var7, var4.start, var8 - var4.start);
               }

               byte var30 = var0[var1++];
               var7[var8++] = encodeTable[var30 >>> 2 & 63];
               var7[var8++] = encodeTable[var30 << 4 & 63];
               var7[var8++] = 61;
               var7[var8++] = 61;
               break;
            case 2:
               if (var10 == 0) {
                  var8 = var3.writeChunk(var7, var4.start, var8 - var4.start);
               }

               byte var29 = var0[var1++];
               byte var31 = var0[var1++];
               var7[var8++] = encodeTable[var29 >>> 2 & 63];
               var7[var8++] = encodeTable[var29 << 4 & 63 | var31 >>> 4 & 15];
               var7[var8++] = encodeTable[var31 << 2 & 63];
               var7[var8++] = 61;
               break;
            default:
               throw new IllegalStateException("should not happen");
         }

         var4.offset = var8;
         return var4;
      } else {
         encodeBlock(var0, var1, var2, var4.bytes, var4.offset);
         var4.offset += var5;
         return var4;
      }
   }

   public static byte[] decode(byte[] var0) {
      return decodeRange(var0, 0, var0.length);
   }

   public static byte[] decodeChars(char[] var0) {
      return decodeCharRange(var0, 0, var0.length);
   }

   public static byte[] decodeRange(byte[] var0, int var1, int var2) {
      if (var2 == 0) {
         return SerialBytes.EMPTY_BYTES;
      }

      if (var2 % 4 != 0) {
         throw new IllegalArgumentException("Input block size is not 4");
      }

      int var3 = var2;
      int var4 = var1 + var2;

      while (var0[--var4] == 61) {
         var3--;
      }

      int var5 = var3 * 3 / 4;
      byte[] var6 = new byte[var5];
      decodeBlock(var0, var1, var2, var6, 0, var5);
      return var6;
   }

   public static byte[] decodeCharRange(char[] var0, int var1, int var2) {
      if (var2 == 0) {
         return SerialBytes.EMPTY_BYTES;
      }

      if (var2 % 4 != 0) {
         throw new IllegalArgumentException("Input block size is not 4");
      }

      int var3 = var2;
      int var4 = var1 + var2;

      while (var0[--var4] == '=') {
         var3--;
      }

      int var5 = var3 * 3 / 4;
      byte[] var6 = new byte[var5];
      decodeCharBlock(var0, var1, var2, var6, 0, var5);
      return var6;
   }

   public static int decodeInto(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      if (var4 == 0) {
         return 0;
      }

      if (var4 % 4 != 0) {
         throw new IllegalArgumentException("Input block size is not 4");
      }

      int var5 = var4;
      int var6 = var3 + var4;

      while (var2[--var6] == 61) {
         var5--;
      }

      int var7 = var5 * 3 / 4;
      assert var0.length - var1 >= var7;
      decodeBlock(var2, var3, var4, var0, var1, var7);
      return var7;
   }

   private static void decodeBlock(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      int var6 = var5 / 3 * 3;

      try {
         while (var4 < var6) {
            byte var7 = decodeTable[var0[var1++]];
            byte var8 = decodeTable[var0[var1++]];
            byte var9 = decodeTable[var0[var1++]];
            byte var10 = decodeTable[var0[var1++]];
            if (var7 < 0 || var8 < 0 || var9 < 0 || var10 < 0) {
               throw new IllegalArgumentException("Not B64 encoded");
            }

            var3[var4++] = (byte)(var7 << 2 | var8 >>> 4);
            var3[var4++] = (byte)(var8 << 4 | var9 >>> 2);
            var3[var4++] = (byte)(var9 << 6 | var10);
         }

         if (var5 != var4) {
            switch (var5 % 3) {
               case 0:
                  break;
               case 1:
                  byte var19 = decodeTable[var0[var1++]];
                  byte var21 = decodeTable[var0[var1++]];
                  if (var19 < 0 || var21 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var19 << 2 | var21 >>> 4);
                  break;
               case 2:
                  byte var18 = decodeTable[var0[var1++]];
                  byte var20 = decodeTable[var0[var1++]];
                  byte var22 = decodeTable[var0[var1++]];
                  if (var18 < 0 || var20 < 0 || var22 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var18 << 2 | var20 >>> 4);
                  var3[var4++] = (byte)(var20 << 4 | var22 >>> 2);
                  break;
               default:
                  throw new IllegalStateException("should not happen");
            }
         }
      } catch (IndexOutOfBoundsException var12) {
         throw new IllegalArgumentException("char " + var1 + " was not B64 encoded");
      }
   }

   private static void decodeCharBlock(char[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      int var6 = var5 / 3 * 3;

      try {
         while (var4 < var6) {
            byte var7 = decodeTable[var0[var1++]];
            byte var8 = decodeTable[var0[var1++]];
            byte var9 = decodeTable[var0[var1++]];
            byte var10 = decodeTable[var0[var1++]];
            if (var7 < 0 || var8 < 0 || var9 < 0 || var10 < 0) {
               throw new IllegalArgumentException("Not B64 encoded");
            }

            var3[var4++] = (byte)(var7 << 2 | var8 >>> 4);
            var3[var4++] = (byte)(var8 << 4 | var9 >>> 2);
            var3[var4++] = (byte)(var9 << 6 | var10);
         }

         if (var5 != var4) {
            switch (var5 % 3) {
               case 0:
                  break;
               case 1:
                  byte var19 = decodeTable[var0[var1++]];
                  byte var21 = decodeTable[var0[var1++]];
                  if (var19 < 0 || var21 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var19 << 2 | var21 >>> 4);
                  break;
               case 2:
                  byte var18 = decodeTable[var0[var1++]];
                  byte var20 = decodeTable[var0[var1++]];
                  byte var22 = decodeTable[var0[var1++]];
                  if (var18 < 0 || var20 < 0 || var22 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var18 << 2 | var20 >>> 4);
                  var3[var4++] = (byte)(var20 << 4 | var22 >>> 2);
                  break;
               default:
                  throw new IllegalStateException("should not happen");
            }
         }
      } catch (IndexOutOfBoundsException var12) {
         throw new IllegalArgumentException("char " + var1 + " was not B64 encoded");
      }
   }

   public static byte[] decodeString(String var0) {
      return decodeStringRange(var0, 0, var0.length());
   }

   public static byte[] decodeStringRange(String var0, int var1, int var2) {
      if (var2 == 0) {
         return new byte[0];
      }

      if (var2 % 4 != 0) {
         throw new IllegalArgumentException("Input block size is not 4");
      }

      int var3 = var2;
      int var4 = var1 + var2;

      while (var0.charAt(--var4) == '=') {
         var3--;
      }

      int var5 = var3 * 3 / 4;
      byte[] var6 = new byte[var5];
      decodeStringBlock(var0, var1, var2, var6, 0, var5);
      return var6;
   }

   public static int decodeStringInto(byte[] var0, int var1, String var2, int var3, int var4) {
      if (var4 == 0) {
         return 0;
      }

      if (var4 % 4 != 0) {
         throw new IllegalArgumentException("Input block size is not 4");
      }

      int var5 = var4;
      int var6 = var3 + var4;

      while (var2.charAt(--var6) == '=') {
         var5--;
      }

      int var7 = var5 * 3 / 4;
      assert var0.length - var1 >= var7;
      decodeStringBlock(var2, var3, var4, var0, var1, var7);
      return var7;
   }

   private static void decodeStringBlock(String var0, int var1, int var2, byte[] var3, int var4, int var5) {
      int var6 = var5 / 3 * 3;

      try {
         while (var4 < var6) {
            byte var7 = decodeTable[var0.charAt(var1++)];
            byte var8 = decodeTable[var0.charAt(var1++)];
            byte var9 = decodeTable[var0.charAt(var1++)];
            byte var10 = decodeTable[var0.charAt(var1++)];
            if (var7 < 0 || var8 < 0 || var9 < 0 || var10 < 0) {
               throw new IllegalArgumentException("Not B64 encoded");
            }

            var3[var4++] = (byte)(var7 << 2 | var8 >>> 4);
            var3[var4++] = (byte)(var8 << 4 | var9 >>> 2);
            var3[var4++] = (byte)(var9 << 6 | var10);
         }

         if (var5 != var4) {
            switch (var5 % 3) {
               case 0:
                  break;
               case 1:
                  byte var19 = decodeTable[var0.charAt(var1++)];
                  byte var21 = decodeTable[var0.charAt(var1++)];
                  if (var19 < 0 || var21 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var19 << 2 | var21 >>> 4);
                  break;
               case 2:
                  byte var18 = decodeTable[var0.charAt(var1++)];
                  byte var20 = decodeTable[var0.charAt(var1++)];
                  byte var22 = decodeTable[var0.charAt(var1++)];
                  if (var18 < 0 || var20 < 0 || var22 < 0) {
                     throw new IllegalArgumentException("Not B64 encoded");
                  }

                  var3[var4++] = (byte)(var18 << 2 | var20 >>> 4);
                  var3[var4++] = (byte)(var20 << 4 | var22 >>> 2);
                  break;
               default:
                  throw new IllegalStateException("should not happen");
            }
         }
      } catch (IndexOutOfBoundsException var12) {
         throw new IllegalArgumentException("char " + var1 + " was not B64 encoded");
      }
   }

   static {
      for (int var0 = 0; var0 < 256; var0++) {
         decodeTable[var0] = -1;
      }

      byte var1 = 0;

      while (var1 < 64) {
         decodeTable[encodeTable[var1]] = var1++;
      }

      decodeTable[61] = 0;
   }
}
