package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public interface SerialReader {
   void skipField(int var1, SerialType var2);

   int readTag(SerialType var1);

   int readInt32();

   int readUInt32();

   int readSInt32();

   int readFixed32();

   int readSFixed32();

   long readUInt64();

   long readInt64();

   long readSInt64();

   long readFixed64();

   long readSFixed64();

   float readFloat();

   double readDouble();

   boolean readBool();

   int readEnum();

   String readString();

   SerialBytes readSerialBytes();

   void readBytesInto(ByteBuffer var1);

   byte[] readByteArray();

   ByteBuffer readByteBuffer();

   Object readMessage(Object var1, SerialType var2);

   void copyBytesTo(SerialWriter var1, boolean var2, int var3, boolean var4);
}
