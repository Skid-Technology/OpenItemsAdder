package dev.lone.itemsadder.world;

import java.util.UUID;

class ChunkSectionKey {
   private final UUID playerId;
   private final long connectionId;
   private final UUID worldId;
   private final int chunkX;
   private final int chunkZ;
   private final int rendererLifecycle;

   ChunkSectionKey(UUID var1, UUID var2, int var3, int var4, int var5) {
      this(var1, -1L, var2, var3, var4, var5);
   }

   ChunkSectionKey(UUID var1, long var2, UUID var4, int var5, int var6, int var7) {
      this.playerId = var1;
      this.connectionId = var2;
      this.worldId = var4;
      this.chunkX = var5;
      this.chunkZ = var6;
      this.rendererLifecycle = var7;
   }
}
