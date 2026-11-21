package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.Log;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class ModelConverterOrdered extends ModelConverterCommon {
   public ModelConverterOrdered(ItemSpec var1) {
      super(var1);
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      JSONObject var3 = var2.getJSONObject("model");
      JSONObject var4 = var3.getJSONObject("fallback");
      var4.getJSONObject("on_false").put("model", this.bHj.bHo);
      JSONObject var5 = var4.getJSONObject("on_true");
      JSONArray var6 = var5.getJSONArray("entries");
      var6.clear();
      List var7 = this.bHk
         .entrySet()
         .stream()
         .filter(var0 -> ((String)var0.getKey()).startsWith("pulling_"))
         .sorted(Comparator.comparingInt(var0 -> Integer.parseInt(((String)var0.getKey()).substring("pulling_".length()))))
         .toList();
      String var8 = this.getModelId("pulling_0");
      if (var8 != null) {
         JSONObject var9 = var5.getJSONObject("fallback");
         var9.put("model", var8);
      }

      int var15 = var7.size() - 1;
      int var10 = 0;

      for (Entry var12 : var7) {
         if (!((String)var12.getKey()).equals("pulling_0")) {
            double var13 = (double)(var10 + 1) / (var15 + 1);
            var6.fluentAdd(
               new JSONObject()
                  .fluentPut("threshold", var13)
                  .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", ((ModelDefinition)var12.getValue()).bHo))
            );
            var10++;
         }
      }

      JSONArray var16 = var3.getJSONArray("cases");
      var16.clear();
      String var17 = this.getModelIdOrDefault("arrow", this.bHj.bHo);
      String var18 = this.getModelIdOrDefault("rocket", this.bHj.bHo);
      var16.fluentAdd(new JSONObject().fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var17)).fluentPut("when", "arrow"))
         .fluentAdd(new JSONObject().fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var18)).fluentPut("when", "rocket"));
      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }
}
