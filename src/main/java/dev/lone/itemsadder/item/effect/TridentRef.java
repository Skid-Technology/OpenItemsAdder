package dev.lone.itemsadder.item.effect;

import java.util.UUID;

class TridentRef {
   private final UUID amA;
   private final long amB;
   private final UUID amC;

   private TridentRef(UUID var1, long var2, UUID var4) {
      this.amA = var1;
      this.amB = var2;
      this.amC = var4;
   }

   public UUID bk() {
      return this.amA;
   }

   public long bl() {
      return this.amB;
   }

   public UUID method20() {
      return this.amC;
   }
}
