package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;

public class ModelConverterMerged extends ModelConverterCommon {
   public ModelConverterMerged(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectTextureModels(ItemSpecWithTexture var1) {
      throw new IllegalArgumentException("Trident graphics cannot be created with textures. Use models instead: " + this.bHi.Eo());
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      JSONObject var3 = var2.getJSONObject("model");
      JSONObject var4 = var3.getJSONObject("fallback");
      String var5 = this.getModelId("throwing");
      if (var5 == null) {
         Log.error("Trident graphics must have 'throwing' status model for throwing animation: " + this.bHi.Eo());
      }

      JSONObject var6 = new JSONObject().fluentPut("model", var5).fluentPut("type", "model");
      JSONObject var7 = new JSONObject().fluentPut("model", this.bHj.bHo).fluentPut("type", "model");
      copyTransformation(var4, var6);
      copyTransformation(var4, var7);
      var4.fluentPut("on_true", var6).fluentPut("on_false", var7);
      if (this.bHi.Em() != null) {
         this.wrapInDisplayContextSelect(var3, this.bHi.Em());
      } else {
         this.wrapInDisplayContextSelect(var3, this.bHj.bHo);
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }

   static void copyTransformation(JSONObject var0, JSONObject var1) {
      JSONObject var2 = var0.getJSONObject("transformation");
      if (var2 != null) {
         var1.put("transformation", JsonHelper.copyObject(var2));
      }
   }
}
