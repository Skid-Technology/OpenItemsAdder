package dev.lone.itemsadder.util;

import dev.lone.itemsadder.world.BlockRegionIndex;
import dev.lone.itemsadder.world.BlockSectionData;
import dev.lone.itemsadder.world.CompressedBlockArray;
import dev.lone.itemsadder.world.PackedBlockStore;

public final class SpatialMath {
   private static final double NEAR_RADIUS_SQ = 4.0;

   private SpatialMath() {
   }

   static void collectVisibleBlocks(
      BlockRegionIndex var0, PackedBlockStore var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14
   ) {
      var1.advanceGeneration();

      for (int var16 = 0; var16 < var0.sectionCount(); var16++) {
         BlockSectionData var17 = var0.getSection(var16);
         if (var17 != null) {
            int var18 = var0.chunkXAt(var16);
            int var19 = var0.chunkZAt(var16);

            for (int var20 = 0; var20 < var17.layerCount(); var20++) {
               int var21 = var17.layerKeyAt(var20);
               CompressedBlockArray var22 = var17.layerAt(var20);

               for (int var23 = 0; var23 < var22.positionCount(); var23++) {
                  byte var24 = var22.faceMaskAt(var23);
                  if (var24 != 0) {
                     int var25 = var22.positionAt(var23) & 4095;
                     int var26 = var18 << 4 | var25 & 15;
                     int var27 = var21 << 4 | var25 >>> 8;
                     int var28 = var19 << 4 | var25 >>> 4 & 15;
                     long var29 = ChunkKeyMath.packChunkKey(var26, var27, var28);
                     byte var31 = var22.kindAt(var23);
                     double var32 = blockDistanceSq(var26, var27, var28, var24, var31, var2, var4, var6, var8, var10, var12, var14);
                     if (var32 >= 0.0) {
                        var1.offerEntry(var29, var22.modelIdAt(var23), var31, var32);
                     }
                  }
               }
            }
         }
      }
   }

   static double distanceSqForKey(long var0, byte var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      return distanceSqForKeyWithKind(var0, var2, (byte)0, var3, var5, var7, var9, var11, var13, var15);
   }

   static double distanceSqForKeyWithKind(
      long var0, byte var2, byte var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      return blockDistanceSq(
         ChunkKeyMath.unpackX(var0), ChunkKeyMath.unpackY(var0), ChunkKeyMath.unpackZ(var0), var2, var3, var4, var6, var8, var10, var12, var14, var16
      );
   }

   private static double blockDistanceSq(
      int var0, int var1, int var2, byte var3, byte var4, double var5, double var7, double var9, double var11, double var13, double var15, double var17
   ) {
      double var19 = var4 != 0 ? 1.5 : 0.5;
      double var21 = var4 == 1 ? 1.5 : 0.5;
      double var23 = var4 == 2 ? 1.5 : 0.5;
      double var25 = var0 + var19;
      double var27 = var1 + var21;
      double var29 = var2 + var23;
      double var31 = clampD(var5, var25 - var19, var25 + var19);
      double var33 = clampD(var7, var27 - var21, var27 + var21);
      double var35 = clampD(var9, var29 - var23, var29 + var23);
      double var37 = var31 - var5;
      double var39 = var33 - var7;
      double var41 = var35 - var9;
      double var43 = var37 * var37 + var39 * var39 + var41 * var41;
      if (var3 == 0 || var43 > var15) {
         return -1.0;
      } else if (var43 <= 4.0) {
         return var43;
      } else {
         return !ChunkKeyMath.isSphereInViewCone(var37, var39, var41, var11, var13, var43, var17) ? -1.0 : var43;
      }
   }

   private static double clampD(double var0, double var2, double var4) {
      return Math.max(var2, Math.min(var4, var0));
   }
}
