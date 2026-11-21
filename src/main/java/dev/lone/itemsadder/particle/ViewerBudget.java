package dev.lone.itemsadder.particle;

final class ViewerBudget {
   private final int aBa;
   private final int aBb;
   private int aBc;
   private int aBd;
   private int aBe;
   private long tE = Long.MIN_VALUE;

   ViewerBudget(int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         this.aBa = var1;
         this.aBb = var2;
      } else {
         throw new IllegalArgumentException("Viewer budget limits must be positive");
      }
   }

   synchronized ViewerBudgetLease JP() {
      this.aBc++;
      return new ViewerBudgetLease(this);
   }

   synchronized int JQ() {
      return this.aBc;
   }

   private synchronized boolean method2213(ViewerBudgetLease var1, long var2, int var4, int var5) {
      if (var4 < 0 || var5 < 0) {
         throw new IllegalArgumentException("Viewer budget costs cannot be negative");
      }

      if (var1.cz) {
         return false;
      }

      if (this.tE != var2) {
         this.tE = var2;
         this.aBd = 0;
         this.aBe = 0;
      }

      if (var1.tE != var2) {
         var1.tE = var2;
         var1.aBf = 0;
         var1.aBg = 0;
      }

      int var6 = Math.max(1, this.aBa / this.aBc);
      int var7 = Math.max(1, this.aBb / this.aBc);
      if (var4 <= this.aBa - this.aBd && var5 <= this.aBb - this.aBe && var4 <= var6 - var1.aBf && var5 <= var7 - var1.aBg) {
         this.aBd += var4;
         this.aBe += var5;
         var1.aBf += var4;
         var1.aBg += var5;
         return true;
      } else {
         return false;
      }
   }

   private synchronized void method2214(ViewerBudgetLease var1) {
      if (!var1.cz) {
         var1.cz = true;
         this.aBc--;
      }
   }
}
