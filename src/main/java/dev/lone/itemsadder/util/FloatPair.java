package dev.lone.itemsadder.util;

public class FloatPair {
   private final float first;
   private final float second;

   public FloatPair(float var1, float var2) {
      if (Float.isFinite(var1) && Float.isFinite(var2) && !(var1 <= 0.0F) && !(var2 <= 0.0F)) {
         this.first = var1;
         this.second = var2;
      } else {
         throw new IllegalArgumentException("controls.collision width and height must be finite positive numbers");
      }
   }

   public float getFirst() {
      return this.first;
   }

   public float getSecond() {
      return this.second;
   }
}
