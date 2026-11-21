package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootJ extends EntityMatcherBase {
   public final boolean ZN;

   @Generated
   protected EntityMatcherRootJ(MatcherNodeJ var1) {
      super(var1);
      this.ZN = var1.ZN;
   }

   @Generated
   public static MatcherNodeJ getNode() {
      return new MatcherNodeJImpl();
   }
}
