package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.worldedit.math.BlockVector3;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import java.util.Map;

class WorldEditSchematicSidecar$Data {
   private final Map blocks;
   private final Map crops;
   static final WorldEditSchematicSidecar$Data EMPTY = new WorldEditSchematicSidecar$Data(Map.of(), Map.of());

   WorldEditSchematicSidecar$Data(Map blocks, Map crops) {
      this.blocks = blocks;
      this.crops = crops;
   }

   boolean isEmpty() {
      return this.blocks.isEmpty() && this.crops.isEmpty();
   }
}
