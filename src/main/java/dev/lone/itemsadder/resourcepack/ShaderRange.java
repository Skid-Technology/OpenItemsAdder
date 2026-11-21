package dev.lone.itemsadder.resourcepack;

class ShaderRange {
   private final int bFe;
   private final int bFf;
   private final String bFg;
   private final boolean bFh;
   private final boolean bFi;

   private ShaderRange(int var1, int var2, String var3, boolean var4, boolean var5) {
      this.bFe = var1;
      this.bFf = var2;
      this.bFg = var3;
      this.bFh = var4;
      this.bFi = var5;
   }

   public int line() {
      return this.bFe;
   }

   public int column() {
      return this.bFf;
   }

   public String target() {
      return this.bFg;
   }

   public boolean relative() {
      return this.bFh;
   }

   public boolean valid() {
      return this.bFi;
   }
}
