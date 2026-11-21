package dev.lone.itemsadder.resourcepack;

import org.jetbrains.annotations.Nullable;

class PackResolutionEntry {
   @Nullable
   private final PackMetadata bBM;
   private final PackResolutionKind bBN;

   private PackResolutionEntry(@Nullable PackMetadata var1, PackResolutionKind var2) {
      this.bBM = var1;
      this.bBN = var2;
   }

   @Nullable
   public PackMetadata Xb() {
      return this.bBM;
   }

   public PackResolutionKind UO() {
      return this.bBN;
   }
}
