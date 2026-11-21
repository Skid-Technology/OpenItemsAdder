package dev.lone.itemsadder.script;

public class CompilationResult {
   long aDe;
   final String[] aDf;
   final Throwable aDg;

   public CompilationResult(long var1) {
      this.aDe = var1;
      this.aDg = null;
      this.aDf = null;
   }

   public CompilationResult(long var1, String[] var3) {
      this.aDg = null;
      this.aDe = var1;
      this.aDf = var3;
   }

   public CompilationResult(long var1, Throwable var3) {
      this.aDg = var3;
      this.aDe = var1;
      this.aDf = null;
   }

   public long KF() {
      return this.aDe;
   }

   public Throwable KG() {
      return this.aDg;
   }

   public String[] KH() {
      return this.aDf;
   }

   public boolean KI() {
      return this.aDf != null || this.aDg != null;
   }
}
