package dev.lone.itemsadder.world;

import java.util.Arrays;
import java.util.Map;

public final class BlockSectionData {
   static final BlockSectionData EMPTY = new BlockSectionData(new int[0], new CompressedBlockArray[0]);
   private final int[] layerKeys;
   private final CompressedBlockArray[] layers;

   private BlockSectionData(int[] var1, CompressedBlockArray[] var2) {
      this.layerKeys = var1;
      this.layers = var2;
   }

   private static BlockSectionData fromLayerBuilders(Map var0) {
      int[] var1 = var0.keySet().stream().mapToInt(Integer::intValue).sorted().toArray();
      CompressedBlockArray[] var2 = new CompressedBlockArray[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = ((BlockLayerBuilder)var0.get(var1[var3])).build();
      }

      return new BlockSectionData(var1, var2);
   }

   BlockSectionData withBlockState(int var1, short var2, int var3, byte var4) {
      int var5 = Arrays.binarySearch(this.layerKeys, var1);
      CompressedBlockArray var6 = var5 < 0 ? CompressedBlockArray.EMPTY : this.layers[var5];
      CompressedBlockArray var7 = var6.withEntry(var2, var3, var4);
      if (var6 == var7) {
         return this;
      } else if (var5 >= 0 && var7.isEmpty()) {
         int[] var12 = new int[this.layerKeys.length - 1];
         CompressedBlockArray[] var13 = new CompressedBlockArray[this.layers.length - 1];
         System.arraycopy(this.layerKeys, 0, var12, 0, var5);
         System.arraycopy(this.layerKeys, var5 + 1, var12, var5, var12.length - var5);
         System.arraycopy(this.layers, 0, var13, 0, var5);
         System.arraycopy(this.layers, var5 + 1, var13, var5, var13.length - var5);
         return var12.length == 0 ? EMPTY : new BlockSectionData(var12, var13);
      } else if (var5 >= 0) {
         CompressedBlockArray[] var11 = (CompressedBlockArray[])this.layers.clone();
         var11[var5] = var7;
         return new BlockSectionData(this.layerKeys, var11);
      } else {
         int var8 = -var5 - 1;
         int[] var9 = new int[this.layerKeys.length + 1];
         CompressedBlockArray[] var10 = new CompressedBlockArray[this.layers.length + 1];
         System.arraycopy(this.layerKeys, 0, var9, 0, var8);
         System.arraycopy(this.layerKeys, var8, var9, var8 + 1, this.layerKeys.length - var8);
         System.arraycopy(this.layers, 0, var10, 0, var8);
         System.arraycopy(this.layers, var8, var10, var8 + 1, this.layers.length - var8);
         var9[var8] = var1;
         var10[var8] = var7;
         return new BlockSectionData(var9, var10);
      }
   }

   private BlockSectionData applyLayerAccumulator(int var1, BlockLayerAccumulator var2) {
      int var3 = Arrays.binarySearch(this.layerKeys, var1);
      if (var3 < 0) {
         return this;
      }

      CompressedBlockArray var4 = this.layers[var3];
      CompressedBlockArray var5 = var4.withFlags(var2.blockIds, var2.layers, var2.size);
      if (var4 == var5) {
         return this;
      }

      CompressedBlockArray[] var6 = (CompressedBlockArray[])this.layers.clone();
      var6[var3] = var5;
      return new BlockSectionData(this.layerKeys, var6);
   }

   boolean isEmpty() {
      return this.layers.length == 0;
   }

   int layerCount() {
      return this.layers.length;
   }

   int layerKeyAt(int var1) {
      return this.layerKeys[var1];
   }

   CompressedBlockArray layerAt(int var1) {
      return this.layers[var1];
   }

   CompressedBlockArray layerForKey(int var1) {
      int var2 = Arrays.binarySearch(this.layerKeys, var1);
      return var2 < 0 ? null : this.layers[var2];
   }
}
