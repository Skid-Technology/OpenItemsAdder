package dev.lone.itemsadder.util;

public enum NbtTagType {
   End(0),
   Byte(1),
   Short(2),
   Int(3),
   Long(4),
   Float(5),
   Double(6),
   ByteArray(7),
   String(8),
   List(9),
   Compound(10),
   IntArray(11),
   LongArray(12);

   private final int id;

   NbtTagType(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }

   public static NbtTagType fromId(int var0) {
      for (NbtTagType var4 : values()) {
         if (var4.getId() == var0) {
            return var4;
         }
      }

      return End;
   }
}
