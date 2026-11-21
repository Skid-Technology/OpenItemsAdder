package dev.lone.itemsadder.util.serial;

public final class Utf8Codec {
   static final int[] POW10_LIMITS = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
   static final char[] DIGITS_TENS = new char[]{
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9'
   };
   static final char[] DIGITS_ONES = new char[]{
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9'
   };
   static final char[] BASE36_DIGITS = new char[]{
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      'a',
      'b',
      'c',
      'd',
      'e',
      'f',
      'g',
      'h',
      'i',
      'j',
      'k',
      'l',
      'm',
      'n',
      'o',
      'p',
      'q',
      'r',
      's',
      't',
      'u',
      'v',
      'w',
      'x',
      'y',
      'z'
   };
   static final byte[] MIN_INT_BYTES = new byte[]{45, 50, 49, 52, 55, 52, 56, 51, 54, 52, 56};
   static final byte[] MIN_LONG_BYTES = new byte[]{45, 57, 50, 50, 51, 51, 55, 50, 48, 51, 54, 56, 53, 52, 55, 55, 53, 56, 48, 56};
   static final int ONE_BYTE_LIMIT = 128;
   static final int bRS = 43;
   static final int TWO_BYTE_LIMIT = 16384;
   static final int bRU = 5462;
   static final int bRV = 2097152;
   static final int bRW = 699051;
   static final int bRX = 268435456;
   static final int bRY = 89478486;

   private Utf8Codec() {
   }

   static void writeIntDigits(int var0, int var1, int var2, byte[] var3) {
      int var6 = var1 + var2;
      byte var7 = 0;
      if (var0 < 0) {
         var7 = 45;
         var0 = -var0;
      }

      while (var0 >= 65536) {
         int var4 = var0 / 100;
         int var5 = var0 - ((var4 << 6) + (var4 << 5) + (var4 << 2));
         var0 = var4;
         var3[--var6] = (byte)DIGITS_ONES[var5];
         var3[--var6] = (byte)DIGITS_TENS[var5];
      }

      do {
         int var8 = var0 * 52429 >>> 19;
         int var9 = var0 - ((var8 << 3) + (var8 << 1));
         var3[--var6] = (byte)BASE36_DIGITS[var9];
         var0 = var8;
      } while (var0 != 0);

      if (var7 != 0) {
         var3[--var6] = (byte)var7;
      }
   }

   static void writeLongDigits(long var0, int var2, int var3, byte[] var4) {
      int var8 = var2 + var3;
      byte var9 = 0;
      if (var0 < 0L) {
         var9 = 45;
         var0 = -var0;
      }

      while (var0 > 2147483647L) {
         long var5 = var0 / 100L;
         int var7 = (int)(var0 - ((var5 << 6) + (var5 << 5) + (var5 << 2)));
         var0 = var5;
         var4[--var8] = (byte)DIGITS_ONES[var7];
         var4[--var8] = (byte)DIGITS_TENS[var7];
      }

      int var11 = (int)var0;

      while (var11 >= 65536) {
         int var10 = var11 / 100;
         int var12 = var11 - ((var10 << 6) + (var10 << 5) + (var10 << 2));
         var11 = var10;
         var4[--var8] = (byte)DIGITS_ONES[var12];
         var4[--var8] = (byte)DIGITS_TENS[var12];
      }

      do {
         int var17 = var11 * 52429 >>> 19;
         int var13 = var11 - ((var17 << 3) + (var17 << 1));
         var4[--var8] = (byte)BASE36_DIGITS[var13];
         var11 = var17;
      } while (var11 != 0);

      if (var9 != 0) {
         var4[--var8] = (byte)var9;
      }
   }

   static int intDigitCount(int var0) {
      int var1 = 0;

      while (var0 > POW10_LIMITS[var1]) {
         var1++;
      }

      return var1 + 1;
   }

