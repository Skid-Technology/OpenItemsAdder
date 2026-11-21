package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeP extends MatcherBuilderBase {
   @Generated
   private String ZY;
   @Generated
   private double field137;
   @Generated
   private float aaa;
   @Generated
   private float aab;
   @Generated
   private int YV;

   @Generated
   public MatcherNodeP withName(String var1) {
      this.ZY = var1;
      return this.yT();
   }

   @Generated
   public MatcherNodeP withDistance(double var1) {
      this.field137 = var1;
      return this.yT();
   }

   @Generated
   public MatcherNodeP withMinScale(float var1) {
      this.aaa = var1;
      return this.yT();
   }

   @Generated
   public MatcherNodeP withMaxScale(float var1) {
      this.aab = var1;
      return this.yT();
   }

   @Generated
   public MatcherNodeP withCount(int var1) {
      this.YV = var1;
      return this.yT();
   }

   @Generated
   protected abstract MatcherNodeP yT();

   @Generated
   public abstract EntityMatcherRootP getRoot();

   @Generated
   @Override
   public String toString() {
      return "ReusableVanillaGoal.ReusableVanillaGoalBuilder(super="
         + super.toString()
         + ", kind="
         + this.ZY
         + ", speed="
         + this.field137
         + ", first="
         + this.aaa
         + ", second="
         + this.aab
         + ", interval="
         + this.YV
         + ")";
   }
}
