package dev.lone.itemsadder.resourcepack;

class CacheCounters {
   private final long bGp;
   private final long bGq;
   private final long bGr;
   private final long bGs;
   private final long bGt;
   private final long bGu;

   CacheCounters(long var1, long var3, long var5, long var7, long var9, long var11) {
      this.bGp = var1;
      this.bGq = var3;
      this.bGr = var5;
      this.bGs = var7;
      this.bGt = var9;
      this.bGu = var11;
   }

   public long hits() {
      return this.bGp;
   }

   public long misses() {
      return this.bGq;
   }

   public long errors() {
      return this.bGr;
   }

   public long inputBytes() {
      return this.bGs;
   }

   public long cacheReadBytes() {
      return this.bGt;
   }

   public long cacheWrittenBytes() {
      return this.bGu;
   }
}
