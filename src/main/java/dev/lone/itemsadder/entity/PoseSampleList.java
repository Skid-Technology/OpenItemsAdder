package dev.lone.itemsadder.entity;

import java.util.List;

public class PoseSampleList {
   private final List BZ;
   private final float Ca;
   private final float Cb;
   private final float Cc;
   private final float Cd;
   private final float Ce;
   private final float Cf;
   private final float Cg;
   private final float Ch;
   private final float Ci;
   private final float Cj;
   private final float Ck;
   private final float Cl;
   private final float Cm;

   public PoseSampleList(
      List var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14
   ) {
      var1 = List.copyOf(var1);
      this.BZ = var1;
      this.Ca = var2;
      this.Cb = var3;
      this.Cc = var4;
      this.Cd = var5;
      this.Ce = var6;
      this.Cf = var7;
      this.Cg = var8;
      this.Ch = var9;
      this.Ci = var10;
      this.Cj = var11;
      this.Ck = var12;
      this.Cl = var13;
      this.Cm = var14;
   }

   public PoseSampleList(List var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var4, var4, 0.0F);
   }

   public List samples() {
      return this.BZ;
   }

   public float oQ() {
      return this.Ca;
   }

   public float oR() {
      return this.Cb;
   }

   public float oE() {
      return this.Cc;
   }

   public float oF() {
      return this.Cd;
   }

   public float oG() {
      return this.Ce;
   }

   public float oH() {
      return this.Cf;
   }

   public float method53() {
      return this.Cg;
   }

   public float oJ() {
      return this.Ch;
   }

   public float oS() {
      return this.Ci;
   }

   public float oT() {
      return this.Cj;
   }

   public float oM() {
      return this.Ck;
   }

   public float oN() {
      return this.Cl;
   }

   public float method54() {
      return this.Cm;
   }
}
