package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class SerialRecordG implements SerialRecord {
   private Map data;
   private int presence;
   public static final SerialType TYPE = SerialTypeG.INSTANCE;

   private SerialRecordG self() {
      if (this.data == null) {
         this.data = new ConcurrentHashMap();
      }

      return this;
   }

   public static SerialRecordG create() {
      return createWith(true);
   }

   public static SerialRecordG createWith(boolean var0) {
      return var0 ? new SerialRecordG().self() : new SerialRecordG();
   }

   public Map getData() {
      return this.data;
   }

   public SerialRecordG withData(Map var1) {
      this.data = var1;
      return this;
   }

   public SerialRecordG putEntry(Integer var1, String var2) {
      this.data.put(var1, var2);
      return this;
   }

   public int dataSize() {
      return this.data.size();
   }

   public boolean hasDataKey(Integer var1) {
      return this.data.containsKey(var1);
   }

   public boolean containsDataValue(String var1) {
      return this.data.containsValue(var1);
   }

   public String getDataValue(Integer var1) {
      return (String)this.data.get(var1);
   }

   public String removeDataValue(Integer var1) {
      return (String)this.data.remove(var1);
   }

   public boolean removeDataValueIf(Integer var1, String var2) {
      return this.data.remove(var1, var2);
   }

   @Override
   public SerialType getType() {
      return SerialTypeG.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordG var2 = (SerialRecordG)obj;
         return Objects.equals(this.data, var2.data);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      byte var1 = 1;
      return 31 * var1 + (this.data == null ? 0 : this.data.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (!this.data.isEmpty()) {
         var1.add("data=" + this.getData());
      }

      return "PPalette{" + String.join(", ", var1) + "}";
   }
}
