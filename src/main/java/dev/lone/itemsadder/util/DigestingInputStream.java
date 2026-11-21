package dev.lone.itemsadder.util;

import dev.lone.itemsadder.resourcepack.PackFileCache;
import dev.lone.itemsadder.resourcepack.PackFileSource;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HexFormat;

public class DigestingInputStream extends FilterInputStream {
   private long bytesRead;
   private boolean finished;

   DigestingInputStream(PackFileSource var1, InputStream var2, MessageDigest var3) {
      super(var2);
      this.digest = var3;
      this.source = var1;
   }

   @Override
   public int read() {
      int var1 = super.read();
      if (var1 < 0) {
         this.verifyDigest();
      } else {
         this.digest.update((byte)var1);
         this.bytesRead++;
      }

      return var1;
   }

   @Override
   public int read(byte[] buffer, int offset, int length) {
      int var4 = this.in.read(buffer, offset, length);
      if (var4 < 0) {
         this.verifyDigest();
      } else {
         this.digest.update(buffer, offset, var4);
         this.bytesRead += var4;
      }

      return var4;
   }

   private void verifyDigest() {
      if (!this.finished) {
         this.finished = true;
         PackFileCache.bwj.addAndGet(this.bytesRead);
         if (this.bytesRead != this.source.bwL || !HexFormat.of().formatHex(this.digest.digest()).equals(this.source.bwK)) {
            throw new IOException("Corrupt generated JSON cache entry: " + this.source.bwJ);
         }
      }
   }
}
