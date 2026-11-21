package dev.lone.itemsadder.item;

public class ToggleSpec {
   private final boolean aqw;
   private final boolean aqx;
   private final int aqy;

   public ToggleSpec(boolean var1, boolean var2, int var3) {
      if (var3 < 0) {
         throw new IllegalArgumentException("'elytra.firework.cooldown_ticks' must be non-negative.");
      }

      this.aqw = var1;
      this.aqx = var2;
      this.aqy = var3;
   }

   public boolean enabled() {
      return this.aqw;
   }

   public boolean CJ() {
      return this.aqx;
   }

   public int lR() {
      return this.aqy;
   }
}
