package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeneratedAssets {
   private final Map axN;
   private final Map axO;
   private final Map axP;

   public GeneratedAssets(Map var1, Map var2, Map var3) {
      this.axN = var1;
      this.axO = var2;
      this.axP = var3;
   }

   public Map models() {
      LinkedHashMap var1 = new LinkedHashMap();
      this.axP.forEach((var1x, var2) -> var1.put(var1x, JSONObject.parseObject(var2.toJSONString(new Feature[0]))));
      return Map.copyOf(var1);
   }

   public Map bundles() {
      return this.axN;
   }

   public Map textures() {
      return this.axO;
   }
}
