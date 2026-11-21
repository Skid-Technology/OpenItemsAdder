package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeN extends MatcherBuilderBase {
   @Generated
   private double Ze;
   @Generated
   private int YV;
   @Generated
   private boolean ZU;

   @Generated
   public MatcherNodeN withDistance(double var1) {
      this.Ze = var1;
      return this.yK();
   }

   @Generated
   public MatcherNodeN withCount(int var1) {
      this.YV = var1;
      return this.yK();
   }

   @Generated
   public MatcherNodeN withEnabled(boolean var1) {
      this.ZU = var1;
      return this.yK();
   }

   @Generated
   protected abstract MatcherNodeN yK();

   @Generated
   public abstract EntityMatcherRootN getRoot();

   @Generated
   @Override
   public String toString() {
      return "RandomWanderAround.RandomWanderAroundBuilder(super="
         + super.toString()
         + ", speedModifier="
         + this.Ze
         + ", interval="
         + this.YV
         + ", checkNoActionTime="
         + this.ZU
         + ")";
   }
}
