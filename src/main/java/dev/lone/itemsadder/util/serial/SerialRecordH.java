package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Objects;

public final class SerialRecordH implements SerialRecord {
   private int key;
   private String value;
   private int presence;
   public static final SerialType TYPE = SerialTypeH.INSTANCE;

   private SerialRecordH self() {
      return this;
   }

   public static SerialRecordH create() {
      return createWith(true);
   }

   public static SerialRecordH createWith(boolean var0) {
      return var0 ? new SerialRecordH().self() : new SerialRecordH();
   }

   public int getKey() {
      return (this.presence & 1) == 0 ? 0 : this.key;
   }

   public SerialRecordH withKey(int var1) {
      this.key = var1;
      this.presence |= 1;
      return this;
   }

   public SerialRecordH clearKey() {
      this.key = 0;
      this.presence &= -2;
      return this;
   }

   public boolean hasKey() {
      return (this.presence & 1) == 1;
   }

   public String getValue() {
      return (this.presence & 2) == 0 ? null : this.value;
   }

   public SerialRecordH withValue(String var1) {
      this.value = var1;
      this.presence |= 2;
      return this;
   }

   public SerialRecordH clearValue() {
      this.value = null;
      this.presence &= -3;
      return this;
   }

   public boolean hasValue() {
      return (this.presence & 2) == 2;
   }

   @Override
   public SerialType getType() {
      return SerialTypeH.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordH var2 = (SerialRecordH)obj;
         return !Objects.equals(this.key, var2.key) ? false : Objects.equals(this.value, var2.value);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 1;
      var1 = 31 * var1 + Integer.hashCode(this.key);
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

      return "DataEntry{" + String.join(", ", var1) + "}";
   }
}
