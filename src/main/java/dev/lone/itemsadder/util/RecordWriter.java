package dev.lone.itemsadder.util;

import java.io.IOException;

public class RecordWriter extends PackedByteStream {
   private final RecordIndex index;
   private boolean locked;

   public RecordWriter(RecordIndex var1, int var2, boolean var3) {
      super(var2);
      this.index = var1;
      if (var3) {
         this.init();
      }
   }

   void init() {
      this.write(PersistentFileStore.MAGIC);
      this.skipBytes(4);
   }

   void writeEntry(String var1, int var2, int var3) {
      if (this.locked) {
         throw new IOException("This buffer is locked!");
      }

      byte[] var4 = var1.getBytes();
      this.write(BinaryIntCodec.toBytes(var4.length));
      this.write(var4);
      this.write(BinaryIntCodec.toBytes(var2));
      this.write(BinaryIntCodec.toBytes(var3));
      this.index.setVersion(this.size());
      this.index.putHeader(var1, new RecordHeader(var2, var3));
   }

   public void finish() {
      this.writeAt(BinaryIntCodec.toBytes(this.size()), PersistentFileStore.MAGIC.length);
      this.locked = true;
   }
}
