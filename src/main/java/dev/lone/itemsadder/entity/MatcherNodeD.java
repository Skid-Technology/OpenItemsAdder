package dev.lone.itemsadder.entity;

import java.util.function.Predicate;
import lombok.Generated;

@Generated
public abstract class MatcherNodeD extends MatcherBuilderBase {
   @Generated
   private Predicate Zc;

   @Generated
   public MatcherNodeD withPredicate(Predicate var1) {
      this.Zc = var1;
      return this.xK();
   }

   @Generated
   protected abstract MatcherNodeD xK();

   @Generated
   public abstract EntityMatcherRootD getRoot();

   @Generated
   @Override
   public String toString() {
      return "EatBlockGoal.EatBlockGoalBuilder(super=" + super.toString() + ", bukkitPredicate=" + this.Zc + ")";
   }
}
