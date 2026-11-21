package dev.lone.itemsadder.block;

class BlockSessionMessage {
   private final BlockSessionEntry nK;
   private final String nL;

   BlockSessionMessage(BlockSessionEntry var1, String var2) {
      this.nK = var1;
      this.nL = var2;
   }

   public BlockSessionEntry target() {
      return this.nK;
   }

   public String namespacedId() {
      return this.nL;
   }
}
