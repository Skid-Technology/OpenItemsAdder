package dev.lone.itemsadder.command;

import dev.lone.itemsadder.core.EffectChainRule;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class CommandPermissionRule {
   final String iJ;
   @Nullable
   final Long iK;
   final String iL;
   @Nullable
   final EffectChainRule iM;
   final boolean iN;

   public boolean cp() {
      return this.iK != null && this.iK > 0L;
   }

   public boolean cq() {
      return this.iL != null;
   }

   public boolean method4940(Entity var1) {
      if (this.iL == null) {
         return true;
      } else {
         return this.iN ? !var1.hasPermission(this.iL) : var1.hasPermission(this.iL);
      }
   }

   @Generated
   protected CommandPermissionRule(CommandPermissionRuleBuilder var1) {
      this.iJ = var1.iJ;
      this.iK = var1.iK;
      this.iL = var1.iL;
      this.iM = var1.iM;
      this.iN = var1.iN;
   }

   @Generated
   public String cr() {
      return this.iJ;
   }

   @Nullable
   @Generated
   public Long cs() {
      return this.iK;
   }

   @Generated
   public String getPermission() {
      return this.iL;
   }

   @Nullable
   @Generated
   public EffectChainRule ct() {
      return this.iM;
   }
}
