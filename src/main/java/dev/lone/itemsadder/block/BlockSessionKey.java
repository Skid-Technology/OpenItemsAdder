package dev.lone.itemsadder.block;

import java.util.UUID;

class BlockSessionKey {
   private final UUID nw;
   private final UUID nx;
   private final int ny;
   private final int nz;

   private BlockSessionKey(UUID var1, UUID var2, int var3, int var4) {
      this.nw = var1;
      this.nx = var2;
      this.ny = var3;
      this.nz = var4;
   }

   public UUID playerId() {
      return this.nw;
   }

   public UUID worldId() {
      return this.nx;
   }

   public int chunkX() {
      return this.ny;
   }

   public int chunkZ() {
      return this.nz;
   }
}
