package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootL extends EntityMatcherBase {
   public final boolean ZO;
   public final float ZP;

   @Generated
   protected EntityMatcherRootL(MatcherNodeL var1) {
      super(var1);
      this.ZO = var1.YW;
      this.ZP = var1.qm;
   }

   @Generated
   public static MatcherNodeL getNode() {
      return new MatcherNodeLImpl();
   }
}
