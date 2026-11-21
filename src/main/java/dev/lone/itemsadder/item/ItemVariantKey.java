package dev.lone.itemsadder.item;

public class ItemVariantKey {
   private final int aRD;
   private final int aRE;
   private final boolean aRF;

   ItemVariantKey(int var1, int var2, boolean var3) {
      this.aRD = var1;
      this.aRE = var2;
      this.aRF = var3;
   }

   public int NU() {
      return this.aRD;
   }

   public int NV() {
      return this.aRE;
   }

   public boolean NW() {
      return this.aRF;
   }
}
