package dev.lone.itemsadder.resourcepack;

import java.util.List;

class ShaderValidationResult {
   private final boolean bEy;
   private final String bEz;
   private final List bEA;

   ShaderValidationResult(boolean var1, String var2, List var3) {
      this.bEy = var1;
      this.bEz = var2;
      this.bEA = var3;
   }

   static ShaderValidationResult unavailable(String var0) {
      return new ShaderValidationResult(false, var0, List.of());
   }

   public boolean available() {
      return this.bEy;
   }

   public String unavailableReason() {
      return this.bEz;
   }

   public List messages() {
      return this.bEA;
   }
}
