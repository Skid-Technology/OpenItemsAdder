package dev.lone.itemsadder.network;

import java.util.UUID;

class TradeSelectionRequest {
   private final UUID ik;
   private final long il;
   private final int im;
   private final int in;

   TradeSelectionRequest(UUID var1, long var2, int var4, int var5) {
      this.ik = var1;
      this.il = var2;
      this.im = var4;
      this.in = var5;
   }

   public UUID bk() {
      return this.ik;
   }

   public long bl() {
      return this.il;
   }

   public int cb() {
      return this.im;
   }

   public int ca() {
      return this.in;
   }
}
