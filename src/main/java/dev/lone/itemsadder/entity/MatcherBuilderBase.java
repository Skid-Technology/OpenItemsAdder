package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherBuilderBase {
   @Generated
   private int zk;
   @Generated
   private boolean YQ;

   @Generated
   public MatcherBuilderBase withPriority(int var1) {
      this.zk = var1;
      return this.xj();
   }

   @Generated
   public MatcherBuilderBase withFlag(boolean var1) {
      this.YQ = var1;
      return this.xj();
   }

   @Generated
   protected abstract MatcherBuilderBase xj();

   @Generated
   public abstract EntityMatcherBase build();

   @Generated
   @Override
   public String toString() {
      return "AbstractGoal.AbstractGoalBuilder(priority=" + this.zk + ", replaceVanilla=" + this.YQ + ")";
   }
}
