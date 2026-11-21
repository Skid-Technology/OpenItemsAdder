package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public class SerialReaderDelegate implements SerialReader {
   protected final SerialReader bQT;

   public SerialReaderDelegate(SerialReader var1) {
      this.bQT = var1;
   }

   @Override
   public void skipField(int var1, SerialType var2) {
      this.bQT.skipField(var1, var2);
   }

   @Override
   public int readTag(SerialType var1) {
      return this.bQT.readTag(var1);
   }

   @Override
   public boolean readBool() {
      return this.bQT.readBool();
   }

   @Override
   public byte[] readByteArray() {
      return this.bQT.readByteArray();
   }

   @Override
   public SerialBytes readSerialBytes() {
      return this.bQT.readSerialBytes();
   }

   @Override
   public void readBytesInto(ByteBuffer var1) {
      this.bQT.readBytesInto(var1);
   }

   @Override
   public double readDouble() {
      return this.bQT.readDouble();
   }

   @Override
   public int readEnum() {
      return this.bQT.readEnum();
   }

   @Override
   public int readFixed32() {
      return this.bQT.readFixed32();
   }

   @Override
   public long readFixed64() {
      return this.bQT.readFixed64();
   }

   @Override
   public float readFloat() {
      return this.bQT.readFloat();
   }

   @Override
   public int readInt32() {
      return this.bQT.readInt32();
   }

   @Override
   public long readUInt64() {
      return this.bQT.readUInt64();
   }

   @Override
   public int readSFixed32() {
      return this.bQT.readSFixed32();
   }

   @Override
   public long readSFixed64() {
      return this.bQT.readSFixed64();
   }

   @Override
   public int readSInt32() {
      return this.bQT.readSInt32();
   }

   @Override
   public long readSInt64() {
      return this.bQT.readSInt64();
   }

   @Override
   public String readString() {
      return this.bQT.readString();
   }

   @Override
   public int readUInt32() {
      return this.bQT.readUInt32();
   }

   @Override
   public long readInt64() {
      return this.bQT.readInt64();
   }

   @Override
   public Object readMessage(Object var1, SerialType var2) {
      return this.bQT.readMessage(var1, var2);
   }

   @Override
   public void copyBytesTo(SerialWriter var1, boolean var2, int var3, boolean var4) {
      this.bQT.copyBytesTo(var1, var2, var3, var4);
   }

   @Override
   public ByteBuffer readByteBuffer() {
      return ByteBuffer.wrap(this.readByteArray());
   }
}
