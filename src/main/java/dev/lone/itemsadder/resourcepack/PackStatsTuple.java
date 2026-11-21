package dev.lone.itemsadder.resourcepack;

class PackStatsTuple {
   private final int bCC;
   private final int bCD;
   private final int bCE;
   private final int bCF;

   PackStatsTuple(int var1, int var2, int var3, int var4) {
      this.bCC = var1;
      this.bCD = var2;
      this.bCE = var3;
      this.bCF = var4;
   }

   long Xw() {
      return (long)this.bCC * this.bCD;
   }

   public int method4() {
      return this.bCC;
   }

   public int JK() {
      return this.bCD;
   }

   public int HS() {
      return this.bCE;
   }

   public int Xx() {
      return this.bCF;
   }
}
