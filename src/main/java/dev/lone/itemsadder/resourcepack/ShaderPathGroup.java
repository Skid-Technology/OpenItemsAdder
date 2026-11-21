package dev.lone.itemsadder.resourcepack;

import java.util.Map;

class ShaderPathGroup {
   private final String bFU;
   private final Map bFV;

   private ShaderPathGroup(String var1, Map var2) {
      this.bFU = var1;
      this.bFV = var2;
   }

   public String path() {
      return this.bFU;
   }

   public Map groups() {
      return this.bFV;
   }
}
