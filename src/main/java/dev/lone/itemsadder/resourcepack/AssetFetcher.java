package dev.lone.itemsadder.resourcepack;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

@FunctionalInterface
interface AssetFetcher {
   void fetchToFile(URI var1, Path var2, long var3);

   default byte[] fetchBytes(URI var1, String var2, long var3, long var5) {
      throw new IOException("Byte ranges are unavailable in this transport");
   }
}
