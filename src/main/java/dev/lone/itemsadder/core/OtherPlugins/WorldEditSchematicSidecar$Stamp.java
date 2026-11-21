package dev.lone.itemsadder.Core.OtherPlugins;

import java.nio.file.attribute.FileTime;

class WorldEditSchematicSidecar$Stamp {
   private final long size;
   private final FileTime modified;

   WorldEditSchematicSidecar$Stamp(long size, FileTime modified) {
      this.size = size;
      this.modified = modified;
   }
}
