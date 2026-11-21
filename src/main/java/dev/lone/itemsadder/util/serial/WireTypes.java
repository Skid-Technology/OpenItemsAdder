package dev.lone.itemsadder.util.serial;

public final class WireTypes {
   public static final int TYPE_VARINT = 0;
   public static final int TYPE_FIXED64 = 1;
   public static final int TYPE_LENGTH_DELIMITED = 2;
   public static final int TYPE_START_GROUP = 3;
   public static final int TYPE_END_GROUP = 4;
   public static final int TYPE_FIXED32 = 5;
   public static final int bSn = 6;
   public static final int bSo = 7;
   static final int TAG_TYPE_BITS = 3;
   static final int TAG_TYPE_MASK = 7;
   static final int bSr = 1;
   static final int bSs = 2;
   static final int bSt = 3;
   static final int FIELD1_START_GROUP = makeTag(1, 3);
   static final int FIELD1_END_GROUP = makeTag(1, 4);
   static final int FIELD2_VARINT = makeTag(2, 0);
   static final int FIELD3_LENGTH_DELIMITED = makeTag(3, 2);

   private WireTypes() {
   }

   public static int getWireType(int var0) {
      return var0 & 7;
   }

   public static int getFieldNumber(int var0) {
      return var0 >>> 3;
   }

   public static int makeTag(int var0, int var1) {
      return var0 << 3 | var1;
   }
}
