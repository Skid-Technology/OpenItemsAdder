package dev.lone.itemsadder.util;

import java.util.List;

class BinaryPackPart {
   private final List chunks;
   private final int idatBytes;
   private final int expectedInflatedBytes;
   private final boolean marked;

   private BinaryPackPart(List var1, int var2, int var3, boolean var4) {
      this.chunks = var1;
      this.idatBytes = var2;
      this.expectedInflatedBytes = var3;
      this.marked = var4;
   }

   public List XK() {
      return this.chunks;
   }

   public int XL() {
      return this.idatBytes;
   }

   public int XM() {
      return this.expectedInflatedBytes;
   }

   public boolean XN() {
      return this.marked;
   }
}
