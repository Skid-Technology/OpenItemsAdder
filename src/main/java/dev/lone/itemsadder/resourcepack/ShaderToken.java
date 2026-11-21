package dev.lone.itemsadder.resourcepack;

class ShaderToken {
   private final char bEM;
   private final int bEN;
   private final int bEO;

   private ShaderToken(char var1, int var2, int var3) {
      this.bEM = var1;
      this.bEN = var2;
      this.bEO = var3;
   }

   public char value() {
      return this.bEM;
   }

   public int line() {
      return this.bEN;
   }

   public int column() {
      return this.bEO;
   }
}
