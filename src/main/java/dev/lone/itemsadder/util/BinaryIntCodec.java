package dev.lone.itemsadder.util;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class BinaryIntCodec {
   public static byte[] toBytes(int var0) {
      return encodeInt(var0);
   }

   public static int decodeInt(byte[] var0) {
      return var0[3] & 0xFF | (var0[2] & 0xFF) << 8 | (var0[1] & 0xFF) << 16 | (var0[0] & 0xFF) << 24;
   }

   public static byte[] encodeInt(int var0) {
      return new byte[]{(byte)(var0 >> 24 & 0xFF), (byte)(var0 >> 16 & 0xFF), (byte)(var0 >> 8 & 0xFF), (byte)(var0 & 0xFF)};
   }

   public static int readInt(RandomAccessFile var0) {
      byte[] var1 = new byte[4];
      var0.read(var1);
      return decodeInt(var1);
   }

   public static String readString(RandomAccessFile var0, int var1) {
      byte[] var2 = new byte[var1];
      var0.read(var2);
      return new String(var2);
   }

   public static String readLengthPrefixedString(RandomAccessFile var0, long var1) {
      int var3 = readInt(var0);
      if (var3 <= var1 && var3 >= 0) {
         return readString(var0, var3);
      } else {
         throw new IOException("String length is invalid! wtf? " + var3);
      }
   }

   public static PackedByteStream readRange(RandomAccessFile var0, int var1, int var2, int var3) {
      var0.seek(var1 + var2);
      byte[] var4 = new byte[Math.min(16384, var3)];
      PackedByteStream var5 = new PackedByteStream(var4.length);

      while (var0.getFilePointer() < var1 + var2 + var3) {
         int var6 = var3 - var5.size();
         int var7 = var0.read(var4, 0, Math.min(var6, var4.length));
         if (var7 <= 0) {
            break;
         }

         var5.write(var4, 0, var7);
      }

      return var5;
   }

   public static PackedByteStream readRecordBody(RandomAccessFile var0, RecordIndex var1, RecordHeader var2) {
      return readRange(var0, var1.version, var2.offset, var2.length);
   }
}
