package dev.lone.itemsadder.particle;

final class ViewerBudgetLease implements AutoCloseable {
   private long tE;
   private int aBf;
   private int aBg;
   private boolean cz;

   ViewerBudgetLease(ViewerBudget var1) {
      this.aBh = var1;
      this.tE = Long.MIN_VALUE;
   }

   boolean method1796(long var1, int var3, int var4) {
      return this.aBh.method2213(this, var1, var3, var4);
   }

   @Override
   public void close() {
      this.aBh.method2214(this);
   }
}
