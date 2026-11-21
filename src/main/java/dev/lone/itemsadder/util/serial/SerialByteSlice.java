package dev.lone.itemsadder.util.serial;

import java.io.DataOutput;
import java.io.OutputStream;

public final class SerialByteSlice {
   public static final int MIN_SIZE = 256;
   public static final int DEFAULT_SIZE = 512;
   final byte[] bytes;
   final int start;
   int offset;
   SerialByteSlice next;

   public static SerialByteSlice createDefault() {
      return new SerialByteSlice(512);
   }

   public static SerialByteSlice create(int var0) {
      if (var0 < 256) {
         throw new IllegalArgumentException("256 is the minimum buffer size.");
      } else {
         return new SerialByteSlice(var0);
      }
   }

   public static SerialByteSlice createLinked(int var0, SerialByteSlice var1) {
      if (var0 < 256) {
         throw new IllegalArgumentException("256 is the minimum buffer size.");
      } else {
         return new SerialByteSlice(var0, var1);
      }
   }

   public static SerialByteSlice wrapRange(byte[] var0, int var1, int var2) {
      return new SerialByteSlice(var0, var1, var1 + var2);
   }

   public static SerialByteSlice wrap(byte[] var0) {
      return wrapAt(var0, 0);
   }

   public static SerialByteSlice wrapAt(byte[] var0, int var1) {
      assert var1 >= 0;
      if (var0.length - var1 < 256) {
         throw new IllegalArgumentException("256 is the minimum buffer size.");
      } else {
         return new SerialByteSlice(var0, var1, var1);
      }
   }

   public static int writeAllTo(OutputStream var0, SerialByteSlice var1) {
      int var2 = 0;

      do {
         int var3;
         if ((var3 = var1.offset - var1.start) > 0) {
            var0.write(var1.bytes, var1.start, var3);
            var2 += var3;
         }
      } while ((var1 = var1.next) != null);

      return var2;
   }

   public static int writeAllToData(DataOutput var0, SerialByteSlice var1) {
      int var2 = 0;

      do {
         int var3;
         if ((var3 = var1.offset - var1.start) > 0) {
            var0.write(var1.bytes, var1.start, var3);
            var2 += var3;
         }
      } while ((var1 = var1.next) != null);

      return var2;
   }

   SerialByteSlice(int var1) {
      this(new byte[var1], 0, 0);
   }

   SerialByteSlice(int var1, SerialByteSlice var2) {
      this(new byte[var1], 0, 0, var2);
   }

   SerialByteSlice(byte[] var1, int var2) {
      this(var1, var2, var2);
   }

   SerialByteSlice(byte[] var1, int var2, int var3) {
      this.bytes = var1;
      this.start = var2;
      this.offset = var3;
   }

   SerialByteSlice(byte[] var1, int var2, SerialByteSlice var3) {
      this(var1, var2, var2);
      var3.next = this;
   }

   SerialByteSlice(byte[] var1, int var2, int var3, SerialByteSlice var4) {
      this(var1, var2, var3);
      var4.next = this;
   }

   SerialByteSlice(SerialByteSlice var1, SerialByteSlice var2) {
      this.bytes = var1.bytes;
      this.offset = this.start = var1.offset;
      var2.next = this;
   }

   public SerialByteSlice rewind() {
      this.next = null;
      this.offset = this.start;
      return this;
   }
}
