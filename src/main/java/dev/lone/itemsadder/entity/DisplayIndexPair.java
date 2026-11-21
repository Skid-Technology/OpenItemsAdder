package dev.lone.itemsadder.entity;

public class DisplayIndexPair {
   private final int blk;
   private final int bll;

   public DisplayIndexPair(int var1, int var2) {
      this.blk = var1;
      this.bll = var2;
   }

   public int entityIndex() {
      return this.blk;
   }

   public int poseIndex() {
      return this.bll;
   }
}
