package dev.lone.itemsadder.util;

public class PackedSampleBuffer {
   private final byte[] bytes;
   private final int[] offsets;
   private final int count;
   private final int[] lengths;

   private PackedSampleBuffer(byte[] var1, int[] var2, int var3, int[] var4) {
      this.bytes = var1;
      this.offsets = var2;
      this.count = var3;
      this.lengths = var4;
   }
}
