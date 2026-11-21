package dev.lone.itemsadder.block;

import java.util.List;

public class BlockStateSnapshot {
   private final List jg;

   public BlockStateSnapshot(List var1) {
      this.jg = var1;
   }

   public List entries() {
      return this.jg;
   }
}
