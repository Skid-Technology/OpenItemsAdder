package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootH extends EntityMatcherBase {
   public final double ZI;
   public final boolean ZJ;

   @Generated
   protected EntityMatcherRootH(MatcherNodeH var1) {
      super(var1);
      this.ZI = var1.Ze;
      this.ZJ = var1.ZJ;
   }

   @Generated
   public static MatcherNodeH getNode() {
      return new MatcherNodeHImpl();
   }
}
