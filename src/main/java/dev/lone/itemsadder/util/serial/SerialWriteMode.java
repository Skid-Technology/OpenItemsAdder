package dev.lone.itemsadder.util.serial;

import dev.lone.itemsadder.util.Base64Codec;

public enum SerialWriteMode {
   BUFFERED {
   @Override
   public SerialByteSlice beginSlice(SerialWriterBase var1, SerialByteSlice var2) {
      return new SerialByteSlice(var1.sliceSize, var2);
   }

   @Override
   public SerialByteSlice method238(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5) {
      return Base64Codec.encodeIntoSlice(var1, var2, var3, var4, var5);
   }

   @Override
   public SerialByteSlice method240(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5) {
      if (var3 == 0) {
         return var5;
      }

      var4.written += var3;
      int var6 = var5.bytes.length - var5.offset;
      if (var3 > var6) {
         if (var6 + var4.sliceSize < var3) {
            return var6 == 0
               ? new SerialByteSlice(var4.sliceSize, new SerialByteSlice(var1, var2, var2 + var3, var5))
               : new SerialByteSlice(var5, new SerialByteSlice(var1, var2, var2 + var3, var5));
         }

         System.arraycopy(var1, var2, var5.bytes, var5.offset, var6);
         var5.offset += var6;
         var5 = new SerialByteSlice(var4.sliceSize, var5);
         int var7 = var3 - var6;
         System.arraycopy(var1, var2 + var6, var5.bytes, 0, var7);
         var5.offset += var7;
         return var5;
      } else {
         System.arraycopy(var1, var2, var5.bytes, var5.offset, var3);
         var5.offset += var3;
         return var5;
      }
   }

   @Override
   public SerialByteSlice method241(byte var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written++;
      if (var3.offset == var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      var3.bytes[var3.offset++] = var1;
      return var3;
   }

   @Override
   public SerialByteSlice method246(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 2;
      if (var3.offset + 2 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      FixedWidthCodec.writeShortBE(var1, var3.bytes, var3.offset);
      var3.offset += 2;
      return var3;
   }

   @Override
   public SerialByteSlice method247(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 2;
      if (var3.offset + 2 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      FixedWidthCodec.writeShortLE(var1, var3.bytes, var3.offset);
      var3.offset += 2;
      return var3;
   }

   @Override
   public SerialByteSlice method242(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 4;
      if (var3.offset + 4 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      FixedWidthCodec.writeIntBE(var1, var3.bytes, var3.offset);
      var3.offset += 4;
      return var3;
   }

   @Override
   public SerialByteSlice method243(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      var3.written += 8;
      if (var4.offset + 8 > var4.bytes.length) {
         var4 = new SerialByteSlice(var3.sliceSize, var4);
      }

      FixedWidthCodec.writeLongBE(var1, var4.bytes, var4.offset);
      var4.offset += 8;
      return var4;
   }

   @Override
   public SerialByteSlice method248(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 4;
      if (var3.offset + 4 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      FixedWidthCodec.writeIntLE(var1, var3.bytes, var3.offset);
      var3.offset += 4;
      return var3;
   }

   @Override
   public SerialByteSlice method249(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      var3.written += 8;
      if (var4.offset + 8 > var4.bytes.length) {
         var4 = new SerialByteSlice(var3.sliceSize, var4);
      }

      FixedWidthCodec.writeLongLE(var1, var4.bytes, var4.offset);
      var4.offset += 8;
      return var4;
   }

   @Override
   public SerialByteSlice method252(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      while (true) {
         var2.written++;
         if (var3.offset == var3.bytes.length) {
            var3 = new SerialByteSlice(var2.sliceSize, var3);
         }

         if ((var1 & -128) == 0) {
            var3.bytes[var3.offset++] = (byte)var1;
            return var3;
         }

         var3.bytes[var3.offset++] = (byte)(var1 & 127 | 128);
         var1 >>>= 7;
      }
   }

   @Override
   public SerialByteSlice method253(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      while (true) {
         var3.written++;
         if (var4.offset == var4.bytes.length) {
            var4 = new SerialByteSlice(var3.sliceSize, var4);
         }

         if ((var1 & -128L) == 0L) {
            var4.bytes[var4.offset++] = (byte)var1;
            return var4;
         }

         var4.bytes[var4.offset++] = (byte)((int)var1 & 127 | 128);
         var1 >>>= 7;
      }
   }

   @Override
   public SerialByteSlice method254(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      return Utf8Codec.writeIntAsText(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method255(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      return Utf8Codec.writeLongAsText(var1, var3, var4);
   }

   @Override
   public SerialByteSlice method256(float var1, SerialWriterBase var2, SerialByteSlice var3) {
      return Utf8Codec.writeFloatAsText(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method257(double var1, SerialWriterBase var3, SerialByteSlice var4) {
      return Utf8Codec.writeDoubleAsText(var1, var3, var4);
   }

   @Override
   public SerialByteSlice method258(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return Utf8Codec.writeUtf8Checked(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method259(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return Utf8Codec.writeUtf8Simple(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method260(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return Utf8Codec.writeUtf8(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method261(CharSequence var1, boolean var2, SerialWriterBase var3, SerialByteSlice var4) {
      return Utf8Codec.writeUtf8WithMode(var1, var2, var3, var4);
   }

   },
   STREAMED {
   @Override
   public SerialByteSlice beginSlice(SerialWriterBase var1, SerialByteSlice var2) {
      var2.offset = var1.writeChunk(var2.bytes, var2.start, var2.offset - var2.start);
      return var2;
   }

   @Override
   public SerialByteSlice method238(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5) {
      return Base64Codec.encodeIntoStreamingSlice(var1, var2, var3, var4, var5);
   }

   @Override
   public SerialByteSlice method240(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5) {
      if (var3 == 0) {
         return var5;
      } else {
         var4.written += var3;
         if (var5.offset + var3 > var5.bytes.length) {
            var5.offset = var4.writeTwoChunks(var5.bytes, var5.start, var5.offset - var5.start, var1, var2, var3);
            return var5;
         } else {
            System.arraycopy(var1, var2, var5.bytes, var5.offset, var3);
            var5.offset += var3;
            return var5;
         }
      }
   }

   @Override
   public SerialByteSlice method241(byte var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written++;
      if (var3.offset == var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
      }

      var3.bytes[var3.offset++] = var1;
      return var3;
   }

   @Override
   public SerialByteSlice method246(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 2;
      if (var3.offset + 2 > var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
      }

      FixedWidthCodec.writeShortBE(var1, var3.bytes, var3.offset);
      var3.offset += 2;
      return var3;
   }

   @Override
   public SerialByteSlice method247(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 2;
      if (var3.offset + 2 > var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
      }

      FixedWidthCodec.writeShortLE(var1, var3.bytes, var3.offset);
      var3.offset += 2;
      return var3;
   }

   @Override
   public SerialByteSlice method242(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 4;
      if (var3.offset + 4 > var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
      }

      FixedWidthCodec.writeIntBE(var1, var3.bytes, var3.offset);
      var3.offset += 4;
      return var3;
   }

   @Override
   public SerialByteSlice method243(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      var3.written += 8;
      if (var4.offset + 8 > var4.bytes.length) {
         var4.offset = var3.writeChunk(var4.bytes, var4.start, var4.offset - var4.start);
      }

      FixedWidthCodec.writeLongBE(var1, var4.bytes, var4.offset);
      var4.offset += 8;
      return var4;
   }

   @Override
   public SerialByteSlice method248(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      var2.written += 4;
      if (var3.offset + 4 > var3.bytes.length) {
         var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
      }

      FixedWidthCodec.writeIntLE(var1, var3.bytes, var3.offset);
      var3.offset += 4;
      return var3;
   }

   @Override
   public SerialByteSlice method249(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      var3.written += 8;
      if (var4.offset + 8 > var4.bytes.length) {
         var4.offset = var3.writeChunk(var4.bytes, var4.start, var4.offset - var4.start);
      }

      FixedWidthCodec.writeLongLE(var1, var4.bytes, var4.offset);
      var4.offset += 8;
      return var4;
   }

   @Override
   public SerialByteSlice method252(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      while (true) {
         var2.written++;
         if (var3.offset == var3.bytes.length) {
            var3.offset = var2.writeChunk(var3.bytes, var3.start, var3.offset - var3.start);
         }

         if ((var1 & -128) == 0) {
            var3.bytes[var3.offset++] = (byte)var1;
            return var3;
         }

         var3.bytes[var3.offset++] = (byte)(var1 & 127 | 128);
         var1 >>>= 7;
      }
   }

   @Override
   public SerialByteSlice method253(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      while (true) {
         var3.written++;
         if (var4.offset == var4.bytes.length) {
            var4.offset = var3.writeChunk(var4.bytes, var4.start, var4.offset - var4.start);
         }

         if ((var1 & -128L) == 0L) {
            var4.bytes[var4.offset++] = (byte)var1;
            return var4;
         }

         var4.bytes[var4.offset++] = (byte)((int)var1 & 127 | 128);
         var1 >>>= 7;
      }
   }

   @Override
   public SerialByteSlice method254(int var1, SerialWriterBase var2, SerialByteSlice var3) {
      return SerialWriteHelper.writeVarIntSlice(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method255(long var1, SerialWriterBase var3, SerialByteSlice var4) {
      return SerialWriteHelper.writeVarLongSlice(var1, var3, var4);
   }

   @Override
   public SerialByteSlice method256(float var1, SerialWriterBase var2, SerialByteSlice var3) {
      return SerialWriteHelper.writeFloatSlice(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method257(double var1, SerialWriterBase var3, SerialByteSlice var4) {
      return SerialWriteHelper.writeDoubleSlice(var1, var3, var4);
   }

   @Override
   public SerialByteSlice method258(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return SerialWriteHelper.writeStringFallback(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method259(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return SerialWriteHelper.writeStringSlice(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method260(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3) {
      return SerialWriteHelper.writeStringEntry(var1, var2, var3);
   }

   @Override
   public SerialByteSlice method261(CharSequence var1, boolean var2, SerialWriterBase var3, SerialByteSlice var4) {
      return SerialWriteHelper.writeStringWithMode(var1, var2, var3, var4);
   }

   };

   SerialWriteMode() {
   }

   public abstract SerialByteSlice beginSlice(SerialWriterBase var1, SerialByteSlice var2);

   public final SerialByteSlice method237(byte[] var1, SerialWriterBase var2, SerialByteSlice var3) {
      return this.method238(var1, 0, var1.length, var2, var3);
   }

   public abstract SerialByteSlice method238(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5);

   public final SerialByteSlice method239(byte[] var1, SerialWriterBase var2, SerialByteSlice var3) {
      return this.method240(var1, 0, var1.length, var2, var3);
   }

   public abstract SerialByteSlice method240(byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5);

   public abstract SerialByteSlice method241(byte var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method242(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method243(long var1, SerialWriterBase var3, SerialByteSlice var4);

   public final SerialByteSlice method244(float var1, SerialWriterBase var2, SerialByteSlice var3) {
      return this.method242(Float.floatToRawIntBits(var1), var2, var3);
   }

   public final SerialByteSlice method245(double var1, SerialWriterBase var3, SerialByteSlice var4) {
      return this.method243(Double.doubleToRawLongBits(var1), var3, var4);
   }

   public abstract SerialByteSlice method246(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method247(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method248(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method249(long var1, SerialWriterBase var3, SerialByteSlice var4);

   public final SerialByteSlice method250(float var1, SerialWriterBase var2, SerialByteSlice var3) {
      return this.method248(Float.floatToRawIntBits(var1), var2, var3);
   }

   public final SerialByteSlice method251(double var1, SerialWriterBase var3, SerialByteSlice var4) {
      return this.method249(Double.doubleToRawLongBits(var1), var3, var4);
   }

   public abstract SerialByteSlice method252(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method253(long var1, SerialWriterBase var3, SerialByteSlice var4);

   public abstract SerialByteSlice method254(int var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method255(long var1, SerialWriterBase var3, SerialByteSlice var4);

   public abstract SerialByteSlice method256(float var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method257(double var1, SerialWriterBase var3, SerialByteSlice var4);

   public abstract SerialByteSlice method258(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method259(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method260(CharSequence var1, SerialWriterBase var2, SerialByteSlice var3);

   public abstract SerialByteSlice method261(CharSequence var1, boolean var2, SerialWriterBase var3, SerialByteSlice var4);
}
