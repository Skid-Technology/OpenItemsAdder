package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.JsonHelper;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public abstract class ModelConverterBase {
   final ItemSpec bHi;
   ModelDefinition bHj;
   Map bHk;
   @Nullable
   JSONObject bHl;
   JSONObject bHm;
   Map bHn = Map.of();

   public ModelConverterBase(ItemSpec var1) {
      this.bHi = var1;
   }

   void applyBaseLayerModel() {
      if (this.bHi.En()) {
         ModelJsonAsset var1 = ModelJsonAsset.forName("generated");
         if (var1.hasError()) {
            throw new IllegalArgumentException("Original model has errors: " + var1.getPath(), var1.getError());
         }

         JSONObject var2 = var1.copyAsObject();
         JSONObject var3 = var2.getJSONObject("textures");
         if (var3 == null) {
            var3 = new JSONObject();
            var2.put("textures", var3);
         }

         var3.put("layer0", this.bHi.Du());
         this.bHl = var2;
      }
   }

   void wrapInDisplayContextSelect(JSONObject var1, String var2) {
      JSONObject var3 = JsonHelper.copyObject(var1);
      if (var3.containsKey("fallback")) {
         var3 = var3.getJSONObject("fallback");
      }

      var1.clear();
      var1.fluentPut("type", "select")
         .fluentPut("property", "display_context")
         .fluentPut(
            "cases",
            new JSONArray()
               .fluentAdd(
                  new JSONObject()
                     .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var2))
                     .fluentPut("when", new JSONArray().fluentAdd("gui").fluentAdd("ground").fluentAdd("fixed"))
               )
         )
         .fluentPut("fallback", var3);
   }

   abstract void collectModels();

   abstract void buildMaterialJson();

   public ModelConversionResult convert() {
      this.collectModels();
      this.buildMaterialJson();
      this.applyBaseLayerModel();
      return new ModelConversionResult(this.bHj, this.bHk, this.bHl, this.bHm, this.bHn);
   }

   void applyItemProperties(JSONObject var1) {
      if (this.bHi.Eq() != null) {
         var1.put("hand_animation_on_swap", this.bHi.Eq());
      }

      if (this.bHi.Er() != null) {
         var1.put("oversized_in_gui", this.bHi.Er());
      }

      if (this.bHi.Es() != null) {
         JSONObject var2 = var1.getJSONObject("model");
         if (var2 != null) {
            JSONArray var3 = new JSONArray();
            var2.put("tints", var3);
            var3.add(new JSONObject().fluentPut("type", "custom_model_data").fluentPut("default", this.bHi.Es()));
            if (this.bHi.Et() != null) {
               var3.add(new JSONObject().fluentPut("type", "custom_model_data").fluentPut("default", this.bHi.Et()));
            }
         }
      }
   }
}
