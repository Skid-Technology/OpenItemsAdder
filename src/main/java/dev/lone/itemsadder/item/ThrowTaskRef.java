package dev.lone.itemsadder.item;

import java.util.UUID;

class ThrowTaskRef {
   private final ThrowTask afF;
   private final long afG;
   private final UUID afH;

   private ThrowTaskRef(ThrowTask var1, long var2, UUID var4) {
      this.afF = var1;
      this.afG = var2;
      this.afH = var4;
   }

   public ThrowTask Bf() {
      return this.afF;
   }

   public long bl() {
      return this.afG;
   }

   public UUID Bg() {
      return this.afH;
   }
}
