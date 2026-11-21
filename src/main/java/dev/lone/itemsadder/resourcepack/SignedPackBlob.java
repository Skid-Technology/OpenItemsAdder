package dev.lone.itemsadder.resourcepack;

import java.util.List;

class SignedPackBlob {
   private final long directoryOffset;
   private final byte[] directory;
   private final byte[] end;
   private final List entries;
   private final String marker;

   private SignedPackBlob(long var1, byte[] var3, byte[] var4, List var5, String var6) {
      this.directoryOffset = var1;
      this.directory = var3;
      this.end = var4;
      this.entries = var5;
      this.marker = var6;
   }
}
