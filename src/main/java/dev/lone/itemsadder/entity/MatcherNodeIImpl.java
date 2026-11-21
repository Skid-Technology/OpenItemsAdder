package dev.lone.itemsadder.entity;

import lombok.Generated;

@Generated
final class MatcherNodeIImpl extends MatcherNodeI {
   @Generated
   private MatcherNodeIImpl() {
   }

   @Generated
   protected MatcherNodeIImpl self() {
      return this;
   }

   @Generated
   @Override
   public EntityMatcherRootI getRoot() {
      return new EntityMatcherRootI(this);
   }
}
