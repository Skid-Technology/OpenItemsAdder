package dev.lone.itemsadder.block;

import java.util.UUID;

class BlockSessionEntry {
   private final UUID nE;
   private final long nF;
   private final UUID nG;
   private final int nH;
   private final int nI;
   private final int nJ;

   BlockSessionEntry(UUID var1, long var2, UUID var4, int var5, int var6, int var7) {
      this.nE = var1;
      this.nF = var2;
      this.nG = var4;
      this.nH = var5;
      this.nI = var6;
      this.nJ = var7;
   }

   public UUID playerId() {
      return this.nE;
   }

   public long connectionId() {
      return this.nF;
   }

   public UUID worldId() {
      return this.nG;
   }

   public int x() {
      return this.nH;
   }

   public int y() {
      return this.nI;
   }

   public int z() {
      return this.nJ;
   }
}
