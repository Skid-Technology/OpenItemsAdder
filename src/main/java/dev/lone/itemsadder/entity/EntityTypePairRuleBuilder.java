package dev.lone.itemsadder.entity;

import lombok.Generated;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

@Generated
public class EntityTypePairRuleBuilder {
   @Generated
   private EntityType bO;
   @Generated
   private LivingEntityPredicate YU;

   @Generated
   EntityTypePairRuleBuilder() {
   }

   @Generated
   public EntityTypePairRuleBuilder method9578(EntityType var1) {
      this.bO = var1;
      return this;
   }

   @Generated
   public EntityTypePairRuleBuilder method9579(@Nullable LivingEntityPredicate var1) {
      this.YU = var1;
      return this;
   }

   @Generated
   public EntityTypePairRule yd() {
      return new EntityTypePairRule(this.bO, this.YU);
   }

   @Generated
   @Override
   public String toString() {
      return "HurtByTargetGoal.Entry.EntryBuilder(type=" + this.bO + ", selector=" + this.YU + ")";
   }
}
