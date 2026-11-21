package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class SizedEntityMatcherBuilderB extends EntityMatcherBuilder {
   @Generated
   private float ZF;
   @Generated
   private float qm;
   @Generated
   private boolean ZH;

   @Generated
   public SizedEntityMatcherBuilderB method9521(float var1) {
      this.ZF = var1;
      return this.yi();
   }

   @Generated
   public SizedEntityMatcherBuilderB method9522(float var1) {
      this.qm = var1;
      return this.yi();
   }

   @Generated
   public SizedEntityMatcherBuilderB ay(boolean var1) {
      this.ZH = var1;
      return this.yi();
   }

   @Generated
   protected abstract SizedEntityMatcherBuilderB yi();

   @Generated
   public abstract SizedEntityTypeMatcherB yj();

   @Generated
   @Override
   public String toString() {
      return "LookAtEntityGoal.LookAtEntityGoalBuilder(super="
         + super.toString()
         + ", range="
         + this.ZF
         + ", chance="
         + this.qm
         + ", lookForward="
         + this.ZH
         + ")";
   }
}
