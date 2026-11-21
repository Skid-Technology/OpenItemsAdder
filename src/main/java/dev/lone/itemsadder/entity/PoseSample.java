package dev.lone.itemsadder.entity;

import java.util.Objects;

public class PoseSample {
   private final CustomEntityDefinition BM;
   private final float BN;
   private final float BO;
   private final float BP;
   private final float BQ;
   private final float BR;
   private final float BS;
   private final float BT;
   private final float BU;
   private final float BV;
   private final float BW;
   private final float BX;
   private final float BY;

   public PoseSample(
      CustomEntityDefinition var1,
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
      float var13
   ) {
      Objects.requireNonNull(var1);
      this.BM = var1;
      this.BN = var2;
      this.BO = var3;
      this.BP = var4;
      this.BQ = var5;
      this.BR = var6;
      this.BS = var7;
      this.BT = var8;
      this.BU = var9;
      this.BV = var10;
      this.BW = var11;
      this.BX = var12;
      this.BY = var13;
   }

   public PoseSample(CustomEntityDefinition var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var2, var3, var3, var3, 0.0F);
   }

   public PoseSample(CustomEntityDefinition var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var3, var3, 0.0F);
   }

   public CustomEntityDefinition definition() {
      return this.BM;
   }

   public float oD() {
      return this.BN;
   }

   public float oE() {
      return this.BO;
   }

   public float oF() {
      return this.BP;
   }

   public float oG() {
      return this.BQ;
   }

   public float oH() {
      return this.BR;
   }

   public float method53() {
      return this.BS;
   }

   public float oJ() {
      return this.BT;
   }

   public float oK() {
      return this.BU;
   }

   public float oL() {
      return this.BV;
   }

   public float oM() {
      return this.BW;
   }

   public float oN() {
      return this.BX;
   }

   public float method54() {
      return this.BY;
   }
}
