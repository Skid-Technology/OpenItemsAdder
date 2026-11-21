package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;

class PackScaleEntry {
   private final float bvV;
   private final Number bvW;
   private final JSONObject bvX;

   private PackScaleEntry(float var1, Number var2, JSONObject var3) {
      this.bvV = var1;
      this.bvW = var2;
      this.bvX = var3;
   }

   public float TX() {
      return this.bvV;
   }

   public Number TY() {
      return this.bvW;
   }

   public JSONObject vK() {
      return this.bvX;
   }
}
