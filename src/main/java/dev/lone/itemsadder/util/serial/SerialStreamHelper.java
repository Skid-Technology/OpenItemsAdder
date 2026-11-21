package dev.lone.itemsadder.util.serial;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class SerialStreamHelper {
   private SerialStreamHelper() {
   }

   static void readFromArrayRange(byte[] var0, int var1, int var2, Object var3, SerialType var4, boolean var5) {
      try {
         ByteArraySerialReader var6 = new ByteArraySerialReader(var0, var1, var2, var5);
         var4.read(var6, var3);
         var6.expectTag(0);
      } catch (ArrayIndexOutOfBoundsException var7) {
         throw new RuntimeException("Truncated.", SerialParseException.wrap(var7));
      } catch (IOException var8) {
         throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", var8);
      }
   }

   static void readFromStreamBuffered(InputStream var0, byte[] var1, Object var2, SerialType var3, boolean var4) {
      StreamSerialReader var5 = new StreamSerialReader(var0, var1, var4);
      var3.read(var5, var2);
      var5.expectTag(0);
   }

   static void readFromStreamDirect(InputStream var0, Object var1, SerialType var2, boolean var3) {
      StreamSerialReader var4 = new StreamSerialReader(var0, var3);
      var2.read(var4, var1);
      var4.expectTag(0);
   }

   static int readFromStreamBufferedCount(InputStream var0, byte[] var1, Object var2, SerialType var3, boolean var4) {
      int var5 = var0.read();
      if (var5 == -1) {
         throw new EOFException("mergeDelimitedFrom");
      }

      int var6 = var5 < 128 ? var5 : StreamSerialReader.readByteOrEnd(var0, var5);
      if (var6 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var6 != 0) {
         if (var6 > var1.length) {
            throw new SerialParseException("size limit exceeded. " + var6 + " > " + var1.length);
         }

         readFully(var0, var1, 0, var6);
         ByteArraySerialReader var7 = new ByteArraySerialReader(var1, 0, var6, var4);

         try {
            var3.read(var7, var2);
         } catch (ArrayIndexOutOfBoundsException var9) {
            throw SerialParseException.wrap(var9);
         }

         var7.expectTag(0);
      }

      return var6;
   }

   static int readFromStreamDirectCount(InputStream var0, Object var1, SerialType var2, boolean var3) {
      int var4 = var0.read();
      if (var4 == -1) {
         throw new EOFException("mergeDelimitedFrom");
      }

      int var5 = var4 < 128 ? var4 : StreamSerialReader.readByteOrEnd(var0, var4);
      if (var5 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var5 != 0) {
         if (var5 > 4096) {
            StreamSerialReader var10 = new StreamSerialReader(new LimitedInputStream(var0, var5), var3);
            var2.read(var10, var1);
            var10.expectTag(0);
            return var5;
         }

         byte[] var6 = new byte[var5];
         readFully(var0, var6, 0, var5);
         ByteArraySerialReader var7 = new ByteArraySerialReader(var6, 0, var5, var3);

         try {
            var2.read(var7, var1);
         } catch (ArrayIndexOutOfBoundsException var9) {
            throw SerialParseException.wrap(var9);
         }

         var7.expectTag(0);
      }

      return var5;
   }

   static int readFromDataInputCount(DataInput var0, Object var1, SerialType var2, boolean var3) {
      byte var4 = var0.readByte();
      int var5 = 0 == (var4 & 128) ? var4 : StreamSerialReader.readVarIntFirstByte(var0, var4);
      if (var5 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var5 != 0) {
         if (var5 > 4096 && var0 instanceof InputStream) {
            StreamSerialReader var10 = new StreamSerialReader(new LimitedInputStream((InputStream)var0, var5), var3);
            var2.read(var10, var1);
            var10.expectTag(0);
         } else {
            byte[] var6 = new byte[var5];
            var0.readFully(var6, 0, var5);
            ByteArraySerialReader var7 = new ByteArraySerialReader(var6, 0, var5, var3);

            try {
               var2.read(var7, var1);
            } catch (ArrayIndexOutOfBoundsException var9) {
               throw SerialParseException.wrap(var9);
            }

            var7.expectTag(0);
         }
      }

      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      } else {
         return var5;
      }
   }

   static void readFully(InputStream var0, byte[] var1, int var2, int var3) {
      int var4 = 0;

      while (var3 > 0) {
         var4 = var0.read(var1, var2, var3);
         if (var4 == -1) {
            throw SerialParseException.afj();
         }

         var3 -= var4;
         var2 += var4;
      }
   }

   static int readLengthPrefix(InputStream var0, boolean var1, SerialByteSlice var2) {
      byte[] var3 = var2.bytes;
      int var4 = var2.start;
      int var5 = var3.length - var4;
      int var6 = var0.read(var3, var4, var5);
      if (var6 < 1) {
         throw new EOFException("fillBufferWithDelimitedMessageFrom");
      }

      int var7 = var4 + var6;
      int var8 = var3[var4++];
      if (0 != (var8 & 128)) {
         var8 &= 127;
         byte var9 = 7;

         while (true) {
            if (var4 == var7) {
               var6 = var0.read(var3, var7, var5 - (var7 - var2.start));
               if (var6 < 1) {
                  throw new EOFException("fillBufferWithDelimitedMessageFrom");
               }

               var7 += var6;
            }

            byte var10 = var3[var4++];
            var8 |= (var10 & 127) << var9;
            if (0 == (var10 & 128)) {
               break;
            }

            if (var9 == 28) {
               int var11 = 0;

               while (true) {
                  if (var4 == var7) {
                     var6 = var0.read(var3, var7, var5 - (var7 - var2.start));
                     if (var6 < 1) {
                        throw new EOFException("fillBufferWithDelimitedMessageFrom");
                     }

                     var7 += var6;
                  }

                  if (var3[var4++] >= 0) {
                     break;
                  }

                  if (5 == ++var11) {
                     throw SerialParseException.malformedVarint();
                  }
               }
            }

            var9 += 7;
         }
      }

      if (var8 == 0) {
         if (var4 != var7) {
            throw SerialParseException.truncatedMessage();
         } else {
            return var8;
         }
      } else {
         if (var8 < 0) {
            throw SerialParseException.negativeSize();
         }

         int var16 = var7 - var4;
         if (var16 < var8) {
            int var17 = var4 - var2.start;
            if (var8 + var17 > var5) {
               if (!var1) {
                  return var8;
               }

               int var18 = var8 - var16;

               while (var18 > 0) {
                  var6 = var0.read(var3, var2.start, Math.min(var18, var5));
                  if (var6 < 1) {
                     throw new EOFException("fillBufferWithDelimitedMessageFrom");
                  }

                  var18 -= var6;
               }

               return var8;
            }

            readFully(var0, var3, var7, var8 - var16);
         }

         var2.offset = var4;
         return var8;
      }
   }

   static int encodeVarIntInto(int var0, byte[] var1, int var2) {
      switch (SerialByteSliceWriter.varIntSize(var0)) {
         case 1:
            var1[var2 + 4] = (byte)var0;
            return var2 + 4;
         case 2:
            var1[var2 + 3] = (byte)(var0 & 127 | 128);
            var1[var2 + 4] = (byte)(var0 >>> 7);
            return var2 + 3;
         case 3:
            var1[var2 + 2] = (byte)(var0 & 127 | 128);
            var1[var2 + 3] = (byte)(var0 >>> 7 & 127 | 128);
            var1[var2 + 4] = (byte)(var0 >>> 14);
            return var2 + 2;
         case 4:
            var1[var2 + 1] = (byte)(var0 & 127 | 128);
            var1[var2 + 2] = (byte)(var0 >>> 7 & 127 | 128);
            var1[var2 + 3] = (byte)(var0 >>> 14 & 127 | 128);
            var1[var2 + 4] = (byte)(var0 >>> 21);
            return var2 + 1;
         default:
            var1[var2] = (byte)(var0 & 127 | 128);
            var1[var2 + 1] = (byte)(var0 >>> 7 & 127 | 128);
            var1[var2 + 2] = (byte)(var0 >>> 14 & 127 | 128);
            var1[var2 + 3] = (byte)(var0 >>> 21 & 127 | 128);
            var1[var2 + 4] = (byte)(var0 >>> 28);
            return var2;
      }
   }
}
