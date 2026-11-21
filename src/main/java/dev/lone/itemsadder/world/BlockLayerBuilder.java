package dev.lone.itemsadder.world;

import java.util.Arrays;
import org.bukkit.World;

final class BlockLayerBuilder {
   private short[] localPositions = new short[8];
   private int[] payloads = new int[8];
   private byte[] stateIds = new byte[8];
   private int size;

   private BlockLayerBuilder() {
   }

   private void add(short var1, int var2) {
      if (this.size == this.localPositions.length) {
         this.localPositions = Arrays.copyOf(this.localPositions, this.size * 2);
         this.payloads = Arrays.copyOf(this.payloads, this.size * 2);
         this.stateIds = Arrays.copyOf(this.stateIds, this.size * 2);
      }

      this.localPositions[this.size] = var1;
      this.payloads[this.size++] = var2;
   }

   private void resolveStateIds(World var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < this.size; var5++) {
         int var6 = this.localPositions[var5] & '\uffff';
         int var7 = var2 << 4 | var6 & 15;
         int var8 = var3 << 4 | var6 >>> 8 & 15;
         int var9 = var4 << 4 | var6 >>> 4 & 15;
         this.stateIds[var5] = BlockStateManager.computeOcclusionMask(var1, var7, var8, var9, 0, 0, 0, null);
      }
   }

   private CompressedBlockArray build() {
      long[] var1 = new long[this.size];

      for (int var2 = 0; var2 < this.size; var2++) {
         var1[var2] = (long)(this.localPositions[var2] & 65535) << 40 | (this.payloads[var2] & 4294967295L) << 8 | this.stateIds[var2] & 63;
      }

      Arrays.sort(var1);
      short[] var6 = new short[this.size];
      int[] var3 = new int[this.size];
      byte[] var4 = new byte[this.size];

      for (int var5 = 0; var5 < this.size; var5++) {
         var6[var5] = (short)(var1[var5] >>> 40);
         var3[var5] = (int)(var1[var5] >>> 8);
         var4[var5] = (byte)(var1[var5] & 63L);
      }

      return new CompressedBlockArray(var6, var3, var4);
   }
}
