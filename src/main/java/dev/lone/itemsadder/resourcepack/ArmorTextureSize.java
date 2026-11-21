package dev.lone.itemsadder.resourcepack;

class ArmorTextureSize {
   public int height;
   public int width;

   public static ArmorTextureSize defaultSize() {
      return new ArmorTextureSize(64, 32);
   }

   public ArmorTextureSize() {
   }

   public ArmorTextureSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   @Override
   public String toString() {
      return this.width + "x" + this.height;
   }

   public boolean isEmpty() {
      return this.width == 0 && this.height == 0;
   }
}
