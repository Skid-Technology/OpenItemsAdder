package dev.lone.itemsadder.command;

import lombok.Generated;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

@Generated
public abstract class EntityPermissionRuleBuilder extends PermissionRuleBuilder {
   @Generated
   private String iZ;
   @Generated
   private EntityType ja;
   @Generated
   private Integer jb;

   @Generated
   public EntityPermissionRuleBuilder aj(@Nullable String var1) {
      this.iZ = var1;
      return this.cN();
   }

   @Generated
   public EntityPermissionRuleBuilder method5021(@Nullable EntityType var1) {
      this.ja = var1;
      return this.cN();
   }

   @Generated
   public EntityPermissionRuleBuilder method5022(@Nullable Integer var1) {
      this.jb = var1;
      return this.cN();
   }

   @Generated
   protected abstract EntityPermissionRuleBuilder cN();

   @Generated
   public abstract EntityPermissionRule cO();

   @Generated
   @Override
   public String toString() {
      return "SummonEntity.SummonEntityBuilder(super="
         + super.toString()
         + ", customEntityId="
         + this.iZ
         + ", bukkitEntityType="
         + this.ja
         + ", maxPerChunk="
         + this.jb
         + ")";
   }
}
