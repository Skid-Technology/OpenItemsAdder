package dev.lone.itemsadder.entity;

import java.util.function.Predicate;
import lombok.Generated;

@Generated
public abstract class MatcherNodeI extends MatcherBuilderBase {
   @Generated
   private Predicate Zc;
   @Generated
   private double Ze;
   @Generated
   private int ZM;

   @Generated
   public MatcherNodeI withPredicate(Predicate var1) {
      this.Zc = var1;
      return this.yq();
   }

   @Generated
   public MatcherNodeI withDistance(double var1) {
      this.Ze = var1;
      return this.yq();
   }

   @Generated
   public MatcherNodeI withCount(int var1) {
      this.ZM = var1;
      return this.yq();
   }

   @Generated
   protected abstract MatcherNodeI yq();

   @Generated
   public abstract EntityMatcherRootI getRoot();

   @Generated
   @Override
   public String toString() {
      return "MoveToBlockGoal.MoveToBlockGoalBuilder(super="
         + super.toString()
         + ", bukkitPredicate="
         + this.Zc
         + ", speedModifier="
         + this.Ze
         + ", searchRange="
         + this.ZM
         + ")";
   }
}
