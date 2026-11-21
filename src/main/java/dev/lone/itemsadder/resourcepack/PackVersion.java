package dev.lone.itemsadder.resourcepack;

class PackVersion implements Comparable {
   private final int major;
   private final int minor;

   PackVersion(int var1, int var2) {
      this.major = var1;
      this.minor = var2;
   }

   public int compareVersion(PackVersion var1) {
      int var2 = Integer.compare(this.major, var1.major);
      return var2 == 0 ? Integer.compare(this.minor, var1.minor) : var2;
   }
}
