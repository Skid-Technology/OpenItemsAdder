package dev.lone.itemsadder.command;

import lombok.Generated;
import org.jetbrains.annotations.Nullable;

@Generated
public abstract class EventPermissionRuleBuilder extends PermissionRuleBuilder {
   @Generated
   private String iT;
   @Generated
   private boolean iU;
   @Generated
   private boolean iV;
   @Generated
   private String iW;

   @Generated
   public EventPermissionRuleBuilder af(String var1) {
      this.iT = var1;
      return this.cF();
   }

   @Generated
   public EventPermissionRuleBuilder method5018(boolean var1) {
      this.iU = var1;
      return this.cF();
   }

   @Generated
   public EventPermissionRuleBuilder method5019(boolean var1) {
      this.iV = var1;
      return this.cF();
   }

   @Generated
   public EventPermissionRuleBuilder ag(@Nullable String var1) {
      this.iW = var1;
      return this.cF();
   }

   @Generated
   protected abstract EventPermissionRuleBuilder cF();

   @Generated
   public abstract EventPermissionRule cG();

   @Generated
   @Override
   public String toString() {
      return "ExecuteCommand.ExecuteCommandBuilder(super="
         + super.toString()
         + ", command="
         + this.iT
         + ", asConsole="
         + this.iU
         + ", silent="
         + this.iV
         + ", flowSuccessMessageIfContains="
         + this.iW
         + ")";
   }
}
