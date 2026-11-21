package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeJ extends MatcherBuilderBase {
   @Generated
   private boolean ZN;

   @Generated
   public MatcherNodeJ withEnabled(boolean var1) {
      this.ZN = var1;
      return this.yu();
   }

   @Generated
   protected abstract MatcherNodeJ yu();

   @Generated
   public abstract EntityMatcherRootJ getRoot();

   @Generated
   @Override
   public String toString() {
      return "OpenDoorGoal.OpenDoorGoalBuilder(super=" + super.toString() + ", closeDoor=" + this.ZN + ")";
   }
}
