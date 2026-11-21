package dev.lone.itemsadder.entity;

public class DisplayUpdateRecord {
   private final DisplayEntityKey blo;
   private final double blp;
   private final double blq;
   private final double blr;
   private final Object bls;
   private final boolean blt;
   private final boolean blu;

   public DisplayUpdateRecord(DisplayEntityKey var1, double var2, double var4, double var6, Object var8, boolean var9, boolean var10) {
      this.blo = var1;
      this.blp = var2;
      this.blq = var4;
      this.blr = var6;
      this.bls = var8;
      this.blt = var9;
      this.blu = var10;
   }

   public DisplayEntityKey key() {
      return this.blo;
   }

   public double ka() {
      return this.blp;
   }

   public double kb() {
      return this.blq;
   }

   public double kc() {
      return this.blr;
   }

   public Object value() {
      return this.bls;
   }

   public boolean Os() {
      return this.blt;
   }

   public boolean Pt() {
      return this.blu;
   }
}
