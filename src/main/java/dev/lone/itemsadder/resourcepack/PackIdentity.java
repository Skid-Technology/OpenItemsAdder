package dev.lone.itemsadder.resourcepack;

class PackIdentity {
   private final String btE;
   private final String btF;

   private PackIdentity(String var1, String var2) {
      this.btE = var1;
      this.btF = var2;
   }

   public String url() {
      return this.btE;
   }

   public String hash() {
      return this.btF;
   }
}
