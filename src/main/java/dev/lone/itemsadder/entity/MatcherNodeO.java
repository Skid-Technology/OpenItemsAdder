package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeO extends MatcherBuilderBase {
   @Generated
   private double Ze;
   @Generated
   private int ZW;
   @Generated
   private float ZX;

   @Generated
   public MatcherNodeO withRange(double var1) {
      this.Ze = var1;
      return this.yP();
   }

   @Generated
   public MatcherNodeO withMin(int var1) {
      this.ZW = var1;
      return this.yP();
   }

   @Generated
   public MatcherNodeO withScale(float var1) {
      this.ZX = var1;
      return this.yP();
   }

   @Generated
   protected abstract MatcherNodeO yP();

   @Generated
   public abstract EntityMatcherRootO getRoot();

   @Generated
   @Override
   public String toString() {
      return "RangedBowAttackGoal.RangedBowAttackGoalBuilder(super="
         + super.toString()
         + ", speedModifier="
         + this.Ze
         + ", attackIntervalMin="
         + this.ZW
         + ", attackRadius="
         + this.ZX
         + ")";
   }
}
