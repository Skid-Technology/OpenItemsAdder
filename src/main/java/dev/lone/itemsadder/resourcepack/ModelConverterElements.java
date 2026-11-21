package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.Log;

public class ModelConverterElements extends ModelConverterCommon {
   public ModelConverterElements(ItemSpec var1) {
      super(var1);
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
      JSONObject var2 = var1.copyAsObject();
      ModelDefinition var3 = (ModelDefinition)this.bHk.get("cast");
      if (var3 == null) {
         throw new IllegalArgumentException("Fishing rod must have 'cast' status model");
      }

      JSONObject var4 = var2.getJSONObject("model");
      var4.getJSONObject("on_false").put("model", this.bHj.bHo);
      var4.getJSONObject("on_true").put("model", var3.bHo);
      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }
}
