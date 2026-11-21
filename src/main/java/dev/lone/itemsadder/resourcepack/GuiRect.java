package dev.lone.itemsadder.resourcepack;

public class GuiRect {
   private final int bDK;
   private final int bDL;
   private final int bDM;
   private final int bDN;

   private GuiRect(int var1, int var2, int var3, int var4) {
      this.bDK = var1;
      this.bDL = var2;
      this.bDM = var3;
      this.bDN = var4;
   }

   public int type() {
      return this.bDK;
   }

   public int dataOffset() {
      return this.bDL;
   }

   public int length() {
      return this.bDM;
   }

   public int crcOffset() {
      return this.bDN;
   }
}
