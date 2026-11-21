package dev.lone.itemsadder.util.serial;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public final class ByteBufferSerialReader implements SerialReader {
   private final ByteBuffer buffer;
   private int lastTag = 0;
   private int delimitedEnd = 0;
   public final boolean lenient;

   public ByteBufferSerialReader(ByteBuffer var1, boolean var2) {
      this.buffer = var1.slice();
      this.lenient = var2;
   }

   public ByteBufferSerialReader slice(int var1, int var2) {
      ((Buffer)this.buffer).rewind();
      return this;
   }

   public int getOffset() {
      return this.buffer.position();
   }

   public int getLimit() {
      return this.buffer.limit();
   }

   public boolean isInDelimited() {
      return this.delimitedEnd != 0 && this.delimitedEnd != this.buffer.position();
   }

   public int getLastTag() {
      return this.lastTag;
   }

   public int readNextTag() {
      if (!this.buffer.hasRemaining()) {
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

            ((Buffer)this.buffer).position(this.buffer.position() + var2);
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
      if (!this.buffer.hasRemaining()) {
         this.lastTag = 0;
         return 0;
      }

      if (this.isInDelimited()) {
         if (this.delimitedEnd < this.buffer.position()) {
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

   private void aet() {
      if (this.delimitedEnd == 0 && WireTypes.getWireType(this.lastTag) == 2) {
         int var1 = this.readVarInt();
         if (var1 < 0) {
            throw SerialParseException.negativeSize();
         }

         if (this.buffer.position() + var1 > this.buffer.limit()) {
            throw SerialParseException.truncatedMessage();
         }

         this.delimitedEnd = this.buffer.position() + var1;
      }
   }

   @Override
   public double readDouble() {
      this.aet();
      return Double.longBitsToDouble(this.readLittleEndianLong());
   }

   @Override
   public float readFloat() {
      this.aet();
      return Float.intBitsToFloat(this.readLittleEndianInt());
   }

   @Override
   public long readInt64() {
      this.aet();
      return this.readVarLong();
   }

   @Override
   public long readUInt64() {
      this.aet();
      return this.readVarLong();
   }

   @Override
   public int readInt32() {
      this.aet();
      return this.readVarInt();
   }

   @Override
   public long readFixed64() {
      this.aet();
      return this.readLittleEndianLong();
   }

   @Override
   public int readFixed32() {
      this.aet();
      return this.readLittleEndianInt();
   }

   @Override
   public boolean readBool() {
      this.aet();
      return this.buffer.get() != 0;
   }

   @Override
   public int readUInt32() {
      this.aet();
      return this.readVarInt();
   }

   @Override
   public int readEnum() {
      this.aet();
      return this.readVarInt();
   }

   @Override
   public int readSFixed32() {
      this.aet();
      return this.readLittleEndianInt();
   }

   @Override
   public long readSFixed64() {
      this.aet();
      return this.readLittleEndianLong();
   }

   @Override
   public int readSInt32() {
      this.aet();
      int var1 = this.readVarInt();
      return var1 >>> 1 ^ -(var1 & 1);
   }

   @Override
   public long readSInt64() {
      this.aet();
      long var1 = this.readVarLong();
      return var1 >>> 1 ^ -(var1 & 1L);
   }

   @Override
   public String readString() {
      int var1 = this.readVarInt();
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      } else if (this.buffer.remaining() < var1) {
         throw SerialParseException.truncatedMessage();
      } else if (this.buffer.hasArray()) {
         int var3 = this.buffer.position();
         ((Buffer)this.buffer).position(this.buffer.position() + var1);
         return Base64Helper.encodeRange(this.buffer.array(), this.buffer.arrayOffset() + var3, var1);
      } else {
         byte[] var2 = new byte[var1];
         this.buffer.get(var2);
         return Base64Helper.encode(var2);
      }
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

      if (this.buffer.remaining() < var2) {
         throw SerialParseException.truncatedMessage();
      }

      var1.put(this.buffer);
   }

   @Override
   public byte[] readByteArray() {
      int var1 = this.readVarInt();
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.buffer.remaining() < var1) {
         throw SerialParseException.truncatedMessage();
      }

      byte[] var2 = new byte[var1];
      this.buffer.get(var2);
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

      if (this.buffer.remaining() < var3) {
         throw SerialParseException.truncatedMessage();
      }

      ByteBuffer var4 = this.buffer.slice();
      ((Buffer)var4).limit(var3);
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      ByteBufferSerialReader var5 = new ByteBufferSerialReader(var4, this.lenient);
      var2.read(var5, var1);
      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      }

      var5.expectTag(0);
      ((Buffer)this.buffer).position(this.buffer.position() + var3);
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
      byte var1 = this.buffer.get();
      if (var1 >= 0) {
         return var1;
      }

      int var2 = var1 & 127;
      if ((var1 = this.buffer.get()) >= 0) {
         var2 |= var1 << 7;
      } else {
         var2 |= (var1 & 127) << 7;
         if ((var1 = this.buffer.get()) >= 0) {
            var2 |= var1 << 14;
         } else {
            var2 |= (var1 & 127) << 14;
            if ((var1 = this.buffer.get()) >= 0) {
               var2 |= var1 << 21;
            } else {
               var2 |= (var1 & 127) << 21;
               var2 |= (var1 = this.buffer.get()) << 28;
               if (var1 < 0) {
                  for (int var3 = 0; var3 < 5; var3++) {
                     if (this.buffer.get() >= 0) {
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
      byte var1 = 0;
      long var2 = 0L;

      while (var1 < 64) {
         byte var4 = this.buffer.get();
         var2 |= (long)(var4 & 127) << var1;
         if ((var4 & 128) == 0) {
            return var2;
         }

         var1 += 7;
      }

      throw SerialParseException.malformedVarint();
   }

   public int readLittleEndianInt() {
      byte[] var1 = new byte[4];
      this.buffer.get(var1);
      return var1[0] & 0xFF | (var1[1] & 0xFF) << 8 | (var1[2] & 0xFF) << 16 | (var1[3] & 0xFF) << 24;
   }

   public long readLittleEndianLong() {
      byte[] var1 = new byte[8];
      this.buffer.get(var1);
      return var1[0] & 255L
         | (var1[1] & 255L) << 8
         | (var1[2] & 255L) << 16
         | (var1[3] & 255L) << 24
         | (var1[4] & 255L) << 32
         | (var1[5] & 255L) << 40
         | (var1[6] & 255L) << 48
         | (var1[7] & 255L) << 56;
   }

   @Override
   public void copyBytesTo(SerialWriter var1, boolean var2, int var3, boolean var4) {
      int var5 = this.readVarInt();
      if (var5 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (var2) {
         if (this.buffer.hasArray()) {
            var1.writeBytesRange(true, var3, this.buffer.array(), this.buffer.arrayOffset() + this.buffer.position(), var5, var4);
            ((Buffer)this.buffer).position(this.buffer.position() + var5);
         } else {
            byte[] var6 = new byte[var5];
            this.buffer.get(var6);
            var1.writeBytesRange(true, var3, var6, 0, var6.length, var4);
         }
      } else {
         if (this.buffer.remaining() < var5) {
            throw SerialParseException.truncatedMessage();
         }

         ByteBuffer var7 = this.buffer.slice();
         ((Buffer)var7).limit(var5);
         var1.writeByteBuffer(var3, var7, var4);
         ((Buffer)this.buffer).position(this.buffer.position() + var5);
      }
   }

   @Override
   public ByteBuffer readByteBuffer() {
      return ByteBuffer.wrap(this.readByteArray());
   }
}
