package dev.lone.itemsadder.entity;

import lombok.Generated;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public abstract class EntityTypeMatcher extends EntityMatcherBase {
   @Nullable
   public final EntityType YR;
   @Nullable
   public final LivingEntityPredicate YS;

   @Generated
   protected EntityTypeMatcher(EntityMatcherBuilder var1) {
      super(var1);
      this.YR = var1.vw;
      this.YS = var1.YS;
   }
}
