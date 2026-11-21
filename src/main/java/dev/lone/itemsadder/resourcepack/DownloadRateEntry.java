package dev.lone.itemsadder.resourcepack;

public class DownloadRateEntry {
   private final long bJS;
   private final boolean bJT;

   public DownloadRateEntry(long var1, boolean var3) {
      this.bJS = var1;
      this.bJT = var3;
   }

   public long retryAfterSeconds() {
      return this.bJS;
   }

   public boolean cooldownStarted() {
      return this.bJT;
   }
}
