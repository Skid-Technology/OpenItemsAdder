package dev.lone.itemsadder.util;

public class NullableBox {
   public Object value;

   NullableBox(Object var1) {
      this.value = var1;
   }

   public static NullableBox of(Object var0) {
      return new NullableBox(var0);
   }

   public static NullableBox empty() {
      return new NullableBox(null);
   }

   public boolean isPresent() {
      return this.value != null;
   }

   public void set(Object var1) {
      this.value = var1;
   }
}
