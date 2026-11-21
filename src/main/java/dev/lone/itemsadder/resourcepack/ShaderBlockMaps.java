package dev.lone.itemsadder.resourcepack;

import java.util.Map;

class ShaderBlockMaps {
   private final Map bFG;
   private final Map bFH;

   private ShaderBlockMaps(Map var1, Map var2) {
      this.bFG = var1;
      this.bFH = var2;
   }

   public Map defaults() {
      return this.bFG;
   }

   public Map blocks() {
      return this.bFH;
   }
}
