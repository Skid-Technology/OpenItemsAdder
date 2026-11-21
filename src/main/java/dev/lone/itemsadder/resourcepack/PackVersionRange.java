package dev.lone.itemsadder.resourcepack;

class PackVersionRange {
   private final PackVersion min;
   private final PackVersion max;

   private PackVersionRange(PackVersion var1, PackVersion var2) {
      this.min = var1;
      this.max = var2;
   }

   boolean contains(PackVersion var1) {
      return this.min.compareVersion(var1) <= 0 && this.max.compareVersion(var1) >= 0;
   }
}
