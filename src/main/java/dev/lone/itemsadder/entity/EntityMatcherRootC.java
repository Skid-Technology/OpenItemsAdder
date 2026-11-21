package dev.lone.itemsadder.entity;

import lombok.Generated;

public final class EntityMatcherRootC extends EntityMatcherBase {
   public final MobGoalSettings Zb;

   @Generated
   protected EntityMatcherRootC(MatcherNodeC var1) {
      super(var1);
      this.Zb = var1.Zb;
   }

   @Generated
   public static MatcherNodeC getNode() {
      return new MatcherNodeCImpl();
   }
}
