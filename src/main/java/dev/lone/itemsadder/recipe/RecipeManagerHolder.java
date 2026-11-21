package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.entity.Player;

public final class RecipeManagerHolder {
   private final AbstractRecipeManager bon = (AbstractRecipeManager)NmsRegistry.method2777(AbstractRecipeManager.class);
   private static RecipeManagerHolder boo;

   RecipeManagerHolder() {
   }

   public static void method5354(Player var0, LinkedList var1) {
      if (var1 != null && !var1.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for (AbstractCustomRecipe var4 : var1) {
            if (PermissionNodes.method1620(var0, var4)) {
               var2.add(var4.api);
            }
         }

         if (!var2.isEmpty()) {
            method5357(var0, var2);
         }
      }
   }

   public static void method5355(Player var0, AbstractCustomRecipe var1) {
      method5356(var0, var1, true);
   }

   public static void method5356(Player var0, AbstractCustomRecipe var1, boolean var2) {
      if (var1 != null && (!var2 || PermissionNodes.method1620(var0, var1))) {
         method5357(var0, Collections.singletonList(var1.api));
      }
   }

   public static void cO(Player var0) {
      Qb().method597(var0, ContentManager.method8084().bp.Ka());
   }

   public static void method5357(Player var0, List var1) {
      Qb().method597(var0, var1);
   }

   private void register() {
      if (this.bon != null) {
         this.bon.register();
      } else {
         Log.error("Failed to register VanillaRecipebook");
      }
   }

   private static RecipeManagerHolder Qa() {
      if (boo == null) {
         boo = new RecipeManagerHolder();
      }

      return boo;
   }

   public static AbstractRecipeManager Qb() {
      return Qa().bon;
   }

   public static void Qc() {
      unregister();
      Qd();
      RecipeManagerHolder var0 = Qa();
      var0.register();
   }

   public static void unregister() {
      if (boo != null && boo.bon != null) {
         boo.bon.unregister();
      }

      Qe();
   }

   private static void Qd() {
      if (Main.config.getBoolean("recipes.hide_vanilla_unrecognized_recipe_errors")) {
         ((Logger)LogManager.getRootLogger()).addFilter(new RecipeLogFilter());
      }
   }

   private static void Qe() {
      Logger var0 = (Logger)LogManager.getRootLogger();
      var0.getFilters().forEachRemaining(var1 -> {
         if (var1.getClass().toString().contains("dev.lone.itemsadder")) {
            var0.get().removeFilter(var1);
         }
      });
   }

   public static String[] method5358(String[] var0, Map var1) {
      for (int var2 = 0; var2 < var0.length; var2++) {
         String var3 = var0[var2];
         StringBuilder var4 = new StringBuilder(var3.length());
         char[] var5 = var3.toCharArray();

         for (char var9 : var5) {
            var4.append(var1.get(var9) == null ? ' ' : var9);
         }

         var0[var2] = var4.toString();
      }

      return var0;
   }
}
