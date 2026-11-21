package dev.lone.itemsadder.item;

import java.util.UUID;

public class BlockInteractKey {
   private final UUID ahS;
   private final long ahT;
   private final int ahU;

   private BlockInteractKey(UUID var1, long var2, int var4) {
      this.ahS = var1;
      this.ahT = var2;
      this.ahU = var4;
   }

   public UUID bk() {
      return this.ahS;
   }

   public long bl() {
      return this.ahT;
   }

   public int jI() {
      return this.ahU;
   }
}
