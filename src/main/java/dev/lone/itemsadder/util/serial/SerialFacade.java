package dev.lone.itemsadder.util.serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class SerialFacade {
   private SerialFacade() {
   }

   public static SerialTypeHandler openHandler(byte[] var0) {
      return openHandlerRange(var0, 0, var0.length);
   }

   public static SerialTypeHandler openHandlerRange(byte[] var0, int var1, int var2) {
      ByteArraySerialReader var3 = new ByteArraySerialReader(var0, var1, var2, false);
      return new ByteArrayTypeHandler(var3);
   }

   public static SerialTypeHandler openHandlerStream(InputStream var0) {
      StreamSerialReader var1 = new StreamSerialReader(var0, false);
      return new StreamTypeHandler(var1);
   }

   public static void readFromBytes(byte[] var0, Object var1, SerialType var2) {
      SerialStreamHelper.readFromArrayRange(var0, 0, var0.length, var1, var2, false);
   }

   public static void readFromBytesRange(byte[] var0, int var1, int var2, Object var3, SerialType var4) {
      SerialStreamHelper.readFromArrayRange(var0, var1, var2, var3, var4, false);
   }

   public static void readFromStream(InputStream var0, Object var1, SerialType var2) {
      SerialStreamHelper.readFromStreamDirect(var0, var1, var2, false);
   }

   public static void readFromStreamWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      SerialStreamHelper.readFromStreamBuffered(var0, var3.bytes, var1, var2, false);
   }

   public static int readFromStreamCount(InputStream var0, Object var1, SerialType var2) {
      return SerialStreamHelper.readFromStreamDirectCount(var0, var1, var2, false);
   }

   public static int readFromStreamCountWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      return SerialStreamHelper.readFromStreamBufferedCount(var0, var3.bytes, var1, var2, false);
   }

   public static int readFromDataInput(DataInput var0, Object var1, SerialType var2) {
      return SerialStreamHelper.readFromDataInputCount(var0, var1, var2, false);
   }

   public static byte[] writeToByteArray(Object var0, SerialType var1, SerialByteSlice var2) {
      if (var2.start != var2.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      SerialByteSliceWriter var3 = new SerialByteSliceWriter(var2);

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

      SerialByteSliceWriter var3 = new SerialByteSliceWriter(var0);

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

      SerialByteSliceWriter var4 = new SerialByteSliceWriter(var3);
      var2.write(var4, var1);
      return SerialByteSlice.writeAllTo(var0, var3);
   }

   public static int writeToStreamBuffered(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      SerialByteSliceWriter var4 = new SerialByteSliceWriter(var3);
      var2.write(var4, var1);
      int var5 = var4.size();
      SerialByteSliceWriter.writeVarIntToStream(var0, var5);
      int var6 = SerialByteSlice.writeAllTo(var0, var3);
      assert var5 == var6;
      return var5;
   }

   public static int writeToDataOutput(DataOutput var0, Object var1, SerialType var2) {
      SerialByteSlice var3 = new SerialByteSlice(256);
      SerialByteSliceWriter var4 = new SerialByteSliceWriter(var3);
      var2.write(var4, var1);
      int var5 = var4.size();
      SerialByteSliceWriter.writeVarIntToData(var0, var5);
      int var6 = SerialByteSlice.writeAllToData(var0, var3);
      assert var5 == var6;
      return var5;
   }

   public static int writeListToStream(OutputStream var0, List var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      SerialByteSliceWriter var4 = new SerialByteSliceWriter(var3);
      int var5 = 0;

      for (Object var7 : var1) {
         var2.write(var4, var7);
         int var8 = var4.size();
         SerialByteSliceWriter.writeVarIntToStream(var0, var8);
         int var9 = SerialByteSlice.writeAllTo(var0, var3);
         assert var8 == var9;
         var5 += var8;
         var4.restartWriter();
      }

      return var5;
   }

   public static List readListFromStream(InputStream var0, SerialType var1) {
      ArrayList var2 = new ArrayList();
      byte[] var3 = null;
      int var4 = 0;
      LimitedInputStream var5 = null;

      for (int var6 = var0.read(); var6 != -1; var6 = var0.read()) {
         Object var7 = var1.defaultValue();
         var2.add(var7);
         int var8 = var6 < 128 ? var6 : StreamSerialReader.readByteOrEnd(var0, var6);
         if (var8 != 0) {
            if (var8 > 4096) {
               if (var5 == null) {
                  var5 = new LimitedInputStream(var0);
               }

               StreamSerialReader var9 = new StreamSerialReader(var5.withLimit(var8), false);
               var1.read(var9, var7);
               var9.expectTag(0);
            } else {
               if (var4 < var8) {
                  var3 = new byte[var8];
                  var4 = var8;
               }

               SerialStreamHelper.readFully(var0, var3, 0, var8);
               ByteArraySerialReader var12 = new ByteArraySerialReader(var3, 0, var8, false);

               try {
                  var1.read(var12, var7);
               } catch (ArrayIndexOutOfBoundsException var11) {
                  throw SerialParseException.wrap(var11);
               }

               var12.expectTag(0);
            }
         }
      }

      return var2;
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

      ByteArraySerialReader var6 = new ByteArraySerialReader(var4.bytes, var4.offset, var5, false);

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

      SerialByteSliceWriter var4 = new SerialByteSliceWriter(var3);
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
