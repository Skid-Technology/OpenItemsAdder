package dev.lone.itemsadder.resourcepack;

class PackHttpRoute {
   private final String bKn;
   private final int bKo;

   private PackHttpRoute(String var1, int var2) {
      this.bKn = var1;
      this.bKo = var2;
   }

   public String value() {
      return this.bKn;
   }

   public int bytes() {
      return this.bKo;
   }
}
