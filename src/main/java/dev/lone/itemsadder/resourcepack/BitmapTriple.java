package dev.lone.itemsadder.resourcepack;

class BitmapTriple {
   private final int bGU;
   private final int bGV;
   private final int bGW;

   BitmapTriple(int var1, int var2, int var3) {
      this.bGU = var1;
      this.bGV = var2;
      this.bGW = var3;
   }

   public int removedProviders() {
      return this.bGU;
   }

   public int sheetsBuilt() {
      return this.bGV;
   }

   public int cacheHits() {
      return this.bGW;
   }
}
