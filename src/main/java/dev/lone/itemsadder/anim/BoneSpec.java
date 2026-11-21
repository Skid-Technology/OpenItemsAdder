package dev.lone.itemsadder.anim;

import java.util.Set;

public class BoneSpec {
   private final String Fq;
   private final Set Fr;
   private final double Fs;
   private final double Ft;
   private final double Fu;
   private final double Fv;
   private final double Fw;
   private final double Fx;
   private final double Fy;
   private final String Fz;

   public BoneSpec(String var1, Set var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, String var17) {
      this.Fq = var1;
      this.Fr = var2;
      this.Fs = var3;
      this.Ft = var5;
      this.Fu = var7;
      this.Fv = var9;
      this.Fw = var11;
      this.Fx = var13;
      this.Fy = var15;
      this.Fz = var17;
   }

   public String ra() {
      return this.Fq;
   }

   public Set rb() {
      return this.Fr;
   }

   public double rc() {
      return this.Fs;
   }

   public double rd() {
      return this.Ft;
   }

   public double re() {
      return this.Fu;
   }

   public double rf() {
      return this.Fv;
   }

   public double rg() {
      return this.Fw;
   }

   public double rh() {
      return this.Fx;
   }

   public double ri() {
      return this.Fy;
   }

   public String rj() {
      return this.Fz;
   }
}
