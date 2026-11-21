package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.Map;

class OverlayPackDefinition {
   private final JSONObject bwg;
   private final Map bwh;

   private OverlayPackDefinition(JSONObject var1, Map var2) {
      this.bwg = var1;
      this.bwh = var2;
   }

   public JSONObject fallback() {
      return this.bwg;
   }

   public Map cases() {
      return this.bwh;
   }
}
