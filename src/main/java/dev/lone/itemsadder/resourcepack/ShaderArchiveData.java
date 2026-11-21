package dev.lone.itemsadder.resourcepack;

import java.util.Set;

class ShaderArchiveData {
   private final byte[] bFO;
   private final byte[] bFP;
   private final Set bFQ;

   private ShaderArchiveData(byte[] var1, byte[] var2, Set var3) {
      this.bFO = var1;
      this.bFP = var2;
      this.bFQ = var3;
   }

   public byte[] soundBytes() {
      return this.bFO;
   }

   public byte[] shaderBytes() {
      return this.bFP;
   }

   public Set overlays() {
      return this.bFQ;
   }
}
