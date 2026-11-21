package dev.lone.itemsadder.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ListCollector {
   public static List collectModelNames(Object var0) {
      ArrayList var1 = new ArrayList();
      collectModels(var0, var1);

      for (int var2 = 0; var2 < var1.size(); var2++) {
         String var3 = (String)var1.get(var2);
         if (!var3.contains(":")) {
            var1.set(var2, "minecraft:" + var3);
         }
      }

      return var1;
   }

   private static void collectModels(Object var0, List var1) {
      if (var0 instanceof JSONObject var2) {
         for (Entry var5 : var2.entrySet()) {
            String var6 = (String)var5.getKey();
            Object var7 = var5.getValue();
            if ("model".equals(var6) && var7 instanceof String) {
               var1.add((String)var7);
            }

            collectModels(var7, var1);
         }
      } else if (var0 instanceof JSONArray) {
         for (Object var9 : (JSONArray)var0) {
            collectModels(var9, var1);
         }
      }
   }
}
