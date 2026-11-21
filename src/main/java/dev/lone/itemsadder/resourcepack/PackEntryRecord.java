package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;

class PackEntryRecord {
   private final String bza;
   private final PackEntry bzb;

   private PackEntryRecord(String var1, PackEntry var2) {
      this.bza = var1;
      this.bzb = var2;
   }

   public String VQ() {
      return this.bza;
   }

   public PackEntry VR() {
      return this.bzb;
   }
}
