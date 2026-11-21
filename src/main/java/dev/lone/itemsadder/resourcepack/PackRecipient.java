package dev.lone.itemsadder.resourcepack;

import java.util.UUID;

class PackRecipient {
   private final UUID id;
   private final PackIdentity delivery;

   private PackRecipient(UUID var1, PackIdentity var2) {
      this.id = var1;
      this.delivery = var2;
   }
}
