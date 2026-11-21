package dev.lone.itemsadder.resourcepack;

import java.io.IOException;

public final class UnsupportedPackVersionException extends IOException {
   private final String version;

   UnsupportedPackVersionException(String var1) {
      super("Minecraft version is absent from Mojang's manifest: " + var1);
      this.version = var1;
   }

   public String version() {
      return this.version;
   }
}
