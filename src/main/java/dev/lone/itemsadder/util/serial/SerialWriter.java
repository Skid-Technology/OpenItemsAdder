package dev.lone.itemsadder.util.serial;

import java.nio.ByteBuffer;

public interface SerialWriter {
   void writeInt(int var1, int var2, boolean var3);

   void writeVarInt(int var1, int var2, boolean var3);

   void writeSignedVarInt(int var1, int var2, boolean var3);

   void writeFixed32(int var1, int var2, boolean var3);

   void writeSFixed32(int var1, int var2, boolean var3);

   void writeVarLong(int var1, long var2, boolean var4);

   void writeInt64(int var1, long var2, boolean var4);

   void writeSignedVarLong(int var1, long var2, boolean var4);

   void writeFixed64(int var1, long var2, boolean var4);

   void writeSFixed64(int var1, long var2, boolean var4);

   void writeFloat(int var1, float var2, boolean var3);

   void writeDouble(int var1, double var2, boolean var4);

   void writeBool(int var1, boolean var2, boolean var3);

   void writeEnum(int var1, int var2, boolean var3);

   void writeString(int var1, CharSequence var2, boolean var3);

   void writeSerialBytes(int var1, SerialBytes var2, boolean var3);

   void writeBytes(int var1, byte[] var2, boolean var3);

   void writeBytesRange(boolean var1, int var2, byte[] var3, int var4, int var5, boolean var6);

   void writeObject(int var1, Object var2, SerialType var3, boolean var4);

   void writeByteBuffer(int var1, ByteBuffer var2, boolean var3);
}
