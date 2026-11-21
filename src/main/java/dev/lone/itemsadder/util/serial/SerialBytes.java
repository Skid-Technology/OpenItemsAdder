package dev.lone.itemsadder.util.serial;

import java.io.DataOutput;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public final class SerialBytes {
   private final byte[] data;
   public static final String EMPTY_STRING = "";
   public static final byte[] EMPTY_BYTES = new byte[0];
   public static final SerialBytes EMPTY = new SerialBytes(EMPTY_BYTES);
   private volatile int cachedHash = 0;

   static SerialBytes wrap(byte[] var0) {
      return new SerialBytes(var0);
   }

   byte[] getBytes() {
      return this.data;
   }

   public static void writeToStream(OutputStream var0, SerialBytes var1) {
      var0.write(var1.data);
   }

   public static void writeToData(DataOutput var0, SerialBytes var1) {
      var0.write(var1.data);
   }

   public static void writeToWriter(SerialWriter var0, SerialBytes var1, int var2, boolean var3) {
      var0.writeBytes(var2, var1.data, var3);
   }

   @Override
   public String toString() {
      return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
   }

   private SerialBytes(byte[] var1) {
      this.data = var1;
   }

   public byte byteAt(int var1) {
      return this.data[var1];
   }

   public int size() {
      return this.data.length;
   }

   public boolean isEmpty() {
      return this.data.length == 0;
   }

   public static SerialBytes copyOfRange(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[var2];
      System.arraycopy(var0, var1, var3, 0, var2);
      return new SerialBytes(var3);
   }

   public static SerialBytes copyOf(byte[] var0) {
      return copyOfRange(var0, 0, var0.length);
   }

   public static SerialBytes fromString(String var0, String var1) {
      try {
         return new SerialBytes(var0.getBytes(var1));
      } catch (UnsupportedEncodingException var3) {
         throw new RuntimeException(var1 + " not supported?", var3);
      }
   }

   public static SerialBytes fromBase64(String var0) {
      return new SerialBytes(Base64Helper.toUtf8Bytes(var0));
   }

   public void copyTo(byte[] var1, int var2) {
      System.arraycopy(this.data, 0, var1, var2, this.data.length);
   }

   public void copyRangeTo(byte[] var1, int var2, int var3, int var4) {
      System.arraycopy(this.data, var2, var1, var3, var4);
   }

   public byte[] toByteArray() {
      int var1 = this.data.length;
      byte[] var2 = new byte[var1];
      System.arraycopy(this.data, 0, var2, 0, var1);
      return var2;
   }

   public ByteBuffer asReadOnlyBuffer() {
      ByteBuffer var1 = ByteBuffer.wrap(this.data);
      return var1.asReadOnlyBuffer();
   }

   public String toBase64() {
      return Base64Helper.encode(this.data);
   }

   @Override
   public boolean equals(Object o) {
      return o == this || o instanceof SerialBytes && contentEquals(this, (SerialBytes)o, false);
   }

   public static boolean contentEquals(SerialBytes var0, SerialBytes var1, boolean var2) {
      int var3 = var0.data.length;
      if (var3 != var1.data.length) {
         return false;
      }

      if (var2) {
         int var4 = var0.cachedHash;
         int var5 = var1.cachedHash;
         if (var4 != 0 && var5 != 0 && var4 != var5) {
            return false;
         }
      }

      byte[] var7 = var0.data;
      byte[] var8 = var1.data;

      for (int var6 = 0; var6 < var3; var6++) {
         if (var7[var6] != var8[var6]) {
            return false;
         }
      }

      return true;
   }

   public boolean contentEqualsArray(byte[] var1) {
      return this.contentEqualsRange(var1, 0, var1.length);
   }

   public boolean contentEqualsRange(byte[] var1, int var2, int var3) {
      byte[] var4 = this.data;
      if (var3 != var4.length) {
         return false;
      }

      int var5 = 0;

      while (var5 < var3) {
         if (var4[var5++] != var1[var2++]) {
            return false;
         }
      }

      return true;
   }

   @Override
   public int hashCode() {
      int var1 = this.cachedHash;
      if (var1 == 0) {
         byte[] var2 = this.data;
         int var3 = this.data.length;
         var1 = var3;

         for (int var4 = 0; var4 < var3; var4++) {
            var1 = var1 * 31 + var2[var4];
         }

         if (var1 == 0) {
            var1 = 1;
         }

         this.cachedHash = var1;
      }

      return var1;
   }

   public static String latin1ToUtf8(String var0) {
      try {
         return new String(var0.getBytes("ISO-8859-1"), "UTF-8");
      } catch (UnsupportedEncodingException var2) {
         throw new IllegalStateException("Java VM does not support a standard character set.", var2);
      }
   }

   public static SerialBytes fromLatin1(String var0) {
      return new SerialBytes(latin1Bytes(var0));
   }

   public static byte[] latin1Bytes(String var0) {
      try {
         return var0.getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var2) {
         throw new IllegalStateException("Java VM does not support a standard character set.", var2);
      }
   }
}
