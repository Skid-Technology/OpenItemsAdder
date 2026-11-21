package dev.lone.itemsadder.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public final class BlockRegionIndex {
   static final BlockRegionIndex EMPTY = new BlockRegionIndex(0, 0, 0, new BlockSectionData[0]);
   private final int minChunkX;
   private final int minChunkZ;
   private final int sizeZ;
   private final BlockSectionData[] sections;

   BlockRegionIndex(int var1, int var2, int var3, BlockSectionData[] var4) {
      this.minChunkX = var1;
      this.minChunkZ = var2;
      this.sizeZ = var3;
      this.sections = var4;
   }

   boolean matchesSections(Long2ObjectOpenHashMap var1) {
      for (int var2 = 0; var2 < this.sections.length; var2++) {
         BlockSectionData var3 = var1 == null ? null : (BlockSectionData)var1.get(BlockStateManager.sectionKey(this.chunkXAt(var2), this.chunkZAt(var2)));
         if (this.sections[var2] != var3) {
            return false;
         }
      }

      return true;
   }

   int sectionCount() {
      return this.sections.length;
   }

   BlockSectionData getSection(int var1) {
      return this.sections[var1];
   }

   BlockSectionData getSectionAt(int var1, int var2) {
      return !this.containsChunk(var1, var2) ? null : this.sections[(var1 - this.minChunkX) * this.sizeZ + var2 - this.minChunkZ];
   }

   boolean containsChunk(int var1, int var2) {
      if (this.sizeZ == 0) {
         return false;
      }

      int var3 = var1 - this.minChunkX;
      int var4 = var2 - this.minChunkZ;
      return var3 >= 0 && var3 < this.sections.length / this.sizeZ && var4 >= 0 && var4 < this.sizeZ;
   }

   int chunkXAt(int var1) {
      return this.minChunkX + var1 / this.sizeZ;
   }

   int chunkZAt(int var1) {
      return this.minChunkZ + var1 % this.sizeZ;
   }
}
