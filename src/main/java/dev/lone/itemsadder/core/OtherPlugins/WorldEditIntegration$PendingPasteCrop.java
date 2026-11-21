package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.worldedit.world.block.BlockType;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.CropHistory;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;

class WorldEditIntegration$PendingPasteCrop {
   private final ByteNamedKey entry;
   private final BlockType blockType;
   private final CropHistory history;

   private WorldEditIntegration$PendingPasteCrop(ByteNamedKey entry, BlockType blockType, CropHistory history) {
      this.entry = entry;
      this.blockType = blockType;
      this.history = history;
   }
}
