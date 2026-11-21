package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONObject;

public class AnimationSourceEntry {
   private final String TW;
   private final JSONObject TX;

   public AnimationSourceEntry(String var1, JSONObject var2) {
      this.TW = var1;
      this.TX = var2;
   }

   public String vJ() {
      return this.TW;
   }

   public JSONObject vK() {
      return this.TX;
   }
}
