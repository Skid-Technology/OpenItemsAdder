package dev.lone.itemsadder.entity;

import java.util.UUID;

public class TrackedEntitySummary {
   private final int Tm;
   private final long Tn;
   private final String To;
   private final String Tp;
   private final UUID Tq;
   private final double Tr;
   private final double Ts;
   private final double Tt;
   private final double Tu;
   private final double Tv;
   private final boolean Tw;

   public TrackedEntitySummary(
      int var1, long var2, String var4, String var5, UUID var6, double var7, double var9, double var11, double var13, double var15, boolean var17
   ) {
      this.Tm = var1;
      this.Tn = var2;
      this.To = var4;
      this.Tp = var5;
      this.Tq = var6;
      this.Tr = var7;
      this.Ts = var9;
      this.Tt = var11;
      this.Tu = var13;
      this.Tv = var15;
      this.Tw = var17;
   }

   public int index() {
      return this.Tm;
   }

   public long timestamp() {
      return this.Tn;
   }

   public String uv() {
      return this.To;
   }

   public String uw() {
      return this.Tp;
   }

   public UUID entityId() {
      return this.Tq;
   }

   public double ka() {
      return this.Tr;
   }

   public double kb() {
      return this.Ts;
   }

   public double kc() {
      return this.Tt;
   }

   public double rc() {
      return this.Tu;
   }

   public double vB() {
      return this.Tv;
   }

   public boolean vC() {
      return this.Tw;
   }
}
