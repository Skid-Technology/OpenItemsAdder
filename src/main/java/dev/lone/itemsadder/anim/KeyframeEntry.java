package dev.lone.itemsadder.anim;

public class KeyframeEntry {
   private final String DW;
   private final String DX;
   private final double DY;
   private final BoneKeyframe DZ;
   private final boolean Ea;
   private final Double Eb;

   public KeyframeEntry(String var1, String var2, double var3, BoneKeyframe var5) {
      this(var1, var2, var3, var5, false, null);
   }

   public KeyframeEntry(String var1, String var2, double var3, BoneKeyframe var5, boolean var6, Double var7) {
      this.DW = var1;
      this.DX = var2;
      this.DY = var3;
      this.DZ = var5;
      this.Ea = var6;
      this.Eb = var7;
   }

   public String id() {
      return this.DW;
   }

   public String bone() {
      return this.DX;
   }

   public double pQ() {
      return this.DY;
   }

   public BoneKeyframe pR() {
      return this.DZ;
   }

   public boolean pS() {
      return this.Ea;
   }

   public Double pT() {
      return this.Eb;
   }
}
