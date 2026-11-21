package dev.lone.itemsadder.resourcepack;

class PackCacheFile {
   private final String bGO;
   private final String bGP;
   private final long bGQ;

   private PackCacheFile(String var1, String var2, long var3) {
      this.bGO = var1;
      this.bGP = var2;
      this.bGQ = var3;
   }

   public String path() {
      return this.bGO;
   }

   public String hash() {
      return this.bGP;
   }

   public long size() {
      return this.bGQ;
   }
}
