package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.JsonHelper;

public final class ModelJsonUtils {
   private ModelJsonUtils() {
   }

   public static JSONObject copyWithLayer0(JSONObject var0, String var1) {
      JSONObject var2 = JsonHelper.copyObject(var0);
      JSONObject var3 = var2.getJSONObject("textures");
      if (var3 == null) {
         var3 = new JSONObject();
         var2.put("textures", var3);
      }

      var3.put("layer0", var1);
      return var2;
   }

   public static JSONObject brokenConditionModel(String var0, String var1) {
      JSONObject var2 = new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var0);
      JSONObject var3 = new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var1);
      return new JSONObject()
         .fluentPut(
            "model",
            new JSONObject()
               .fluentPut("type", "minecraft:condition")
               .fluentPut("property", "minecraft:broken")
               .fluentPut("on_false", var2)
               .fluentPut("on_true", var3)
         );
   }
}
