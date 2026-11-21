package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.util.DoubleVector4;
import java.util.UUID;

class TimedSample {
   private final UUID Qq;
   private final double Qr;
   private final double Qs;
   private final double Qt;
   private final float Qu;
   private final float Qv;
   private final long Qw;
   private final float[] Qx;
   private final DoubleVector4[] Qy;

   private TimedSample(UUID var1, double var2, double var4, double var6, float var8, float var9, long var10, float[] var12, DoubleVector4[] var13) {
      this.Qq = var1;
      this.Qr = var2;
      this.Qs = var4;
      this.Qt = var6;
      this.Qu = var8;
      this.Qv = var9;
      this.Qw = var10;
      this.Qx = var12;
      this.Qy = var13;
   }

   public UUID mq() {
      return this.Qq;
   }

   public double ka() {
      return this.Qr;
   }

   public double kb() {
      return this.Qs;
   }

   public double kc() {
      return this.Qt;
   }

   public float bm() {
      return this.Qu;
   }

   public float uh() {
      return this.Qv;
   }

   public long ai() {
      return this.Qw;
   }

   public float[] ui() {
      return this.Qx;
   }

   public DoubleVector4[] uj() {
      return this.Qy;
   }
}
