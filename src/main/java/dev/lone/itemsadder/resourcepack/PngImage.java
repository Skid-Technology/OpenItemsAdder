package dev.lone.itemsadder.resourcepack;

public class PngImage {
   private final String aAN;
   private final int aAO;
   private final int aAP;
   private final byte[] aAQ;

   public PngImage(String var1, int var2, int var3, byte[] var4) {
      var4 = (byte[])var4.clone();
      this.aAN = var1;
      this.aAO = var2;
      this.aAP = var3;
      this.aAQ = var4;
   }

   public byte[] png() {
      return (byte[])this.aAQ.clone();
   }

   public String digest() {
      return this.aAN;
   }

   public int width() {
      return this.aAO;
   }

   public int height() {
      return this.aAP;
   }
}
