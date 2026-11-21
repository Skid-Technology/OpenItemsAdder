package dev.lone.itemsadder.resourcepack;

class ShaderEntry {
   private final String bEB;
   private final int bEC;
   private final boolean bED;
   private final String bEE;

   ShaderEntry(String var1, int var2, boolean var3, String var4) {
      this.bEB = var1;
      this.bEC = var2;
      this.bED = var3;
      this.bEE = var4;
   }

   public String kind() {
      return this.bEB;
   }

   public int stageIndex() {
      return this.bEC;
   }

   public boolean success() {
      return this.bED;
   }

   public String log() {
      return this.bEE;
   }
}
