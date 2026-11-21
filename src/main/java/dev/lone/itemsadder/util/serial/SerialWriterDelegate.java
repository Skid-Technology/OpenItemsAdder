package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public class SerialWriterDelegate implements SerialWriter {
   protected final SerialWriter delegate;

   public SerialWriterDelegate(SerialWriter var1) {
      this.delegate = var1;
   }

   @Override
   public void writeBool(int var1, boolean var2, boolean var3) {
      this.delegate.writeBool(var1, var2, var3);
   }

   @Override
   public void writeBytes(int var1, byte[] var2, boolean var3) {
      this.delegate.writeBytes(var1, var2, var3);
   }

   @Override
   public void writeBytesRange(boolean var1, int var2, byte[] var3, int var4, int var5, boolean var6) {
      this.delegate.writeBytesRange(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public void writeSerialBytes(int var1, SerialBytes var2, boolean var3) {
      this.delegate.writeSerialBytes(var1, var2, var3);
   }

   @Override
   public void writeDouble(int var1, double var2, boolean var4) {
      this.delegate.writeDouble(var1, var2, var4);
   }

   @Override
   public void writeEnum(int var1, int var2, boolean var3) {
      this.delegate.writeEnum(var1, var2, var3);
   }

   @Override
   public void writeFixed32(int var1, int var2, boolean var3) {
      this.delegate.writeFixed32(var1, var2, var3);
   }

   @Override
   public void writeFixed64(int var1, long var2, boolean var4) {
      this.delegate.writeFixed64(var1, var2, var4);
   }

   @Override
   public void writeFloat(int var1, float var2, boolean var3) {
      this.delegate.writeFloat(var1, var2, var3);
   }

   @Override
   public void writeInt(int var1, int var2, boolean var3) {
      this.delegate.writeInt(var1, var2, var3);
   }

   @Override
   public void writeVarLong(int var1, long var2, boolean var4) {
      this.delegate.writeVarLong(var1, var2, var4);
   }

   @Override
   public void writeObject(int var1, Object var2, SerialType var3, boolean var4) {
      this.delegate.writeObject(var1, var2, var3, var4);
   }

   @Override
   public void writeSFixed32(int var1, int var2, boolean var3) {
      this.delegate.writeSFixed32(var1, var2, var3);
   }

   @Override
   public void writeSFixed64(int var1, long var2, boolean var4) {
      this.delegate.writeSFixed64(var1, var2, var4);
   }

   @Override
   public void writeSignedVarInt(int var1, int var2, boolean var3) {
      this.delegate.writeSignedVarInt(var1, var2, var3);
   }

   @Override
   public void writeSignedVarLong(int var1, long var2, boolean var4) {
      this.delegate.writeSignedVarLong(var1, var2, var4);
   }

   @Override
   public void writeString(int var1, CharSequence var2, boolean var3) {
      this.delegate.writeString(var1, var2, var3);
   }

   @Override
   public void writeVarInt(int var1, int var2, boolean var3) {
      this.delegate.writeVarInt(var1, var2, var3);
   }

   @Override
   public void writeInt64(int var1, long var2, boolean var4) {
      this.delegate.writeInt64(var1, var2, var4);
   }

   @Override
   public void writeByteBuffer(int var1, ByteBuffer var2, boolean var3) {
      this.writeBytesRange(false, var1, var2.array(), var2.arrayOffset() + var2.position(), var2.remaining(), var3);
   }
}
