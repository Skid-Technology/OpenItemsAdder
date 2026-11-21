package dev.lone.itemsadder.item.effect;

final class CooldownEntry {
   final String alF;
   int alG;
   int alH;
   double alI;

   CooldownEntry(String var1) {
      this.alF = var1;
   }

   int computeRemaining(int var1, double var2) {
      double var4 = var1 * var2 + this.alI;
      int var6 = Math.max(0, (int)Math.floor(var4));
      this.alI = var4 - var6;
      return var6;
   }
}
