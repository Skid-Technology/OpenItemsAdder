package dev.lone.itemsadder.particle;

class ParticleState {
   private final ParticleId azi;
   private final String azj;
   private final double azk;
   private final double azl;
   private final double azm;
   private final double azn;
   private final double azo;
   private final double azp;
   private final double azq;
   private final double azr;
   private final double azs;
   private final int azt;
   private final int azu;
   private final int azv;
   private final double azw;
   private final ParentPose azx;

   private ParticleState(
      ParticleId var1,
      String var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      int var21,
      int var22,
      int var23,
      double var24,
      ParentPose var26
   ) {
      this.azi = var1;
      this.azj = var2;
      this.azk = var3;
      this.azl = var5;
      this.azm = var7;
      this.azn = var9;
      this.azo = var11;
      this.azp = var13;
      this.azq = var15;
      this.azr = var17;
      this.azs = var19;
      this.azt = var21;
      this.azu = var22;
      this.azv = var23;
      this.azw = var24;
      this.azx = var26;
   }

   boolean method7(ParticleState var1) {
      return this.azq == var1.azq && this.azr == var1.azr && this.azs == var1.azs && this.azt == var1.azt && this.azv == var1.azv;
   }

   public ParticleId Jn() {
      return this.azi;
   }

   public String Jo() {
      return this.azj;
   }

   public double ka() {
      return this.azk;
   }

   public double kb() {
      return this.azl;
   }

   public double kc() {
      return this.azm;
   }

   public double sv() {
      return this.azn;
   }

   public double HX() {
      return this.azo;
   }

   public double sw() {
      return this.azp;
   }

   public double Jp() {
      return this.azq;
   }

   public double Jq() {
      return this.azr;
   }

   public double Jr() {
      return this.azs;
   }

   public int pG() {
      return this.azt;
   }

   public int tg() {
      return this.azu;
   }

   public int Js() {
      return this.azv;
   }

   public double Jt() {
      return this.azw;
   }

   public ParentPose Ju() {
      return this.azx;
   }
}
