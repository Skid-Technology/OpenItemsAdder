package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class MatcherNodeL extends MatcherBuilderBase {
   @Generated
   private boolean YW;
   @Generated
   private float qm;

   @Generated
   public MatcherNodeL withEnabled(boolean var1) {
      this.YW = var1;
      return this.yC();
   }

   @Generated
   public MatcherNodeL withScale(float var1) {
      this.qm = var1;
      return this.yC();
   }

   @Generated
   protected abstract MatcherNodeL yC();

   @Generated
   public abstract EntityMatcherRootL getRoot();

   @Generated
   @Override
   public String toString() {
      return "RandomTeleportOnHit.RandomTeleportOnHitBuilder(super=" + super.toString() + ", checkVisibility=" + this.YW + ", chance=" + this.qm + ")";
   }
}
