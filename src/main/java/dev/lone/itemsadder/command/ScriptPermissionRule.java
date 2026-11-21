package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.script.CompilationResult;
import dev.lone.itemsadder.script.CompiledScript;
import dev.lone.itemsadder.util.Log;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ScriptPermissionRule extends AbstractCommandPermissionRule {
   final String iX;
   final CompiledScript iY;

   @Override
   public PermissionRuleOutcome method4941(Event var1, CustomEntityImpl var2, @Nullable Player var3) {
      try {
         CompilationResult var4 = ContentManager.method8084()
            .field90
            .method2294(this.iY, Main.getInstance(), var1, var3, CustomEntity.byAlreadySpawned(var2.getLivingEntity()));
         if (var4.KI()) {
            if (var4.KH() != null) {
               Log.warn("Error executing script: " + this.iX);

               for (String var8 : var4.KH()) {
                  Log.warn(var8);
               }
            } else if (var4.KG() != null) {
               Log.warn("Error executing script: " + this.iX);
               Log.warn("StackTrace: ", var4.KG());
            }

            return PermissionRuleOutcome.cy();
         }

         if (var4.KF() > 1000L) {
            Log.warn("[Scripts] '" + this.iX + "' took too long to execute: " + var4.KF() + "ms");
            return PermissionRuleOutcome.cy();
         }
      } catch (Exception var9) {
         Log.warn("Unexpected executing script: " + this.iX);
         var9.printStackTrace();
         return PermissionRuleOutcome.cy();
      }

      return PermissionRuleOutcome.cz();
   }

   @Override
   public PermissionRuleOutcome method4942(Event var1, CustomEntityImpl var2) {
      return this.method4941(var1, var2, null);
   }

   @Generated
   protected ScriptPermissionRule(ScriptPermissionRuleBuilder var1) {
      super(var1);
      this.iX = var1.iX;
      this.iY = var1.iY;
   }

   @Generated
   public static ScriptPermissionRuleBuilder cI() {
      return new ScriptPermissionRuleBuilderImpl();
   }
}
