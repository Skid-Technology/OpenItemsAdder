package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.DoubleVector3Record;
import java.util.UUID;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

class EntityStateSnapshot {
   @Nullable
   private final UUID tJ;
   private final double tK;
   private final double tL;
   private final double tM;
   private final float tN;
   private final float tO;
   private final boolean tP;
   private final float tQ;
   private final float tR;
   private final float tS;
   private final float tT;
   private final float tU;
   private final boolean tV;
   private final boolean tW;
   private final boolean tX;
   private final boolean tY;
   private final boolean tZ;
   private final boolean ua;
   private final boolean ub;
   private final int uc;
   private final float ud;
   private final boolean ue;
   private final boolean uf;
   private final boolean ug;
   private final boolean uh;
   @Nullable
   private final VisibilityFlags ui;
   private final double uj;
   private final boolean uk;
   @Nullable
   private final DoubleVector3Record ul;
   private final double um;
   private final float un;
   private final float uo;
   private final float up;
   private final boolean uq;

   private EntityStateSnapshot(
      @Nullable UUID var1,
      double var2,
      double var4,
      double var6,
      float var8,
      float var9,
      boolean var10,
      float var11,
      float var12,
      float var13,
      float var14,
      float var15,
      boolean var16,
      boolean var17,
      boolean var18,
      boolean var19,
      boolean var20,
      boolean var21,
      boolean var22,
      int var23,
      float var24,
      boolean var25,
      boolean var26,
      boolean var27,
      boolean var28,
      @Nullable VisibilityFlags var29,
      double var30,
      boolean var32,
      @Nullable DoubleVector3Record var33,
      double var34,
      float var36,
      float var37,
      float var38,
      boolean var39
   ) {
      this.tJ = var1;
      this.tK = var2;
      this.tL = var4;
      this.tM = var6;
      this.tN = var8;
      this.tO = var9;
      this.tP = var10;
      this.tQ = var11;
      this.tR = var12;
      this.tS = var13;
      this.tT = var14;
      this.tU = var15;
      this.tV = var16;
      this.tW = var17;
      this.tX = var18;
      this.tY = var19;
      this.tZ = var20;
      this.ua = var21;
      this.ub = var22;
      this.uc = var23;
      this.ud = var24;
      this.ue = var25;
      this.uf = var26;
      this.ug = var27;
      this.uh = var28;
      this.ui = var29;
      this.uj = var30;
      this.uk = var32;
      this.ul = var33;
      this.um = var34;
      this.un = var36;
      this.uo = var37;
      this.up = var38;
      this.uq = var39;
   }

   Location toLocation() {
      return new Location(null, this.tK, this.tL, this.tM, this.tN, this.tO);
   }

   @Nullable
   public UUID uuid() {
      return this.tJ;
   }

   public double x() {
      return this.tK;
   }

   public double y() {
      return this.tL;
   }

   public double z() {
      return this.tM;
   }

   public float yaw() {
      return this.tN;
   }

   public float pitch() {
      return this.tO;
   }

   public boolean kg() {
      return this.tP;
   }

   public float kh() {
      return this.tQ;
   }

   public float ki() {
      return this.tR;
   }

   public float kj() {
      return this.tS;
   }

   public float method26() {
      return this.tT;
   }

   public float kl() {
      return this.tU;
   }

   public boolean km() {
      return this.tV;
   }

   public boolean kn() {
      return this.tW;
   }

   public boolean onGround() {
      return this.tX;
   }

   public boolean ko() {
      return this.tY;
   }

   public boolean kp() {
      return this.tZ;
   }

   public boolean kq() {
      return this.ua;
   }

   public boolean kr() {
      return this.ub;
   }

   public int ks() {
      return this.uc;
   }

   public float kt() {
      return this.ud;
   }

   public boolean ku() {
      return this.ue;
   }

   public boolean kv() {
      return this.uf;
   }

   public boolean kw() {
      return this.ug;
   }

   public boolean kx() {
      return this.uh;
   }

   @Nullable
   public VisibilityFlags ky() {
      return this.ui;
   }

   public double kz() {
      return this.uj;
   }

   public boolean kA() {
      return this.uk;
   }

   @Nullable
   public DoubleVector3Record kB() {
      return this.ul;
   }

   public double kC() {
      return this.um;
   }

   public float kD() {
      return this.un;
   }

   public float kE() {
      return this.uo;
   }

   public float kF() {
      return this.up;
   }

   public boolean kG() {
      return this.uq;
   }
}
