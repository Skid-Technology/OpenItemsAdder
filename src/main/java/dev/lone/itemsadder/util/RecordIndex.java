package dev.lone.itemsadder.util;

import java.util.HashMap;

public class RecordIndex {
   public int version;
   public final HashMap headers = new HashMap();

   public RecordIndex() {
   }

   public RecordIndex(int var1) {
      this.version = var1;
   }

   public void setVersion(int var1) {
      this.version = var1;
   }

   public void putHeader(String var1, RecordHeader var2) {
      this.headers.put(var1, var2);
   }

   public RecordHeader getHeader(String var1) {
      return (RecordHeader)this.headers.get(var1);
   }

   public void clear() {
      this.version = 0;
      this.headers.clear();
   }

   RecordWriter createWriter() {
      return new RecordWriter(this, PersistentFileStore.MAX_HEADER_SIZE, true);
   }
}
