package dev.lone.itemsadder.entity;

import lombok.Generated;

public class SizedEntityTypeMatcherB extends EntityTypeMatcher {
   public final float ZF;
   public final float ZG;
   public final boolean ZH;

   @Generated
   protected SizedEntityTypeMatcherB(SizedEntityMatcherBuilderB var1) {
      super(var1);
      this.ZF = var1.ZF;
      this.ZG = var1.qm;
      this.ZH = var1.ZH;
   }

   @Generated
   public static SizedEntityMatcherBuilderB yh() {
      return new SizedEntityMatcherBuilderBImpl();
   }
}
