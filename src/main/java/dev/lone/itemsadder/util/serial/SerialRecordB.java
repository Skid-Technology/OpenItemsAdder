package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Objects;

public final class SerialRecordB implements SerialRecord {
   private int intValue;
   private SerialRecordA attributes;
   private int presence;
   public static final SerialType TYPE = SerialTypeB.INSTANCE;

   private SerialRecordB self() {
      return this;
   }

   public static SerialRecordB create() {
      return createWith(true);
   }

   public static SerialRecordB createWith(boolean var0) {
      return var0 ? new SerialRecordB().self() : new SerialRecordB();
   }

   public int getIntValue() {
      return (this.presence & 1) == 0 ? 0 : this.intValue;
   }

   public SerialRecordB withIntValue(int var1) {
      this.intValue = var1;
      this.presence |= 1;
      return this;
   }

   public SerialRecordB clearIntValue() {
      this.intValue = 0;
      this.presence &= -2;
      return this;
   }

   public boolean hasIntValue() {
      return (this.presence & 1) == 1;
   }

   public SerialRecordA getAttributes() {
      return (this.presence & 2) == 0 ? null : this.attributes;
   }

   public SerialRecordB withAttributes(SerialRecordA var1) {
      this.attributes = var1;
      this.presence |= 2;
      return this;
   }

   public SerialRecordB clearAttributes() {
      this.attributes = null;
      this.presence &= -3;
      return this;
   }

   public boolean hasAttributes() {
      return (this.presence & 2) == 2;
   }

   @Override
   public SerialType getType() {
      return SerialTypeB.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordB var2 = (SerialRecordB)obj;
         return !Objects.equals(this.intValue, var2.intValue) ? false : Objects.equals(this.attributes, var2.attributes);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 1;
      var1 = 31 * var1 + Integer.hashCode(this.intValue);
      return 31 * var1 + (this.attributes == null ? 0 : this.attributes.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (this.hasIntValue()) {
         var1.add("blockId=" + this.getIntValue());
      }

      if (this.hasAttributes()) {
         var1.add("additionalProperties=" + this.getAttributes());
      }

      return "PBlock{" + String.join(", ", var1) + "}";
   }
}
