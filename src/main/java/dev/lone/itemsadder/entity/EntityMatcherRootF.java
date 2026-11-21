package dev.lone.itemsadder.entity;

import java.util.List;
import lombok.Generated;

public class EntityMatcherRootF extends EntityMatcherBase {
   public final List Zd;
   public final double Ze;
   public final boolean Zf;

   @Generated
   protected EntityMatcherRootF(MatcherNodeF var1) {
      super(var1);
      this.Zd = var1.Zd;
      this.Ze = var1.Ze;
      this.Zf = var1.Zf;
   }

   @Generated
   public static MatcherNodeF getNode() {
      return new MatcherNodeFImpl();
   }
}
