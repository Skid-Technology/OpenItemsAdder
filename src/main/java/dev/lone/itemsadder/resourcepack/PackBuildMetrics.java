package dev.lone.itemsadder.resourcepack;

final class PackBuildMetrics {
   private long bAS;
   private long bAT;
   private long bAU;
   private long bAV;
   private long bAW;
   private long bAX;
   private long bAY;
   private long bAZ;
   private int bBa;
   private long bBb;
   private long bBc;

   private PackBuildMetrics() {
   }

   private void reset() {
      this.bAS = 0L;
      this.bAT = 0L;
      this.bAU = 0L;
      this.bAV = 0L;
      this.bAW = 0L;
      this.bAX = 0L;
      this.bAY = 0L;
      this.bAZ = 0L;
      this.bBa = 0;
      this.bBb = 0L;
      this.bBc = 0L;
   }

   private PackBuildMetricsSnapshot WJ() {
      return new PackBuildMetricsSnapshot(this.bAS, this.bAT, this.bAU, this.bAV, this.bAW, this.bAX, this.bAY, this.bAZ, this.bBa, this.bBb, this.bBc);
   }
}
