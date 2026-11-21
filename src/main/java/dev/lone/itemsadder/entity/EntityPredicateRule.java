package dev.lone.itemsadder.entity;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class EntityPredicateRule {
   @Nullable
   private final EntityType YT;
   @Nullable
   private final LivingEntityPredicate YU;

   public EntityPredicateRule(@Nullable EntityType var1, @Nullable LivingEntityPredicate var2) {
      this.YT = var1;
      this.YU = var2;
   }

   @Nullable
   public EntityType entityType() {
      return this.YT;
   }

   @Nullable
   public LivingEntityPredicate predicate() {
      return this.YU;
   }
}
