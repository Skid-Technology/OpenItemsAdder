package dev.lone.itemsadder.entity;

class ViewerVisibility {
   private final double vc;
   private final double vd;
   private final boolean ve;
   private final boolean visible;
   private final boolean vf;

   private ViewerVisibility(double var1, double var3, boolean var5, boolean var6, boolean var7) {
      this.vc = var1;
      this.vd = var3;
      this.ve = var5;
      this.visible = var6;
      this.vf = var7;
   }

   public double fm() {
      return this.vc;
   }

   public double lm() {
      return this.vd;
   }

   public boolean ln() {
      return this.ve;
   }

   public boolean method24() {
      return this.vf;
   }
}
