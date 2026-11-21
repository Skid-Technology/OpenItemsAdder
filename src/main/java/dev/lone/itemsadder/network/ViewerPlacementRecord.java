package dev.lone.itemsadder.network;

import java.util.UUID;

public class ViewerPlacementRecord {
   private final UUID Ws;
   private final UUID Wt;
   private final int Wu;
   private final int Wv;
   private final int Ww;
   private final double Wx;
   private final double Wy;
   private final double Wz;
   private final double WA;
   private final double WB;
   private final int WC;
   private final int WD;
   private final int WE;

   private ViewerPlacementRecord(
      UUID var1, UUID var2, int var3, int var4, int var5, double var6, double var8, double var10, double var12, double var14, int var16, int var17, int var18
   ) {
      this.Ws = var1;
      this.Wt = var2;
      this.Wu = var3;
      this.Wv = var4;
      this.Ww = var5;
      this.Wx = var6;
      this.Wy = var8;
      this.Wz = var10;
      this.WA = var12;
      this.WB = var14;
      this.WC = var16;
      this.WD = var17;
      this.WE = var18;
   }

   public UUID bk() {
      return this.Ws;
   }

   public UUID method20() {
      return this.Wt;
   }

   public int method101() {
      return this.Wu;
   }

   public int wx() {
      return this.Wv;
   }

   public int ut() {
      return this.Ww;
   }

   public double em() {
      return this.Wx;
   }

   public double en() {
      return this.Wy;
   }

   public double eo() {
      return this.Wz;
   }

   public double wy() {
      return this.WA;
   }

   public double wz() {
      return this.WB;
   }

   public int wA() {
      return this.WC;
   }

   public int wB() {
      return this.WD;
   }

   public int wC() {
      return this.WE;
   }
}
