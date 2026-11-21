package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.List;

class PackElementSpec {
   private final JSONObject bvY;
   private final String bvZ;
   private final int bwa;
   private final float bwb;
   private final JSONObject bwc;
   private final List bwd;

   private PackElementSpec(JSONObject var1, String var2, int var3, float var4, JSONObject var5, List var6) {
      this.bvY = var1;
      this.bvZ = var2;
      this.bwa = var3;
      this.bwb = var4;
      this.bwc = var5;
      this.bwd = var6;
   }

   public JSONObject TZ() {
      return this.bvY;
   }

   public String Ua() {
      return this.bvZ;
   }

   public int Ub() {
      return this.bwa;
   }

   public float uh() {
      return this.bwb;
   }

   public JSONObject Uc() {
      return this.bwc;
   }

   public List Jk() {
      return this.bwd;
   }
}
