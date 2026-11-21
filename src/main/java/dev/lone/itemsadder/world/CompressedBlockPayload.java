package dev.lone.itemsadder.world;

class CompressedBlockPayload {
   private final short[] positions;
   private final int[] modelIds;
   private final byte[] faceMasks;
   private final byte[] kinds;

   private CompressedBlockPayload(short[] var1, int[] var2, byte[] var3, byte[] var4) {
      this.positions = var1;
      this.modelIds = var2;
      this.faceMasks = var3;
      this.kinds = var4;
   }
}
