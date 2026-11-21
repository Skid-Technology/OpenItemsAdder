package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;

class VanillaBitmapEntry {
   private final int bGS;
   private final JSONObject bGT;

   private VanillaBitmapEntry(int var1, JSONObject var2) {
      this.bGS = var1;
      this.bGT = var2;
   }

   public int index() {
      return this.bGS;
   }

   public JSONObject provider() {
      return this.bGT;
   }
}
