package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.script.CompilationResult;
import dev.lone.itemsadder.script.CompiledScript;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ScriptRunEffect extends ItemEffect {
   public final String akh;
   CompiledScript iY;

   public ScriptRunEffect(String var1, CompiledScript var2) {
      this.akh = var1;
      this.iY = var2;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      try {
         CompilationResult var5 = ContentManager.method8084().field90.method2293(this.iY, Main.getInstance(), var1, var4, CustomStack.fromInternal(var2), var3);
         if (var5.KI()) {
            if (var5.KH() != null) {
               Log.warn("Error executing script: " + this.akh);

               for (String var9 : var5.KH()) {
                  Log.warn(var9);
               }
            } else if (var5.KG() != null) {
               Log.warn("Error executing script: " + this.akh);
               Log.warn("StackTrace: ", var5.KG());
            }

            return EffectOutcome.success();
         }

         if (var5.KF() > 1000L) {
            Log.warn("[Scripts] '" + this.akh + "' took too long to execute: " + var5.KF() + "ms");
            return EffectOutcome.success();
         }
      } catch (Exception var10) {
         Log.warn("Unexpected executing script: " + this.akh);
         var10.printStackTrace();
         return EffectOutcome.success();
      }

      return EffectOutcome.failureWith(var3);
   }
}
