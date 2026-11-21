package dev.lone.itemsadder.util.serial;

import java.io.DataOutput;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public final class SerialByteSliceWriter extends SerialWriterBase implements SerialWriter {
   public static final int FIXED32_SIZE = 4;
   public static final int FIXED64_SIZE = 8;

   public SerialByteSliceWriter(SerialByteSlice var1) {
      super(var1);
   }

   public SerialByteSliceWriter(SerialByteSlice var1, int var2) {
      super(var1, var2);
   }

   public SerialByteSliceWriter restartWriter() {
      super.restart();
      return this;
   }

   @Override
   public void writeInt(int var1, int var2, boolean var3) {
      if (var2 < 0) {
         this.cursor = writeVarLongInto(WireTypes.makeTag(var1, 0), var2, this, this.cursor);
      } else {
         this.cursor = writeVarIntInto(WireTypes.makeTag(var1, 0), var2, this, this.cursor);
      }
   }

   @Override
   public void writeVarInt(int var1, int var2, boolean var3) {
      this.cursor = writeVarIntInto(WireTypes.makeTag(var1, 0), var2, this, this.cursor);
   }

   @Override
   public void writeSignedVarInt(int var1, int var2, boolean var3) {
      this.cursor = writeVarIntInto(WireTypes.makeTag(var1, 0), zigZag32(var2), this, this.cursor);
   }

   @Override
   public void writeFixed32(int var1, int var2, boolean var3) {
      this.cursor = writeFixed32Into(WireTypes.makeTag(var1, 5), var2, this, this.cursor);
   }

   @Override
   public void writeSFixed32(int var1, int var2, boolean var3) {
      this.cursor = writeFixed32Into(WireTypes.makeTag(var1, 5), var2, this, this.cursor);
   }

   @Override
   public void writeVarLong(int var1, long var2, boolean var4) {
      this.cursor = writeVarLongInto(WireTypes.makeTag(var1, 0), var2, this, this.cursor);
   }

   @Override
   public void writeInt64(int var1, long var2, boolean var4) {
      this.cursor = writeVarLongInto(WireTypes.makeTag(var1, 0), var2, this, this.cursor);
   }

   @Override
   public void writeSignedVarLong(int var1, long var2, boolean var4) {
      this.cursor = writeVarLongInto(WireTypes.makeTag(var1, 0), zigZag64(var2), this, this.cursor);
   }

   @Override
   public void writeFixed64(int var1, long var2, boolean var4) {
      this.cursor = writeFixed64Into(WireTypes.makeTag(var1, 1), var2, this, this.cursor);
   }

   @Override
   public void writeSFixed64(int var1, long var2, boolean var4) {
      this.cursor = writeFixed64Into(WireTypes.makeTag(var1, 1), var2, this, this.cursor);
   }

   @Override
   public void writeFloat(int var1, float var2, boolean var3) {
      this.cursor = writeFixed32Into(WireTypes.makeTag(var1, 5), Float.floatToRawIntBits(var2), this, this.cursor);
   }

   @Override
   public void writeDouble(int var1, double var2, boolean var4) {
      this.cursor = writeFixed64Into(WireTypes.makeTag(var1, 1), Double.doubleToRawLongBits(var2), this, this.cursor);
   }

   @Override
   public void writeBool(int var1, boolean var2, boolean var3) {
      this.cursor = writeVarIntInto(WireTypes.makeTag(var1, 0), var2 ? 1 : 0, this, this.cursor);
   }

   @Override
   public void writeEnum(int var1, int var2, boolean var3) {
      this.writeInt(var1, var2, var3);
   }

   @Override
   public void writeString(int var1, CharSequence var2, boolean var3) {
      this.cursor = Utf8Codec.writeUtf8(var2, this, encodeVarIntInto(WireTypes.makeTag(var1, 2), this, this.cursor));
   }

   @Override
   public void writeSerialBytes(int var1, SerialBytes var2, boolean var3) {
      this.writeBytes(var1, var2.getBytes(), var3);
   }

   @Override
   public void writeBytes(int var1, byte[] var2, boolean var3) {
      this.cursor = writeBytesInto(WireTypes.makeTag(var1, 2), var2, 0, var2.length, this, this.cursor);
   }

   @Override
   public void writeBytesRange(boolean var1, int var2, byte[] var3, int var4, int var5, boolean var6) {
      this.cursor = writeBytesInto(WireTypes.makeTag(var2, 2), var3, var4, var5, this, this.cursor);
   }

   @Override
   public void writeObject(int var1, Object var2, SerialType var3, boolean var4) {
      SerialByteSlice var5;
      if (var1 < 16 && this.cursor.offset != this.cursor.bytes.length) {
         var5 = this.cursor;
         this.written++;
         var5.bytes[var5.offset++] = (byte)WireTypes.makeTag(var1, 2);
      } else {
         this.cursor = var5 = encodeVarIntInto(WireTypes.makeTag(var1, 2), this, this.cursor);
      }

      int var6 = this.cursor.offset;
      int var7 = this.written;
      if (var6 == var5.bytes.length) {
         SerialByteSlice var11 = new SerialByteSlice(this.sliceSize);
         this.cursor = var11;
         var3.write(this, var2);
         int var12 = this.written - var7;
         byte[] var13 = new byte[varIntSize(var12)];
         writeVarIntBytes(var12, var13, 0);
         this.written += var13.length;
         (new SerialByteSlice(var13, 0, var13.length, var5)).next = var11;
      } else {
         var5.offset++;
         this.written++;
         var3.write(this, var2);
         int var8 = this.written - var7 - 1;
         if (var8 < 128) {
            var5.bytes[var6] = (byte)var8;
         } else {
            SerialByteSlice var9 = new SerialByteSlice(var5.bytes, var6 + 1, var5.offset);
            if (var5 == this.cursor) {
               this.cursor = var9;
            } else {
               var9.next = var5.next;
            }

            var5.offset = var6;
            byte[] var10 = new byte[varIntSize(var8)];
            writeVarIntBytes(var8, var10, 0);
            this.written += var10.length - 1;
            (new SerialByteSlice(var10, 0, var10.length, var5)).next = var9;
         }
      }
   }

   public static SerialByteSlice encodeVarIntInto(int var0, SerialWriterBase var1, SerialByteSlice var2) {
      int var3 = varIntSize(var0);
      if (var2.offset + var3 > var2.bytes.length) {
         var2 = new SerialByteSlice(var1.sliceSize, var2);
      }

      byte[] var4 = var2.bytes;
      int var5 = var2.offset;
      var2.offset += var3;
      var1.written += var3;
      if (var3 == 1) {
         var4[var5] = (byte)var0;
      } else {
         int var6 = 0;

         for (int var7 = var3 - 1; var6 < var7; var0 >>>= 7) {
            var4[var5++] = (byte)(var0 & 127 | 128);
            var6++;
         }

         var4[var5] = (byte)var0;
      }

      return var2;
   }

   public static SerialByteSlice writeLengthAndLink(int var0, SerialByteSlice var1, SerialWriterBase var2, SerialByteSlice var3) {
      int var4 = var1.offset - var1.start;
      if (var4 == 0) {
         return writeVarIntInto(var0, var4, var2, var3);
      }

      var3 = writeVarIntInto(var0, var4, var2, var3);
      var3.next = var1;
      int var5 = var3.bytes.length - var3.offset;
      return var5 == 0 ? new SerialByteSlice(var2.sliceSize, var1) : new SerialByteSlice(var3, var1);
   }

   public static SerialByteSlice writeBytesInto(int var0, byte[] var1, int var2, int var3, SerialWriterBase var4, SerialByteSlice var5) {
      if (var3 == 0) {
         return writeVarIntInto(var0, var3, var4, var5);
      }

      var5 = writeVarIntInto(var0, var3, var4, var5);
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

   public static SerialByteSlice writeVarIntInto(int var0, int var1, SerialWriterBase var2, SerialByteSlice var3) {
      int var4 = varIntSize(var0);
      int var5 = varIntSize(var1);
      int var6 = var4 + var5;
      if (var3.offset + var6 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      byte[] var7 = var3.bytes;
      int var8 = var3.offset;
      var3.offset += var6;
      var2.written += var6;
      if (var4 == 1) {
         var7[var8++] = (byte)var0;
      } else {
         int var9 = 0;

         for (int var10 = var4 - 1; var9 < var10; var0 >>>= 7) {
            var7[var8++] = (byte)(var0 & 127 | 128);
            var9++;
         }

         var7[var8++] = (byte)var0;
      }

      if (var5 == 1) {
         var7[var8] = (byte)var1;
      } else {
         int var12 = 0;

         for (int var13 = var5 - 1; var12 < var13; var1 >>>= 7) {
            var7[var8++] = (byte)(var1 & 127 | 128);
            var12++;
         }

         var7[var8] = (byte)var1;
      }

      return var3;
   }

   public static SerialByteSlice writeVarLongInto(int var0, long var1, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = varIntSize(var0);
      int var6 = varLongSize(var1);
      int var7 = var5 + var6;
      if (var4.offset + var7 > var4.bytes.length) {
         var4 = new SerialByteSlice(var3.sliceSize, var4);
      }

      byte[] var8 = var4.bytes;
      int var9 = var4.offset;
      var4.offset += var7;
      var3.written += var7;
      if (var5 == 1) {
         var8[var9++] = (byte)var0;
      } else {
         int var10 = 0;

         for (int var11 = var5 - 1; var10 < var11; var0 >>>= 7) {
            var8[var9++] = (byte)(var0 & 127 | 128);
            var10++;
         }

         var8[var9++] = (byte)var0;
      }

      if (var6 == 1) {
         var8[var9] = (byte)var1;
      } else {
         int var13 = 0;

         for (int var14 = var6 - 1; var13 < var14; var1 >>>= 7) {
            var8[var9++] = (byte)((int)var1 & 127 | 128);
            var13++;
         }

         var8[var9] = (byte)var1;
      }

      return var4;
   }

   public static SerialByteSlice writeFixed32Into(int var0, int var1, SerialWriterBase var2, SerialByteSlice var3) {
      int var4 = varIntSize(var0);
      int var5 = var4 + 4;
      if (var3.offset + var5 > var3.bytes.length) {
         var3 = new SerialByteSlice(var2.sliceSize, var3);
      }

      byte[] var6 = var3.bytes;
      int var7 = var3.offset;
      var3.offset += var5;
      var2.written += var5;
      if (var4 == 1) {
         var6[var7++] = (byte)var0;
      } else {
         int var8 = 0;

         for (int var9 = var4 - 1; var8 < var9; var0 >>>= 7) {
            var6[var7++] = (byte)(var0 & 127 | 128);
            var8++;
         }

         var6[var7++] = (byte)var0;
      }

      writeVarIntAt(var1, var6, var7);
      return var3;
   }

   public static SerialByteSlice writeFixed64Into(int var0, long var1, SerialWriterBase var3, SerialByteSlice var4) {
      int var5 = varIntSize(var0);
      int var6 = var5 + 8;
      if (var4.offset + var6 > var4.bytes.length) {
         var4 = new SerialByteSlice(var3.sliceSize, var4);
      }

      byte[] var7 = var4.bytes;
      int var8 = var4.offset;
      var4.offset += var6;
      var3.written += var6;
      if (var5 == 1) {
         var7[var8++] = (byte)var0;
      } else {
         int var9 = 0;

         for (int var10 = var5 - 1; var9 < var10; var0 >>>= 7) {
            var7[var8++] = (byte)(var0 & 127 | 128);
            var9++;
         }

         var7[var8++] = (byte)var0;
      }

      writeVarLongAt(var1, var7, var8);
      return var4;
   }

   public static void writeVarIntBytes(int var0, byte[] var1, int var2) {
      while ((var0 & -128) != 0) {
         var1[var2++] = (byte)(var0 & 127 | 128);
         var0 >>>= 7;
      }

      var1[var2] = (byte)var0;
   }

   public static void writeVarIntToStream(OutputStream var0, int var1) {
      while ((var1 & -128) != 0) {
         var0.write(var1 & 127 | 128);
         var1 >>>= 7;
      }

      var0.write(var1);
   }

   public static void writeVarIntToData(DataOutput var0, int var1) {
      while ((var1 & -128) != 0) {
         var0.write(var1 & 127 | 128);
         var1 >>>= 7;
      }

      var0.write(var1);
   }

   public static byte[] varIntBytes(int var0, int var1) {
      int var2 = varIntSize(var0);
      int var3 = varIntSize(var1);
      int var4 = 0;
      byte[] var5 = new byte[var2 + var3];
      if (var2 == 1) {
         var5[var4++] = (byte)var0;
      } else {
         int var6 = 0;

         for (int var7 = var2 - 1; var6 < var7; var0 >>>= 7) {
            var5[var4++] = (byte)(var0 & 127 | 128);
            var6++;
         }

         var5[var4++] = (byte)var0;
      }

      if (var3 == 1) {
         var5[var4] = (byte)var1;
      } else {
         int var9 = 0;

         for (int var10 = var3 - 1; var9 < var10; var1 >>>= 7) {
            var5[var4++] = (byte)(var1 & 127 | 128);
            var9++;
         }

         var5[var4] = (byte)var1;
      }

      return var5;
   }

   public static byte[] varLongBytes(int var0, long var1) {
      int var3 = varIntSize(var0);
      int var4 = varLongSize(var1);
      int var5 = 0;
      byte[] var6 = new byte[var3 + var4];
      if (var3 == 1) {
         var6[var5++] = (byte)var0;
      } else {
         int var7 = 0;

         for (int var8 = var3 - 1; var7 < var8; var0 >>>= 7) {
            var6[var5++] = (byte)(var0 & 127 | 128);
            var7++;
         }

         var6[var5++] = (byte)var0;
      }

      if (var4 == 1) {
         var6[var5] = (byte)var1;
      } else {
         int var10 = 0;

         for (int var11 = var4 - 1; var10 < var11; var1 >>>= 7) {
            var6[var5++] = (byte)((int)var1 & 127 | 128);
            var10++;
         }

         var6[var5] = (byte)var1;
      }

      return var6;
   }

   public static byte[] fixed32Bytes(int var0, int var1) {
      int var2 = varIntSize(var0);
      int var3 = 0;
      byte[] var4 = new byte[var2 + 4];
      if (var2 == 1) {
         var4[var3++] = (byte)var0;
      } else {
         int var5 = 0;

         for (int var6 = var2 - 1; var5 < var6; var0 >>>= 7) {
            var4[var3++] = (byte)(var0 & 127 | 128);
            var5++;
         }

         var4[var3++] = (byte)var0;
      }

      writeVarIntAt(var1, var4, var3);
      return var4;
   }

   public static byte[] fixed64Bytes(int var0, long var1) {
      int var3 = varIntSize(var0);
      int var4 = 0;
      byte[] var5 = new byte[var3 + 8];
      if (var3 == 1) {
         var5[var4++] = (byte)var0;
      } else {
         int var6 = 0;

         for (int var7 = var3 - 1; var6 < var7; var0 >>>= 7) {
            var5[var4++] = (byte)(var0 & 127 | 128);
            var6++;
         }

         var5[var4++] = (byte)var0;
      }

      writeVarLongAt(var1, var5, var4);
      return var5;
   }

   public static int writeVarIntAt(int var0, byte[] var1, int var2) {
      if (var1.length - var2 < 4) {
         throw new IllegalArgumentException("buffer capacity not enough.");
      }

      var1[var2++] = (byte)(var0 & 0xFF);
      var1[var2++] = (byte)(var0 >> 8 & 0xFF);
      var1[var2++] = (byte)(var0 >> 16 & 0xFF);
      var1[var2] = (byte)(var0 >> 24 & 0xFF);
      return 4;
   }

   public static int writeVarLongAt(long var0, byte[] var2, int var3) {
      if (var2.length - var3 < 8) {
         throw new IllegalArgumentException("buffer capacity not enough.");
      }

      var2[var3++] = (byte)(var0 & 255L);
      var2[var3++] = (byte)(var0 >> 8 & 255L);
      var2[var3++] = (byte)(var0 >> 16 & 255L);
      var2[var3++] = (byte)(var0 >> 24 & 255L);
      var2[var3++] = (byte)(var0 >> 32 & 255L);
      var2[var3++] = (byte)(var0 >> 40 & 255L);
      var2[var3++] = (byte)(var0 >> 48 & 255L);
      var2[var3] = (byte)(var0 >> 56 & 255L);
      return 8;
   }

   public static byte[] encodeVarIntBytes(int var0) {
      int var1 = varIntSize(var0);
      if (var1 == 1) {
         return new byte[]{(byte)var0};
      }

      int var2 = 0;
      byte[] var3 = new byte[var1];
      int var4 = 0;

      for (int var5 = var1 - 1; var4 < var5; var0 >>>= 7) {
         var3[var2++] = (byte)(var0 & 127 | 128);
         var4++;
      }

      var3[var2] = (byte)var0;
      return var3;
   }

   public static int varIntSize(int var0) {
      if ((var0 & -128) == 0) {
         return 1;
      } else if ((var0 & -16384) == 0) {
         return 2;
      } else if ((var0 & -2097152) == 0) {
         return 3;
      } else {
         return (var0 & -268435456) == 0 ? 4 : 5;
      }
   }

   public static int varLongSize(long var0) {
      if ((var0 & -128L) == 0L) {
         return 1;
      } else if ((var0 & -16384L) == 0L) {
         return 2;
      } else if ((var0 & -2097152L) == 0L) {
         return 3;
      } else if ((var0 & -268435456L) == 0L) {
         return 4;
      } else if ((var0 & -34359738368L) == 0L) {
         return 5;
      } else if ((var0 & -4398046511104L) == 0L) {
         return 6;
      } else if ((var0 & -562949953421312L) == 0L) {
         return 7;
      } else if ((var0 & -72057594037927936L) == 0L) {
         return 8;
      } else {
         return (var0 & Long.MIN_VALUE) == 0L ? 9 : 10;
      }
   }

   public static int zigZag32(int var0) {
      return var0 << 1 ^ var0 >> 31;
   }

   public static long zigZag64(long var0) {
      return var0 << 1 ^ var0 >> 63;
   }

   @Override
   public void writeByteBuffer(int var1, ByteBuffer var2, boolean var3) {
      this.writeBytesRange(false, var1, var2.array(), var2.arrayOffset() + var2.position(), var2.remaining(), var3);
   }
}
