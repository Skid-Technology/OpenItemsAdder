package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import java.util.Map;

public class PackBuildPlan {
   private final Map bvc;
   private final JSONArray bvd;
   private final PackDefinition bve;
   private final Map bvf;

   public PackBuildPlan(Map var1, JSONArray var2, PackDefinition var3, Map var4) {
      this.bvc = var1;
      this.bvd = var2;
      this.bve = var3;
      this.bvf = var4;
   }

   public Map Ts() {
      return this.bvc;
   }

   public JSONArray Tt() {
      return this.bvd;
   }

   public PackDefinition Tu() {
      return this.bve;
   }

   public Map To() {
      return this.bvf;
   }
}
