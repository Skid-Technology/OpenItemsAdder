package dev.lone.itemsadder.config;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Consumer;

public final class CategoryTreeValidator {
   private CategoryTreeValidator() {
   }

   static Map method2545(Map var0, Consumer var1) {
      LinkedHashMap var2 = new LinkedHashMap(var0);
      var2.replaceAll((var2x, var3x) -> {
         if (var3x == null || var3x.isBlank()) {
            return null;
         }

         if (var0.containsKey(var3x)) {
            return (String)var3x;
         }

         var1.accept("Category '" + var2x + "' has unknown parent '" + var3x + "'; keeping it at the root.");
         return null;
      });
      HashSet var3 = new HashSet();

      for (String var5 : var2.keySet()) {
         LinkedHashSet var6 = new LinkedHashSet();
         String var7 = var5;

         while (var7 != null && !var3.contains(var7) && var6.add(var7)) {
            var7 = (String)var2.get(var7);
         }

         if (var7 != null && var6.contains(var7)) {
            boolean var8 = false;

            for (String var10 : var6) {
               if (var10.equals(var7)) {
                  var8 = true;
               }

               if (var8) {
                  var1.accept("Category '" + var10 + "' has a cyclic parent; keeping it at the root.");
                  var2.put(var10, null);
               }
            }
         }

         var3.addAll(var6);
      }

      return var2;
   }
}
