package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootM extends EntityMatcherBase {
   public final int ZQ;
   public final int ZR;

   @Generated
   protected EntityMatcherRootM(MatcherNodeM var1) {
      super(var1);
      this.ZQ = var1.ZQ;
      this.ZR = var1.ZR;
   }

   @Generated
   public static MatcherNodeM getNode() {
      return new MatcherNodeMImpl();
   }
}
