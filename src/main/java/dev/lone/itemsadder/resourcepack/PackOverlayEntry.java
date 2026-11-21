package dev.lone.itemsadder.resourcepack;

import java.util.Map;

class PackOverlayEntry {
   private final String sourceId;
   private final boolean block;
   private final Map textures;

   private PackOverlayEntry(String var1, boolean var2, Map var3) {
      this.sourceId = var1;
      this.block = var2;
      this.textures = var3;
   }
}
