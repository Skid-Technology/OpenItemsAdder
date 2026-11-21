package dev.lone.itemsadder.resourcepack;

class PackFileData {
   private final byte[] bwR;
   private final String bwS;
   private final int bwT;

   private PackFileData(byte[] var1, String var2, int var3) {
      this.bwR = var1;
      this.bwS = var2;
      this.bwT = var3;
   }

   public byte[] bytes() {
      return this.bwR;
   }

   public String digest() {
      return this.bwS;
   }

   public int length() {
      return this.bwT;
   }
}
