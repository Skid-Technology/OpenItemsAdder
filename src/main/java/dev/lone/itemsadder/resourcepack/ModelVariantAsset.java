package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;

public class ModelVariantAsset extends JsonAssetWithCallbacks {
   public ModelVariantAsset(String var1) {
      super(var1);
   }

   public String getParent() {
      return ((JSONObject)this.bIG).getString("parent");
   }

   public boolean hasDisplay() {
      return ((JSONObject)this.bIG).containsKey("display");
   }

   public JSONObject getDisplay() {
      return ((JSONObject)this.bIG).getJSONObject("display");
   }

   public JSONObject getTextures() {
      return ((JSONObject)this.bIG).getJSONObject("textures");
   }

   public boolean hasTextures() {
      return ((JSONObject)this.bIG).containsKey("textures");
   }

   public int textureCount() {
      return !this.hasTextures() ? 0 : this.getTextures().entrySet().size();
   }
}
