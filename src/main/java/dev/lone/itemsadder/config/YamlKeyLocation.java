package dev.lone.itemsadder.config;

class YamlKeyLocation {
   private final String bNx;
   private final int bNy;
   private final int bNz;

   YamlKeyLocation(String var1, int var2, int var3) {
      this.bNx = var1;
      this.bNy = var2;
      this.bNz = var3;
   }

   public String path() {
      return this.bNx;
   }

   public int acF() {
      return this.bNy;
   }

   public int acG() {
      return this.bNz;
   }
}
