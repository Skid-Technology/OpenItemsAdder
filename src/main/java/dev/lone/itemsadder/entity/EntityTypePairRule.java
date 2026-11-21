package dev.lone.itemsadder.entity;

import lombok.Generated;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityTypePairRule {
   EntityType bO;
   @Nullable
   LivingEntityPredicate YU;

   public boolean test(LivingEntity seeker, LivingEntity target) {
      return this.bO != null && target.getType() != this.bO ? false : this.YU == null || this.YU.test(seeker, target);
   }

   @Generated
   EntityTypePairRule(EntityType var1, @Nullable LivingEntityPredicate var2) {
      this.bO = var1;
      this.YU = var2;
   }

   @Generated
   public static EntityTypePairRuleBuilder yc() {
      return new EntityTypePairRuleBuilder();
   }
}
