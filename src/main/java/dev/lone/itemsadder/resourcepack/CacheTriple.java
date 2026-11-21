package dev.lone.itemsadder.resourcepack;

class CacheTriple {
   private final long bww;
   private final long bwx;
   private final long bwy;

   CacheTriple(long var1, long var3, long var5) {
      this.bww = var1;
      this.bwx = var3;
      this.bwy = var5;
   }

   public long bytesWritten() {
      return this.bww;
   }

   public long bytesRead() {
      return this.bwx;
   }

   public long cacheHits() {
      return this.bwy;
   }
}
