package dev.lone.itemsadder.anim;

public class PlaybackRequest {
   private final String field18;
   private final double FG;
   private final double FH;
   private final long FI;
   private final boolean FJ;
   private final float FK;

   public PlaybackRequest(String var1, double var2, double var4, long var6, boolean var8, float var9) {
      this.field18 = var1;
      this.FG = var2;
      this.FH = var4;
      this.FI = var6;
      this.FJ = var8;
      this.FK = var9;
   }

   public String rl() {
      return this.field18;
   }

   public double rn() {
      return this.FG;
   }

   public double ro() {
      return this.FH;
   }

   public long ge() {
      return this.FI;
   }

   public boolean rm() {
      return this.FJ;
   }

   public float rp() {
      return this.FK;
   }
}
