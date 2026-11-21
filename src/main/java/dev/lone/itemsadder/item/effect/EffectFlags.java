package dev.lone.itemsadder.item.effect;

class EffectFlags {
   private final boolean alA;
   private final boolean alB;
   private final boolean alC;
   static final EffectFlags alD = new EffectFlags(false, true, true);

   EffectFlags(boolean var1, boolean var2, boolean var3) {
      this.alA = var1;
      this.alB = var2;
      this.alC = var3;
   }

   public boolean CH() {
      return this.alA;
   }

   public boolean CI() {
      return this.alB;
   }

   public boolean CJ() {
      return this.alC;
   }
}
