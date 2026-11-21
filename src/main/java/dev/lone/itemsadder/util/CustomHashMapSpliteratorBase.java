package dev.lone.itemsadder.util;

class CustomHashMapSpliteratorBase {
   final CustomHashMap bOy;
   CustomHashMapNode bOv;
   int bwa;
   int bOz;
   int bOA;
   int bOw;

   CustomHashMapSpliteratorBase(CustomHashMap var1, int var2, int var3, int var4, int var5) {
      this.bOy = var1;
      this.bwa = var2;
      this.bOz = var3;
      this.bOA = var4;
      this.bOw = var5;
   }

   final int adk() {
      int var1 = this.bOz;
      if (this.bOz < 0) {
         CustomHashMap var2 = this.bOy;
         this.bOA = var2.XN;
         this.bOw = var2.bOo;
         CustomHashMapNode[] var3 = var2.bOm;
         var1 = this.bOz = var3 == null ? 0 : var3.length;
      }

      return var1;
   }

   public final long estimateSize() {
      this.adk();
      return this.bOA;
   }
}
