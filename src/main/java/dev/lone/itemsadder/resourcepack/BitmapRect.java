package dev.lone.itemsadder.resourcepack;

class BitmapRect {
   private final int bGX;
   private final int bGY;
   private final int bGZ;
   private final int bHa;

   private BitmapRect(int var1, int var2, int var3, int var4) {
      this.bGX = var1;
      this.bGY = var2;
      this.bGZ = var3;
      this.bHa = var4;
   }

   public int width() {
      return this.bGX;
   }

   public int height() {
      return this.bGY;
   }

   public int renderedHeight() {
      return this.bGZ;
   }

   public int ascent() {
      return this.bHa;
   }
}
