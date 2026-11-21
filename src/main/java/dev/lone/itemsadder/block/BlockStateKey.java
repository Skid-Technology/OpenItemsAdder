package dev.lone.itemsadder.block;

public class BlockStateKey {
   private final int jG;
   private final int jH;
   private final BlockStateSnapshot jI;

   public BlockStateKey(int var1, int var2, BlockStateSnapshot var3) {
      this.jG = var1;
      this.jH = var2;
      this.jI = var3;
   }

   public int x() {
      return this.jG;
   }

   public int z() {
      return this.jH;
   }

   public BlockStateSnapshot view() {
      return this.jI;
   }
}
