package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.Log;

public class ModelConverterBlockFaces extends ModelConverterCommon {
   public ModelConverterBlockFaces(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectTextureModels(ItemSpecWithTexture var1) {
      throw new IllegalArgumentException("Shield graphics cannot be created with textures. Use models instead: " + this.bHi.Eo());
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      JSONObject var3 = var2.getJSONObject("model");
      String var4 = this.getModelId("blocking");
      if (var4 == null) {
         Log.error("Shield graphics must have 'blocking' status model for blocking animation: " + this.bHi.Eo());
      }

      var3.fluentPut("on_true", new JSONObject().fluentPut("model", var4).fluentPut("type", "model"))
         .fluentPut("on_false", new JSONObject().fluentPut("model", this.bHj.bHo).fluentPut("type", "model"));
      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }
}
