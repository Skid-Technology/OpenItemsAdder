package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.awt.image.BufferedImage;

class AtlasEntry {
   private final JSONObject buq;
   private final String bur;
   private final BufferedImage bus;

   private AtlasEntry(JSONObject var1, String var2, BufferedImage var3) {
      this.buq = var1;
      this.bur = var2;
      this.bus = var3;
   }

   public JSONObject provider() {
      return this.buq;
   }

   public String character() {
      return this.bur;
   }

   public BufferedImage image() {
      return this.bus;
   }
}
