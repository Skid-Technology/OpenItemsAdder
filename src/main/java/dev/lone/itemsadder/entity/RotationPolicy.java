package dev.lone.itemsadder.entity;

public enum RotationPolicy {
   COMPATIBLE("compatible"),
   SINGLE_AXIS_45("single_axis_45"),
   UNRESTRICTED("unrestricted");

   private final String key;

   RotationPolicy(String var3) {
      this.key = var3;
   }

   public String getKey() {
      return this.key;
   }

   public static RotationPolicy fromId(int var0) {
      return switch (var0) {
         case 771, 772, 773 -> SINGLE_AXIS_45;
         case 774, 775, 776, 777 -> UNRESTRICTED;
         default -> COMPATIBLE;
      };
   }

   public static RotationPolicy fromKey(String var0) {
      for (RotationPolicy var4 : values()) {
         if (var4.key.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown geometry profile '" + var0 + "'");
   }
}
