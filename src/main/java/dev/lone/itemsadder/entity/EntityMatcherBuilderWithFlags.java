package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
public abstract class EntityMatcherBuilderWithFlags extends EntityMatcherBuilder {
   @Generated
   private int YV;
   @Generated
   private boolean YW;
   @Generated
   private boolean YX;

   @Generated
   public EntityMatcherBuilderWithFlags be(int var1) {
      this.YV = var1;
      return this.xq();
   }

   @Generated
   public EntityMatcherBuilderWithFlags av(boolean var1) {
      this.YW = var1;
      return this.xq();
   }

   @Generated
   public EntityMatcherBuilderWithFlags aw(boolean var1) {
      this.YX = var1;
      return this.xq();
   }

   @Generated
   protected abstract EntityMatcherBuilderWithFlags xq();

   @Generated
   public abstract EntityTypeMatcherB xr();

   @Generated
   @Override
   public String toString() {
      return "AttackTargetGoal.AttackTargetGoalBuilder(super="
         + super.toString()
         + ", interval="
         + this.YV
         + ", checkVisibility="
         + this.YW
         + ", checkCanNavigate="
         + this.YX
         + ")";
   }
}
