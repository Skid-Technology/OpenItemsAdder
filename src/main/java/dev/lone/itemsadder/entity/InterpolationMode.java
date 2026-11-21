package dev.lone.itemsadder.entity;

public enum InterpolationMode {
   SMOOTH("smooth"),
   PRECISE("precise");

   private final String str;

   InterpolationMode(String var3) {
      this.str = var3;
   }

   public static InterpolationMode aY(String var0) {
      for (InterpolationMode var4 : values()) {
         if (var4.str.equals(var0)) {
            return var4;
         }
      }

      return PRECISE;
   }

   @Override
   public String toString() {
      return this.str;
   }
}
