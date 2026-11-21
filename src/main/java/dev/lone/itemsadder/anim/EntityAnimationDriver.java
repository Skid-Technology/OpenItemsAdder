package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.block.BlockPlacementRecord;
import dev.lone.itemsadder.entity.CustomEntityDefinition;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.physics.BoxBounds;
import dev.lone.itemsadder.util.DoubleVector4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class EntityAnimationDriver {
   final AnimationWindow OW;
   private final CustomEntityImpl OX;
   private final List OY;
   private final int[] OZ;
   private final int[] Pa;
   private final int[] Pb;
   private final float[] Pc;
   private final float[] Pd;
   private final float[] Pe;
   private final float[] Pf;
   private final float[] Pg;
   private final float[] Ph;
   private final Vector3f[] Pi;
   private final Matrix4f[] Pj;
   private final int[][] Pk;
   private final int[][] Pl;
   private final int[][] Pm;
   private final boolean[] Pn;
   private final boolean[] Po;
   private final boolean[] Pp;
   private final float[] Pq;
   private float Pr;
   private final float[] Ps;
   private final Matrix4f Pt = new Matrix4f();
   private final Quaternionf Pu = new Quaternionf();
   private final Vector3f Pv = new Vector3f();
   private final Vector3f Pw = new Vector3f();
   private final Vector3f Px = new Vector3f();
   private final Vector3f Py = new Vector3f();
   private final Vector3f Pz = new Vector3f();
   private final Vector3f PA = new Vector3f();
   private final Vector3f PB = new Vector3f();
   private final Vector3f PC = new Vector3f();
   private final Vector3f PD = new Vector3f();
   private final Vector3f PE = new Vector3f();
   private final Vector3f PF = new Vector3f();
   private volatile TimedSample PG;
   private TimedSampleGroup PH;
   private volatile AnimationFrameBundle PI;
   private AnimationFrameBundle PJ;
   private volatile boolean wq;
   private long PK;
   private volatile long bG;
   private long PL;
   private TimedSampleGroup PM;
   private boolean PN;
   private boolean PO = true;
   private UUID field128;
   private double PQ;
   private double PR;
   private double PS;
   long PT;
   long PU;
   long PV;
   long PW;
   long PX;
   long PY;

   EntityAnimationDriver(CustomEntityImpl var1, AnimationWindow var2, List var3) {
      this.OX = var1;
      this.OW = var2;
      this.OY = var3;
      int var4 = var2.qm().size();
      this.OZ = new int[var4];
      this.Pa = new int[var4];
      this.Pb = new int[var4];
      this.Pc = new float[var4];
      this.Pd = new float[var4];
      this.Pe = new float[var4];
      this.Pi = new Vector3f[var4];
      this.Pf = new float[var4];
      this.Pg = new float[var4];
      this.Ph = new float[var4];
      this.Pj = new Matrix4f[var3.size()];
      this.Pk = new int[var4][];
      this.Pl = new int[var4][];
      this.Pm = new int[var4][];
      this.Pn = new boolean[var4];
      this.Ps = new float[var4 * 4];
      this.Po = new boolean[var4];
      this.Pp = new boolean[var4];
      this.Pq = new float[var4 * 3];
      Matrix4f[] var5 = new Matrix4f[var3.size()];

      for (CustomEntityDefinition var7 : var3) {
         int var8 = var7.nH();
         var5[var8] = new Matrix4f(var7.getTransform());
         this.Pj[var8] = new Matrix4f();
         if (var7.getParent() != null) {
            var5[var8].mulLocal(var5[var7.getParent().nH()]);
         }
      }

      for (int var9 = 0; var9 < var4; var9++) {
         AnimationEventMarker var11 = (AnimationEventMarker)var2.qm().get(var9);
         this.OZ[var9] = ((CustomEntityDefinition)var1.getModel().AL.get(var11.qr())).nH();
         this.Pa[var9] = ((CustomEntityDefinition)var1.getModel().AL.get(var11.qs())).nH();
         this.Pb[var9] = ((CustomEntityDefinition)var1.getModel().AL.get(var11.qt())).nH();
         this.Pc[var9] = var5[this.Pb[var9]].m31();
         this.Pd[var9] = var5[this.Pb[var9]].getScale(new Vector3f()).y;
         this.Pi[var9] = new Vector3f(0.0F, 1.0F, 0.0F);
         this.Pk[var9] = this.av(this.OZ[var9]);
      }

      for (int var10 = 0; var10 < var4; var10++) {
         this.Pl[var10] = this.av(this.Pa[var10]);
         this.Pm[var10] = this.av(this.Pb[var10]);
      }
   }

   private int[] av(int var1) {
      for (int var2 = 0; var2 < this.OZ.length; var2++) {
         if (this.OZ[var2] == var1 && this.Pk[var2] != null) {
            return this.Pk[var2];
         }

         if (this.Pa[var2] == var1 && this.Pl[var2] != null) {
            return this.Pl[var2];
         }

         if (this.Pb[var2] == var1 && this.Pm[var2] != null) {
            return this.Pm[var2];
         }
      }

      ArrayList var5 = new ArrayList();

      for (int var3 = var1; var3 < this.OY.size(); var3++) {
         CustomEntityDefinition var4 = (CustomEntityDefinition)this.OY.get(var3);

         while (var4 != null && var4.nH() != var1) {
            var4 = var4.getParent();
         }

         if (var4 != null) {
            var5.add(var3);
         }
      }

      return var5.stream().mapToInt(Integer::intValue).toArray();
   }

   void tU() {
      this.bG++;
      this.PI = null;
      this.PG = null;
   }

   void method9112(World var1, Location var2, boolean var3) {
      this.wq = var3;
      TimedSample var4 = this.PG;
      if (var3 && var1 != null && var4 != null && var4.Qw == this.bG && var1.getUID().equals(var4.Qq) && !(method9127(var4, var2) > 0.25)) {
         AnimationFrameBundle var5 = this.PI;
         long var6 = System.nanoTime();
         if (var5 != null && var5.PZ.Qq.equals(var4.Qq) && var5.PZ.Qw == var4.Qw && method9113(var5.Qa, var4.Qy)) {
            boolean var8 = true;
            if (var6 >= this.PK) {
               this.PK = var6 + 500000000L;
               var8 = !var5.Qc;

               for (BlockPlacementRecord var10 : var5.Qb) {
                  this.PY++;
                  if (var10.QC == null
                     ? var1.isChunkLoaded(var10.Qz >> 4, var10.QB >> 4)
                     : !var1.isChunkLoaded(var10.Qz >> 4, var10.QB >> 4) || !var1.getBlockAt(var10.Qz, var10.QA, var10.QB).getBlockData().equals(var10.QC)) {
                     var8 = false;
                     break;
                  }
               }
            }

            if (var8) {
               if (var5.PZ != var4) {
                  this.PI = new AnimationFrameBundle(var4, var5.Qa, var5.Qb, var5.Qc);
               }

               return;
            }
         }

         ArrayList var19 = new ArrayList();

         for (DoubleVector4 var12 : var4.Qy) {
            if (var12 != null) {
               int var13 = (int)Math.floor(var12.x);
               int var14 = (int)Math.floor(var12.z);
               int var15 = (int)Math.floor(var12.y - var12.w);
               int var16 = (int)Math.floor(var12.y);
               int var17 = -1;

               for (int var18 = 0; var18 < var19.size(); var18++) {
                  if (((SampleRange)var19.get(var18)).Qd == var13 && ((SampleRange)var19.get(var18)).Qe == var14) {
                     var17 = var18;
                     break;
                  }
               }

               if (var17 < 0) {
                  var19.add(new SampleRange(var13, var14, var15, var16, List.of()));
               } else {
                  SampleRange var31 = (SampleRange)var19.get(var17);
                  var19.set(var17, new SampleRange(var13, var14, Math.min(var15, var31.Qf), Math.max(var16, var31.Qg), List.of()));
               }
            }
         }

         ArrayList var21 = new ArrayList();
         boolean var23 = false;

         for (int var24 = 0; var24 < var19.size(); var24++) {
            SampleRange var25 = (SampleRange)var19.get(var24);
            ArrayList var26 = new ArrayList();
            if (!var1.isChunkLoaded(var25.Qd >> 4, var25.Qe >> 4)) {
               var21.add(new BlockPlacementRecord(var25.Qd, var1.getMinHeight(), var25.Qe, null));
            } else {
               for (int var27 = Math.max(var25.Qf, var1.getMinHeight()); var27 <= Math.min(var25.Qg, var1.getMaxHeight() - 1); var27++) {
                  Block var28 = var1.getBlockAt(var25.Qd, var27, var25.Qe);
                  BlockData var29 = var28.getBlockData();
                  this.PY++;
                  var21.add(new BlockPlacementRecord(var25.Qd, var27, var25.Qe, var29));
                  var23 |= var29.getMaterial() == Material.MOVING_PISTON || Tag.SHULKER_BOXES.isTagged(var29.getMaterial());

                  for (BoundingBox var32 : var28.getCollisionShape().getBoundingBoxes()) {
                     var26.add(
                        new BoxBounds(
                           var25.Qd + var32.getMinX(),
                           var27 + var32.getMinY(),
                           var25.Qe + var32.getMinZ(),
                           var25.Qd + var32.getMaxX(),
                           var27 + var32.getMaxY(),
                           var25.Qe + var32.getMaxZ()
                        )
                     );
                  }
               }

               var19.set(var24, new SampleRange(var25.Qd, var25.Qe, var25.Qf, var25.Qg, List.copyOf(var26)));
            }
         }

         this.PI = new AnimationFrameBundle(var4, List.copyOf(var19), List.copyOf(var21), var23);
         this.PX++;
         this.PK = var6 + 500000000L;
      } else {
         this.PI = null;
      }
   }

   private static boolean method9113(List var0, DoubleVector4[] var1) {
      for (DoubleVector4 var5 : var1) {
         if (var5 != null) {
            boolean var6 = false;

            for (SampleRange var8 : var0) {
               if (var8.method71(var5)) {
                  var6 = true;
                  break;
               }
            }

            if (!var6) {
               return false;
            }
         }
      }

      return true;
   }

   private void tV() {
      AnimationFrameBundle var1 = this.PI;
      if (var1 != this.PJ) {
         this.PJ = var1;
         if (var1 == null) {
            this.PH = null;
         } else {
            TimedSample var2 = var1.PZ;
            double[] var3 = new double[this.OZ.length];
            float[] var4 = new float[this.OZ.length * 3];
            Arrays.fill(var3, Double.NaN);
            double var5 = Math.PI - var2.Qu;
            double var7 = Math.cos(var5);
            double var9 = Math.sin(var5);
            double var11 = 0.125 * var2.Qv;
            double var13 = Math.cos(Math.toRadians(this.OW.qo()));

            for (int var15 = 0; var15 < var3.length; var15++) {
               DoubleVector4 var16 = var2.Qy[var15 * 3];
               if (var16 != null) {
                  this.PT++;
                  double var17 = method9115(var1, var16);
                  if (Double.isFinite(var17)) {
                     this.PT += 2L;
                     double var19 = method9115(var1, var2.Qy[var15 * 3 + 1]);
                     double var21 = method9115(var1, var2.Qy[var15 * 3 + 2]);
                     double var23 = Double.isFinite(var19) ? (var17 - var19) / var11 : 0.0;
                     double var25 = Double.isFinite(var21) ? (var17 - var21) / var11 : 0.0;
                     double var27 = Math.sqrt(var23 * var23 + var25 * var25 + 1.0);
                     if (1.0 / var27 < var13) {
                        var23 = 0.0;
                        var25 = 0.0;
                        var27 = 1.0;
                     }

                     var3[var15] = var17;
                     var4[var15 * 3] = (float)((var7 * var23 - var9 * var25) / var27);
                     var4[var15 * 3 + 1] = (float)(1.0 / var27);
                     var4[var15 * 3 + 2] = (float)((var9 * var23 + var7 * var25) / var27);
                  }
               }
            }

            this.PH = new TimedSampleGroup(var2, var3, var4, var1.Qb);
         }
      }
   }

   static double method9114(List var0, double var1, double var3, double var5, double var7) {
      double var9 = Double.NaN;

      for (BoxBounds var12 : var0) {
         if (!(var1 < var12.IR) && !(var1 > var12.IU) && !(var5 < var12.IT) && !(var5 > var12.IW)) {
            if (var3 < var12.IV && var3 > var12.IS) {
               return var3 - var7 * 0.001;
            }

            if (var12.IV <= var3 && var12.IV >= var3 - var7 && (!Double.isFinite(var9) || var12.IV > var9)) {
               var9 = var12.IV;
            }
         }
      }

      return var9;
   }

   private static double method9115(AnimationFrameBundle var0, DoubleVector4 var1) {
      int var2 = (int)Math.floor(var1.x);
      int var3 = (int)Math.floor(var1.z);

      for (SampleRange var5 : var0.Qa) {
         if (var5.Qd == var2 && var5.Qe == var3) {
            return method9114(var5.Qh, var1.x, var1.y, var1.z, var1.w);
         }
      }

      return Double.NaN;
   }

   private TimedSample method9116(UUID var1, Location var2, float var3, long var4) {
      float var6 = this.OX.getBodyYaw();
      double var7 = Math.PI - var6;
      double var9 = Math.cos(var7);
      double var11 = Math.sin(var7);
      double var13 = this.OW.qn() * var3;
      double var15 = 0.125 * var3;
      DoubleVector4[] var17 = new DoubleVector4[this.OZ.length * 3];

      for (int var18 = 0; var18 < this.OZ.length; var18++) {
         int var19 = var18 * 4;
         if (Float.isFinite(this.Ps[var19 + 1])) {
            double var20 = var2.getX() + (var9 * this.Ps[var19] + var11 * this.Ps[var19 + 2]) * var3;
            double var22 = var2.getZ() + (-var11 * this.Ps[var19] + var9 * this.Ps[var19 + 2]) * var3;
            double var24 = var2.getY() + (this.Ps[var19 + 1] - this.Ps[var19 + 3]) * var3 + var13 + 0.001;
            var17[var18 * 3] = new DoubleVector4(var20, var24, var22, var13 * 2.0 + 0.002);
            var17[var18 * 3 + 1] = new DoubleVector4(var20 + var15, var24, var22, var13 * 2.0 + 0.002);
            var17[var18 * 3 + 2] = new DoubleVector4(var20, var24, var22 + var15, var13 * 2.0 + 0.002);
         }
      }

      return new TimedSample(var1, var2.getX(), var2.getY(), var2.getZ(), var6, var3, var4, (float[])this.Ps.clone(), var17);
   }

   boolean tW() {
      if (this.Pr != 0.0F) {
         return true;
      }

      for (boolean var4 : this.Pn) {
         if (var4) {
            return true;
         }
      }

      return false;
   }

   boolean tX() {
      return this.wq;
   }

   void method9117(Matrix4f[] var1, Matrix4f[] var2, boolean[] var3, boolean var4, boolean var5) {
      this.method9118(var1, var2, var3, var4, var5, this.wq);
   }

   void method9118(Matrix4f[] var1, Matrix4f[] var2, boolean[] var3, boolean var4, boolean var5, boolean var6) {
      boolean var7 = var6 && var5 && !this.OX.isRagdolling();
      if (!var7) {
         this.method9119(var1, var2, this.OX.getYawOffset());
         this.PG = null;
         this.PI = null;
         this.PH = null;
         this.PJ = null;
         this.PM = null;
      } else {
         Location var8 = this.OX.getBaseLocation();
         UUID var9 = this.OX.getEntityUuid();
         float var10 = this.OX.getYawOffset();
         long var11 = this.bG;
         boolean var13 = this.PL != var11
            || !Objects.equals(this.field128, var9)
            || method9126(var8.getX() - this.PQ) + method9126(var8.getY() - this.PR) + method9126(var8.getZ() - this.PS) > 4.0;
         this.PL = var11;
         this.field128 = var9;
         this.PQ = var8.getX();
         this.PR = var8.getY();
         this.PS = var8.getZ();
         this.tV();
         TimedSampleGroup var14 = this.PH;
         boolean var15 = var7 && var10 > 1.0E-4F;
         if (var13 || !var15 || var4 || this.PN || var14 != this.PM || this.method9122(this.PG, var9, var8, var10, this.OX.getBodyYaw())) {
            this.PM = var14;
            boolean var16 = var15
               && !var13
               && var14 != null
               && var14.Qi == this.PG
               && Objects.equals(var9, var14.Qi.Qq)
               && var14.Qi.Qw == var11
               && var10 == var14.Qi.Qv
               && method9127(var14.Qi, var8) <= 0.25;
            float var17 = 0.0F;

            for (int var18 = 0; var18 < this.OZ.length; var18++) {
               var1[this.Pb[var18]].getTranslation(this.Px);
               Matrix4f var19 = var1[this.Pb[var18]];
               float var20 = ((AnimationEventMarker)this.OW.qm().get(var18)).qx()
                  * (float)Math.sqrt(method9126(var19.m10()) + method9126(var19.m11()) + method9126(var19.m12()))
                  / this.Pd[var18];
               AnimationEventMarker var21 = (AnimationEventMarker)this.OW.qm().get(var18);
               var19.transformDirection(this.PF.set(var21.qy() / this.Pd[var18], 0.0F, var21.qz() / this.Pd[var18]));
               boolean var22 = this.Px.y <= this.Pc[var18] + this.OW.method80()
                  && method9125(var1[this.OZ[var18]])
                  && method9125(var1[this.Pa[var18]])
                  && method9125(var1[this.Pb[var18]]);
               this.Ps[var18 * 4] = this.Px.x + this.PF.x;
               this.Ps[var18 * 4 + 1] = var22 ? this.Px.y + this.PF.y : Float.NaN;
               this.Ps[var18 * 4 + 2] = this.Px.z + this.PF.z;
               this.Ps[var18 * 4 + 3] = var20;
               boolean var23 = var16 && var22 && Double.isFinite(var14.Qj[var18]);
               if (var23) {
                  this.PC.set(var19.m10(), var19.m11(), var19.m12()).normalize();
                  this.PD.set(var14.Qk[var18 * 3], var14.Qk[var18 * 3 + 1], var14.Qk[var18 * 3 + 2]);
                  this.Pu.rotationTo(this.PC, this.PD).transform(this.PF);
               }

               float var24 = var23 ? (float)((var14.Qj[var18] - var8.getY()) / var10) + var14.Qk[var18 * 3 + 1] * var20 - this.Px.y - this.PF.y : 0.0F;
               boolean var25 = var23 && Math.abs(var24) <= this.OW.qn();
               if (!var25) {
                  var24 = 0.0F;
               }

               this.Po[var18] = var22;
               this.Pp[var18] = var25;
               this.Pq[var18 * 3] = var25 ? var14.Qk[var18 * 3] * var20 : 0.0F;
               this.Pq[var18 * 3 + 1] = var24;
               this.Pq[var18 * 3 + 2] = var25 ? var14.Qk[var18 * 3 + 2] * var20 : 0.0F;
               if (var25 && var24 < 0.0F) {
                  var1[this.OZ[var18]].getTranslation(this.Pv);
                  var1[this.Pa[var18]].getTranslation(this.Pw);
                  this.Py.set(this.Px).add(this.Pq[var18 * 3], var24, this.Pq[var18 * 3 + 2]);
                  float var26 = this.Pv.distance(this.Pw) + this.Pw.distance(this.Px) - 1.0E-4F;
                  float var27 = (float)(method9126(this.Py.x - this.Pv.x) + method9126(this.Py.z - this.Pv.z));
                  if (var27 < var26 * var26 && this.Py.y < this.Pv.y) {
                     var17 = Math.min(var17, this.Py.y + (float)Math.sqrt(var26 * var26 - var27) - this.Pv.y);
                  }
               }
            }

            var17 = Math.max(-this.OW.qn(), var17);
            float var44 = this.Pr;
            this.Pr = !var13 && !this.OX.isRagdolling() ? this.method9121(this.Pr, var17) : 0.0F;
            boolean var45 = this.Pr != var44;
            boolean var46 = var45 || this.Pr != 0.0F && var4;
            this.PN = this.Pr != var17;
            this.PO = this.Pr == 0.0F;
            if (var46) {
               for (int var47 = 0; var47 < var1.length; var47++) {
                  this.Pj[var47].set(var1[var47]).m31(var1[var47].m31() + this.Pr);
                  this.method9120(var2, this.Pj, var47, var10);
               }
            }

            for (int var48 = 0; var48 < this.OZ.length; var48++) {
               AnimationEventMarker var49 = (AnimationEventMarker)this.OW.qm().get(var48);
               Matrix4f var50 = var1[this.Pb[var48]];
               boolean var51 = this.Po[var48];
               boolean var52 = this.Pp[var48];
               float var53 = this.Pq[var48 * 3 + 1];
               float var54 = this.Pe[var48];
               float var28 = this.Ph[var48];
               this.Ph[var48] = var13 ? 0.0F : this.method9121(this.Ph[var48], var52 ? 1.0F : 0.0F);
               float var29 = this.Pf[var48];
               float var30 = this.Pg[var48];
               float var31 = this.Pq[var48 * 3];
               float var32 = this.Pq[var48 * 3 + 2];
               this.Pf[var48] = var13 ? 0.0F : this.method9121(this.Pf[var48], var31);
               this.Pg[var48] = var13 ? 0.0F : this.method9121(this.Pg[var48], var32);
               if (!var13 && !this.OX.isRagdolling()) {
                  this.Pe[var48] = this.Pe[var48] + (var53 - this.Pe[var48]) * this.OW.oJ();
               } else {
                  this.Pe[var48] = 0.0F;
               }

               if (Math.abs(this.Pe[var48] - var53) < 1.0E-5F) {
                  this.Pe[var48] = var53;
               }

               this.PE.set(0.0F, 1.0F, 0.0F);
               if (var52) {
                  this.PE.set(var14.Qk[var48 * 3], var14.Qk[var48 * 3 + 1], var14.Qk[var48 * 3 + 2]);
               }

               float var33 = this.Pi[var48].x;
               float var34 = this.Pi[var48].y;
               float var35 = this.Pi[var48].z;
               if (var13) {
                  this.Pi[var48].set(0.0F, 1.0F, 0.0F);
               } else {
                  this.Pi[var48].lerp(this.PE, this.OW.oJ()).normalize();
               }

               if (this.Pi[var48].distanceSquared(this.PE) < 1.0E-10F) {
                  this.Pi[var48].set(this.PE);
               }

               this.PN = this.PN
                  | (
                     this.Pe[var48] != var53
                        || this.Pf[var48] != var31
                        || this.Pg[var48] != var32
                        || this.Ph[var48] != (var52 ? 1 : 0)
                        || !this.Pi[var48].equals(this.PE)
                  );
               this.PO = this.PO & (this.Pe[var48] == 0.0F && this.Pf[var48] == 0.0F && this.Pg[var48] == 0.0F && this.Ph[var48] == 0.0F);
               boolean var36 = var54 != this.Pe[var48]
                  || var29 != this.Pf[var48]
                  || var30 != this.Pg[var48]
                  || var28 != this.Ph[var48]
                  || var33 != this.Pi[var48].x
                  || var34 != this.Pi[var48].y
                  || var35 != this.Pi[var48].z
                  || var46;
               this.PC.set(var50.m10(), var50.m11(), var50.m12()).normalize();
               boolean var37 = var51
                  && (
                     this.Pe[var48] != 0.0F
                        || this.Pf[var48] != 0.0F
                        || this.Pg[var48] != 0.0F
                        || this.Ph[var48] > 0.0F && (this.Pr != 0.0F || this.PC.distanceSquared(this.Pi[var48]) > 1.0E-10F)
                  );
               boolean var38 = false;
               if (var4) {
                  for (int var42 : this.Pk[var48]) {
                     var38 |= var3[var42];
                  }
               }

               if (!var37) {
                  if (this.Pn[var48]) {
                     for (int var70 : this.Pk[var48]) {
                        this.Pj[var70].set(var1[var70]).m31(var1[var70].m31() + this.Pr);
                        this.method9120(var2, this.Pj, var70, var10);
                     }
                  }

                  this.Pn[var48] = false;
               } else if (var36 || var38 || !this.Pn[var48]) {
                  for (int var67 : this.Pk[var48]) {
                     this.Pj[var67].set(var1[var67]).m31(var1[var67].m31() + this.Pr);
                  }

                  this.Pj[this.OZ[var48]].getTranslation(this.Pv);
                  this.Pj[this.Pa[var48]].getTranslation(this.Pw);
                  this.Pj[this.Pb[var48]].getTranslation(this.Px);
                  this.Py.set(this.Px).add(this.Pf[var48], this.Pe[var48] - this.Pr * this.Ph[var48], this.Pg[var48]);
                  this.PB.set(var49.qu(), var49.qv(), var49.qw());
                  if (this.Py.distanceSquared(this.Px) > 1.0E-10F) {
                     if (!method9124(this.Pv, this.Pw, this.Px, this.Py, this.PB, this.Pz, this.PA)) {
                        for (int var69 : this.Pk[var48]) {
                           this.Pj[var69].set(var1[var69]).m31(var1[var69].m31() + this.Pr);
                           this.method9120(var2, this.Pj, var69, var10);
                        }

                        this.Pn[var48] = false;
                        continue;
                     }

                     this.PC.set(this.Pw).sub(this.Pv);
                     this.PD.set(this.Pz).sub(this.Pv);
                     this.method9123(this.Pj, this.Pk[var48], this.Pv, this.PC, this.PD);
                     this.Pj[this.Pa[var48]].getTranslation(this.Pw);
                     this.Pj[this.Pb[var48]].getTranslation(this.Px);
                     this.PC.set(this.Px).sub(this.Pw);
                     this.PD.set(this.Py).sub(this.Pw);
                     this.method9123(this.Pj, this.Pl[var48], this.Pw, this.PC, this.PD);
                  }

                  this.Pj[this.Pb[var48]].getTranslation(this.Px);
                  this.PC.set(this.Pj[this.Pb[var48]].m10(), this.Pj[this.Pb[var48]].m11(), this.Pj[this.Pb[var48]].m12());
                  this.PD.set(var50.m10(), var50.m11(), var50.m12()).normalize().lerp(this.Pi[var48], this.Ph[var48]).normalize();
                  this.method9123(this.Pj, this.Pm[var48], this.Px, this.PC, this.PD);

                  for (int var68 : this.Pk[var48]) {
                     this.method9120(var2, this.Pj, var68, var10);
                  }

                  this.Pn[var48] = true;
                  this.PU++;
               }
            }

            if (!var15) {
               this.PG = null;
            } else if (this.method9122(this.PG, var9, var8, var10, this.OX.getBodyYaw())) {
               this.PG = this.method9116(var9, var8, var10, var11);
               this.PW++;
            }
         }
      }
   }

   private void method9119(Matrix4f[] var1, Matrix4f[] var2, float var3) {
      if (!this.PO) {
         if (this.Pr != 0.0F) {
            for (int var4 = 0; var4 < var1.length; var4++) {
               this.method9120(var2, var1, var4, var3);
            }
         } else {
            for (int var9 = 0; var9 < this.Pn.length; var9++) {
               if (this.Pn[var9]) {
                  for (int var8 : this.Pk[var9]) {
                     this.method9120(var2, var1, var8, var3);
                  }
               }
            }
         }

         Arrays.fill(this.Pe, 0.0F);
         Arrays.fill(this.Pf, 0.0F);
         Arrays.fill(this.Pg, 0.0F);
         Arrays.fill(this.Ph, 0.0F);
         Arrays.fill(this.Pn, false);

         for (Vector3f var13 : this.Pi) {
            var13.set(0.0F, 1.0F, 0.0F);
         }

         this.Pr = 0.0F;
         this.PN = false;
         this.PO = true;
      }
   }

   private void method9120(Matrix4f[] var1, Matrix4f[] var2, int var3, float var4) {
      var1[var3].scaling(-var4, var4, -var4).mul(var2[var3]).scale(((CustomEntityDefinition)this.OY.get(var3)).getScaleFactor());
      this.PV++;
   }

   private float method9121(float var1, float var2) {
      float var3 = var1 + (var2 - var1) * this.OW.oJ();
      return Math.abs(var3 - var2) < 1.0E-5F ? var2 : var3;
   }

   private boolean method9122(TimedSample var1, UUID var2, Location var3, float var4, float var5) {
      if (var1 != null
         && var1.Qw == this.bG
         && Objects.equals(var2, var1.Qq)
         && !(method9127(var1, var3) > 1.0E-4)
         && !(Math.abs(var4 - var1.Qv) > 1.0E-4F)
         && !(Math.abs(var5 - var1.Qu) > 0.0087F)) {
         for (int var6 = 0; var6 < this.Ps.length; var6++) {
            if (Float.isNaN(this.Ps[var6]) != Float.isNaN(var1.Qx[var6]) || Math.abs(this.Ps[var6] - var1.Qx[var6]) > 0.0025F) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void method9123(Matrix4f[] var1, int[] var2, Vector3f var3, Vector3f var4, Vector3f var5) {
      if (!(var4.lengthSquared() < 1.0E-10F) && !(var5.lengthSquared() < 1.0E-10F)) {
         this.Pu.rotationTo(var4, var5);
         this.Pt.identity().translate(var3).rotate(this.Pu).translate(-var3.x, -var3.y, -var3.z);

         for (int var9 : var2) {
            var1[var9].mulLocal(this.Pt);
         }
      }
   }

   static boolean method9124(Vector3f var0, Vector3f var1, Vector3f var2, Vector3f var3, Vector3f var4, Vector3f var5, Vector3f var6) {
      float var7 = var0.distance(var1);
      float var8 = var1.distance(var2);
      var6.set(var3).sub(var0);
      float var9 = var6.length();
      if (!(var7 < 1.0E-4F) && !(var8 < 1.0E-4F) && !(var9 < 1.0E-4F)) {
         var6.div(var9);
         var9 = Math.max(Math.abs(var7 - var8) + 1.0E-4F, Math.min(var7 + var8 - 1.0E-4F, var9));
         var3.set(var6).mul(var9).add(var0);
         var4.fma(-var4.dot(var6), var6);
         if (var4.lengthSquared() < 1.0E-8F) {
            return false;
         }

         var4.normalize();
         float var10 = (var7 * var7 - var8 * var8 + var9 * var9) / (2.0F * var9);
         float var11 = (float)Math.sqrt(Math.max(0.0F, var7 * var7 - var10 * var10));
         var5.set(var0).fma(var10, var6).fma(var11, var4);
         return var5.isFinite();
      } else {
         return false;
      }
   }

   private static boolean method9125(Matrix4f var0) {
      float var1 = var0.m00() * var0.m00() + var0.m01() * var0.m01() + var0.m02() * var0.m02();
      float var2 = var0.m10() * var0.m10() + var0.m11() * var0.m11() + var0.m12() * var0.m12();
      float var3 = var0.m20() * var0.m20() + var0.m21() * var0.m21() + var0.m22() * var0.m22();
      return var1 > 1.0E-8F
         && Math.abs(var1 - var2) < var1 * 0.001F
         && Math.abs(var1 - var3) < var1 * 0.001F
         && var0.determinant3x3() > 0.0F
         && var0.isFinite()
         && Math.abs(var0.m00() * var0.m10() + var0.m01() * var0.m11() + var0.m02() * var0.m12()) < var1 * 0.001F
         && Math.abs(var0.m00() * var0.m20() + var0.m01() * var0.m21() + var0.m02() * var0.m22()) < var1 * 0.001F
         && Math.abs(var0.m10() * var0.m20() + var0.m11() * var0.m21() + var0.m12() * var0.m22()) < var1 * 0.001F;
   }

   private static double method9126(double var0) {
      return var0 * var0;
   }

   private static double method9127(TimedSample var0, Location var1) {
      return method9126(var0.Qr - var1.getX()) + method9126(var0.Qs - var1.getY()) + method9126(var0.Qt - var1.getZ());
   }
}
