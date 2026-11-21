package dev.lone.itemsadder.resourcepack;

import java.util.List;

class ShaderStage {
   private final String bFz;
   private final int bFA;
   private final List bFB;

   ShaderStage(String var1, int var2, List var3) {
      this.bFz = var1;
      this.bFA = var2;
      this.bFB = var3;
   }

   public String file() {
      return this.bFz;
   }

   public int line() {
      return this.bFA;
   }

   public List includeStack() {
      return this.bFB;
   }
}
