package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SerialRecordE implements SerialRecord {
   private List chunks;
   private int presence;
   public static final SerialType TYPE = SerialTypeE.INSTANCE;

   private SerialRecordE self() {
      if (this.chunks == null) {
         this.chunks = Collections.synchronizedList(new ArrayList());
      }

      return this;
   }

   public static SerialRecordE create() {
      return createWith(true);
   }

   public static SerialRecordE createWith(boolean var0) {
      return var0 ? new SerialRecordE().self() : new SerialRecordE();
   }

   public List getChunks() {
      return this.chunks;
   }

   public SerialRecordE withChunks(List var1) {
      this.chunks = var1;
      return this;
   }

   public SerialRecordE addChunk(String var1) {
      this.chunks.add(var1);
      return this;
   }

   public SerialRecordE addAllChunks(List var1) {
      this.chunks.addAll(var1);
      return this;
   }

   public int chunkCount() {
      return this.chunks.size();
   }

   public String getChunk(int var1) {
      return (String)this.chunks.get(var1);
   }

   @Override
   public SerialType getType() {
      return SerialTypeE.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordE var2 = (SerialRecordE)obj;
         return Objects.equals(this.chunks, var2.chunks);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      byte var1 = 1;
      return 31 * var1 + (this.chunks == null ? 0 : this.chunks.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (!this.chunks.isEmpty()) {
         var1.add("chunk=" + this.getChunks());
      }

      return "PFixedGlitchedChunks{" + String.join(", ", var1) + "}";
   }
}
