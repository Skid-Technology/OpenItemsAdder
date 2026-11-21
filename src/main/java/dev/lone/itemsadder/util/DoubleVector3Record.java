package dev.lone.itemsadder.util;

public class DoubleVector3Record {
   private final double x;
   private final double y;
   private final double z;

   private DoubleVector3Record(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
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
}
