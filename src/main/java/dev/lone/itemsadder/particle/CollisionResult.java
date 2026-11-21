package dev.lone.itemsadder.particle;

class CollisionResult {
   private final double avd;
   private final double ave;
   private final double avf;
   private final int avg;

   CollisionResult(double var1, double var3, double var5, int var7) {
      this.avd = var1;
      this.ave = var3;
      this.avf = var5;
      this.avg = var7;
   }

   public double sv() {
      return this.avd;
   }

   public double HX() {
      return this.ave;
   }

   public double sw() {
      return this.avf;
   }

   public int HY() {
      return this.avg;
   }
}
