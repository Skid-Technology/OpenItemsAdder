package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeM extends MatcherBuilderBase {
   @Generated
   private int ZQ;
   @Generated
   private int ZR;

   @Generated
   public MatcherNodeM withMin(int var1) {
      this.ZQ = var1;
      return this.yG();
   }

   @Generated
   public MatcherNodeM withMax(int var1) {
      this.ZR = var1;
      return this.yG();
   }

   @Generated
   protected abstract MatcherNodeM yG();

   @Generated
   public abstract EntityMatcherRootM getRoot();

   @Generated
   @Override
   public String toString() {
      return "RandomTeleportTimed.RandomTeleportTimedBuilder(super=" + super.toString() + ", minDelay=" + this.ZQ + ", maxDelay=" + this.ZR + ")";
   }
}
