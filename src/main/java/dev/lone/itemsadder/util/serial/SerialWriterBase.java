package dev.lone.itemsadder.util.serial;

import java.io.OutputStream;

public class SerialWriterBase {
   public final SerialByteSlice root;
   protected SerialByteSlice cursor;
   protected int written = 0;
   public final int sliceSize;
   public final OutputStream output;
   public final SerialSliceWriter sliceWriter;
   public final SerialWriteMode mode;

   public SerialWriterBase(SerialByteSlice var1) {
      this(var1, 512);
   }

   public SerialWriterBase(SerialByteSlice var1, int var2) {
      this.cursor = var1;
      this.root = var1;
      this.sliceSize = var2;
      this.output = null;
      this.sliceWriter = null;
      this.mode = SerialWriteMode.BUFFERED;
   }

   public SerialWriterBase(SerialByteSlice var1, OutputStream var2, SerialSliceWriter var3, int var4) {
      this.cursor = var1;
      this.root = var1;
      this.sliceSize = var4;
      this.output = var2;
      this.sliceWriter = var3;
      this.mode = SerialWriteMode.STREAMED;
      assert var2 != null;
   }

   public SerialWriterBase(SerialByteSlice var1, OutputStream var2) {
      this(var1, var2, null, 512);
   }

   public void reset() {
   }

   public SerialWriterBase restart() {
      this.cursor = this.root.rewind();
      this.written = 0;
      return this;
   }

   public final int size() {
      return this.written;
   }

   public final byte[] toByteArray() {
      SerialByteSlice var1 = this.root;
      int var2 = 0;
      byte[] var4 = new byte[this.written];

      do {
         int var3;
         if ((var3 = var1.offset - var1.start) > 0) {
            System.arraycopy(var1.bytes, var1.start, var4, var2, var3);
            var2 += var3;
         }
      } while ((var1 = var1.next) != null);

      return var4;
   }

   protected int writeChunk(byte[] var1, int var2, int var3) {
      if (this.sliceWriter != null) {
         return this.sliceWriter.writeChunk(this, var1, var2, var3);
      }

      this.output.write(var1, var2, var3);
      return var2;
   }

   protected int writeTwoChunks(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6) {
      if (this.sliceWriter != null) {
         return this.sliceWriter.writeTwoChunks(this, var1, var2, var3, var4, var5, var6);
      }

      this.output.write(var1, var2, var3);
      this.output.write(var4, var5, var6);
      return var2;
   }

   protected int flushSlice(SerialByteSlice var1, byte[] var2, int var3, int var4) {
      if (this.sliceWriter != null) {
         return this.sliceWriter.flushSlice(this, var1, var2, var3, var4);
      }

      this.output.write(var2, var3, var4);
      return var1.start;
   }
}
