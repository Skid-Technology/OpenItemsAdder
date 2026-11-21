package dev.lone.itemsadder.resourcepack;

import java.util.List;

class ShaderReport {
   private final List bFr;

   ShaderReport(List var1) {
      this.bFr = var1;
   }

   long countErrors() {
      return this.bFr.stream().filter(var0 -> var0.severity() == ShaderSeverity.ERROR).count();
   }

   public List diagnostics() {
      return this.bFr;
   }
}
