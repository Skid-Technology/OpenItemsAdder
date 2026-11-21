package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSONObject;

public class JsonTransformEntry {
   private final JSONObject Cy;
   private final float Cz;

   public JsonTransformEntry(JSONObject var1, float var2) {
      this.Cy = var1;
      this.Cz = var2;
   }

   public JSONObject pd() {
      return this.Cy;
   }

   public float lZ() {
      return this.Cz;
   }
}
