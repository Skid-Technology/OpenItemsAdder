package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
final class SizedEntityMatcherBuilderImpl extends SizedEntityMatcherBuilder {
   @Generated
   private SizedEntityMatcherBuilderImpl() {
   }

   @Generated
   protected SizedEntityMatcherBuilderImpl self() {
      return this;
   }

   @Generated
   @Override
   public SizedEntityTypeMatcher buildTypeMatcher() {
      return new SizedEntityTypeMatcher(this);
   }
}
