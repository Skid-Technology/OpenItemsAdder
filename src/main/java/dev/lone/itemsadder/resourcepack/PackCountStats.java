package dev.lone.itemsadder.resourcepack;

import java.util.List;

class PackCountStats {
   private final int bCp;
   private final int bCq;
   private final int bCr;
   private final boolean bCs;
   private final List bCt;
   private final List bCu;

   PackCountStats(int var1, int var2, int var3, boolean var4, List var5, List var6) {
      this.bCp = var1;
      this.bCq = var2;
      this.bCr = var3;
      this.bCs = var4;
      this.bCt = var5;
      this.bCu = var6;
   }

   int method132(PackStatsTuple var1) {
      return var1.bCC / this.bCp * (var1.bCD / this.bCq);
   }

   public int method4() {
      return this.bCp;
   }

   public int JK() {
      return this.bCq;
   }

   public int Xn() {
      return this.bCr;
   }

   public boolean Xo() {
      return this.bCs;
   }

   public List Xp() {
      return this.bCt;
   }

   public List Xq() {
      return this.bCu;
   }
}
