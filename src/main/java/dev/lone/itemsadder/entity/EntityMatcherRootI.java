package dev.lone.itemsadder.entity;

import java.util.function.Predicate;
import lombok.Generated;

public class EntityMatcherRootI extends EntityMatcherBase {
   public final Predicate ZK;
   public final double ZL;
   public final int ZM;

   @Generated
   protected EntityMatcherRootI(MatcherNodeI var1) {
      super(var1);
      this.ZK = var1.Zc;
      this.ZL = var1.Ze;
      this.ZM = var1.ZM;
   }

   @Generated
   public static MatcherNodeI getNode() {
      return new MatcherNodeIImpl();
   }
}
