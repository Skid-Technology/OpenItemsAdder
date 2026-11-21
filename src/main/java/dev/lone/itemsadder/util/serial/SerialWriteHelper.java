package dev.lone.itemsadder.util.serial;

public final class SerialWriteHelper {
   private SerialWriteHelper() {
   }

   public static SerialByteSlice writeVarIntSlice(int var0, SerialWriterBase var1, SerialByteSlice var2) {
      if (var0 == Integer.MIN_VALUE) {
         int var4 = Utf8Codec.MIN_INT_BYTES.length;
         var1.written += var4;
         if (var2.offset + var4 > var2.bytes.length) {
            var2.offset = var1.writeChunk(var2.bytes, var2.start, var2.offset - var2.start);
         }

         System.arraycopy(Utf8Codec.MIN_INT_BYTES, 0, var2.bytes, var2.offset, var4);
         var2.offset += var4;
         return var2;
      } else {
         int var3 = var0 < 0 ? Utf8Codec.intDigitCount(-var0) + 1 : Utf8Codec.intDigitCount(var0);
         var1.written += var3;
         if (var2.offset + var3 > var2.bytes.length) {
            var2.offset = var1.writeChunk(var2.bytes, var2.start, var2.offset - var2.start);
         }

         Utf8Codec.writeIntDigits(var0, var2.offset, var3, var2.bytes);
         var2.offset += var3;
         return var2;
      }
   }

   public static SerialByteSlice writeVarLongSlice(long var0, SerialWriterBase var2, SerialByteSlice var3) {
      if (var0 == Long.MIN_VALUE) {
         int var5 = Utf8Codec.MIN_LONG_BYTES.length;
         var2.written += var5;
         if (var3.offset + var5 > var3.bytes.length) {
            var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
         }

         System.arraycopy(Utf8Codec.MIN_LONG_BYTES, 0, var3.bytes, var3.offset, var5);
         var3.offset += var5;
         return var3;
      } else {
         int var4 = var0 < 0L ? Utf8Codec.longDigitCount(-var0) + 1 : Utf8Codec.longDigitCount(var0);
         var2.written += var4;
         if (var3.offset + var4 > var3.bytes.length) {
            var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
         }

         Utf8Codec.writeLongDigits(var0, var3.offset, var4, var3.bytes);
         var3.offset += var4;
         return var3;
      }
   }

   public static SerialByteSlice writeFloatSlice(float var0, SerialWriterBase var1, SerialByteSlice var2) {
      return writeStringFallback(Float.toString(var0), var1, var2);
   }

   public static SerialByteSlice writeDoubleSlice(double var0, SerialWriterBase var2, SerialByteSlice var3) {
      return writeStringFallback(Double.toString(var0), var2, var3);
   }

   public static SerialByteSlice writeStringSlice(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         return var2;
      }

      byte[] var4 = var2.bytes;
      int var5 = var4.length;
      int var6 = var2.offset;
      int var7 = 0;

      do {
         char var8 = var0.charAt(var7++);
         if (var8 < 128) {
            if (var6 == var5) {
               var1.written = var1.written + (var6 - var2.offset);
               var2.offset = var6 = var1.writeChunk(var4, var2.start, var6 - var2.start);
            }

            var4[var6++] = (byte)var8;
         } else if (var8 < 2048) {
            if (var6 + 2 > var5) {
               var1.written = var1.written + (var6 - var2.offset);
               var2.offset = var6 = var1.writeChunk(var4, var2.start, var6 - var2.start);
            }

            var4[var6++] = (byte)(192 | var8 >> 6 & 31);
            var4[var6++] = (byte)(128 | var8 >> 0 & 63);
         } else if (Character.isHighSurrogate(var8) && var7 < var3 && Character.isLowSurrogate(var0.charAt(var7))) {
            if (var6 + 4 > var4.length) {
               var1.written = var1.written + (var6 - var2.offset);
               var2.offset = var6 = var1.writeChunk(var4, var2.start, var6 - var2.start);
            }

            int var9 = Character.toCodePoint(var8, var0.charAt(var7));
            var4[var6++] = (byte)(240 | var9 >> 18 & 7);
            var4[var6++] = (byte)(128 | var9 >> 12 & 63);
            var4[var6++] = (byte)(128 | var9 >> 6 & 63);
            var4[var6++] = (byte)(128 | var9 >> 0 & 63);
            var7++;
         } else {
            if (var6 + 3 > var5) {
               var1.written = var1.written + (var6 - var2.offset);
               var2.offset = var6 = var1.writeChunk(var4, var2.start, var6 - var2.start);
            }

            var4[var6++] = (byte)(224 | var8 >> '\f' & 15);
            var4[var6++] = (byte)(128 | var8 >> 6 & 63);
            var4[var6++] = (byte)(128 | var8 >> 0 & 63);
         }
      } while (var7 < var3);

