package dev.lone.itemsadder.resourcepack;

class AssetFileHeader {
   private final String bym;
   private final long byn;
   private final long byo;
   private final long byp;
   private final int byq;
   private final long byr;
   private final long bys;

   private AssetFileHeader(String var1, long var2, long var4, long var6, int var8, long var9, long var11) {
      this.bym = var1;
      this.byn = var2;
      this.byo = var4;
      this.byp = var6;
      this.byq = var8;
      this.byr = var9;
      this.bys = var11;
   }

   public String name() {
      return this.bym;
   }

   public long localOffset() {
      return this.byn;
   }

   public long recordEnd() {
      return this.byo;
   }

   public long compressedSize() {
      return this.byp;
   }

   public int method() {
      return this.byq;
   }

   public long uncompressedSize() {
      return this.byr;
   }

   public long crc() {
      return this.bys;
   }
}
