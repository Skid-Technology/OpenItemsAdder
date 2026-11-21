package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityMatcherRootN extends EntityMatcherBase {
   public final double ZS;
   public final int ZT;
   public final boolean ZU;

   @Generated
   protected EntityMatcherRootN(MatcherNodeN var1) {
      super(var1);
      this.ZS = var1.Ze;
      this.ZT = var1.YV;
      this.ZU = var1.ZU;
   }

   @Generated
   public static MatcherNodeN getNode() {
      return new MatcherNodeNImpl();
   }
}
