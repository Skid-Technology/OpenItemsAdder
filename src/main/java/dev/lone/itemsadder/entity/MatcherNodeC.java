package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeC extends MatcherBuilderBase {
   @Generated
   private MobGoalSettings Zb;

   @Generated
   public MatcherNodeC withSettings(MobGoalSettings var1) {
      this.Zb = var1;
      return this.xG();
   }

   @Generated
   protected abstract MatcherNodeC xG();

   @Generated
   public abstract EntityMatcherRootC getRoot();

   @Generated
   @Override
   public String toString() {
      return "ConfiguredBehaviourGoal.ConfiguredBehaviourGoalBuilder(super=" + super.toString() + ", spec=" + this.Zb + ")";
   }
}
