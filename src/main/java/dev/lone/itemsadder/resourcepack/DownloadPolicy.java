package dev.lone.itemsadder.resourcepack;

public class DownloadPolicy {
   private final boolean hS;
   private final boolean hT;
   private final int hU;
   private final int hV;
   private final boolean hW;
   private final int hX;
   private final int hY;
   private final int hZ;
   private final int ia;

   public DownloadPolicy(boolean var1, boolean var2, int var3, int var4, boolean var5, int var6, int var7, int var8, int var9) {
      this.hS = var1;
      this.hT = var2;
      this.hU = var3;
      this.hV = var4;
      this.hW = var5;
      this.hX = var6;
      this.hY = var7;
      this.hZ = var8;
      this.ia = var9;
   }

   public boolean blockNonGameRequests() {
      return this.hS;
   }

   public boolean rateLimitEnabled() {
      return this.hT;
   }

   public int maxRequests() {
      return this.hU;
   }

   public int periodSeconds() {
      return this.hV;
   }

   public boolean cooldownEnabled() {
      return this.hW;
   }

   public int afterRejections() {
      return this.hX;
   }

   public int cooldownMinutes() {
      return this.hY;
   }

   public int maxConcurrentDownloadsPerIp() {
      return this.hZ;
   }

   public int maxSpeedMibPerSecond() {
      return this.ia;
   }
}
