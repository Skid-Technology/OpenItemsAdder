package dev.lone.itemsadder.block;

import org.bukkit.block.data.BlockData;

public class BlockPlacementRecord {
   private final int Qz;
   private final int QA;
   private final int QB;
   private final BlockData QC;

   private BlockPlacementRecord(int var1, int var2, int var3, BlockData var4) {
      this.Qz = var1;
      this.QA = var2;
      this.QB = var3;
      this.QC = var4;
   }

   public int x() {
      return this.Qz;
   }

   public int y() {
      return this.QA;
   }

   public int z() {
      return this.QB;
   }

   public BlockData data() {
      return this.QC;
   }
}
