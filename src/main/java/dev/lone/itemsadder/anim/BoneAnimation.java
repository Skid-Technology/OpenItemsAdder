package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.util.DoubleVector3;

public class BoneAnimation {
   private final String Ek;
   private final String El;
   private final String Em;
   private final DoubleVector3 En;
   private final DoubleVector3 Eo;
   private final DoubleVector3 Ep;
   private final DoubleVector3 Eq;
   private final Double Er;
   private final Double Es;
   private final Double Et;
   private final Double Eu;

   public BoneAnimation(
      String var1,
      String var2,
      String var3,
      DoubleVector3 var4,
      DoubleVector3 var5,
      DoubleVector3 var6,
      DoubleVector3 var7,
      Double var8,
      Double var9,
      Double var10,
      Double var11
   ) {
      this.Ek = var1;
      this.El = var2;
      this.Em = var3;
      this.En = var4;
      this.Eo = var5;
      this.Ep = var6;
      this.Eq = var7;
      this.Er = var8;
      this.Es = var9;
      this.Et = var10;
      this.Eu = var11;
   }

   public String qb() {
      return this.Ek;
   }

   public String qc() {
      return this.El;
   }

   public String pU() {
      return this.Em;
   }

   public DoubleVector3 qd() {
      return this.En;
   }

   public DoubleVector3 qe() {
      return this.Eo;
   }

   public DoubleVector3 qf() {
      return this.Ep;
   }

   public DoubleVector3 qg() {
      return this.Eq;
   }

   public Double qh() {
      return this.Er;
   }

   public Double qi() {
      return this.Es;
   }

   public Double qj() {
      return this.Et;
   }

   public Double qk() {
      return this.Eu;
   }
}
