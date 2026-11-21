package dev.lone.itemsadder.block;

class BlockSessionRecord {
   private final BlockSessionKey nA;
   private final long nB;
   private final long nC;
   private final long nD;

   private BlockSessionRecord(BlockSessionKey var1, long var2, long var4, long var6) {
      this.nA = var1;
      this.nB = var2;
      this.nC = var4;
      this.nD = var6;
   }

   public BlockSessionKey key() {
      return this.nA;
   }

   public long connectionId() {
      return this.nB;
   }

   public long callbackGeneration() {
      return this.nC;
   }

   public long chunkGeneration() {
      return this.nD;
   }
}
