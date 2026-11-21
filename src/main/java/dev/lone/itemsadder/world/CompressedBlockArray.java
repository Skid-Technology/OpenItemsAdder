package dev.lone.itemsadder.world;

import java.util.Arrays;

public final class CompressedBlockArray {
   static final CompressedBlockArray EMPTY = new CompressedBlockArray(new short[0], new int[0], new byte[0]);
   private final short[] blockIds;
   private final int[] stateCodes;
   private final byte[] flags;
   private final short[] positions;
   private final int[] modelIds;
   private final byte[] faceMasks;
   private final byte[] kinds;

   private CompressedBlockArray(short[] var1, int[] var2, byte[] var3) {
      this(var1, var2, var3, buildPayload(var1, var2, var3));
   }

   private CompressedBlockArray(short[] var1, int[] var2, byte[] var3, CompressedBlockPayload var4) {
      this.blockIds = var1;
      this.stateCodes = var2;
      this.flags = var3;
      this.positions = var4.positions;
      this.modelIds = var4.modelIds;
      this.faceMasks = var4.faceMasks;
      this.kinds = var4.kinds;
   }

   private CompressedBlockArray withEntry(short var1, int var2, byte var3) {
      int var4 = Arrays.binarySearch(this.blockIds, var1);
      if (var4 >= 0) {
         if (var2 == -1) {
            short[] var10 = new short[this.blockIds.length - 1];
            int[] var12 = new int[this.stateCodes.length - 1];
            byte[] var13 = new byte[this.flags.length - 1];
            System.arraycopy(this.blockIds, 0, var10, 0, var4);
            System.arraycopy(this.blockIds, var4 + 1, var10, var4, var10.length - var4);
            System.arraycopy(this.stateCodes, 0, var12, 0, var4);
            System.arraycopy(this.stateCodes, var4 + 1, var12, var4, var12.length - var4);
            System.arraycopy(this.flags, 0, var13, 0, var4);
            System.arraycopy(this.flags, var4 + 1, var13, var4, var13.length - var4);
            return var10.length == 0 ? EMPTY : this.fromArrays(var10, var12, var13);
         }

         if (this.stateCodes[var4] == var2 && this.flags[var4] == var3) {
            return this;
         }

         int[] var9 = this.stateCodes;
         byte[] var11 = this.flags;
         if (this.stateCodes[var4] != var2) {
            var9 = (int[])this.stateCodes.clone();
            var9[var4] = var2;
         }

         if (this.flags[var4] != var3) {
            var11 = (byte[])this.flags.clone();
            var11[var4] = var3;
         }

         return this.fromArrays(this.blockIds, var9, var11);
      } else {
         if (var2 == -1) {
            return this;
         }

         int var5 = -var4 - 1;
         short[] var6 = new short[this.blockIds.length + 1];
         int[] var7 = new int[this.stateCodes.length + 1];
         byte[] var8 = new byte[this.flags.length + 1];
         System.arraycopy(this.blockIds, 0, var6, 0, var5);
         System.arraycopy(this.blockIds, var5, var6, var5 + 1, this.blockIds.length - var5);
         System.arraycopy(this.stateCodes, 0, var7, 0, var5);
         System.arraycopy(this.stateCodes, var5, var7, var5 + 1, this.stateCodes.length - var5);
         System.arraycopy(this.flags, 0, var8, 0, var5);
         System.arraycopy(this.flags, var5, var8, var5 + 1, this.flags.length - var5);
         var6[var5] = var1;
         var7[var5] = var2;
         var8[var5] = var3;
         return this.fromArrays(var6, var7, var8);
      }
   }

   private CompressedBlockArray fromArrays(short[] var1, int[] var2, byte[] var3) {
      return new CompressedBlockArray(var1, var2, var3);
   }

   CompressedBlockArray withFlags(short[] var1, byte[] var2, int var3) {
      byte[] var4 = null;

      for (int var5 = 0; var5 < var3; var5++) {
         int var6 = Arrays.binarySearch(this.blockIds, var1[var5]);
         if (var6 >= 0 && this.flags[var6] != var2[var5]) {
            if (var4 == null) {
               var4 = (byte[])this.flags.clone();
            }

            var4[var6] = var2[var5];
         }
      }

      return var4 == null ? this : new CompressedBlockArray(this.blockIds, this.stateCodes, var4);
   }

   boolean isEmpty() {
      return this.blockIds.length == 0;
   }

   int size() {
      return this.blockIds.length;
   }

   int getStateId(short var1) {
      int var2 = this.getRawStateCode(var1);
      return var2 == -1 ? -1 : BlockStateManager.stripEntityFlag(var2);
   }

