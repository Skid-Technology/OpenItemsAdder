package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Objects;

public final class SerialRecordD implements SerialRecord {
   private String key;
   private SerialRecordB value;
   private int presence;
   public static final SerialType TYPE = SerialTypeD.INSTANCE;

   private SerialRecordD self() {
      return this;
   }

   public static SerialRecordD create() {
      return createWith(true);
   }

   public static SerialRecordD createWith(boolean var0) {
      return var0 ? new SerialRecordD().self() : new SerialRecordD();
   }

   public String getKey() {
      return (this.presence & 1) == 0 ? null : this.key;
   }

   public SerialRecordD withKey(String var1) {
      this.key = var1;
      this.presence |= 1;
      return this;
   }

   public SerialRecordD clearKey() {
      this.key = null;
      this.presence &= -2;
      return this;
   }

   public boolean hasKey() {
      return (this.presence & 1) == 1;
   }

   public SerialRecordB getValue() {
      return (this.presence & 2) == 0 ? null : this.value;
   }

   public SerialRecordD withValue(SerialRecordB var1) {
      this.value = var1;
      this.presence |= 2;
      return this;
   }

   public SerialRecordD clearValue() {
      this.value = null;
      this.presence &= -3;
      return this;
   }

   public boolean hasValue() {
      return (this.presence & 2) == 2;
   }

   @Override
   public SerialType getType() {
      return SerialTypeD.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordD var2 = (SerialRecordD)obj;
         return !Objects.equals(this.key, var2.key) ? false : Objects.equals(this.value, var2.value);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 1;
      var1 = 31 * var1 + (this.key == null ? 0 : this.key.hashCode());
      return 31 * var1 + (this.value == null ? 0 : this.value.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (this.hasKey()) {
         var1.add("key=" + this.getKey());
      }

      if (this.hasValue()) {
         var1.add("value=" + this.getValue());
      }

      return "BlockEntry{" + String.join(", ", var1) + "}";
   }
}
