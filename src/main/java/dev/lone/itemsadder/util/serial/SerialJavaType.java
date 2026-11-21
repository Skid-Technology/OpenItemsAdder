package dev.lone.itemsadder.util.serial;

public enum SerialJavaType {
   INT(0),
   LONG(0L),
   FLOAT(0.0F),
   DOUBLE(0.0),
   BOOLEAN(false),
   STRING(""),
   BYTE_STRING(SerialBytes.EMPTY),
   ENUM(null),
   MESSAGE(null);

   private final Object defaultValue;

   SerialJavaType(Object var3) {
      this.defaultValue = var3;
   }

   Object getDefaultValue() {
      return this.defaultValue;
   }
}
