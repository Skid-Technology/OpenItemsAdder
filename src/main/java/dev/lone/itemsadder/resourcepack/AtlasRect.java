package dev.lone.itemsadder.resourcepack;

class AtlasRect {
   private final int but;
   private final int buu;
   private final int buv;
   private final int buw;

   private AtlasRect(int var1, int var2, int var3, int var4) {
      this.but = var1;
      this.buu = var2;
      this.buv = var3;
      this.buw = var4;
   }

   public int width() {
      return this.but;
   }

   public int height() {
      return this.buu;
   }

   public int renderedHeight() {
      return this.buv;
   }

   public int ascent() {
      return this.buw;
   }
}
