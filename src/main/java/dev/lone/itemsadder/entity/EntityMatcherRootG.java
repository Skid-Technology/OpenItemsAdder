package dev.lone.itemsadder.entity;

import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class EntityMatcherRootG extends EntityMatcherBase {
   @Nullable
   public final EntityTypePairRule[] ZD;
   @Nullable
   public final EntityTypePairRule[] ZE;

   @Generated
   protected EntityMatcherRootG(MatcherNodeG var1) {
      super(var1);
      this.ZD = var1.ZD;
      this.ZE = var1.ZE;
   }

   @Generated
   public static MatcherNodeG getNode() {
      return new MatcherNodeGImpl();
   }
}
