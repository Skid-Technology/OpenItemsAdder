package dev.lone.itemsadder.util.serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SerialFacadeDup2 {
   private SerialFacadeDup2() {
   }

   public static SerialTypeHandler openHandler(byte[] var0) {
      return openHandlerRange(var0, 0, var0.length);
   }

   public static SerialTypeHandler openHandlerRange(byte[] var0, int var1, int var2) {
      ByteArraySerialReader var3 = new ByteArraySerialReader(var0, var1, var2, true);
      return new ByteArrayTypeHandlerDup2(var3);
   }

   public static SerialTypeHandler openHandlerStream(InputStream var0) {
      StreamSerialReader var1 = new StreamSerialReader(var0, true);
      return new StreamTypeHandlerDup2(var1);
   }

   public static void readFromBytes(byte[] var0, Object var1, SerialType var2) {
      SerialStreamHelper.readFromArrayRange(var0, 0, var0.length, var1, var2, true);
   }

   public static void readFromBytesRange(byte[] var0, int var1, int var2, Object var3, SerialType var4) {
      SerialStreamHelper.readFromArrayRange(var0, var1, var2, var3, var4, true);
   }

   public static void readFromStream(InputStream var0, Object var1, SerialType var2) {
      SerialStreamHelper.readFromStreamDirect(var0, var1, var2, true);
   }

   public static void readFromStreamWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      SerialStreamHelper.readFromStreamBuffered(var0, var3.bytes, var1, var2, true);
   }

   public static int readFromStreamCount(InputStream var0, Object var1, SerialType var2) {
      return SerialStreamHelper.readFromStreamDirectCount(var0, var1, var2, true);
   }

   public static int readFromStreamCountWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      return SerialStreamHelper.readFromStreamBufferedCount(var0, var3.bytes, var1, var2, true);
   }

   public static int readFromDataInput(DataInput var0, Object var1, SerialType var2) {
      return SerialStreamHelper.readFromDataInputCount(var0, var1, var2, true);
   }

   public static byte[] writeToByteArray(Object var0, SerialType var1, SerialByteSlice var2) {
      if (var2.start != var2.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var3 = new StreamSerialWriter(var2);

      try {
         var1.write(var3, var0);
      } catch (IOException var5) {
         throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", var5);
      }

      return var3.toByteArray();
   }

   public static int writeToSlice(SerialByteSlice var0, Object var1, SerialType var2) {
      if (var0.start != var0.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var3 = new StreamSerialWriter(var0);

      try {
         var2.write(var3, var1);
      } catch (IOException var5) {
         throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException (should never happen).", var5);
      }

      return var3.size();
   }

   public static int writeToStream(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var4 = new StreamSerialWriter(var3, var0);
      var2.write(var4, var1);
      SerialByteSlice.writeAllTo(var0, var3);
      return var4.written;
   }

   public static int writeToStreamBuffered(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var4 = new StreamSerialWriter(var3);
      var2.write(var4, var1);
      SerialByteSliceWriter.writeVarIntToStream(var0, var4.written);
      SerialByteSlice.writeAllTo(var0, var3);
      return var4.written;
   }

   public static int writeToDataOutput(DataOutput var0, Object var1, SerialType var2) {
      SerialByteSlice var3 = new SerialByteSlice(256);
      StreamSerialWriter var4 = new StreamSerialWriter(var3);
      var2.write(var4, var1);
      SerialByteSliceWriter.writeVarIntToData(var0, var4.written);
      SerialByteSlice.writeAllToData(var0, var3);
      return var4.written;
   }

   public static int writeListToStream(OutputStream var0, List var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      int var4 = var1.size();
      if (var4 == 0) {
         return 0;
      }

      StreamSerialWriter var5 = new StreamSerialWriter(var3, var0);
      var5.mode.method252(var4, var5, var3);

      for (Object var7 : var1) {
         var2.write(var5, var7);
         var5.mode.method241((byte)7, var5, var3);
      }

      SerialByteSlice.writeAllTo(var0, var3);
      return var5.written;
   }

   public static List readListFromStream(InputStream var0, SerialType var1) {
      int var2 = var0.read();
      if (var2 == -1) {
         return Collections.emptyList();
      }

      if (var2 > 127) {
         var2 = StreamSerialReader.readByteOrEnd(var0, var2);
      }

      ArrayList var3 = new ArrayList(var2);
      StreamSerialReader var4 = new StreamSerialReader(var0, true);

      for (int var5 = 0; var5 < var2; var5++) {
         Object var6 = var1.defaultValue();
         var3.add(var6);
         var1.read(var4, var6);
         var4.expectTag(0);
      }

      assert var0.read() == -1;
      return var3;
   }

   public static boolean readDelimitedFromStream(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      return readDelimitedFromStreamFlag(var0, var1, var2, true, var3);
   }

   public static boolean readDelimitedFromStreamFlag(InputStream var0, Object var1, SerialType var2, boolean var3, SerialByteSlice var4) {
      if (var4.start != var4.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      int var5 = SerialStreamHelper.readLengthPrefix(var0, var3, var4);
      if (var5 == 0) {
         return true;
      }

      if (var4.start == var4.offset) {
         return false;
      }

      ByteArraySerialReader var6 = new ByteArraySerialReader(var4.bytes, var4.offset, var5, true);

      try {
         var2.read(var6, var1);
         var6.expectTag(0);
      } catch (ArrayIndexOutOfBoundsException var11) {
         throw SerialParseException.wrap(var11);
      } finally {
         var4.offset = var4.start;
      }

      return true;
   }

   public static int writeDelimitedToStream(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var4 = new StreamSerialWriter(var3);
      var3.offset = var3.start + 5;
      var4.written += 5;
      var2.write(var4, var1);
      int var5 = var4.written - 5;
      int var6 = SerialStreamHelper.encodeVarIntInto(var5, var3.bytes, var3.start);
      var0.write(var3.bytes, var6, var3.offset - var6);
      if (var3.next != null) {
         SerialByteSlice.writeAllTo(var0, var3.next);
      }

      return var5;
   }
}
