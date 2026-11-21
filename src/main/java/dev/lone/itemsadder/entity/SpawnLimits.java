package dev.lone.itemsadder.entity;

public class SpawnLimits {
   private final boolean wq;
   private final int wr;
   private final boolean ws;
   private final int wt;
   private final int wu;
   public static final SpawnLimits wv = new SpawnLimits(true, 4);

   public SpawnLimits(boolean var1, int var2) {
      this(var1, var2, false, 2, 8);
   }

   public SpawnLimits(boolean var1, int var2, boolean var3, int var4, int var5) {
      if (var2 < 2 || var2 > 40) {
         throw new IllegalArgumentException("client_interpolation.settle_ticks must be an integer from 2 to 40");
      }

      if (var4 >= 1 && var4 <= 8 && var5 >= 1 && var5 <= 8) {
         this.wq = var1;
         this.wr = var2;
         this.ws = var3;
         this.wt = var4;
         this.wu = var5;
      } else {
         throw new IllegalArgumentException("client_interpolation intervals must be integers from 1 to 8");
      }
   }

   public boolean enabled() {
      return this.wq;
   }

   public int perChunk() {
      return this.wr;
   }

   public boolean lL() {
      return this.ws;
   }

   public int lM() {
      return this.wt;
   }

   public int lN() {
      return this.wu;
   }
}
