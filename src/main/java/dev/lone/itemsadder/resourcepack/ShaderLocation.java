package dev.lone.itemsadder.resourcepack;

class ShaderLocation {
   private final String bFC;
   private final int bFD;
   private final int bFE;
   private final boolean bFF;

   private ShaderLocation(String var1, int var2, int var3, boolean var4) {
      this.bFC = var1;
      this.bFD = var2;
      this.bFE = var3;
      this.bFF = var4;
   }

   public String value() {
      return this.bFC;
   }

   public int line() {
      return this.bFD;
   }

   public int column() {
      return this.bFE;
   }

   public boolean identifier() {
      return this.bFF;
   }
}
