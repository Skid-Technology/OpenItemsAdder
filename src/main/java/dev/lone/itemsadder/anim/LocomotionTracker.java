package dev.lone.itemsadder.anim;

public final class LocomotionTracker {
   static final double My = 0.4;
   static final double Mz = 4.0;
   static final double MA = 1.4;
   static final double MB = 0.01;
   private double MC;
   private double MD;
   private double Fx;
   private double Fy;
   private double ME;
   private double MF;
   private double MG;
   private double MH;
   private boolean MI;
   private double MJ = Double.NaN;
   private LocomotionType MK = LocomotionType.IDLE;
   private LocomotionSample ML = new LocomotionSample(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, LocomotionType.IDLE, 0.0, 0.0, 0.0);

   void qJ() {
      this.MD = 1.5;
      this.Fx = 1.5;
      this.Fy = 0.0;
      this.MI = true;
   }

   LocomotionSample method9076(double var1, double var3, double var5, boolean var7) {
      return this.method9077(var1, var3, var5, var7, false);
   }

   LocomotionSample method9077(double var1, double var3, double var5, boolean var7, boolean var8) {
      return this.method9078(var1, var3, var5, var7, var8, false);
   }

   LocomotionSample method9078(double var1, double var3, double var5, boolean var7, boolean var8, boolean var9) {
      boolean var10 = var7 || var9;
      this.ME = var10 ? Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5) : Math.hypot(var1, var5);
      double var11 = this.ME;
      if (var8 && !var10) {
         if (Double.isNaN(this.MJ)) {
            this.MJ = this.ML.rc() > 1.0E-12 ? this.ML.rc() : var11;
         }

         var11 = this.MJ;
      } else {
         this.MJ = Double.NaN;
      }

      double var13 = Math.min(var11 * 4.0, 1.0);
      this.MD = this.MD + (var13 - this.MD) * 0.4;
      this.MC = this.MC + this.MD;
      this.method9079(var13);
      if (this.MI && !this.tk()) {
         this.MI = false;
      }

      double var15 = this.ME;
      if (var15 > 1.0E-12) {
         this.MF = var1 / var15;
         this.MG = var10 ? var3 / var15 : 0.0;
         this.MH = var5 / var15;
         this.MK = var7 ? LocomotionType.FLY : (var9 ? LocomotionType.SWIM : LocomotionType.WALK);
      } else if (!this.tk()) {
         this.MK = LocomotionType.IDLE;
      }

      this.ML = new LocomotionSample(var11, var11 * 20.0, this.MC, this.MC, var13, this.MD, this.Fx, this.Fy, this.MK, this.MF, this.MG, this.MH);
      return this.ML;
   }

   private void method9079(double var1) {
      double var3 = this.Fx - var1;
      double var5 = this.Fy + 1.4 * var3;
      double var7 = Math.exp(-1.4);
      this.Fx = var1 + (var3 + var5) * var7;
      this.Fy = (this.Fy - 1.4 * var5) * var7;
      if (Math.abs(this.Fx - var1) < 1.0E-12 && Math.abs(this.Fy) < 1.0E-12) {
         this.Fx = var1;
         this.Fy = 0.0;
      }
   }

   LocomotionSample ti() {
      return this.ML;
   }

   double tj() {
      return this.ME;
   }

   boolean tk() {
      return this.Fx > 0.01 || Math.abs(this.Fy) > 0.01;
   }

   boolean tl() {
      return this.MI;
   }
}
