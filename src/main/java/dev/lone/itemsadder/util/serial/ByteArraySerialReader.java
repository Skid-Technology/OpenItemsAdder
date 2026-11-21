package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public final class ByteArraySerialReader implements SerialReader {
   private final byte[] data;
   private int offset;
   private int limit;
   private int lastTag = 0;
   private int delimitedEnd = 0;
   public final boolean lenient;

   public ByteArraySerialReader(byte[] var1, boolean var2) {
      this(var1, 0, var1.length, var2);
   }

   public ByteArraySerialReader(byte[] var1, int var2, int var3, boolean var4) {
      this.data = var1;
      this.offset = var2;
      this.limit = var2 + var3;
      this.lenient = var4;
   }

   public ByteArraySerialReader slice(int var1, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException("length cannot be negative.");
      }

      this.offset = var1;
      this.limit = var1 + var2;
      this.delimitedEnd = 0;
      return this;
   }

   public ByteArraySerialReader window(int var1, int var2) {
      this.offset = var1;
      this.limit = var2;
      this.delimitedEnd = 0;
      return this;
   }

   public int getOffset() {
      return this.offset;
   }

   public int getLimit() {
      return this.limit;
   }

   public boolean isInDelimited() {
      return this.delimitedEnd != 0 && this.delimitedEnd != this.offset;
   }

   public int getLastTag() {
      return this.lastTag;
   }

   public int readNextTag() {
      if (this.offset == this.limit) {
         this.lastTag = 0;
         return 0;
      }

      int var1 = this.readVarInt();
      if (var1 >>> 3 == 0) {
         throw SerialParseException.invalidTag();
      }

      this.lastTag = var1;
      return var1;
   }

   public void expectTag(int var1) {
      if (this.lastTag != var1) {
         throw SerialParseException.mismatchedTag();
      }
   }

   public boolean skipUnknownField(int var1) {
      switch (WireTypes.getWireType(var1)) {
         case 0:
            this.readInt32();
            return true;
         case 1:
            this.readLittleEndianLong();
            return true;
         case 2:
            int var2 = this.readVarInt();
            if (var2 < 0) {
               throw SerialParseException.negativeSize();
            }

            this.offset += var2;
            return true;
         case 3:
            this.skipMessage();
            this.expectTag(WireTypes.makeTag(WireTypes.getFieldNumber(var1), 4));
            return true;
         case 4:
            return false;
         case 5:
            this.readLittleEndianInt();
            return true;
         default:
            throw SerialParseException.invalidWireType();
      }
   }

   public void skipMessage() {
      int var1;
      do {
         var1 = this.readNextTag();
      } while (var1 != 0 && this.skipUnknownField(var1));
   }

   @Override
   public void skipField(int var1, SerialType var2) {
      this.skipUnknownField(this.lastTag);
   }

   @Override
   public int readTag(SerialType var1) {
      if (this.offset == this.limit) {
         this.lastTag = 0;
         return 0;
      }

      if (this.isInDelimited()) {
         if (this.delimitedEnd < this.offset) {
            throw SerialParseException.truncatedMessage();
         } else {
            return this.lastTag >>> 3;
         }
      } else {
         this.delimitedEnd = 0;
         int var2 = this.readVarInt();
         int var3 = var2 >>> 3;
         if (var3 == 0) {
            if (this.lenient && 7 == (var2 & 7)) {
               this.lastTag = 0;
               return 0;
            } else {
               throw SerialParseException.invalidTag();
            }
         } else if (this.lenient && 4 == (var2 & 7)) {
            this.lastTag = 0;
            return 0;
         } else {
            this.lastTag = var2;
            return var3;
         }
      }
   }

   private void enterDelimited() {
      if (this.delimitedEnd == 0 && WireTypes.getWireType(this.lastTag) == 2) {
         int var1 = this.readVarInt();
         if (var1 < 0) {
            throw SerialParseException.negativeSize();
         }

         if (this.offset + var1 > this.limit) {
            throw SerialParseException.truncatedMessage();
         }

         this.delimitedEnd = this.offset + var1;
      }
   }

   @Override
   public double readDouble() {
      this.enterDelimited();
      return Double.longBitsToDouble(this.readLittleEndianLong());
   }

   @Override
   public float readFloat() {
      this.enterDelimited();
      return Float.intBitsToFloat(this.readLittleEndianInt());
   }

   @Override
   public long readInt64() {
      this.enterDelimited();
      return this.readVarLong();
   }

   @Override
   public long readUInt64() {
      this.enterDelimited();
      return this.readVarLong();
   }

   @Override
   public int readInt32() {
      this.enterDelimited();
      return this.readVarInt();
   }

   @Override
   public long readFixed64() {
      this.enterDelimited();
      return this.readLittleEndianLong();
   }

   @Override
   public int readFixed32() {
      this.enterDelimited();
      return this.readLittleEndianInt();
   }

   @Override
   public boolean readBool() {
      this.enterDelimited();
      return this.data[this.offset++] != 0;
   }

   @Override
   public int readUInt32() {
      this.enterDelimited();
      return this.readVarInt();
   }

   @Override
   public int readEnum() {
      this.enterDelimited();
      return this.readVarInt();
   }

   @Override
   public int readSFixed32() {
      this.enterDelimited();
      return this.readLittleEndianInt();
   }

   @Override
   public long readSFixed64() {
      this.enterDelimited();
      return this.readLittleEndianLong();
   }

   @Override
   public int readSInt32() {
      this.enterDelimited();
      int var1 = this.readVarInt();
      return var1 >>> 1 ^ -(var1 & 1);
   }

   @Override
   public long readSInt64() {
      this.enterDelimited();
      long var1 = this.readVarLong();
      return var1 >>> 1 ^ -(var1 & 1L);
   }

   @Override
   public String readString() {
      int var1 = this.readVarInt();
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.offset + var1 > this.limit) {
         throw SerialParseException.truncatedMessage();
      }

      int var2 = this.offset;
      this.offset += var1;
      return Base64Helper.encodeRange(this.data, var2, var1);
   }

   @Override
   public SerialBytes readSerialBytes() {
      return SerialBytes.wrap(this.readByteArray());
   }

   @Override
   public void readBytesInto(ByteBuffer var1) {
      int var2 = this.readVarInt();
      if (var2 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.offset + var2 > this.limit) {
         throw SerialParseException.truncatedMessage();
      }

      var1.put(this.data, this.offset, var2);
      this.offset += var2;
   }

   @Override
   public byte[] readByteArray() {
      int var1 = this.readVarInt();
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.offset + var1 > this.limit) {
         throw SerialParseException.truncatedMessage();
      }

      byte[] var2 = new byte[var1];
      System.arraycopy(this.data, this.offset, var2, 0, var1);
      this.offset += var1;
      return var2;
   }

   @Override
   public Object readMessage(Object var1, SerialType var2) {
      if (this.lenient) {
         return this.readNestedMessage(var1, var2);
      }

      int var3 = this.readVarInt();
      if (var3 < 0) {
         throw SerialParseException.negativeSize();
      }

      int var4 = this.limit;
      this.limit = this.offset + var3;
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      var2.read(this, var1);
      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      }

      this.expectTag(0);
      this.limit = var4;
      return var1;
   }

   private Object readNestedMessage(Object var1, SerialType var2) {
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      var2.read(this, var1);
      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      }

      this.expectTag(0);
      return var1;
   }

   public int readVarInt() {
      byte var1 = this.data[this.offset++];
      if (var1 >= 0) {
         return var1;
      }

      int var2 = var1 & 127;
      if ((var1 = this.data[this.offset++]) >= 0) {
         var2 |= var1 << 7;
      } else {
         var2 |= (var1 & 127) << 7;
         if ((var1 = this.data[this.offset++]) >= 0) {
            var2 |= var1 << 14;
         } else {
            var2 |= (var1 & 127) << 14;
            if ((var1 = this.data[this.offset++]) >= 0) {
               var2 |= var1 << 21;
            } else {
               var2 |= (var1 & 127) << 21;
               var2 |= (var1 = this.data[this.offset++]) << 28;
               if (var1 < 0) {
                  for (int var3 = 0; var3 < 5; var3++) {
                     if (this.data[this.offset++] >= 0) {
                        return var2;
                     }
                  }

                  throw SerialParseException.malformedVarint();
               }
            }
         }
      }

      return var2;
   }

   public long readVarLong() {
      byte[] var1 = this.data;
      int var2 = this.offset;
      byte var3 = 0;
      long var4 = 0L;

      while (var3 < 64) {
         byte var6 = var1[var2++];
         var4 |= (long)(var6 & 127) << var3;
         if ((var6 & 128) == 0) {
            this.offset = var2;
            return var4;
         }

         var3 += 7;
      }

      throw SerialParseException.malformedVarint();
   }

   public int readLittleEndianInt() {
      byte[] var1 = this.data;
      int var2 = this.offset;
      byte var3 = var1[var2++];
      byte var4 = var1[var2++];
      byte var5 = var1[var2++];
      byte var6 = var1[var2++];
      this.offset = var2;
      return var3 & 0xFF | (var4 & 0xFF) << 8 | (var5 & 0xFF) << 16 | (var6 & 0xFF) << 24;
   }

   public long readLittleEndianLong() {
      byte[] var1 = this.data;
      int var2 = this.offset;
      byte var3 = var1[var2++];
      byte var4 = var1[var2++];
      byte var5 = var1[var2++];
      byte var6 = var1[var2++];
      byte var7 = var1[var2++];
      byte var8 = var1[var2++];
      byte var9 = var1[var2++];
      byte var10 = var1[var2++];
      this.offset = var2;
      return var3 & 255L
         | (var4 & 255L) << 8
         | (var5 & 255L) << 16
         | (var6 & 255L) << 24
         | (var7 & 255L) << 32
         | (var8 & 255L) << 40
         | (var9 & 255L) << 48
         | (var10 & 255L) << 56;
   }

   @Override
   public void copyBytesTo(SerialWriter var1, boolean var2, int var3, boolean var4) {
      int var5 = this.readVarInt();
      if (var5 < 0) {
         throw SerialParseException.negativeSize();
      }

      var1.writeBytesRange(var2, var3, this.data, this.offset, var5, var4);
      this.offset += var5;
   }

   @Override
   public ByteBuffer readByteBuffer() {
      return ByteBuffer.wrap(this.readByteArray());
   }
}
