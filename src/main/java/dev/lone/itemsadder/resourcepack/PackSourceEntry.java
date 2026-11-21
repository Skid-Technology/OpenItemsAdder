package dev.lone.itemsadder.resourcepack;

import java.nio.file.Path;

class PackSourceEntry {
   private final String namespace;
   private final Path source;
   private final boolean geometry;

   private PackSourceEntry(String var1, Path var2, boolean var3) {
      this.namespace = var1;
      this.source = var2;
      this.geometry = var3;
   }
}
