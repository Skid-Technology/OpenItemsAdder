package dev.lone.itemsadder.util.serial;

public enum SerialFieldKind {
   DOUBLE(SerialJavaType.DOUBLE, 1),
   FLOAT(SerialJavaType.FLOAT, 5),
   INT64(SerialJavaType.LONG, 0),
   UINT64(SerialJavaType.LONG, 0),
   INT32(SerialJavaType.INT, 0),
   FIXED64(SerialJavaType.LONG, 1),
   FIXED32(SerialJavaType.INT, 5),
   BOOL(SerialJavaType.BOOLEAN, 0),
   STRING("STRING", 8, SerialJavaType.STRING, 2),
   GROUP("GROUP", 9, SerialJavaType.MESSAGE, 3),
   MESSAGE("MESSAGE", 10, SerialJavaType.MESSAGE, 2),
   BYTES("BYTES", 11, SerialJavaType.BYTE_STRING, 2),
   UINT32(SerialJavaType.INT, 0),
   ENUM(SerialJavaType.ENUM, 0),
   SFIXED32(SerialJavaType.INT, 5),
   SFIXED64(SerialJavaType.LONG, 1),
   SINT32(SerialJavaType.INT, 0),
   SINT64(SerialJavaType.LONG, 0);

   public final SerialJavaType javaType;
   public final int wireType;

   SerialFieldKind(SerialJavaType var3, int var4) {
      this.javaType = var3;
      this.wireType = var4;
   }

   public SerialJavaType getJavaType() {
      return this.javaType;
   }

   public int getWireType() {
      return this.wireType;
   }

   public boolean isPackable() {
      return true;
   }
}
