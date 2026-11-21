package dev.lone.itemsadder.resourcepack;

class AssetCacheStamp {
   private final int bxK;
   private final long bxL;
   private final int bxM;

   private AssetCacheStamp(int var1, long var2, int var4) {
      this.bxK = var1;
      this.bxL = var2;
      this.bxM = var4;
   }

   public int files() {
      return this.bxK;
   }

   public long bytes() {
      return this.bxL;
   }

   public int names() {
      return this.bxM;
   }
}
