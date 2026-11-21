package dev.lone.itemsadder.resourcepack;

public class PackDownloadResult {
   private final int bJn;
   private final String bJo;
   private final long bJp;

   public PackDownloadResult(int var1, String var2, long var3) {
      this.bJn = var1;
      this.bJo = var2;
      this.bJp = var3;
   }

   public int status() {
      return this.bJn;
   }

   public String message() {
      return this.bJo;
   }

   public long retryAfterSeconds() {
      return this.bJp;
   }
}
