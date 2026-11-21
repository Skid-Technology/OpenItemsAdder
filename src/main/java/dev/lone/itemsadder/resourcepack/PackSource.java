package dev.lone.itemsadder.resourcepack;

import java.nio.file.Path;

class PackSource {
   private final Path file;
   private final String sha1;

   PackSource(Path var1, String var2) {
      this.file = var1;
      this.sha1 = var2;
   }
}
