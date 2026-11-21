package dev.lone.itemsadder.resourcepack;

class PackAssetEntry {
   private final String bwX;
   private final boolean bwY;
   private final PackOrigin bwZ;
   private final String bxa;
   private final String bxb;
   private final String bxc;
   private final boolean bxd;
   private final boolean bxe;

   PackAssetEntry(String var1, boolean var2, PackOrigin var3, String var4, String var5, boolean var6) {
      this(var1, var2, var3, var4, var4, var5, var6, false);
   }

   PackAssetEntry(String var1, boolean var2, PackOrigin var3, String var4, String var5, boolean var6, boolean var7) {
      this(var1, var2, var3, var4, var4, var5, var6, var7);
   }

   PackAssetEntry(String var1, boolean var2, PackOrigin var3, String var4, String var5, String var6, boolean var7, boolean var8) {
      var1 = ItemContextBuilder.normalizeId(var1);
      var3 = var3 == null ? PackOrigin.UNKNOWN : var3;
      var4 = var4 != null && !var4.isBlank() ? var4 : ItemContextBuilder.modelJsonPath(var1);
      var5 = var5 != null && !var5.isBlank() ? var5 : var4;
      var6 = var6 != null && !var6.isBlank() ? var6 : (var2 ? "folder fallback; no blockstate root" : "folder fallback; no item declaration root");
      this.bwX = var1;
      this.bwY = var2;
      this.bwZ = var3;
      this.bxa = var4;
      this.bxb = var5;
      this.bxc = var6;
      this.bxd = var7;
      this.bxe = var8;
   }

   String UF() {
      return this.bwZ.ordinal()
         + "\u0000"
         + PackReport.namespaceOf(this.bwX)
         + "\u0000"
         + this.bwX
         + "\u0000"
         + this.bxb
         + "\u0000"
         + this.bxa
         + "\u0000"
         + this.bwY
         + "\u0000"
         + this.bxc
         + "\u0000"
         + this.bxd
         + "\u0000"
         + this.bxe;
   }

   public String uM() {
      return this.bwX;
   }

   public boolean UG() {
      return this.bwY;
   }

   public PackOrigin UH() {
      return this.bwZ;
   }

   public String UI() {
      return this.bxa;
   }

   public String UJ() {
      return this.bxb;
   }

   public String Ue() {
      return this.bxc;
   }

   public boolean UK() {
      return this.bxd;
   }

   public boolean UL() {
      return this.bxe;
   }
}
