package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.Log;
import org.jetbrains.annotations.Nullable;

public class ModelConverterJsonBuilder extends ModelConverterCommon {
   public ModelConverterJsonBuilder(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectTextureModels(ItemSpecWithTexture var1) {
      super.collectTextureModels(var1);
      this.setParentOverride("open_back", "minecraft:item/template_bundle_open_back");
      this.setParentOverride("open_front", "minecraft:item/template_bundle_open_front");
   }

   private void setParentOverride(String var1, String var2) {
      ModelDefinition var3 = (ModelDefinition)this.bHk.get(var1);
      if (var3 != null && var3.bHq != null) {
         var3.bHq.put("parent", var2);
      }
   }

   @Override
   void buildMaterialJson() {
      if (this.bHj == null) {
         throw new IllegalArgumentException("Bundle must have a 'normal' status model");
      }

      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      ModelDefinition var3 = (ModelDefinition)this.bHk.get("filled");
      ModelDefinition var4 = (ModelDefinition)this.bHk.get("open_back");
      ModelDefinition var5 = (ModelDefinition)this.bHk.get("open_front");
      buildBundleModel(
         var2.getJSONObject("model"), this.bHj.bHo, var3 == null ? null : var3.bHo, var4 == null ? null : var4.bHo, var5 == null ? null : var5.bHo
      );
      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }

   static void buildBundleModel(JSONObject var0, String var1, @Nullable String var2, @Nullable String var3, @Nullable String var4) {
      var0.put("fallback", rangeFullnessModel(var1, var2));
      JSONObject var5 = var0.getJSONArray("cases").getJSONObject(0).getJSONObject("model");
      var5.put("on_false", rangeFullnessModel(var1, var2));
      JSONArray var6 = var5.getJSONObject("on_true").getJSONArray("models");
      if (var3 != null) {
         var6.getJSONObject(0).put("model", var3);
      }

      if (var4 != null) {
         var6.getJSONObject(var6.size() - 1).put("model", var4);
      }
   }

   private static JSONObject rangeFullnessModel(String var0, @Nullable String var1) {
      JSONObject var2 = modelReference(var0);
      return var1 == null
         ? var2
         : new JSONObject()
            .fluentPut("type", "minecraft:range_dispatch")
            .fluentPut("property", "minecraft:bundle/fullness")
            .fluentPut("entries", new JSONArray().fluentAdd(new JSONObject().fluentPut("threshold", 1.0E-7).fluentPut("model", modelReference(var1))))
            .fluentPut("fallback", var2);
   }

   private static JSONObject modelReference(String var0) {
      return new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var0);
   }
}
