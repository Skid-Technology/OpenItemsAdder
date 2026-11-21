package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class SizedEntityMatcherBuilder extends EntityMatcherBuilder {
   @Generated
   private float EG;
   @Generated
   private double YZ;
   @Generated
   private double Za;

   @Generated
   public SizedEntityMatcherBuilder withScale(float var1) {
      this.EG = var1;
      return this.xu();
   }

   @Generated
   public SizedEntityMatcherBuilder withMinWidth(double var1) {
      this.YZ = var1;
      return this.xu();
   }

   @Generated
   public SizedEntityMatcherBuilder withMinHeight(double var1) {
      this.Za = var1;
      return this.xu();
   }

   @Generated
   protected abstract SizedEntityMatcherBuilder xu();

   @Generated
   public abstract SizedEntityTypeMatcher buildTypeMatcher();

   @Generated
   @Override
   public String toString() {
      return "AvoidEntityGoal.AvoidEntityGoalBuilder(super="
         + super.toString()
         + ", maxDistance="
         + this.EG
         + ", walkSpeedModifier="
         + this.YZ
         + ", sprintSpeedModifier="
         + this.Za
         + ")";
   }
}
