package dev.lone.itemsadder.resourcepack;

import java.io.IOException;

public final class AssetDownloadException extends IOException {
   private AssetDownloadException(IOException var1) {
      super("Cannot retrieve the official Minecraft asset catalog", var1);
   }
}
