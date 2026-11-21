package dev.lone.itemsadder.resourcepack;

import java.util.List;
import java.util.Objects;

class ShaderIssue {
   private final ShaderSeverity bEP;
   private final String bEQ;
   private final ShaderFileKind bER;
   private final String bES;
   private final String bET;
   private final int bEU;
   private final int bEV;
   private final String bEW;
   private final List bEX;

   ShaderIssue(ShaderSeverity var1, String var2, ShaderFileKind var3, String var4, String var5, int var6, int var7, String var8, List var9) {
      var9 = var9 == null ? List.of() : List.copyOf(var9);
      this.bEP = var1;
      this.bEQ = var2;
      this.bER = var3;
      this.bES = var4;
      this.bET = var5;
      this.bEU = var6;
      this.bEV = var7;
      this.bEW = var8;
      this.bEX = var9;
   }

   String formatReport() {
      StringBuilder var1 = new StringBuilder("[").append(this.bEP).append("] ").append(this.bEQ);
      if (this.bET != null) {
         var1.append("\nFile: ").append(this.bET);
         if (this.bEU > 0) {
            var1.append(':').append(this.bEU);
         }

         if (this.bEV > 0) {
            var1.append(':').append(this.bEV);
         }
      }

      if (this.bER != ShaderFileKind.NONE) {
         var1.append("\nStage: ").append(this.bER);
      }

      if (this.bES != null && !Objects.equals(this.bES, this.bET)) {
         var1.append("\nResource: ").append(this.bES);
      }

      var1.append("\n").append(this.bEW);
      if (this.bEX.size() > 1) {
         var1.append("\nInclude stack: ").append(String.join(" -> ", this.bEX));
      }

      return var1.toString();
   }

   public ShaderSeverity severity() {
      return this.bEP;
   }

   public String code() {
      return this.bEQ;
   }

   public ShaderFileKind stage() {
      return this.bER;
   }

   public String resourcePath() {
      return this.bES;
   }

   public String originalFile() {
      return this.bET;
   }

   public int line() {
      return this.bEU;
   }

   public int column() {
      return this.bEV;
   }

   public String message() {
      return this.bEW;
   }

   public List includeStack() {
      return this.bEX;
   }
}
