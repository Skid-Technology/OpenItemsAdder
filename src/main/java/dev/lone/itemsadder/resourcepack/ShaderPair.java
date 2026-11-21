package dev.lone.itemsadder.resourcepack;

class ShaderPair {
   private final String bFp;
   private final String bFq;

   private ShaderPair(String var1, String var2) {
      this.bFp = var1;
      this.bFq = var2;
   }

   public String vertex() {
      return this.bFp;
   }

   public String fragment() {
      return this.bFq;
   }
}
