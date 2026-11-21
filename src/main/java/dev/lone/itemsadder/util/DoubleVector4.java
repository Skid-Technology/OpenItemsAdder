package dev.lone.itemsadder.util;

public class DoubleVector4 {
   private final double x;
   private final double y;
   private final double z;
   private final double w;

   private DoubleVector4(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public double getW() {
      return this.w;
   }
}
