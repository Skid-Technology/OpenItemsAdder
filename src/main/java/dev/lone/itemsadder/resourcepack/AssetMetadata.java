package dev.lone.itemsadder.resourcepack;

class AssetMetadata {
   private final String bxN;
   private final long bxO;
   private final long bxP;
   private final int bxQ;
   private final long bxR;
   private final long bxS;

   private AssetMetadata(String var1, long var2, long var4, int var6, long var7, long var9) {
      this.bxN = var1;
      this.bxO = var2;
      this.bxP = var4;
      this.bxQ = var6;
      this.bxR = var7;
      this.bxS = var9;
   }

   public String fullName() {
      return this.bxN;
   }

   public long localOffset() {
      return this.bxO;
   }

   public long compressedSize() {
      return this.bxP;
   }

   public int method() {
      return this.bxQ;
   }

   public long uncompressedSize() {
      return this.bxR;
   }

   public long crc() {
      return this.bxS;
   }
}
