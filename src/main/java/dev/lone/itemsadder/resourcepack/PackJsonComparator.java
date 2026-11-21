package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.Comparator;

public class PackJsonComparator implements Comparator {
   public int compareCustomModelData(JSONObject var1, JSONObject var2) {
      try {
         int var3 = -100;
         int var4 = -100;
         if (var1.getJSONObject("predicate").containsKey("custom_model_data")) {
            var3 = var1.getJSONObject("predicate").getInteger("custom_model_data");
         }

         if (var2.getJSONObject("predicate").containsKey("custom_model_data")) {
            var4 = var2.getJSONObject("predicate").getInteger("custom_model_data");
         }

         return Integer.compare(var3, var4);
      } catch (Exception var5) {
         var5.printStackTrace();
         return 0;
      }
   }
}
