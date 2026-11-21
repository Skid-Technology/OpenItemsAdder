package dev.lone.itemsadder.util.serial;

public interface SerialSliceWriter {
   int writeChunk(SerialWriterBase var1, byte[] var2, int var3, int var4);

   int writeTwoChunks(SerialWriterBase var1, byte[] var2, int var3, int var4, byte[] var5, int var6, int var7);

   int flushSlice(SerialWriterBase var1, SerialByteSlice var2, byte[] var3, int var4, int var5);
}
