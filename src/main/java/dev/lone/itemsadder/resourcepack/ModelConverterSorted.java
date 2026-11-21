package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.Log;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class ModelConverterSorted extends ModelConverterCommon {
   public ModelConverterSorted(ItemSpec var1) {
      super(var1);
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      JSONObject var3 = var2.getJSONObject("model");
      var3.getJSONObject("on_false").put("model", this.bHj.bHo);
      JSONObject var4 = var3.getJSONObject("on_true");
      JSONArray var5 = var4.getJSONArray("entries");
      var5.clear();
      List var6 = this.bHk
         .entrySet()
         .stream()
         .filter(var0 -> ((String)var0.getKey()).startsWith("pulling_"))
         .sorted(Comparator.comparingInt(var0 -> Integer.parseInt(((String)var0.getKey()).substring("pulling_".length()))))
         .toList();
      String var7 = this.getModelId("pulling_0");
      if (var7 != null) {
         JSONObject var8 = var4.getJSONObject("fallback");
         var8.put("model", var7);
      }

      int var14 = var6.size() - 1;
      int var9 = 0;

      for (Entry var11 : var6) {
         if (!((String)var11.getKey()).equals("pulling_0")) {
            double var12 = (double)(var9 + 1) / (var14 + 1);
            var5.fluentAdd(
               new JSONObject()
                  .fluentPut("threshold", var12)
                  .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", ((ModelDefinition)var11.getValue()).bHo))
            );
            var9++;
         }
      }

      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }
}
