package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public final class OverlayPackFile {
   private final byte[] bwe;
   private final String bwf;
   private JSONObject Cy;

   private OverlayPackFile(byte[] var1, String var2) {
      this.bwe = var1;
      this.bwf = var2;
   }

   public byte[] bytes() {
      return this.bwe;
   }

   public String error() {
      return this.bwf;
   }

   public JSONObject json() {
      if (this.Cy == null && this.bwe != null) {
         this.Cy = JSON.parseObject(this.bwe);
      }

      return this.Cy;
   }

   public boolean isValid() {
      return this.bwf == null;
   }
}
