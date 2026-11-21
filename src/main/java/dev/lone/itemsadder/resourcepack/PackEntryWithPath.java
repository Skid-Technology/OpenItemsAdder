package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;

class PackEntryWithPath {
   private final String bAQ;
   private final PackEntry bAR;

   private PackEntryWithPath(String var1, PackEntry var2) {
      this.bAQ = var1;
      this.bAR = var2;
   }

   public String path() {
      return this.bAQ;
   }

   public PackEntry VR() {
      return this.bAR;
   }
}
