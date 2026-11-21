package dev.lone.itemsadder.particle;

import org.joml.Vector3d;

public class ParentPose {
   private final double axy;
   private final double axz;
   private final double axA;
   private final double axB;
   private final double axC;
   private final double axD;
   private final double axE;

   ParentPose(double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (Double.isFinite(var1)
         && Double.isFinite(var3)
         && Double.isFinite(var5)
         && Double.isFinite(var7)
         && Double.isFinite(var9)
         && Double.isFinite(var11)
         && Double.isFinite(var13)
         && !(Math.abs(var7 * var7 + var9 * var9 + var11 * var11 + var13 * var13 - 1.0) > 1.0E-8)) {
         this.axy = var1;
         this.axz = var3;
         this.axA = var5;
         this.axB = var7;
         this.axC = var9;
         this.axD = var11;
         this.axE = var13;
      } else {
         throw new IllegalArgumentException("Invalid particle parent pose");
      }
   }

   Vector3d method164(Vector3d var1) {
      double var2 = this.axE * var1.x + this.axC * var1.z - this.axD * var1.y;
      double var4 = this.axE * var1.y + this.axD * var1.x - this.axB * var1.z;
      double var6 = this.axE * var1.z + this.axB * var1.y - this.axC * var1.x;
      double var8 = -this.axB * var1.x - this.axC * var1.y - this.axD * var1.z;
      return var1.set(
         var2 * this.axE - var8 * this.axB - var4 * this.axD + var6 * this.axC,
         var4 * this.axE - var8 * this.axC - var6 * this.axB + var2 * this.axD,
         var6 * this.axE - var8 * this.axD - var2 * this.axC + var4 * this.axB
      );
   }

   Vector3d method165(Vector3d var1) {
      return this.method164(var1).add(this.axy, this.axz, this.axA);
   }

   public double ka() {
      return this.axy;
   }

   public double kb() {
      return this.axz;
   }

   public double kc() {
      return this.axA;
   }

   public double HG() {
      return this.axB;
   }

   public double method131() {
      return this.axC;
   }

   public double HI() {
      return this.axD;
   }

   public double HJ() {
      return this.axE;
   }
}
