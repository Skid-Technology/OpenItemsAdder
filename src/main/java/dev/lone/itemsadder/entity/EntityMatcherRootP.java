package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootP extends EntityMatcherBase {
   public final String ZY;
   public final double field135;
   public final float aaa;
   public final float aab;
   public final int aac;

   @Generated
   protected EntityMatcherRootP(MatcherNodeP var1) {
      super(var1);
      this.ZY = var1.ZY;
      this.field135 = var1.field137;
      this.aaa = var1.aaa;
      this.aab = var1.aab;
      this.aac = var1.YV;
   }

   @Generated
   public static MatcherNodeP getNode() {
      return new MatcherNodePImpl();
   }
}
