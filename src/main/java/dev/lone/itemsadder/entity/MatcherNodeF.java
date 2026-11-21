package dev.lone.itemsadder.entity;

import java.util.List;
import lombok.Generated;

@Generated
public abstract class MatcherNodeF extends MatcherBuilderBase {
   @Generated
   private List Zd;
   @Generated
   private double Ze;
   @Generated
   private boolean Zf;

   @Generated
   public MatcherNodeF withItems(List var1) {
      this.Zd = var1;
      return this.xS();
   }

   @Generated
   public MatcherNodeF withDistance(double var1) {
      this.Ze = var1;
      return this.xS();
   }

   @Generated
   public MatcherNodeF withEnabled(boolean var1) {
      this.Zf = var1;
      return this.xS();
   }

   @Generated
   protected abstract MatcherNodeF xS();

   @Generated
   public abstract EntityMatcherRootF getRoot();

   @Generated
   @Override
   public String toString() {
      return "FollowHeldItemGoal.FollowHeldItemGoalBuilder(super="
         + super.toString()
         + ", items="
         + this.Zd
         + ", speedModifier="
         + this.Ze
         + ", canScare="
         + this.Zf
         + ")";
   }
}
