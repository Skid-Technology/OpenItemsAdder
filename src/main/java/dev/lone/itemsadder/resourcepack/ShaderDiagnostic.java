package dev.lone.itemsadder.resourcepack;

class ShaderDiagnostic {
   private final ShaderSeverity bFl;
   private final int bFm;
   private final int bFn;
   private final String bFo;

   private ShaderDiagnostic(ShaderSeverity var1, int var2, int var3, String var4) {
      this.bFl = var1;
      this.bFm = var2;
      this.bFn = var3;
      this.bFo = var4;
   }

   public ShaderSeverity severity() {
      return this.bFl;
   }

   public int line() {
      return this.bFm;
   }

   public int column() {
      return this.bFn;
   }

   public String message() {
      return this.bFo;
   }
}
