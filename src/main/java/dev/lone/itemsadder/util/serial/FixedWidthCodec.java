package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public final class FixedWidthCodec {
   private FixedWidthCodec() {
   }

   public static void writeShortBE(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >>> 8 & 0xFF);
      var1[var2] = (byte)var0;
   }

   public static void writeShortLE(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)var0;
      var1[var2] = (byte)(var0 >>> 8 & 0xFF);
   }

   public static void writeShortLEBuf(int var0, ByteBuffer var1) {
      var1.put((byte)var0);
      var1.put((byte)(var0 >>> 8 & 0xFF));
   }

   public static void writeIntBE(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >>> 24 & 0xFF);
      var1[var2++] = (byte)(var0 >>> 16 & 0xFF);
      var1[var2++] = (byte)(var0 >>> 8 & 0xFF);
      var1[var2] = (byte)(var0 >>> 0 & 0xFF);
   }

   public static void writeIntLE(int var0, byte[] var1, int var2) {
      var1[var2++] = (byte)(var0 >>> 0 & 0xFF);
      var1[var2++] = (byte)(var0 >>> 8 & 0xFF);
      var1[var2++] = (byte)(var0 >>> 16 & 0xFF);
      var1[var2] = (byte)(var0 >>> 24 & 0xFF);
   }

   public static void writeIntLEBuf(int var0, ByteBuffer var1) {
      var1.put((byte)(var0 >>> 0 & 0xFF));
      var1.put((byte)(var0 >>> 8 & 0xFF));
      var1.put((byte)(var0 >>> 16 & 0xFF));
      var1.put((byte)(var0 >>> 24 & 0xFF));
   }

   public static void writeLongBE(long var0, byte[] var2, int var3) {
      var2[var3++] = (byte)(var0 >>> 56);
      var2[var3++] = (byte)(var0 >>> 48);
      var2[var3++] = (byte)(var0 >>> 40);
      var2[var3++] = (byte)(var0 >>> 32);
      var2[var3++] = (byte)(var0 >>> 24);
      var2[var3++] = (byte)(var0 >>> 16);
      var2[var3++] = (byte)(var0 >>> 8);
      var2[var3] = (byte)(var0 >>> 0);
   }

   public static void writeLongLE(long var0, byte[] var2, int var3) {
      var2[var3++] = (byte)(var0 >>> 0);
      var2[var3++] = (byte)(var0 >>> 8);
      var2[var3++] = (byte)(var0 >>> 16);
      var2[var3++] = (byte)(var0 >>> 24);
      var2[var3++] = (byte)(var0 >>> 32);
      var2[var3++] = (byte)(var0 >>> 40);
      var2[var3++] = (byte)(var0 >>> 48);
      var2[var3] = (byte)(var0 >>> 56);
   }

   public static void writeLongLEBuf(long var0, ByteBuffer var2) {
      var2.put((byte)(var0 >>> 0));
      var2.put((byte)(var0 >>> 8));
      var2.put((byte)(var0 >>> 16));
      var2.put((byte)(var0 >>> 24));
      var2.put((byte)(var0 >>> 32));
      var2.put((byte)(var0 >>> 40));
      var2.put((byte)(var0 >>> 48));
      var2.put((byte)(var0 >>> 56));
   }
}
