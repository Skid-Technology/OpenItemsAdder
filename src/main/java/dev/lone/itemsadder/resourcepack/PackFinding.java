package dev.lone.itemsadder.resourcepack;

class PackFinding {
   private final PackFindingSeverity severity;
   private final String code;
   private final String path;
   private final String source;
   private final String detail;

   PackFinding(PackFindingSeverity var1, String var2, String var3, String var4, String var5) {
      this.severity = var1;
      this.code = var2;
      this.path = var3;
      this.source = var4;
      this.detail = var5;
   }
}
