package dev.lone.itemsadder.entity;

import java.util.function.Predicate;
import lombok.Generated;

public class EntityMatcherRootD extends EntityMatcherBase {
   public final Predicate Zc;

   @Generated
   protected EntityMatcherRootD(MatcherNodeD var1) {
      super(var1);
      this.Zc = var1.Zc;
   }

   @Generated
   public static MatcherNodeD getNode() {
      return new MatcherNodeDImpl();
   }
}
