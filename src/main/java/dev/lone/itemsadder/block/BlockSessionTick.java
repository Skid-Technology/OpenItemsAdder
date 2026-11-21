package dev.lone.itemsadder.block;

class BlockSessionTick {
   private final BlockSessionKey nr;
   private final long ns;
   private final long nt;
   private final long nu;
   private final int nv;

   private BlockSessionTick(BlockSessionKey var1, long var2, long var4, long var6, int var8) {
      this.nr = var1;
      this.ns = var2;
      this.nt = var4;
      this.nu = var6;
      this.nv = var8;
   }

   public BlockSessionKey key() {
      return this.nr;
   }

   public long connectionId() {
      return this.ns;
   }

   public long callbackGeneration() {
      return this.nt;
   }

   public long chunkGeneration() {
      return this.nu;
   }

   public int repositoryGeneration() {
      return this.nv;
   }
}
