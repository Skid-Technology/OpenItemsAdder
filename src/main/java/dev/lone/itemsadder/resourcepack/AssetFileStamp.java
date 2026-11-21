package dev.lone.itemsadder.resourcepack;

public class AssetFileStamp {
   private final int bxX;
   private final long bxY;
   private final int bxZ;
   private final int bya;
   private final long byb;
   private final int byc;
   private final long byd;

   public AssetFileStamp(int var1, long var2, int var4, int var5, long var6, int var8, long var9) {
      this.bxX = var1;
      this.bxY = var2;
      this.bxZ = var4;
      this.bya = var5;
      this.byb = var6;
      this.byc = var8;
      this.byd = var9;
   }

   public int rangeRequests() {
      return this.bxX;
   }

   public long downloadedBytes() {
      return this.bxY;
   }

   public int maximumConcurrency() {
      return this.bxZ;
   }

   public int extractedFiles() {
      return this.bya;
   }

   public long extractedBytes() {
      return this.byb;
   }

   public int listedAssets() {
      return this.byc;
   }

   public long elapsedMillis() {
      return this.byd;
   }
}
