package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.anim.BoneSpec;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class TrackedEntityRecord {
   private final int Rl;
   private final long Rm;
   private final String Rn;
   private final String Ro;
   private final UUID Rp;
   @Nullable
   private final UUID Rq;
   private final double Rr;
   private final double Rs;
   private final double Rt;
   private final double Ru;
   private final double Rv;
   private final double Rw;
   private final double Rx;
   private final float Ry;
   private final float Rz;
   private final boolean RA;
   private final float RB;
   private final String RC;
   private final String RD;
   private final boolean RE;
   private final String RF;
   @Nullable
   private final BoneSpec RG;
   private final Map RH;

   public TrackedEntityRecord(
      int var1,
      long var2,
      String var4,
      String var5,
      UUID var6,
      @Nullable UUID var7,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      float var22,
      float var23,
      boolean var24,
      float var25,
      String var26,
      String var27,
      boolean var28,
      String var29,
      @Nullable BoneSpec var30,
      Map var31
   ) {
      this.Rl = var1;
      this.Rm = var2;
      this.Rn = var4;
      this.Ro = var5;
      this.Rp = var6;
      this.Rq = var7;
      this.Rr = var8;
      this.Rs = var10;
      this.Rt = var12;
      this.Ru = var14;
      this.Rv = var16;
      this.Rw = var18;
      this.Rx = var20;
      this.Ry = var22;
      this.Rz = var23;
      this.RA = var24;
      this.RB = var25;
      this.RC = var26;
      this.RD = var27;
      this.RE = var28;
      this.RF = var29;
      this.RG = var30;
      this.RH = var31;
   }

   public int index() {
      return this.Rl;
   }

   public long timestamp() {
      return this.Rm;
   }

   public String uv() {
      return this.Rn;
   }

   public String uw() {
      return this.Ro;
   }

   public UUID entityId() {
      return this.Rp;
   }

   @Nullable
   public UUID worldId() {
      return this.Rq;
   }

   public double ka() {
      return this.Rr;
   }

   public double kb() {
      return this.Rs;
   }

   public double kc() {
      return this.Rt;
   }

   public double rc() {
      return this.Ru;
   }

   public double uy() {
      return this.Rv;
   }

   public double kz() {
      return this.Rw;
   }

   public double uz() {
      return this.Rx;
   }

   public float kj() {
      return this.Ry;
   }

   public float kh() {
      return this.Rz;
   }

   public boolean kr() {
      return this.RA;
   }

   public float kt() {
      return this.RB;
   }

   public String rl() {
      return this.RC;
   }

   public String uA() {
      return this.RD;
   }

   public boolean uB() {
      return this.RE;
   }

   public String uC() {
      return this.RF;
   }

   @Nullable
   public BoneSpec boneSpec() {
      return this.RG;
   }

   public Map metadata() {
      return this.RH;
   }
}
