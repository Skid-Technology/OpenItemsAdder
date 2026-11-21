package dev.lone.itemsadder.util.serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SerialIO {
   private SerialIO() {
   }

   public static void readFromBytes(byte[] var0, Object var1, SerialType var2) {
      readFromBytesRange(var0, 0, var0.length, var1, var2);
   }

   public static void readFromBytesRange(byte[] var0, int var1, int var2, Object var3, SerialType var4) {
      try {
         ByteArraySerialReader var5 = new ByteArraySerialReader(var0, var1, var2, true);
         SerialListType var6 = new SerialListType(var5);
         var4.read(var6, var3);
         var5.expectTag(0);
      } catch (ArrayIndexOutOfBoundsException var7) {
         throw new RuntimeException("Truncated.", SerialParseException.wrap(var7));
      } catch (IOException var8) {
         throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", var8);
      }
   }

   public static void readFromStreamReader(StreamSerialReader var0, Object var1, SerialType var2) {
      SerialListTypeDup2 var3 = new SerialListTypeDup2(var0);
      var2.read(var3, var1);
      var0.expectTag(0);
   }

   public static void readFromStream(InputStream var0, Object var1, SerialType var2) {
      StreamSerialReader var3 = new StreamSerialReader(var0, true);
      readFromStreamReader(var3, var1, var2);
   }

   public static void readFromStreamWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      StreamSerialReader var4 = new StreamSerialReader(var0, var3.bytes, true);
      SerialListTypeDup2 var5 = new SerialListTypeDup2(var4);
      var2.read(var5, var1);
      var4.expectTag(0);
   }

   public static int readFromStreamCount(InputStream var0, Object var1, SerialType var2) {
      int var3 = var0.read();
      if (var3 == -1) {
         throw SerialParseException.afj();
      }

      int var4 = var3 < 128 ? var3 : StreamSerialReader.readByteOrEnd(var0, var3);
      if (var4 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var4 != 0) {
         if (var4 > 4096) {
            StreamSerialReader var10 = new StreamSerialReader(new LimitedInputStream(var0, var4), true);
            SerialListTypeDup2 var11 = new SerialListTypeDup2(var10);
            var2.read(var11, var1);
            var10.expectTag(0);
            return var4;
         }

         byte[] var5 = new byte[var4];
         SerialStreamHelper.readFully(var0, var5, 0, var4);
         ByteArraySerialReader var6 = new ByteArraySerialReader(var5, 0, var4, true);
         SerialListType var7 = new SerialListType(var6);

         try {
            var2.read(var7, var1);
         } catch (ArrayIndexOutOfBoundsException var9) {
            throw SerialParseException.wrap(var9);
         }

         var6.expectTag(0);
      }

      return var4;
   }

   public static int readFromStreamCountWithSlice(InputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      int var4 = var0.read();
      if (var4 == -1) {
         throw SerialParseException.afj();
      }

      byte[] var5 = var3.bytes;
      int var6 = var4 < 128 ? var4 : StreamSerialReader.readByteOrEnd(var0, var4);
      if (var6 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var6 != 0) {
         if (var6 > var5.length) {
            throw new SerialParseException("size limit exceeded. " + var6 + " > " + var5.length);
         }

         SerialStreamHelper.readFully(var0, var5, 0, var6);
         ByteArraySerialReader var7 = new ByteArraySerialReader(var5, 0, var6, true);
         SerialListType var8 = new SerialListType(var7);

         try {
            var2.read(var8, var1);
         } catch (ArrayIndexOutOfBoundsException var10) {
            throw SerialParseException.wrap(var10);
         }

         var7.expectTag(0);
      }

      return var6;
   }

   public static int readFromDataInput(DataInput var0, Object var1, SerialType var2) {
      byte var3 = var0.readByte();
      int var4 = 0 == (var3 & 128) ? var3 : StreamSerialReader.readVarIntFirstByte(var0, var3);
      if (var4 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var4 != 0) {
         if (var4 > 4096 && var0 instanceof InputStream) {
            StreamSerialReader var10 = new StreamSerialReader(new LimitedInputStream((InputStream)var0, var4), true);
            SerialListTypeDup2 var11 = new SerialListTypeDup2(var10);
            var2.read(var11, var1);
            var10.expectTag(0);
         } else {
            byte[] var5 = new byte[var4];
            var0.readFully(var5, 0, var4);
            ByteArraySerialReader var6 = new ByteArraySerialReader(var5, 0, var4, true);
            SerialListType var7 = new SerialListType(var6);

            try {
               var2.read(var7, var1);
            } catch (ArrayIndexOutOfBoundsException var9) {
               throw SerialParseException.wrap(var9);
            }

            var6.expectTag(0);
         }
      }

      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      } else {
         return var4;
      }
   }

   public static byte[] writeToByteArray(Object var0, SerialType var1, SerialByteSlice var2) {
      if (var2.start != var2.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var3 = new StreamSerialWriter(var2);
      SerialWriterBuffered var4 = new SerialWriterBuffered(var3);

      try {
         var1.write(var4, var0);
      } catch (IOException var6) {
         throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", var6);
      }

      return var3.toByteArray();
   }

   public static int writeToSlice(SerialByteSlice var0, Object var1, SerialType var2) {
      if (var0.start != var0.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var3 = new StreamSerialWriter(var0);
      SerialWriterBuffered var4 = new SerialWriterBuffered(var3);

      try {
         var2.write(var4, var1);
      } catch (IOException var6) {
         throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException (should never happen).", var6);
      }

      return var3.size();
   }

   public static int writeToStream(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var4 = new StreamSerialWriter(var3, var0);
      SerialWriterBuffered var5 = new SerialWriterBuffered(var4);
      var2.write(var5, var1);
      SerialByteSlice.writeAllTo(var0, var3);
      return var4.written;
   }

   public static int writeToStreamBuffered(OutputStream var0, Object var1, SerialType var2, SerialByteSlice var3) {
      if (var3.start != var3.offset) {
         throw new IllegalArgumentException("Buffer previously used and had not been reset.");
      }

      StreamSerialWriter var4 = new StreamSerialWriter(var3);
      SerialWriterBuffered var5 = new SerialWriterBuffered(var4);
      var2.write(var5, var1);
      SerialByteSliceWriter.writeVarIntToStream(var0, var4.written);
      SerialByteSlice.writeAllTo(var0, var3);
      return var4.written;
   }

   public static int writeToDataOutput(DataOutput var0, Object var1, SerialType var2) {
      SerialByteSlice var3 = new SerialByteSlice(256);
      StreamSerialWriter var4 = new StreamSerialWriter(var3);
      SerialWriterBuffered var5 = new SerialWriterBuffered(var4);
      var2.write(var5, var1);
      SerialByteSliceWriter.writeVarIntToData(var0, var4.written);
      SerialByteSlice.writeAllToData(var0, var3);
      return var4.written;
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
      SerialListType var7 = new SerialListType(var6);

      try {
         var2.read(var7, var1);
         var6.expectTag(0);
      } catch (ArrayIndexOutOfBoundsException var12) {
         throw SerialParseException.wrap(var12);
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
      SerialWriterBuffered var5 = new SerialWriterBuffered(var4);
      var3.offset = var3.start + 5;
      var4.written += 5;
      var2.write(var5, var1);
      int var6 = var4.written - 5;
      int var7 = SerialStreamHelper.encodeVarIntInto(var6, var3.bytes, var3.start);
      var0.write(var3.bytes, var7, var3.offset - var7);
      if (var3.next != null) {
         SerialByteSlice.writeAllTo(var0, var3.next);
      }

      return var6;
   }
}
