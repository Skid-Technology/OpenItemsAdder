package dev.lone.itemsadder.anim;

class BezierNode {
   private final double awE;
   private final double awF;
   private final double awG;
   private final double awH;
   private final double awI;

   private BezierNode(double var1, double var3, double var5, double var7, double var9) {
      this.awE = var1;
      this.awF = var3;
      this.awG = var5;
      this.awH = var7;
      this.awI = var9;
   }

   public double nr() {
      return this.awE;
   }

   public double Ib() {
      return this.awF;
   }

   public double Ic() {
      return this.awG;
   }

   public double Id() {
      return this.awH;
   }

   public double Ie() {
      return this.awI;
   }
}
