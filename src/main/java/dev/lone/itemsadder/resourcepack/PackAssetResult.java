package dev.lone.itemsadder.resourcepack;

import java.util.Locale;

class PackAssetResult {
   private final PackIssueCategory bxi;
   private final PackResolutionKind bxj;
   private final boolean bxk;
   private final String bxl;
   private final String bxm;
   private final String bxn;
   private final String bxo;

   private PackAssetResult(PackIssueCategory var1, PackResolutionKind var2, boolean var3, String var4, String var5, String var6, String var7) {
      this.bxi = var1;
      this.bxj = var2;
      this.bxk = var3;
      this.bxl = var4;
      this.bxm = var5;
      this.bxn = var6;
      this.bxo = var7;
   }

   static PackAssetResult method119(PackResolutionResult var0) {
      return new PackAssetResult(var0.kind(), var0.provenance(), false, "", "", "", var0.message());
   }

   static PackAssetResult method120(PackResolutionResult var0, PackValidationError var1) {
      String var2 = var1.reason();
      int var3 = var2.indexOf(" or matching Vanilla model for ");
      if (var3 >= 0) {
         var2 = var2.substring(0, var3);
      }

      return new PackAssetResult(var0.kind(), var0.provenance(), true, var1.declaration(), var1.model(), var2, "");
   }

   boolean UM() {
      return this.bxk && this.bxn.startsWith("original model reference is unresolved");
   }

   String Ll() {
      if (this.UM()) {
         return "Missing model: " + this.bxm;
      } else if (this.bxk && this.bxn.startsWith("item model has unresolved face texture slots")) {
         return "Model has unresolved textures: " + this.bxm;
      } else if (this.bxk && this.bxn.startsWith("cannot read texture value")) {
         return "Invalid texture value in model: " + this.bxm;
      } else if (this.bxk && this.bxn.contains("has no selected pack entry")) {
         return "Missing texture file referenced by model: " + this.bxm;
      } else {
         return this.bxk && this.bxn.contains("atlas")
            ? "Texture is missing from the item/block atlas: " + this.bxm
            : this.bxi.name().toLowerCase(Locale.ROOT).replace('_', ' ') + (this.bxk ? ": " + this.bxm : "");
      }
   }

   String UF() {
      return this.Ll() + "\u0000" + this.bxl + "\u0000" + this.bxm + "\u0000" + this.bxj + "\u0000" + this.bxn + "\u0000" + this.bxo;
   }

   public PackIssueCategory UN() {
      return this.bxi;
   }

   public PackResolutionKind UO() {
      return this.bxj;
   }

   public boolean UP() {
      return this.bxk;
   }

   public String UQ() {
      return this.bxl;
   }

   public String uM() {
      return this.bxm;
   }

   public String Ue() {
      return this.bxn;
   }

   public String message() {
      return this.bxo;
   }
}
