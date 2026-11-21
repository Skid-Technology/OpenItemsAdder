package dev.lone.itemsadder.resourcepack;

import java.io.IOException;
import java.nio.file.Path;

final class PackCacheException extends IOException {
   private PackCacheException(Path var1, Exception var2) {
      super("Invalid vanilla asset index: " + var1, var2);
   }
}
