package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Material;

public class ModelConverterSimple extends ModelConverterMapped {
   public ModelConverterSimple(ItemSpec var1) {
      super(var1);
   }

   @Override
   void buildMaterialJson() {
      MaterialJsonAsset var1 = new MaterialJsonAsset(Material.PAPER);
      JSONObject var2 = var1.copyAsObject();
      JSONObject var3 = var2.getJSONObject("model");
      if (!var3.getString("type").equals("minecraft:model")) {
         throw new IllegalArgumentException("Unsupported material " + this.bHi.Ep() + " for now. Use another one or suggest it on GitHub.");
      }

      var3.put("model", this.bHj.bHo);
      if (ItemStackUtils.al(this.bHi.Ep())) {
         var3.put("tints", new JSONArray().fluentAdd(new JSONObject().fluentPut("default", -6265536).fluentPut("type", "dye")));
      }

      if (this.bHi.En()) {
         Log.warn("'" + this.bHi.Ep() + "' do not support icons yet. Create the 'item_model' manually. Skipping icon generation for: " + this.bHi.Eo());
      }

      this.applyItemProperties(var2);
      this.bHm = var2;
   }
}
