package dev.lone.itemsadder.entity;

public class EntityPlacementSpec {
   private final CustomEntityDefinition OB;
   private final float OC;
   private final float OD;
   private final float OE;
   private final float OF;
   private final float OG;
   private final float OH;
   private final float field13;
   private final float OJ;
   private final float OK;
   private final float OL;
   private final float OM;
   private final float ON;
   private final float field14;
   private final float OQ;
   private final float OR;
   private final boolean OS;

   private EntityPlacementSpec(
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
      float var13,
      float var14,
      float var15,
      float var16,
      boolean var17
   ) {
      this.OB = var1;
      this.OC = var2;
      this.OD = var3;
      this.OE = var4;
      this.OF = var5;
      this.OG = var6;
      this.OH = var7;
      this.field13 = var8;
      this.OJ = var9;
      this.OK = var10;
      this.OL = var11;
      this.OM = var12;
      this.ON = var13;
      this.field14 = var14;
      this.OQ = var15;
      this.OR = var16;
      this.OS = var17;
   }

   public CustomEntityDefinition definition() {
      return this.OB;
   }

   public float tQ() {
      return this.OC;
   }

   public float oE() {
      return this.OD;
   }

   public float oF() {
      return this.OE;
   }

   public float tR() {
      return this.OF;
   }

   public float oL() {
      return this.OG;
   }

   public float oG() {
      return this.OH;
   }

   public float oH() {
      return this.field13;
   }

   public float method53() {
      return this.OJ;
   }

   public float oJ() {
      return this.OK;
   }

   public float oS() {
      return this.OL;
   }

   public float oT() {
      return this.OM;
   }

   public float tS() {
      return this.ON;
   }

   public float oM() {
      return this.field14;
   }

   public float oN() {
      return this.OQ;
   }

   public float method54() {
      return this.OR;
   }

   public boolean tT() {
      return this.OS;
   }
}
