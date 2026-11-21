package dev.lone.itemsadder.world;

final class BlockLayerAccumulator {
   private final short[] blockIds = new short[7];
   private final byte[] layers = new byte[7];
   private int size;

   private BlockLayerAccumulator() {
   }

   private void add(short var1, byte var2) {
      this.blockIds[this.size] = var1;
      this.layers[this.size++] = var2;
   }
}
