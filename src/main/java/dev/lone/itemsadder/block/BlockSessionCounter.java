package dev.lone.itemsadder.block;

class BlockSessionCounter {
   private final long nM;
   private final int nN;
   private final int nO;
   private final int nP;

   BlockSessionCounter(long var1, int var3, int var4, int var5) {
      this.nM = var1;
      this.nN = var3;
      this.nO = var4;
      this.nP = var5;
   }

   public long connectionId() {
      return this.nM;
   }

   public int x() {
      return this.nN;
   }

   public int y() {
      return this.nO;
   }

   public int z() {
      return this.nP;
   }
}
