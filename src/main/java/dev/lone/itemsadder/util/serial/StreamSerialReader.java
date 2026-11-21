package dev.lone.itemsadder.util.serial;

import java.io.DataInput;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public final class StreamSerialReader implements SerialReader {
   private final byte[] buffer;
   private int bufferPos;
   private int bufferLimit;
   private int bufferSize;
   private final InputStream input;
   private int lastTag;
   private int delimitedEnd = 0;
   private int totalConsumed;
   private int limit = Integer.MAX_VALUE;
   public final boolean lenient;
   private int depth = 67108864;
   static final int DEFAULT_BUFFER_SIZE = 67108864;
   static final int bQR = 4096;

   public static StreamSerialReader method5805(InputStream var0) {
      return new StreamSerialReader(var0, false);
   }

   public static StreamSerialReader method5806(byte[] var0) {
      return method5807(var0, 0, var0.length);
   }

   public static StreamSerialReader method5807(byte[] var0, int var1, int var2) {
      return new StreamSerialReader(var0, var1, var2, false);
   }

   public int readNextTag() {
      if (this.isAtEnd()) {
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
            this.skipBytes(this.readVarInt());
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
      return this.readVarInt() != 0;
   }

   @Override
   public String readString() {
      int var1 = this.readVarInt();
      if (var1 <= this.bufferPos - this.bufferSize && var1 > 0) {
         String var2 = Base64Helper.encodeRange(this.buffer, this.bufferSize, var1);
         this.bufferSize += var1;
         return var2;
      } else {
         return Base64Helper.encode(this.readBytes(var1));
      }
   }

   @Override
   public void readBytesInto(ByteBuffer var1) {
      int var2 = this.readVarInt();
      if (var2 <= this.bufferPos - this.bufferSize && var2 > 0) {
         ((Buffer)var1).limit(var2);
         var1.put(this.buffer, this.bufferSize, var2);
         this.bufferSize += var2;
      } else {
         var1.put(this.readBytes(var2));
      }
   }

   @Override
   public Object readMessage(Object var1, SerialType var2) {
      if (this.lenient) {
         return this.readNestedMessage(var1, var2);
      }

      int var3 = this.readVarInt();
      int var4 = this.popLimit(var3);
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      var2.read(this, var1);
      if (!var2.accepts(var1)) {
         throw new SerialTypeMismatchException(var1, var2);
      }

      this.expectTag(0);
      this.setLimitRaw(var4);
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

   @Override
   public SerialBytes readSerialBytes() {
      int var1 = this.readVarInt();
      if (var1 == 0) {
         return SerialBytes.EMPTY;
      } else if (var1 <= this.bufferPos - this.bufferSize && var1 > 0) {
         SerialBytes var2 = SerialBytes.copyOfRange(this.buffer, this.bufferSize, var1);
         this.bufferSize += var1;
         return var2;
      } else {
         return SerialBytes.wrap(this.readBytes(var1));
      }
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
      return decodeZigZag32(this.readVarInt());
   }

   @Override
   public long readSInt64() {
      this.enterDelimited();
      return decodeZigZag64(this.readVarLong());
   }

   public int readVarInt() {
      byte var1 = this.readByte();
      if (var1 >= 0) {
         return var1;
      }

      int var2 = var1 & 127;
      if ((var1 = this.readByte()) >= 0) {
         var2 |= var1 << 7;
      } else {
         var2 |= (var1 & 127) << 7;
         if ((var1 = this.readByte()) >= 0) {
            var2 |= var1 << 14;
         } else {
            var2 |= (var1 & 127) << 14;
            if ((var1 = this.readByte()) >= 0) {
               var2 |= var1 << 21;
            } else {
               var2 |= (var1 & 127) << 21;
               var2 |= (var1 = this.readByte()) << 28;
               if (var1 < 0) {
                  for (int var3 = 0; var3 < 5; var3++) {
                     if (this.readByte() >= 0) {
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

   static int readRawVarInt32(InputStream var0) {
      int var1 = var0.read();
      if (var1 == -1) {
         throw SerialParseException.afj();
      } else {
         return (var1 & 128) == 0 ? var1 : readByteOrEnd(var0, var1);
      }
   }

   static int readByteOrEnd(InputStream var0, int var1) {
      int var2 = var1 & 127;

      byte var3;
      for (var3 = 7; var3 < 32; var3 += 7) {
         int var4 = var0.read();
         if (var4 == -1) {
            throw SerialParseException.afj();
         }

         var2 |= (var4 & 127) << var3;
         if ((var4 & 128) == 0) {
            return var2;
         }
      }

      while (var3 < 64) {
         int var5 = var0.read();
         if (var5 == -1) {
            throw SerialParseException.afj();
         }

         if ((var5 & 128) == 0) {
            return var2;
         }

         var3 += 7;
      }

      throw SerialParseException.malformedVarint();
   }

   static int readVarIntFirstByte(DataInput var0, byte var1) {
      int var2 = var1 & 127;

      byte var3;
      for (var3 = 7; var3 < 32; var3 += 7) {
         byte var4 = var0.readByte();
         var2 |= (var4 & 127) << var3;
         if ((var4 & 128) == 0) {
            return var2;
         }
      }

      while (var3 < 64) {
         byte var5 = var0.readByte();
         if ((var5 & 128) == 0) {
            return var2;
         }

         var3 += 7;
      }

      throw SerialParseException.malformedVarint();
   }

   public long readVarLong() {
      byte var1 = 0;
      long var2 = 0L;

      while (var1 < 64) {
         byte var4 = this.readByte();
         var2 |= (long)(var4 & 127) << var1;
         if ((var4 & 128) == 0) {
            return var2;
         }

         var1 += 7;
      }

      throw SerialParseException.malformedVarint();
   }

   public int readLittleEndianInt() {
      byte var1 = this.readByte();
      byte var2 = this.readByte();
      byte var3 = this.readByte();
      byte var4 = this.readByte();
      return var1 & 0xFF | (var2 & 0xFF) << 8 | (var3 & 0xFF) << 16 | (var4 & 0xFF) << 24;
   }

   public long readLittleEndianLong() {
      byte var1 = this.readByte();
      byte var2 = this.readByte();
      byte var3 = this.readByte();
      byte var4 = this.readByte();
      byte var5 = this.readByte();
      byte var6 = this.readByte();
      byte var7 = this.readByte();
      byte var8 = this.readByte();
      return var1 & 255L
         | (var2 & 255L) << 8
         | (var3 & 255L) << 16
         | (var4 & 255L) << 24
         | (var5 & 255L) << 32
         | (var6 & 255L) << 40
         | (var7 & 255L) << 48
         | (var8 & 255L) << 56;
   }

   public static int decodeZigZag32(int var0) {
      return var0 >>> 1 ^ -(var0 & 1);
   }

   public static long decodeZigZag64(long var0) {
      return var0 >>> 1 ^ -(var0 & 1L);
   }

   public StreamSerialReader(byte[] var1, int var2, int var3, boolean var4) {
      this.buffer = var1;
      this.bufferPos = var2 + var3;
      this.bufferSize = var2;
      this.totalConsumed = -var2;
      this.input = null;
      this.lenient = var4;
   }

   public StreamSerialReader(InputStream var1, boolean var2) {
      this(var1, new byte[4096], 0, 0, var2);
   }

   public StreamSerialReader(InputStream var1, byte[] var2, boolean var3) {
      this(var1, var2, 0, 0, var3);
   }

   public StreamSerialReader(InputStream var1, byte[] var2, int var3, int var4, boolean var5) {
      this.buffer = var2;
      this.bufferPos = var4;
      this.bufferSize = var3;
      this.totalConsumed = -var3;
      this.input = var1;
      this.lenient = var5;
   }

   public int pushLimit(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Size limit cannot be negative: " + var1);
      }

      int var2 = this.depth;
      this.depth = var1;
      return var2;
   }

   public void fillBuffer() {
      this.totalConsumed = -this.bufferSize;
   }

   public void rewind() {
      this.bufferPos = 0;
      this.bufferSize = 0;
      this.bufferLimit = 0;
      this.limit = Integer.MAX_VALUE;
      this.lastTag = 0;
      this.delimitedEnd = 0;
      this.depth = 67108864;
      this.fillBuffer();
   }

   public int popLimit(int var1) {
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      var1 += this.totalConsumed + this.bufferSize;
      int var2 = this.limit;
      if (var1 > var2) {
         throw SerialParseException.afj();
      }

      this.limit = var1;
      this.compactBuffer();
      return var2;
   }

   private void compactBuffer() {
      this.bufferPos = this.bufferPos + this.bufferLimit;
      int var1 = this.totalConsumed + this.bufferPos;
      if (var1 > this.limit) {
         this.bufferLimit = var1 - this.limit;
         this.bufferPos = this.bufferPos - this.bufferLimit;
      } else {
         this.bufferLimit = 0;
      }
   }

   public void setLimitRaw(int var1) {
      this.limit = var1;
      this.compactBuffer();
   }

   public int available() {
      if (this.limit == Integer.MAX_VALUE) {
         return -1;
      }

      int var1 = this.totalConsumed + this.bufferSize;
      return this.limit - var1;
   }

   public boolean isInDelimited() {
      return this.delimitedEnd != 0 && this.delimitedEnd != this.position();
   }

   public boolean isAtEnd() {
      return this.bufferSize == this.bufferPos && !this.ensureBytes(false);
   }

   public int position() {
      return this.totalConsumed + this.bufferSize;
   }

   private boolean ensureBytes(boolean var1) {
      if (this.bufferSize < this.bufferPos) {
         throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
      }

      if (this.totalConsumed + this.bufferPos == this.limit) {
         if (var1) {
            throw SerialParseException.afj();
         } else {
            return false;
         }
      } else {
         this.totalConsumed = this.totalConsumed + this.bufferPos;
         this.bufferSize = 0;
         this.bufferPos = this.input == null ? -1 : this.input.read(this.buffer);
         if (this.bufferPos == 0 || this.bufferPos < -1) {
            throw new IllegalStateException(
               "InputStream#read(byte[]) returned invalid result: " + this.bufferPos + "\nThe InputStream implementation is buggy."
            );
         }

         if (this.bufferPos == -1) {
            this.bufferPos = 0;
            if (var1) {
               throw SerialParseException.afj();
            } else {
               return false;
            }
         } else {
            this.compactBuffer();
            int var2 = this.totalConsumed + this.bufferPos + this.bufferLimit;
            if (var2 <= this.depth && var2 >= 0) {
               return true;
            } else {
               throw SerialParseException.afr();
            }
         }
      }
   }

   public byte readByte() {
      if (this.bufferSize == this.bufferPos) {
         this.ensureBytes(true);
      }

      return this.buffer[this.bufferSize++];
   }

   public byte[] readBytes(int var1) {
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.totalConsumed + this.bufferSize + var1 > this.limit) {
         this.skipBytes(this.limit - this.totalConsumed - this.bufferSize);
         throw SerialParseException.afj();
      }

      if (var1 <= this.bufferPos - this.bufferSize) {
         byte[] var11 = new byte[var1];
         System.arraycopy(this.buffer, this.bufferSize, var11, 0, var1);
         this.bufferSize += var1;
         return var11;
      }

      if (var1 < this.buffer.length) {
         byte[] var10 = new byte[var1];
         int var12 = this.bufferPos - this.bufferSize;
         System.arraycopy(this.buffer, this.bufferSize, var10, 0, var12);
         this.bufferSize = this.bufferPos;
         this.ensureBytes(true);

         while (var1 - var12 > this.bufferPos) {
            System.arraycopy(this.buffer, 0, var10, var12, this.bufferPos);
            var12 += this.bufferPos;
            this.bufferSize = this.bufferPos;
            this.ensureBytes(true);
         }

         System.arraycopy(this.buffer, 0, var10, var12, var1 - var12);
         this.bufferSize = var1 - var12;
         return var10;
      } else {
         int var2 = this.bufferSize;
         int var3 = this.bufferPos;
         this.totalConsumed = this.totalConsumed + this.bufferPos;
         this.bufferSize = 0;
         this.bufferPos = 0;
         int var4 = var1 - (var3 - var2);
         ArrayList var5 = new ArrayList();

         while (var4 > 0) {
            byte[] var6 = new byte[Math.min(var4, this.buffer.length)];
            int var7 = 0;

            while (var7 < var6.length) {
               int var8 = this.input == null ? -1 : this.input.read(var6, var7, var6.length - var7);
               if (var8 == -1) {
                  throw SerialParseException.afj();
               }

               this.totalConsumed += var8;
               var7 += var8;
            }

            var4 -= var6.length;
            var5.add(var6);
         }

         byte[] var13 = new byte[var1];
         int var14 = var3 - var2;
         System.arraycopy(this.buffer, var2, var13, 0, var14);

         for (byte[] var9 : var5) {
            System.arraycopy(var9, 0, var13, var14, var9.length);
            var14 += var9.length;
         }

         return var13;
      }
   }

   public void skipBytes(int var1) {
      if (var1 < 0) {
         throw SerialParseException.negativeSize();
      }

      if (this.totalConsumed + this.bufferSize + var1 > this.limit) {
         this.skipBytes(this.limit - this.totalConsumed - this.bufferSize);
         throw SerialParseException.afj();
      }

      if (var1 <= this.bufferPos - this.bufferSize) {
         this.bufferSize += var1;
      } else {
         int var2 = this.bufferPos - this.bufferSize;
         this.bufferSize = this.bufferPos;
         this.ensureBytes(true);

         while (var1 - var2 > this.bufferPos) {
            var2 += this.bufferPos;
            this.bufferSize = this.bufferPos;
            this.ensureBytes(true);
         }

         this.bufferSize = var1 - var2;
      }
   }

   @Override
   public int readTag(SerialType var1) {
      if (this.isAtEnd()) {
         this.lastTag = 0;
         return 0;
      }

      if (this.isInDelimited()) {
         if (this.delimitedEnd < this.position()) {
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

         this.delimitedEnd = this.position() + var1;
      }
   }

   @Override
   public byte[] readByteArray() {
      int var1 = this.readVarInt();
      if (var1 <= this.bufferPos - this.bufferSize && var1 > 0) {
         byte[] var2 = new byte[var1];
         System.arraycopy(this.buffer, this.bufferSize, var2, 0, var1);
         this.bufferSize += var1;
         return var2;
      } else {
         return this.readBytes(var1);
      }
   }

   @Override
   public void skipField(int var1, SerialType var2) {
      this.skipUnknownField(this.lastTag);
   }

   @Override
   public void copyBytesTo(SerialWriter var1, boolean var2, int var3, boolean var4) {
      int var5 = this.readVarInt();
      if (var5 <= this.bufferPos - this.bufferSize && var5 > 0) {
         var1.writeBytesRange(var2, var3, this.buffer, this.bufferSize, var5, var4);
         this.bufferSize += var5;
      } else {
         var1.writeBytesRange(var2, var3, this.readBytes(var5), 0, var5, var4);
      }
   }

   public int getLastTag() {
      return this.lastTag;
   }

   @Override
   public ByteBuffer readByteBuffer() {
      return ByteBuffer.wrap(this.readByteArray());
   }
}
