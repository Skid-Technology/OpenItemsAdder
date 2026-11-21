package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.ImageOptimizeStatus;

class PackManifestEntry {
   private final ImageOptimizeStatus status;
   private final String reason;

   private PackManifestEntry(ImageOptimizeStatus var1, String var2) {
      this.status = var1;
      this.reason = var2;
   }
}
