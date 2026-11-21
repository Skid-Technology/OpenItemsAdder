package dev.lone.itemsadder.util;

public class DoubleQuad {
   private final double a;
   private final double b;
   private final double c;
   private final double d;

   private DoubleQuad(double var1, double var3, double var5, double var7) {
      this.a = var1;
      this.b = var3;
      this.c = var5;
      this.d = var7;
   }

   public double getA() {
      return this.a;
   }

   public double getB() {
      return this.b;
   }

   public double getC() {
      return this.c;
   }

   public double getD() {
      return this.d;
   }
}
