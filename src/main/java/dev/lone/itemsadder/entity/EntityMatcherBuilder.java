package dev.lone.itemsadder.entity;

import lombok.Generated;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

@Generated
public abstract class EntityMatcherBuilder extends MatcherBuilderBase {
   @Generated
   private EntityType vw;
   @Generated
   private LivingEntityPredicate YS;

   @Generated
   public EntityMatcherBuilder method9511(@Nullable EntityType var1) {
      this.vw = var1;
      return this.xl();
   }

   @Generated
   public EntityMatcherBuilder method9512(@Nullable LivingEntityPredicate var1) {
      this.YS = var1;
      return this.xl();
   }

   @Generated
   protected abstract EntityMatcherBuilder xl();

   @Generated
   public abstract EntityTypeMatcher xm();

   @Generated
   @Override
   public String toString() {
      return "AbstractTargetEntityGoal.AbstractTargetEntityGoalBuilder(super="
         + super.toString()
         + ", entityType="
         + this.vw
         + ", bukkitTargetSelector="
         + this.YS
         + ")";
   }
}
