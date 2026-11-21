package dev.lone.itemsadder.command;

import dev.lone.itemsadder.script.CompiledScript;
import lombok.Generated;

@Generated
public abstract class ScriptPermissionRuleBuilder extends PermissionRuleBuilder {
   @Generated
   private String iX;
   @Generated
   private CompiledScript iY;

   @Generated
   public ScriptPermissionRuleBuilder ah(String var1) {
      this.iX = var1;
      return this.cJ();
   }

   @Generated
   public ScriptPermissionRuleBuilder method5020(CompiledScript var1) {
      this.iY = var1;
      return this.cJ();
   }

   @Generated
   protected abstract ScriptPermissionRuleBuilder cJ();

   @Generated
   public abstract ScriptPermissionRule cK();

   @Generated
   @Override
   public String toString() {
      return "RunScript.RunScriptBuilder(super=" + super.toString() + ", scriptIdentifier=" + this.iX + ", compiled=" + this.iY + ")";
   }
}
