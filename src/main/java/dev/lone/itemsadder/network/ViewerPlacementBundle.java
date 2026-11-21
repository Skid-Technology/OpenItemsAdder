package dev.lone.itemsadder.network;

import dev.lone.itemsadder.world.BlockRegionIndex;
import dev.lone.itemsadder.world.ChunkViewerState;

public class ViewerPlacementBundle {
   private final ViewerPlacementRecord WF;
   private final BlockRegionIndex WG;
   private final ChunkViewerState WH;

   private ViewerPlacementBundle(ViewerPlacementRecord var1, BlockRegionIndex var2, ChunkViewerState var3) {
      this.WF = var1;
      this.WG = var2;
      this.WH = var3;
   }

   public ViewerPlacementRecord wD() {
      return this.WF;
   }

   public BlockRegionIndex wE() {
      return this.WG;
   }

   public ChunkViewerState wF() {
      return this.WH;
   }
}
