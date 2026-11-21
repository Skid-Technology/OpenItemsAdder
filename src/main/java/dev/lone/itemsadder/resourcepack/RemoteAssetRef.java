package dev.lone.itemsadder.resourcepack;

import java.net.URI;

public class RemoteAssetRef {
   private final String version;
   private final String sha1;
   private final long size;
   private final URI url;

   public RemoteAssetRef(String var1, String var2, long var3, URI var5) {
      this.version = var1;
      this.sha1 = var2;
      this.size = var3;
      this.url = var5;
   }
}
