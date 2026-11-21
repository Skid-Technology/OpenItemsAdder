package dev.lone.itemsadder.entity;

import lombok.Generated;

public class SizedEntityTypeMatcher extends EntityTypeMatcher {
   public final float field136;
   public final double YZ;
   public final double Za;

   @Generated
   protected SizedEntityTypeMatcher(SizedEntityMatcherBuilder var1) {
      super(var1);
      this.field136 = var1.EG;
      this.YZ = var1.YZ;
      this.Za = var1.Za;
   }

   @Generated
   public static SizedEntityMatcherBuilder getBuilder() {
      return new SizedEntityMatcherBuilderImpl();
   }
}
