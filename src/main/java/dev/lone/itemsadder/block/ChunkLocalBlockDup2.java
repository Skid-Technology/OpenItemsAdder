package dev.lone.itemsadder.block;

public class ChunkLocalBlockDup2 {
   private final int jo;
   private final int jp;
   private final int jq;
   private final String jr;
   private final byte js;

   public ChunkLocalBlockDup2(int var1, int var2, int var3, String var4, byte var5) {
      this.jo = var1;
      this.jp = var2;
      this.jq = var3;
      this.jr = var4;
      this.js = var5;
   }

   public int relX() {
      return this.jo;
   }

   public int y() {
      return this.jp;
   }

   public int relZ() {
      return this.jq;
   }

   public String namespacedId() {
      return this.jr;
   }

   public byte age() {
      return this.js;
   }
}
