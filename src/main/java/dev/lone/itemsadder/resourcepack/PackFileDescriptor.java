package dev.lone.itemsadder.resourcepack;

class PackFileDescriptor {
   private final int bLg;
   private final int bLh;
   private final int bLi;
   private final String bLj;
   private final int bLk;

   private PackFileDescriptor(int var1, int var2, int var3, String var4, int var5) {
      this.bLg = var1;
      this.bLh = var2;
      this.bLi = var3;
      this.bLj = var4;
      this.bLk = var5;
   }

   public int offset() {
      return this.bLg;
   }

   public int commentOffset() {
      return this.bLh;
   }

   public int end() {
      return this.bLi;
   }

   public String name() {
      return this.bLj;
   }

   public int commentLength() {
      return this.bLk;
   }
}
