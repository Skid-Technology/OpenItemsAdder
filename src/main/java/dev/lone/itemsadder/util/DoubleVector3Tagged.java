package dev.lone.itemsadder.util;

public class DoubleVector3Tagged {
   private final double x;
   private final double y;
   private final double z;
   private final int tag;

   public DoubleVector3Tagged(double var1, double var3, double var5, int var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.tag = var7;
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

   public int getTag() {
      return this.tag;
   }
}