      var1.written = var1.written + (var6 - var2.offset);
      var2.offset = var6;
      return var2;
   }

   public static SerialByteSlice writeStringFallback(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         return var2;
      }

      int var4 = var2.offset;
      int var5 = var2.bytes.length;
      byte[] var6 = var2.bytes;
      var1.written += var3;
      if (var4 + var3 > var5) {
         int var7 = 0;
         int var8 = var2.start;
         int var9 = var5 - var8;
         int var10 = var5 - var4;
         int var11 = var3 - var10;

         while (var10-- > 0) {
            var6[var4++] = (byte)var0.charAt(var7++);
         }

         for (var4 = var1.writeChunk(var6, var8, var9); var11-- > 0; var6[var4++] = (byte)var0.charAt(var7++)) {
            if (var4 == var5) {
               var4 = var1.writeChunk(var6, var8, var9);
            }
         }
      } else {
         for (int var12 = 0; var12 < var3; var12++) {
            var6[var4++] = (byte)var0.charAt(var12);
         }
      }

      var2.offset = var4;
      return var2;
   }

   private static void finalizeSlice(SerialByteSlice var0, SerialWriterBase var1) {
      do {
         int var2;
         if ((var2 = var0.offset - var0.start) > 0) {
            var0.offset = var1.flushSlice(var0, var0.bytes, var0.start, var2);
         }
      } while ((var0 = var0.next) != null);
   }

   public static SerialByteSlice writeStringDirect(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      return writeStringWithMode(var0, false, var1, var2);
   }

   public static SerialByteSlice writeStringWithMode(CharSequence var0, boolean var1, SerialWriterBase var2, SerialByteSlice var3) {
      int var4 = var2.written;
      int var5 = var0.length();
      int var6 = var3.offset + 2;
      if (var6 + var5 > var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
         var6 = var3.offset + 2;
         if (var5 == 0) {
            Utf8Codec.writeAsciiBytes(0, var3.bytes, var6 - 2, var1);
            var3.offset = var6;
            var2.written += 2;
            return var3;
         }

         if (var6 + var5 > var3.bytes.length) {
            var3.offset = var6;
            SerialByteSlice var7 = Utf8Codec.encodeAsciiChunk(var0, 0, var5, var3.bytes, var6, var3.bytes.length, var2, var3);
            Utf8Codec.writeAsciiBytes(var2.written - var4, var3.bytes, var6 - 2, var1);
            var2.written += 2;
            assert var7 != var3;
            finalizeSlice(var3, var2);
            return var3;
         }
      } else if (var5 == 0) {
         Utf8Codec.writeAsciiBytes(0, var3.bytes, var6 - 2, var1);
         var3.offset = var6;
         var2.written += 2;
         return var3;
      }

      var3.offset = var6;
      SerialByteSlice var8 = Utf8Codec.encodeUtf8Range(var0, 0, var5, var2, var3);
      Utf8Codec.writeAsciiBytes(var2.written - var4, var3.bytes, var6 - 2, var1);
      var2.written += 2;
      if (var8 != var3) {
         finalizeSlice(var3, var2);
      }

      return var3;
   }

   private static SerialByteSlice encodeStringRange(CharSequence var0, int var1, int var2, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = var3.written;
      int var6 = var4.offset + 1;
      if (var6 + var2 > var4.bytes.length) {
         var4.offset = var3.writeChunk(var4.bytes, var4.start, var4.offset - var4.start);
         var6 = var4.offset + 1;
      }

      var4.offset = var6;
      SerialByteSlice var7 = Utf8Codec.encodeUtf8Range(var0, var1, var2, var3, var4);
      var4.bytes[var6 - 1] = (byte)(var3.written - var5);
      var3.written++;
      if (var7 != var4) {
         finalizeSlice(var4, var3);
      }

      return var4;
   }

   private static SerialByteSlice encodeStringMixed(CharSequence var0, int var1, int var2, int var3, int var4, SerialWriterBase var5, SerialByteSlice var6) {
      int var7 = var5.written;
      int var8 = var6.offset;
      int var9 = var8 + var4;
      if (var9 + var2 > var6.bytes.length) {
         var8 = var5.writeChunk(var6.bytes, var6.start, var6.offset - var6.start);
         var9 = var8 + var4;
         if (var9 + var2 > var6.bytes.length) {
            var6.offset = var9;
            SerialByteSlice var18 = Utf8Codec.encodeAsciiChunk(var0, var1, var2, var6.bytes, var9, var6.bytes.length, var5, var6);
            int var19 = var5.written - var7;
            if (var19 < var3) {
               var5.written = var5.written + --var4;
               int var20 = ++var8;

               while (--var4 > 0) {
                  var6.bytes[var20++] = (byte)(var19 & 127 | 128);
                  var19 >>>= 7;
               }

               var6.bytes[var20] = (byte)var19;
               var6.offset = var5.flushSlice(var6, var6.bytes, var8, var6.offset - var8);
               assert var18 != var6;
               finalizeSlice(var6.next, var5);
               return var6;
            }

            for (var5.written += var4; --var4 > 0; var19 >>>= 7) {
               var6.bytes[var8++] = (byte)(var19 & 127 | 128);
            }

            var6.bytes[var8] = (byte)var19;
            assert var18 != var6;
            finalizeSlice(var6, var5);
            return var6;
         }
      }

      var6.offset = var9;
      SerialByteSlice var10 = Utf8Codec.encodeUtf8Range(var0, var1, var2, var5, var6);
      int var11 = var5.written - var7;
      if (var11 < var3) {
         if (var10 != var6 || var4 != 2) {
            var5.written = var5.written + --var4;
            int var12 = var8++;
            int var13 = var8;

            while (--var4 > 0) {
               var6.bytes[var13++] = (byte)(var11 & 127 | 128);
               var11 >>>= 7;
            }

            var6.bytes[var13] = (byte)var11;
            if (var12 == var6.start) {
               var6.offset = var5.flushSlice(var6, var6.bytes, var8, var6.offset - var8);
            } else {
               var6.offset = var5.writeTwoChunks(var6.bytes, var6.start, var12 - var6.start, var6.bytes, var8, var6.offset - var8);
            }

            if (var10 != var6) {
               finalizeSlice(var6.next, var5);
            }

            return var6;
         }

         System.arraycopy(var6.bytes, var9, var6.bytes, var9 - 1, var6.offset - var9);
         var4--;
         var6.offset--;
      }

      for (var5.written += var4; --var4 > 0; var11 >>>= 7) {
         var6.bytes[var8++] = (byte)(var11 & 127 | 128);
      }

      var6.bytes[var8] = (byte)var11;
      if (var10 != var6) {
         finalizeSlice(var6, var5);
      }

      return var6;
   }

   public static SerialByteSlice writeStringEntry(CharSequence var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = var0.length();
      if (var3 == 0) {
         if (var2.offset == var2.bytes.length) {
            var2.offset = var1.writeChunk(var2.bytes, var2.start, var2.offset - var2.start);
         }

         var2.bytes[var2.offset++] = 0;
         var1.written++;
         return var2;
      } else if (var3 < 43) {
         return encodeStringRange(var0, 0, var3, var1, var2);
      } else if (var3 < 5462) {
         return encodeStringMixed(var0, 0, var3, 128, 2, var1, var2);
      } else if (var3 < 699051) {
         return encodeStringMixed(var0, 0, var3, 16384, 3, var1, var2);
      } else {
         return var3 < 89478486 ? encodeStringMixed(var0, 0, var3, 2097152, 4, var1, var2) : encodeStringMixed(var0, 0, var3, 268435456, 5, var1, var2);
      }
   }
}
