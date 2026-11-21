package dev.lone.itemsadder.resourcepack;

class PackRangeEntry {
   private final String directory;
   private final SizeRange modern;
   private final SizeRange legacy;

   private PackRangeEntry(String var1, SizeRange var2, SizeRange var3) {
      this.directory = var1;
      this.modern = var2;
      this.legacy = var3;
   }
}
