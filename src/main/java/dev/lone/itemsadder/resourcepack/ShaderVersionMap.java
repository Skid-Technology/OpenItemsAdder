package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import java.util.Map;

class ShaderVersionMap {
   private final Version bFj;
   private final Map bFk;

   private ShaderVersionMap(Version var1, Map var2) {
      this.bFj = var1;
      this.bFk = var2;
   }

   public Version version() {
      return this.bFj;
   }

   public Map paths() {
      return this.bFk;
   }
}
