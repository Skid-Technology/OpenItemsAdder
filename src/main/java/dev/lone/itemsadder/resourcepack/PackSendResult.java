package dev.lone.itemsadder.resourcepack;

import java.util.List;

class PackSendResult {
   private final PackIdentity main;
   private final List sounds;
   private final PackRecipient shaders;

   private PackSendResult(PackIdentity var1, List var2, PackRecipient var3) {
      this.main = var1;
      this.sounds = var2;
      this.shaders = var3;
   }
}