   private int getRawStateCode(short var1) {
      int var2 = Arrays.binarySearch(this.blockIds, var1);
      return var2 < 0 ? -1 : this.stateCodes[var2];
   }

   byte getFlags(short var1) {
      int var2 = Arrays.binarySearch(this.blockIds, var1);
      return var2 < 0 ? 0 : this.flags[var2];
   }

   short blockIdAt(int var1) {
      return this.blockIds[var1];
   }

   int stateIdAt(int var1) {
      return BlockStateManager.stripEntityFlag(this.stateCodes[var1]);
   }

   byte flagsAt(int var1) {
      return this.flags[var1];
   }

   int positionCount() {
      return this.positions.length;
   }

   short positionAt(int var1) {
      return this.positions[var1];
   }

   int modelIdAt(int var1) {
      return this.modelIds[var1];
   }

   byte faceMaskAt(int var1) {
      return this.faceMasks[var1];
   }

   byte kindAt(int var1) {
      return this.kinds[var1];
   }

   private static CompressedBlockPayload buildPayload(short[] var0, int[] var1, byte[] var2) {
      int[] var3 = new int[4096];
      Arrays.fill(var3, -1);
      byte[] var4 = new byte[4096];

      for (int var5 = 0; var5 < var0.length; var5++) {
         int var6 = var0[var5] & 4095;
         var3[var6] = var1[var5];
         var4[var6] = var2[var5];
      }

      long[] var13 = new long[var0.length];
      boolean[] var14 = new boolean[4096];
      int var7 = collectMergedRegions(var13, var3, var4, var14, (byte)1, 0);
      var7 = collectMergedRegions(var13, var3, var4, var14, (byte)2, var7);

      for (int var8 = 0; var8 < var3.length; var8++) {
         if (var3[var8] != -1 && !var14[var8]) {
            var13[var7++] = packEntry((short)var8, BlockStateManager.stripEntityFlag(var3[var8]), var4[var8], (byte)0);
         }
      }

      Arrays.sort(var13, 0, var7);
      short[] var16 = new short[var7];
      int[] var9 = new int[var7];
      byte[] var10 = new byte[var7];
      byte[] var11 = new byte[var7];

      for (int var12 = 0; var12 < var7; var12++) {
         var16[var12] = (short)(var13[var12] >>> 40);
         var9[var12] = (int)(var13[var12] >>> 8);
         var10[var12] = (byte)(var13[var12] >>> 2 & 63L);
         var11[var12] = (byte)(var13[var12] & 3L);
      }

      return new CompressedBlockPayload(var16, var9, var10, var11);
   }

   private static int collectMergedRegions(long[] var0, int[] var1, byte[] var2, boolean[] var3, byte var4, int var5) {
      byte var6 = 3;

      for (int var7 = 0; var7 < 16; var7++) {
         for (byte var8 = 0; var8 <= 16 - var6; var8 += var6) {
            for (byte var9 = 0; var9 <= 16 - var6; var9 += var6) {
               int var10 = linearIndex(var4, var9, var8, var7);
               if (var1[var10] != -1 && BlockStateManager.hasEntityFlag(var1[var10])) {
                  int var11 = BlockStateManager.stripEntityFlag(var1[var10]);
                  if (isRegionUniform(var1, var3, var11, var9, var8, var7, var6, var4)) {
                     byte var12 = 0;

                     for (int var13 = 0; var13 < var6; var13++) {
                        for (int var14 = 0; var14 < var6; var14++) {
                           int var15 = linearIndex(var4, var9 + var14, var8 + var13, var7);
                           var3[var15] = true;
                           var12 |= var2[var15];
                        }
                     }

                     var0[var5++] = packEntry((short)var10, var11, var12, var4);
                  }
               }
            }
         }
      }

      return var5;
   }

   private static int linearIndex(byte var0, int var1, int var2, int var3) {
      return var0 == 1 ? var1 | var3 << 4 | var2 << 8 : var1 | var2 << 4 | var3 << 8;
   }

   private static boolean isRegionUniform(int[] var0, boolean[] var1, int var2, int var3, int var4, int var5, int var6, byte var7) {
      for (int var8 = 0; var8 < var6; var8++) {
         for (int var9 = 0; var9 < var6; var9++) {
            int var10 = linearIndex(var7, var3 + var9, var4 + var8, var5);
            if (var0[var10] == -1 || var1[var10] || BlockStateManager.stripEntityFlag(var0[var10]) != var2 || !BlockStateManager.hasEntityFlag(var0[var10])) {
               return false;
            }
         }
      }

      return true;
   }

   private static long packEntry(short var0, int var1, byte var2, byte var3) {
      return (long)(var0 & 65535) << 40 | (var1 & 4294967295L) << 8 | (long)(var2 & 63) << 2 | var3;
   }
}
