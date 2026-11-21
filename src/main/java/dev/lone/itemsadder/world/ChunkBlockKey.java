package dev.lone.itemsadder.world;

class ChunkBlockKey {
   private final int chunkX;
   private final int chunkZ;
   private final int sectionY;

   private ChunkBlockKey(int var1, int var2, int var3) {
      this.chunkX = var1;
      this.chunkZ = var2;
      this.sectionY = var3;
   }
}
