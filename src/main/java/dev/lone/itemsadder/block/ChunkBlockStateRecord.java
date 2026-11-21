package dev.lone.itemsadder.block;

public class ChunkBlockStateRecord {
   private final int jJ;
   private final int jK;
   private final int jL;
   private final BlockSoundProfile jM;
   private final byte jN;

   public ChunkBlockStateRecord(int var1, int var2, int var3, BlockSoundProfile var4, byte var5) {
      this.jJ = var1;
      this.jK = var2;
      this.jL = var3;
      this.jM = var4;
      this.jN = var5;
   }

   public String getNamespacedId() {
      return this.jM.jP;
   }

   public int relX() {
      return this.jJ;
   }

   public int y() {
      return this.jK;
   }

   public int relZ() {
      return this.jL;
   }

   public BlockSoundProfile cropInfo() {
      return this.jM;
   }

   public byte nextAge() {
      return this.jN;
   }
}
