package dev.lone.itemsadder.particle;

class ParticleBounds {
   private final double auI;
   private final double auJ;
   private final double auK;
   private final double auL;
   private final double auM;
   private final double auN;

   ParticleBounds(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (Double.isFinite(var1)
         && Double.isFinite(var3)
         && Double.isFinite(var5)
         && Double.isFinite(var7)
         && Double.isFinite(var9)
         && Double.isFinite(var11)
         && !(var1 > var7)
         && !(var3 > var9)
         && !(var5 > var11)) {
         this.auI = var1;
         this.auJ = var3;
         this.auK = var5;
         this.auL = var7;
         this.auM = var9;
         this.auN = var11;
      } else {
         throw new IllegalArgumentException("Invalid particle entity bounds");
      }
   }

   public double HL() {
      return this.auI;
   }

   public double HM() {
      return this.auJ;
   }

   public double HN() {
      return this.auK;
   }

   public double HO() {
      return this.auL;
   }

   public double HP() {
      return this.auM;
   }

   public double HQ() {
      return this.auN;
   }
}