   static int longDigitCount(long var0) {
      long var2 = 10L;

      for (int var4 = 1; var4 < 19; var4++) {
         if (var0 < var2) {
            return var4;
         }

         var2 = 10L * var2;
      }

      return 19;
   }

   public static SerialByteSlice writeIntAsText(int var0, SerialWriterBase var1, SerialByteSlice var2) {
      if (var0 == Integer.MIN_VALUE) {
         int var4 = MIN_INT_BYTES.length;
         if (var2.offset + var4 > var2.bytes.length) {
            var2 = new SerialByteSlice(var1.sliceSize, var2);
         }

         System.arraycopy(MIN_INT_BYTES, 0, var2.bytes, var2.offset, var4);
         var2.offset += var4;
         var1.written += var4;
         return var2;
      } else {
         int var3 = var0 < 0 ? intDigitCount(-var0) + 1 : intDigitCount(var0);
         if (var2.offset + var3 > var2.bytes.length) {
            var2 = new SerialByteSlice(var1.sliceSize, var2);
         }

         writeIntDigits(var0, var2.offset, var3, var2.bytes);
         var2.offset += var3;
         var1.written += var3;
         return var2;
      }
   }

   public static SerialByteSlice writeLongAsText(long var0, SerialWriterBase var2, SerialByteSlice var3) {
      if (var0 == Long.MIN_VALUE) {
         int var5 = MIN_LONG_BYTES.length;
         if (var3.offset + var5 > var3.bytes.length) {
            var3 = new SerialByteSlice(var2.sliceSize, var3);
         }

         System.arraycopy(MIN_LONG_BYTES, 0, var3.bytes, var3.offset, var5);
         var3.offset += var5;
         var2.written += var5;
         return var3;
      } else {
         int var4 = var0 < 0L ? longDigitCount(-var0) + 1 : longDigitCount(var0);
         if (var3.offset + var4 > var3.bytes.length) {
            var3 = new SerialByteSlice(var2.sliceSize, var3);
         }

         writeLongDigits(var0, var3.offset, var4, var3.bytes);
         var3.offset += var4;
         var2.written += var4;
         return var3;
      }
   }

   public static SerialByteSlice writeFloatAsText(float var0, SerialWriterBase var1, SerialByteSlice var2) {
      return writeUtf8Checked(Float.toString(var0), var1, var2);
   }

   public static SerialByteSlice writeDoubleAsText(double var0, SerialWriterBase var2, SerialByteSlice var3) {
      return writeUtf8Checked(Double.toString(var0), var2, var3);
   }

   public static int utf8Length(CharSequence var0, int var1, int var2) {
      int var3 = var2;

      for (int var4 = var1; var4 < var2; var4++) {
         char var5 = var0.charAt(var4);
         if (var5 >= 128) {
            if (var5 < 2048) {
               var3++;
            } else {
               var3 += 2;
            }
         }
      }

      return var3;
   }

