package dev.lone.itemsadder.util.serial;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public final class StreamSerialWriter extends SerialWriterBase implements SerialWriter {
   public StreamSerialWriter(SerialByteSlice var1) {
      super(var1);
   }

   public StreamSerialWriter(SerialByteSlice var1, OutputStream var2) {
      super(var1, var2);
   }

   public StreamSerialWriter(SerialByteSlice var1, OutputStream var2, SerialSliceWriter var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public StreamSerialWriter restartWriter() {
      super.restart();
      return this;
   }

   @Override
   public void writeInt(int var1, int var2, boolean var3) {
      if (var2 < 0) {
         this.cursor = this.mode.method253(var2, this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
      } else {
         this.cursor = this.mode.method252(var2, this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
      }
   }

   @Override
   public void writeVarInt(int var1, int var2, boolean var3) {
      this.cursor = this.mode.method252(var2, this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeSignedVarInt(int var1, int var2, boolean var3) {
      this.cursor = this.mode.method252(SerialByteSliceWriter.zigZag32(var2), this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeFixed32(int var1, int var2, boolean var3) {
      this.cursor = this.mode.method248(var2, this, this.mode.method252(WireTypes.makeTag(var1, 5), this, this.cursor));
   }

   @Override
   public void writeSFixed32(int var1, int var2, boolean var3) {
      this.cursor = this.mode.method248(var2, this, this.mode.method252(WireTypes.makeTag(var1, 5), this, this.cursor));
   }

   @Override
   public void writeVarLong(int var1, long var2, boolean var4) {
      this.cursor = this.mode.method253(var2, this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeInt64(int var1, long var2, boolean var4) {
      this.cursor = this.mode.method253(var2, this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeSignedVarLong(int var1, long var2, boolean var4) {
      this.cursor = this.mode.method253(SerialByteSliceWriter.zigZag64(var2), this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeFixed64(int var1, long var2, boolean var4) {
      this.cursor = this.mode.method249(var2, this, this.mode.method252(WireTypes.makeTag(var1, 1), this, this.cursor));
   }

   @Override
   public void writeSFixed64(int var1, long var2, boolean var4) {
      this.cursor = this.mode.method249(var2, this, this.mode.method252(WireTypes.makeTag(var1, 1), this, this.cursor));
   }

   @Override
   public void writeFloat(int var1, float var2, boolean var3) {
      this.cursor = this.mode.method248(Float.floatToRawIntBits(var2), this, this.mode.method252(WireTypes.makeTag(var1, 5), this, this.cursor));
   }

   @Override
   public void writeDouble(int var1, double var2, boolean var4) {
      this.cursor = this.mode.method249(Double.doubleToRawLongBits(var2), this, this.mode.method252(WireTypes.makeTag(var1, 1), this, this.cursor));
   }

   @Override
   public void writeBool(int var1, boolean var2, boolean var3) {
      this.cursor = this.mode.method241((byte)(var2 ? 1 : 0), this, this.mode.method252(WireTypes.makeTag(var1, 0), this, this.cursor));
   }

   @Override
   public void writeEnum(int var1, int var2, boolean var3) {
      this.writeInt(var1, var2, var3);
   }

   @Override
   public void writeString(int var1, CharSequence var2, boolean var3) {
      this.cursor = this.mode.method260(var2, this, this.mode.method252(WireTypes.makeTag(var1, 2), this, this.cursor));
   }

   @Override
   public void writeSerialBytes(int var1, SerialBytes var2, boolean var3) {
      this.writeBytes(var1, var2.getBytes(), var3);
   }

   @Override
   public void writeBytes(int var1, byte[] var2, boolean var3) {
      this.cursor = this.mode
         .method240(var2, 0, var2.length, this, this.mode.method252(var2.length, this, this.mode.method252(WireTypes.makeTag(var1, 2), this, this.cursor)));
   }

   @Override
   public void writeBytesRange(boolean var1, int var2, byte[] var3, int var4, int var5, boolean var6) {
      this.cursor = this.mode
         .method240(var3, var4, var5, this, this.mode.method252(var5, this, this.mode.method252(WireTypes.makeTag(var2, 2), this, this.cursor)));
   }

   @Override
   public void writeObject(int var1, Object var2, SerialType var3, boolean var4) {
      this.cursor = this.mode.method252(WireTypes.makeTag(var1, 3), this, this.cursor);
      var3.write(this, var2);
      this.cursor = this.mode.method252(WireTypes.makeTag(var1, 4), this, this.cursor);
   }

   @Override
   public void writeByteBuffer(int var1, ByteBuffer var2, boolean var3) {
      this.writeBytesRange(false, var1, var2.array(), var2.arrayOffset() + var2.position(), var2.remaining(), var3);
   }
}
