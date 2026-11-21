package dev.lone.itemsadder.resourcepack;

import java.net.URI;
import java.nio.file.Path;

class DefaultAssetFetcher implements AssetFetcher {
   @Override
   public void fetchToFile(URI var1, Path var2, long var3) {
      VanillaAssetCache.downloadFollowingRedirects(var1, var2, var3);
   }

   @Override
   public byte[] fetchBytes(URI var1, String var2, long var3, long var5) {
      return VanillaAssetCache.fetchRange(var1, var2, var3, var5);
   }
}
