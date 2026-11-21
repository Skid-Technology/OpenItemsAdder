package dev.lone.itemsadder.entity;

import lombok.Generated;

public class EntityTypeMatcherB extends EntityTypeMatcher {
   public final int YV;
   public final boolean YW;
   public final boolean YX;

   @Generated
   protected EntityTypeMatcherB(EntityMatcherBuilderWithFlags var1) {
      super(var1);
      this.YV = var1.YV;
      this.YW = var1.YW;
      this.YX = var1.YX;
   }

   @Generated
   public static EntityMatcherBuilderWithFlags getBuilder() {
      return new EntityMatcherBuilderWithFlagsImpl();
   }
}
