package dev.lone.itemsadder.molang;

class MolangToken {
   private final String axr;
   private final TokenKind axs;
   private final int axt;

   private MolangToken(String var1, TokenKind var2, int var3) {
      this.axr = var1;
      this.axs = var2;
      this.axt = var3;
   }

   public String IC() {
      return this.axr;
   }

   public TokenKind ID() {
      return this.axs;
   }

   public int IE() {
      return this.axt;
   }
}
