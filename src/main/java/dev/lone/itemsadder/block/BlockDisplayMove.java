package dev.lone.itemsadder.block;

import org.bukkit.util.Vector;

class BlockDisplayMove {
   private final Vector mH;
   private final Vector mI;
   private final double mJ;
   private final BlockDisplayEntry mK;
   private final long mL;

   private BlockDisplayMove(Vector var1, Vector var2, double var3, BlockDisplayEntry var5, long var6) {
      this.mH = var1.clone();
      this.mI = var2.clone();
      this.mJ = var3;
      this.mK = var5;
      this.mL = var6;
   }

   public Vector lastOrigin() {
      return this.mH;
   }

   public Vector lastDir() {
      return this.mI;
   }

   public double lastReach() {
      return this.mJ;
   }

   public BlockDisplayEntry result() {
      return this.mK;
   }

   public long tick() {
      return this.mL;
   }
}
