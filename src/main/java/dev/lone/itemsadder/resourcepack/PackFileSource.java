package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.DigestingInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class PackFileSource {
   private final Path bwJ;
   private final String bwK;
   private final int bwL;

   PackFileSource(Path var1, String var2, int var3) {
      this.bwJ = var1;
      this.bwK = var2;
      this.bwL = var3;
   }

   InputStream openDigesting() {
      InputStream var1 = Files.newInputStream(this.bwJ);
      MessageDigest var2 = PackFileCache.sha256Digest();
      return new DigestingInputStream(this, var1, var2);
   }

   public Path path() {
      return this.bwJ;
   }

   public String digest() {
      return this.bwK;
   }

   public int length() {
      return this.bwL;
   }
}
