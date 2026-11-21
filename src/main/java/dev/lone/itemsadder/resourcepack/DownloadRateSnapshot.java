package dev.lone.itemsadder.resourcepack;

class DownloadRateSnapshot {
   private final long bJL;
   private final int bJM;
   private final int bJN;
   private final long bJO;
   private final long bJP;
   private final boolean bJQ;
   private final long bJR;

   private DownloadRateSnapshot(long var1, int var3, int var4, long var5, long var7, boolean var9, long var10) {
      this.bJL = var1;
      this.bJM = var3;
      this.bJN = var4;
      this.bJO = var5;
      this.bJP = var7;
      this.bJQ = var9;
      this.bJR = var10;
   }

   public long windowStart() {
      return this.bJL;
   }

   public int count() {
      return this.bJM;
   }

   public int rejections() {
      return this.bJN;
   }

   public long lastRejection() {
      return this.bJO;
   }

   public long blockedAt() {
      return this.bJP;
   }

   public boolean blocked() {
      return this.bJQ;
   }

   public long lastAccess() {
      return this.bJR;
   }
}
