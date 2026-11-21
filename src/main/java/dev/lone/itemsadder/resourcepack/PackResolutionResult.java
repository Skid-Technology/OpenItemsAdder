package dev.lone.itemsadder.resourcepack;

import org.jetbrains.annotations.Nullable;

class PackResolutionResult {
   private final PackIssueCategory bBG;
   private final PackResolutionKind bBH;
   private final String bBI;
   @Nullable
   private final PackValidationError bBJ;

   PackResolutionResult(PackIssueCategory var1, PackResolutionKind var2, String var3) {
      this(var1, var2, var3, null);
   }

   PackResolutionResult(PackIssueCategory var1, PackResolutionKind var2, String var3, @Nullable PackValidationError var4) {
      this.bBG = var1;
      this.bBH = var2;
      this.bBI = var3;
      this.bBJ = var4;
   }

   boolean isFatal() {
      return this.bBG == PackIssueCategory.PLANNER_FATAL || this.bBG == PackIssueCategory.VANILLA_VERSION_SELECTION_ERROR;
   }

   public PackIssueCategory kind() {
      return this.bBG;
   }

   public PackResolutionKind provenance() {
      return this.bBH;
   }

   public String message() {
      return this.bBI;
   }

   @Nullable
   public PackValidationError tree() {
      return this.bBJ;
   }
}
