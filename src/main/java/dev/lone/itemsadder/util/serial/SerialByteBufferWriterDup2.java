package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public final class SerialByteBufferWriterDup2 implements SerialWriter {
   public SerialByteBuffer buffer;

   public SerialByteBufferWriterDup2() {
      this.buffer = new SerialByteBuffer();
   }

   public SerialByteBufferWriterDup2(SerialByteBuffer var1) {
      this.buffer = var1;
   }

   @Override
   public void writeInt(int var1, int var2, boolean var3) {
      if (var2 < 0) {
         this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
         this.buffer.writeVarLong(var2);
      } else {
         this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
         this.buffer.writeVarInt(var2);
      }
   }

   @Override
   public void writeVarInt(int var1, int var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeVarInt(var2);
   }

   @Override
   public void writeSignedVarInt(int var1, int var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeVarInt(SerialByteSliceWriter.zigZag32(var2));
   }

   @Override
   public void writeFixed32(int var1, int var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 5));
      this.buffer.writeIntLE(var2);
   }

   @Override
   public void writeSFixed32(int var1, int var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 5));
      this.buffer.writeIntLE(var2);
   }

   @Override
   public void writeVarLong(int var1, long var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeVarLong(var2);
   }

   @Override
   public void writeInt64(int var1, long var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeVarLong(var2);
   }

   @Override
   public void writeSignedVarLong(int var1, long var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeVarLong(SerialByteSliceWriter.zigZag64(var2));
   }

   @Override
   public void writeFixed64(int var1, long var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 1));
      this.buffer.writeLongLE(var2);
   }

   @Override
   public void writeSFixed64(int var1, long var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 1));
      this.buffer.writeLongLE(var2);
   }

   @Override
   public void writeFloat(int var1, float var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 5));
      this.buffer.writeIntLE(Float.floatToRawIntBits(var2));
   }

   @Override
   public void writeDouble(int var1, double var2, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 1));
      this.buffer.writeLongLE(Double.doubleToRawLongBits(var2));
   }

   @Override
   public void writeBool(int var1, boolean var2, boolean var3) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 0));
      this.buffer.writeByte((byte)(var2 ? 1 : 0));
   }

   @Override
   public void writeEnum(int var1, int var2, boolean var3) {
      this.writeInt(var1, var2, var3);
   }

   @Override
   public void writeString(int var1, CharSequence var2, boolean var3) {
      byte[] var4 = var2.toString().getBytes("UTF-8");
      this.writeBytes(var1, var4, var3);
   }

   @Override
   public void writeSerialBytes(int var1, SerialBytes var2, boolean var3) {
      this.writeBytes(var1, var2.getBytes(), var3);
   }

   @Override
   public void writeBytes(int var1, byte[] var2, boolean var3) {
      this.writeBytesRange(false, var1, var2, 0, var2.length, var3);
   }

   @Override
   public void writeBytesRange(boolean var1, int var2, byte[] var3, int var4, int var5, boolean var6) {
      this.buffer.writeVarInt(WireTypes.makeTag(var2, 2));
      this.buffer.writeVarInt(var5);
      this.buffer.writeBytes(var3, var4, var5);
   }

   @Override
   public void writeObject(int var1, Object var2, SerialType var3, boolean var4) {
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 3));
      var3.write(this, var2);
      this.buffer.writeVarInt(WireTypes.makeTag(var1, 4));
   }

   @Override
   public void writeByteBuffer(int var1, ByteBuffer var2, boolean var3) {
      this.writeBytesRange(false, var1, var2.array(), var2.arrayOffset() + var2.position(), var2.remaining(), var3);
   }
}
