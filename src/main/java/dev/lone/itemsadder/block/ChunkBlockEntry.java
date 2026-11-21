package dev.lone.itemsadder.block;

public class ChunkBlockEntry {
   private final ChunkKey jA;
   private final int jB;
   private final BlockStateSnapshot jC;

   public ChunkBlockEntry(ChunkKey var1, int var2, BlockStateSnapshot var3) {
      this.jA = var1;
      this.jB = var2;
      this.jC = var3;
   }

   public ChunkKey key() {
      return this.jA;
   }

   public int version() {
      return this.jB;
   }

   public BlockStateSnapshot view() {
      return this.jC;
   }
}
