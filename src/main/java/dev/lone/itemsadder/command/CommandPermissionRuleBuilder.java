package dev.lone.itemsadder.command;

import dev.lone.itemsadder.core.EffectChainRule;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public abstract class CommandPermissionRuleBuilder {
   @Generated
   private String iJ;
   @Generated
   private Long iK;
   @Generated
   private String iL;
   @Generated
   private EffectChainRule iM;
   @Generated
   private boolean iN;

   public CommandPermissionRuleBuilder ad(String var1) {
      this.iL = var1;
      if (var1 == null || !var1.startsWith("!") && !var1.startsWith("-")) {
         this.iN = false;
      } else {
         this.iN = true;
         this.iL = var1.substring(1);
      }

      return this.cu();
   }

   @Generated
   public CommandPermissionRuleBuilder ae(String var1) {
      this.iJ = var1;
      return this.cu();
   }

   @Generated
   public CommandPermissionRuleBuilder method5015(@Nullable Long var1) {
      this.iK = var1;
      return this.cu();
   }

   @Generated
   public CommandPermissionRuleBuilder method5016(@Nullable EffectChainRule var1) {
      this.iM = var1;
      return this.cu();
   }

   @Generated
   public CommandPermissionRuleBuilder method5017(boolean var1) {
      this.iN = var1;
      return this.cu();
   }

   @Generated
   protected abstract CommandPermissionRuleBuilder cu();

   @Generated
   public abstract CommandPermissionRule cv();

   @Generated
   @Override
   public String toString() {
      return "AbstractEventAction.AbstractEventActionBuilder(key="
         + this.iJ
         + ", delay="
         + this.iK
         + ", permission="
         + this.iL
         + ", flowModifier="
         + this.iM
         + ", permissionInverted="
         + this.iN
         + ")";
   }
}