   static SerialByteSlice encodeAsciiChunk(CharSequence var0, int var1, int var2, byte[] var3, int var4, int var5, SerialWriterBase var6, SerialByteSlice var7) {
      char var8 = 0;

      while (true) {
         for (; var1 == var2 || var4 == var5 || (var8 = var0.charAt(var1++)) >= 128; var8 = 0) {
            if (var1 == var2 && var8 < 128) {
               var6.written = var6.written + (var4 - var7.offset);
               var7.offset = var4;
               return var7;
            }

            if (var4 == var5) {
               var6.written = var6.written + (var4 - var7.offset);
               var7.offset = var4;
               if (var7.next == null) {
                  var4 = 0;
                  var5 = var6.sliceSize;
                  var3 = new byte[var5];
                  var7 = new SerialByteSlice(var3, 0, var7);
               } else {
                  var7 = var7.next;
                  var4 = var7.start;
                  var7.offset = var7.start;
                  var3 = var7.bytes;
                  var5 = var3.length;
               }
            } else if (var8 < 2048) {
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(192 | var8 >> 6 & 31);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var8 >> 0 & 63);
            } else if (Character.isHighSurrogate(var8) && var1 < var2 && Character.isLowSurrogate(var0.charAt(var1))) {
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               int var9 = Character.toCodePoint(var8, var0.charAt(var1));
               var3[var4++] = (byte)(240 | var9 >> 18 & 7);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var9 >> 12 & 63);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var9 >> 6 & 63);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var9 >> 0 & 63);
               var1++;
            } else {
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(224 | var8 >> '\f' & 15);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var8 >> 6 & 63);
               if (var4 == var5) {
                  var6.written = var6.written + (var4 - var7.offset);
                  var7.offset = var4;
                  if (var7.next == null) {
                     var4 = 0;
                     var5 = var6.sliceSize;
                     var3 = new byte[var5];
                     var7 = new SerialByteSlice(var3, 0, var7);
                  } else {
                     var7 = var7.next;
                     var4 = var7.start;
                     var7.offset = var7.start;
                     var3 = var7.bytes;
                     var5 = var3.length;
                  }
               }

               var3[var4++] = (byte)(128 | var8 >> 0 & 63);
            }
         }

         var3[var4++] = (byte)var8;
      }
   }

   static SerialByteSlice encodeUtf8Range(CharSequence var0, int var1, int var2, SerialWriterBase var3, SerialByteSlice var4) {
      byte[] var5 = var4.bytes;
      char var6 = 0;
      int var7 = var4.offset;
      int var8 = var7 + var2;

      while (true) {
         for (; var1 == var2 || (var6 = var0.charAt(var1++)) >= 128; var6 = 0) {
            if (var1 == var2 && var6 < 128) {
               var3.written = var3.written + (var7 - var4.offset);
               var4.offset = var7;
               return var4;
            }

            if (var6 < 2048) {
               if (++var8 > var5.length) {
                  var3.written = var3.written + (var7 - var4.offset);
                  var4.offset = var7;
                  return encodeAsciiChunk(var0, var1 - 1, var2, var5, var7, var5.length, var3, var4);
               }

               var5[var7++] = (byte)(192 | var6 >> 6 & 31);
               var5[var7++] = (byte)(128 | var6 >> 0 & 63);
            } else if (Character.isHighSurrogate((char)var6) && var1 < var2 && Character.isLowSurrogate(var0.charAt(var1))) {
               var8 += 3;
               if (var8 > var5.length) {
                  var3.written = var3.written + (var7 - var4.offset);
                  var4.offset = var7;
                  return encodeAsciiChunk(var0, var1 - 1, var2, var5, var7, var5.length, var3, var4);
               }

               int var9 = Character.toCodePoint((char)var6, var0.charAt(var1));
               var5[var7++] = (byte)(240 | var9 >> 18 & 7);
               var5[var7++] = (byte)(128 | var9 >> 12 & 63);
               var5[var7++] = (byte)(128 | var9 >> 6 & 63);
               var5[var7++] = (byte)(128 | var9 >> 0 & 63);
               var1++;
            } else {
               var8 += 2;
               if (var8 > var5.length) {
                  var3.written = var3.written + (var7 - var4.offset);
                  var4.offset = var7;
                  return encodeAsciiChunk(var0, var1 - 1, var2, var5, var7, var5.length, var3, var4);
               }

               var5[var7++] = (byte)(224 | var6 >> '\f' & 15);
               var5[var7++] = (byte)(128 | var6 >> 6 & 63);
               var5[var7++] = (byte)(128 | var6 >> 0 & 63);
            }
         }

         var5[var7++] = (byte)var6;
      }
   }

   public static SerialByteSlice writeUtf8Simple(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         return var2;
      } else {
         return var2.offset + var3 > var2.bytes.length
            ? encodeAsciiChunk(var0, 0, var3, var2.bytes, var2.offset, var2.bytes.length, var1, var2)
            : encodeUtf8Range(var0, 0, var3, var1, var2);
      }
   }

   public static SerialByteSlice writeUtf8Checked(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         return var2;
      }

      byte[] var4 = var2.bytes;
      int var5 = var2.offset;
      int var6 = var2.bytes.length;
      var1.written += var3;
      if (var5 + var3 > var6) {
         for (int var7 = 0; var7 < var3; var7++) {
            if (var5 == var6) {
               var2.offset = var5;
               var5 = 0;
               var6 = var1.sliceSize;
               var4 = new byte[var6];
               var2 = new SerialByteSlice(var4, 0, var2);
            }

            var4[var5++] = (byte)var0.charAt(var7);
         }
      } else {
         for (int var8 = 0; var8 < var3; var8++) {
            var4[var5++] = (byte)var0.charAt(var8);
         }
      }

      var2.offset = var5;
      return var2;
   }

   static void writeAsciiBytes(int var0, byte[] var1, int var2, boolean var3) {
      if (var3) {
         var1[var2++] = (byte)var0;
         var1[var2] = (byte)(var0 >>> 8 & 0xFF);
      } else {
         var1[var2++] = (byte)(var0 >>> 8 & 0xFF);
         var1[var2] = (byte)var0;
      }
   }

   public static SerialByteSlice writeUtf8Unchecked(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      return writeUtf8WithMode(var0, false, var1, var2);
   }

   public static SerialByteSlice writeUtf8WithMode(CharSequence var0, boolean var1, SerialWriterBase var2, SerialByteSlice var3) {
      int var4 = var2.written;
      int var5 = var0.length();
      int var6 = var3.offset + 2;
      if (var6 > var3.bytes.length) {
         var3 = new SerialByteSlice(var5 + 2 > var2.sliceSize ? var5 + 2 : var2.sliceSize, var3);
         var3.offset = 2;
         if (var5 == 0) {
            writeAsciiBytes(0, var3.bytes, 0, var1);
            var2.written += 2;
            return var3;
         } else {
            SerialByteSlice var10 = encodeUtf8Range(var0, 0, var5, var2, var3);
            writeAsciiBytes(var2.written - var4, var3.bytes, 0, var1);
            var2.written += 2;
            return var10;
         }
      } else if (var5 == 0) {
         writeAsciiBytes(0, var3.bytes, var3.offset, var1);
         var3.offset = var6;
         var2.written += 2;
         return var3;
      } else if (var6 + var5 > var3.bytes.length) {
         var3.offset = var6;
         SerialByteSlice var9 = encodeAsciiChunk(var0, 0, var5, var3.bytes, var6, var3.bytes.length, var2, var3);
         writeAsciiBytes(var2.written - var4, var3.bytes, var6 - 2, var1);
         var2.written += 2;
         return var9;
      } else {
         var3.offset = var6;
         SerialByteSlice var7 = encodeUtf8Range(var0, 0, var5, var2, var3);
         writeAsciiBytes(var2.written - var4, var3.bytes, var6 - 2, var1);
         var2.written += 2;
         return var7;
      }
   }

   private static SerialByteSlice encodeSurrogatePairs(CharSequence var0, int var1, int var2, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = var3.written;
      if (var4.offset == var4.bytes.length) {
         var4 = new SerialByteSlice(var2 + 1 > var3.sliceSize ? var2 + 1 : var3.sliceSize, var4);
         var4.offset = 1;
         SerialByteSlice var10 = encodeUtf8Range(var0, var1, var2, var3, var4);
         var4.bytes[0] = (byte)(var3.written - var5);
         var3.written++;
         return var10;
      } else {
         int var6 = var4.offset + 1;
         if (var6 + var2 > var4.bytes.length) {
            var4.offset = var6;
            byte[] var11 = var4.bytes;
            SerialByteSlice var8 = encodeAsciiChunk(var0, var1, var2, var11, var6, var11.length, var3, var4);
            var11[var6 - 1] = (byte)(var3.written - var5);
            var3.written++;
            return var8;
         } else {
            var4.offset = var6;
            SerialByteSlice var7 = encodeUtf8Range(var0, var1, var2, var3, var4);
            var4.bytes[var6 - 1] = (byte)(var3.written - var5);
            var3.written++;
            return var7;
         }
      }
   }

   private static SerialByteSlice encodeMixedRange(CharSequence var0, int var1, int var2, int var3, int var4, SerialWriterBase var5, SerialByteSlice var6) {
      int var7 = var5.written;
      int var8 = var6.offset;
      int var9 = var8 + var4;
      if (var9 > var6.bytes.length) {
         var6 = new SerialByteSlice(var2 + var4 > var5.sliceSize ? var2 + var4 : var5.sliceSize, var6);
         var8 = var6.start;
         var6.offset = var9 = var8 + var4;
         SerialByteSlice var16 = encodeUtf8Range(var0, var1, var2, var5, var6);
         int var18 = var5.written - var7;
         if (var18 < var3) {
            System.arraycopy(var6.bytes, var9, var6.bytes, var9 - 1, var6.offset - var9);
            var4--;
            var6.offset--;
         }

         for (var5.written += var4; --var4 > 0; var18 >>>= 7) {
            var6.bytes[var8++] = (byte)(var18 & 127 | 128);
         }

         var6.bytes[var8] = (byte)var18;
         return var16;
      } else if (var9 + var2 > var6.bytes.length) {
         var6.offset = var9;
         SerialByteSlice var15 = encodeAsciiChunk(var0, var1, var2, var6.bytes, var9, var6.bytes.length, var5, var6);
         int var17 = var5.written - var7;
         if (var17 < var3) {
            System.arraycopy(var6.bytes, var9, var6.bytes, var9 - 1, var6.offset - var9);
            var4--;
            var6.offset--;
         }

         for (var5.written += var4; --var4 > 0; var17 >>>= 7) {
            var6.bytes[var8++] = (byte)(var17 & 127 | 128);
         }

         var6.bytes[var8] = (byte)var17;
         return var15;
      } else {
         var6.offset = var9;
         SerialByteSlice var10 = encodeUtf8Range(var0, var1, var2, var5, var6);
         int var11 = var5.written - var7;
         if (var11 < var3) {
            System.arraycopy(var6.bytes, var9, var6.bytes, var9 - 1, var6.offset - var9);
            var4--;
            var6.offset--;
         }

         for (var5.written += var4; --var4 > 0; var11 >>>= 7) {
            var6.bytes[var8++] = (byte)(var11 & 127 | 128);
         }

         var6.bytes[var8] = (byte)var11;
         return var10;
      }
   }

   public static SerialByteSlice writeUtf8(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         if (var2.offset == var2.bytes.length) {
            var2 = new SerialByteSlice(var1.sliceSize, var2);
         }

         var2.bytes[var2.offset++] = 0;
         var1.written++;
         return var2;
      } else if (var3 < 43) {
         return encodeSurrogatePairs(var0, 0, var3, var1, var2);
      } else if (var3 < 5462) {
         return encodeMixedRange(var0, 0, var3, 128, 2, var1, var2);
      } else if (var3 < 699051) {
         return encodeMixedRange(var0, 0, var3, 16384, 3, var1, var2);
      } else {
         return var3 < 89478486 ? encodeMixedRange(var0, 0, var3, 2097152, 4, var1, var2) : encodeMixedRange(var0, 0, var3, 268435456, 5, var1, var2);
      }
   }

   public static char highSurrogate(int var0) {
      return (char)((var0 >>> 10) + 55232);
   }

   public static char lowSurrogate(int var0) {
      return (char)((var0 & 1023) + 56320);
   }
}
