package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeH extends MatcherBuilderBase {
   @Generated
   private double Ze;
   @Generated
   private boolean ZJ;

   @Generated
   public MatcherNodeH withRange(double var1) {
      this.Ze = var1;
      return this.ym();
   }

   @Generated
   public MatcherNodeH withEnabled(boolean var1) {
      this.ZJ = var1;
      return this.ym();
   }

   @Generated
   protected abstract MatcherNodeH ym();

   @Generated
   public abstract EntityMatcherRootH getRoot();

   @Generated
   @Override
   public String toString() {
      return "MeleeAttackGoal.MeleeAttackGoalBuilder(super=" + super.toString() + ", speedModifier=" + this.Ze + ", pauseWhenIdle=" + this.ZJ + ")";
   }
}
