package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.entity.AnimationSetup;
import dev.lone.itemsadder.entity.CustomEntityDefinition;
import dev.lone.itemsadder.entity.EntityPlacementSpec;
import dev.lone.itemsadder.entity.PoseFrame;
import dev.lone.itemsadder.entity.PoseSample;
import dev.lone.itemsadder.entity.PoseSampleList;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.entity.TransformBuilder;
import dev.lone.itemsadder.entity.TransformDecomposition;
import dev.lone.itemsadder.util.FloatCurveSampler;
import dev.lone.itemsadder.util.IntArrayHolder;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class SkeletonPose {
   private final EntityPlacementSpec[] Nt;
   private final int Nu;
   private final IntArrayHolder[] Nv;
   @Nullable
   private final IntPair Nw;
   @Nullable
   private final IndexedKeyframe Nx;
   private final int[] Ny;
   private final float[][] Nz;
   private final float[] NA;
   private final float[] NB;
   private final float[] NC;
   private final FloatCurveSampler ND;
   private final CustomEntityDefinition[] NE;
   private final int[] NF;
   private final TransformDecomposition[] NG;
   private final Map NH = new HashMap();
   private final Matrix4f[] NI;
   private final Matrix4f[] NJ;
   private final Matrix4f[] NK;
   private final Matrix4f[] NL;
   private final Matrix4f[] NM;
   private final Matrix4f[] field125;
   private final Matrix4f[] NO;
   private final Matrix4f[] NP;
   private final Matrix4f[] NQ;
   private final Matrix4f[] NR;
   private final boolean[] NS;
   private final boolean[] NT;
   private final Quaternionf[] NU;
   private final Matrix4f NV = new Matrix4f();
   private final Matrix4f NW = new Matrix4f();
   private final Matrix4f NX = new Matrix4f();
   private final Quaternionf NY = new Quaternionf();
   private final Quaternionf NZ = new Quaternionf();
   private final Quaternionf Oa = new Quaternionf();
   private final Quaternionf Ob = new Quaternionf();
   private final Vector3f Oc = new Vector3f();
   private final Vector3f Od = new Vector3f();
   private final Vector3f Oe = new Vector3f();
   private final Vector3f Of = new Vector3f();
   private final float[] Og = new float[16];
   private final float[] Oh = new float[16];
   private float Oi;
   private float Oj;
   private boolean Ok;
   private boolean field126;
   private final boolean Om;
   private float On = Float.NaN;
   private float field127;
   private float Op;
   private float Oq;
   private float Or;
   private long mL;

   SkeletonPose(AnimationSetup var1, List var2) {
      this.ND = new FloatCurveSampler(var1.oV() + 1);
      this.Om = "native".equals(var1.oW());
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      method9098(var3, var1.oX());
      this.Nu = var1.oX() == null ? -1 : 0;
      method9098(var3, var1.oY());
      method9099(var3, var4, var1.pa());
      method9099(var3, var4, var1.pb());
      method9098(var3, var1.oZ());
      this.Nt = var3.toArray(EntityPlacementSpec[]::new);
      this.Nv = var4.toArray(IntArrayHolder[]::new);
      this.Nz = new float[this.Nt.length][3];
      this.NA = new float[this.Nt.length * 6];
      this.NB = new float[this.NA.length];
      this.NC = new float[this.NA.length];

      for (int var5 = 0; var5 < this.Nt.length; var5++) {
         this.NA[var5 * 6] = this.Nt[var5].OC;
         this.NA[var5 * 6 + 1] = this.Nt[var5].OD;
         this.NA[var5 * 6 + 2] = this.Nt[var5].OF;
         this.NA[var5 * 6 + 3] = this.Nt[var5].OG;
         this.NA[var5 * 6 + 4] = this.Nt[var5].field14;
         this.NA[var5 * 6 + 5] = this.Nt[var5].OQ;
      }

      this.Ny = new int[this.Nt.length];
      Arrays.fill(this.Ny, -1);
      HashMap var13 = new HashMap();

      for (int var6 = 0; var6 < this.Nt.length; var6++) {
         var13.put(this.Nt[var6].OB, var6);
      }

      for (int var14 = 0; var14 < this.Nt.length; var14++) {
         for (CustomEntityDefinition var7 = this.Nt[var14].OB.getParent(); var7 != null; var7 = var7.getParent()) {
            Integer var8 = (Integer)var13.get(var7);
            if (var8 != null) {
               this.Ny[var14] = var8;
               break;
            }
         }
      }

      this.NE = var2.toArray(CustomEntityDefinition[]::new);
      this.NF = new int[this.NE.length];
      this.NG = new TransformDecomposition[this.NE.length];
      this.NI = ae(this.NE.length);
      this.NJ = ae(this.NE.length);
      this.NK = ae(this.NE.length);
      this.NL = ae(this.NE.length);
      this.NM = ae(this.NE.length);
      this.field125 = ae(this.NE.length);
      this.NO = ae(this.NE.length);
      this.NP = ae(this.NE.length);
      this.NQ = ae(this.NE.length);
      this.NR = ae(this.NE.length);
      this.NS = new boolean[this.NE.length];
      this.NT = new boolean[this.NE.length];
      this.NU = new Quaternionf[this.NE.length];
      Arrays.setAll(this.NU, var0 -> new Quaternionf());

      for (int var15 = 0; var15 < this.NE.length; var15++) {
         this.NH.put(this.NE[var15], var15);
      }

      for (int var16 = 0; var16 < this.NE.length; var16++) {
         CustomEntityDefinition var18 = this.NE[var16];
         CustomEntityDefinition var20 = var18.getParent();
         this.NF[var16] = var20 == null ? -1 : (Integer)this.NH.get(var20);
         Matrix4fc var9 = var18.getTransform();
         if (var9 == null) {
            throw new IllegalArgumentException("Procedural Motion requires local V2 transforms for bone '" + var18.getId() + "'");
         }

         this.NI[var16].set(var9);
         this.NG[var16] = TransformDecomposition.decompose(var9);
         if (this.NF[var16] >= 0) {
            this.NI[var16].set(this.NI[this.NF[var16]]).mul(var9);
         }
      }

      CustomEntityDefinition var17 = var1.oZ() == null ? null : var1.oZ().definition();
      List var19 = var1.pa() == null ? List.of() : var1.pa().samples();
      CustomEntityDefinition var21 = var19.isEmpty() ? null : (CustomEntityDefinition)var19.get(var19.size() - 1);
      this.Nw = var17 != null && var21 != null ? new IntPair((Integer)this.NH.get(var21), (Integer)this.NH.get(var17)) : null;

      for (EntityPlacementSpec var12 : this.Nt) {
         if (var12.OS) {
            this.NT[this.NH.get(var12.OB)] = true;
         }
      }

      PoseFrame var23 = var1.pc();
      if (var23 == null) {
         this.Nx = null;
      } else {
         int[] var24 = var23.samples().stream().mapToInt(this.NH::get).toArray();
         this.Nx = new IndexedKeyframe(
            (Integer)this.NH.get(var23.definition()), var24, (Integer)var13.get(var1.oY().definition()), var23.offsetX(), var23.offsetY(), var23.offsetZ()
         );
      }

      this.tG();
   }

   private void tG() {
      Matrix4f var1 = new Matrix4f();
      Vector3f var2 = new Vector3f();
      Vector3f var3 = new Vector3f();
      Vector3f var4 = new Vector3f();

      for (IntArrayHolder var8 : this.Nv) {
         for (int var9 = 0; var9 < var8.data.length; var9++) {
            int var10 = (Integer)this.NH.get(this.Nt[var8.data[var9]].OB);
            int var11;
            if (var9 + 1 < var8.data.length) {
               var11 = (Integer)this.NH.get(this.Nt[var8.data[var9 + 1]].OB);
            } else if (this.Nw != null && this.Nw.first == var10) {
               var11 = this.Nw.second;
            } else {
               if (var9 <= 0) {
                  continue;
               }

               var11 = (Integer)this.NH.get(this.Nt[var8.data[var9 - 1]].OB);
            }

            this.NI[var10].getTranslation(this.Oe);
            this.NI[var11].getTranslation(this.Of);
            var3.set(this.Of).sub(this.Oe);
            if (var9 + 1 >= var8.data.length && (this.Nw == null || this.Nw.first != var10)) {
               var3.negate();
            }

            int var12 = this.NF[var10];
            if (var12 >= 0) {
               var1.set(this.NI[var12]).invert().transformDirection(var3);
            }

            this.NI[var10].transformDirection(var2.set(0.0F, 0.0F, -1.0F));
            this.NI[var10].transformDirection(var4.set(0.0F, 1.0F, 0.0F));
            if (var12 >= 0) {
               var1.transformDirection(var2);
               var1.transformDirection(var4);
            }

            var2.normalize();
            var3.normalize();
            var4.normalize();
            if (var2.dot(var3) < -0.9999F) {
               this.NU[var10].rotationAxis((float) Math.PI, var4.x, var4.y, var4.z);
            } else {
               this.NU[var10].rotationTo(var2, var3);
            }
         }
      }
   }

   private static Matrix4f[] ae(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];
      Arrays.setAll(var1, var0x -> new Matrix4f());
      return var1;
   }

   private static void method9098(List var0, PoseSample var1) {
      if (var1 != null) {
         var0.add(
            new EntityPlacementSpec(
               var1.definition(),
               var1.oD(),
               var1.oE(),
               var1.oF(),
               var1.oK(),
               var1.oL(),
               var1.oG(),
               var1.oH(),
               var1.method53(),
               var1.oJ(),
               0.0F,
               0.0F,
               0.0F,
               var1.oM(),
               var1.oN(),
               var1.method54(),
               false
            )
         );
      }
   }

   private static void method9099(List var0, List var1, PoseSampleList var2) {
      if (var2 != null && !var2.samples().isEmpty()) {
         int var3 = var2.samples().size();
         int[] var4 = new int[var3];

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = var3 == 1 ? 0.5F : (float)var5 / (var3 - 1);
            var4[var5] = var0.size();
            var0.add(
               new EntityPlacementSpec(
                  (CustomEntityDefinition)var2.samples().get(var5),
                  method9107(var2.oQ(), var2.oR(), var6),
                  var2.oE(),
                  var2.oF(),
                  method9107(var2.oQ(), var2.oR(), var6),
                  var2.oE(),
                  var2.oG(),
                  var2.oH(),
                  var2.method53(),
                  var2.oJ(),
                  var2.oS(),
                  var2.oT(),
                  var6,
                  var2.oM(),
                  var2.oN(),
                  var2.method54(),
                  true
               )
            );
         }

         var1.add(new IntArrayHolder(var4));
      }
   }

   void method9100(float var1, float var2, long var3, boolean var5) {
      this.method9101(var1, var2, var3, var5, null);
   }

   void method9101(float var1, float var2, long var3, boolean var5, @Nullable Entity var6) {
      this.On = this.Om && var6 != null ? ProjectileTypeHelper.getAdapter().ac(var6) : Float.NaN;
      boolean var7 = var6 != null && ProjectileTypeHelper.getAdapter().method3238(var6, this.NA, this.NB, this.NC);
      if (var7) {
         this.field126 = true;
         if (this.Nu >= 0 && this.NA[this.Nu * 6] == 0.0F) {
            this.NB[this.Nu * 6] = method9106(var1);
         }

         if (!this.Ok || var5) {
            for (float[] var15 : this.Nz) {
               Arrays.fill(var15, 0.0F);
            }
         }

         this.Ok = true;
      } else {
         if (this.field126) {
            this.Ok = false;
         }

         this.field126 = false;
         if (this.Ok && !var5) {
            this.Oq = method9106(var1 - this.field127) / (float)Math.max(1L, var3);
            this.Or = (var2 - this.Op) / (float)Math.max(1L, var3);
         } else {
            this.Ok = true;
            this.field127 = method9106(var1);
            this.Op = var2;
            this.Oq = this.Or = 0.0F;
            this.ND.reset(this.field127, this.Op);

            for (float[] var11 : this.Nz) {
               Arrays.fill(var11, 0.0F);
            }
         }
      }
   }

   boolean aq(boolean var1) {
      this.field127 = method9106(this.field127 + this.Oq);
      this.Op = this.Op + this.Or;
      this.ND.push(this.field127, this.Op);
      this.mL++;
      boolean var2 = false;

      for (int var3 = 0; var3 < this.Nt.length; var3++) {
         EntityPlacementSpec var4 = this.Nt[var3];
         float var5 = var1
            ? (this.field126 ? method9106(this.NB[var3 * 6] - this.NB[var3 * 6 + 1]) : method9106(this.ND.sampleAngle(var4.OC) - this.ND.sampleAngle(var4.OD)))
            : 0.0F;
         float var6 = var1
            ? (this.field126 ? this.NC[var3 * 6 + 2] - this.NC[var3 * 6 + 3] : this.ND.sampleFloat(var4.OF) - this.ND.sampleFloat(var4.OG))
            : 0.0F;
         float var7 = this.field126
            ? method9106(this.NB[var3 * 6 + 4] + method9106(this.NB[var3 * 6 + 5] - this.NB[var3 * 6 + 4]) * var4.OR)
            : method9106(this.ND.sampleAngle(var4.field14) + method9106(this.ND.sampleAngle(var4.OQ) - this.ND.sampleAngle(var4.field14)) * var4.OR);
         float var8 = var1 ? method9106((this.field126 ? this.NB[var3 * 6] : this.ND.sampleAngle(var4.OC)) - var7) : 0.0F;
         float var9 = Float.isFinite(this.On) ? this.On * (float) (Math.PI * 2) : (float)this.mL * var4.OM;
         float var10 = var1 && var4.OL != 0.0F ? var4.OL * (float)Math.sin(var9 - var4.ON * Math.PI * 2.0) : 0.0F;
         var2 |= method9105(this.Nz[var3], 0, (var6 * var4.OH + var10) * var4.OJ, var4.OK);
         var2 |= method9105(this.Nz[var3], 1, var5 * var4.OE * var4.OJ, var4.OK);
         var2 |= method9105(this.Nz[var3], 2, var8 * var4.field13 * var4.OJ, var4.OK);
      }

      return var2;
   }

   void method9102(TransformBuilder[] var1) {
      float var2 = (float) (Math.PI / 180.0);

      for (int var3 = 0; var3 < this.NE.length; var3++) {
         var1[this.NE[var3].nH()].method8687(this.NJ[var3]);
      }

      for (int var14 = 0; var14 < this.Nt.length; var14++) {
         EntityPlacementSpec var4 = this.Nt[var14];
         if (var4.OS) {
            int var5 = (Integer)this.NH.get(var4.OB);
            float[] var6 = this.Nz[var14];
            TransformBuilder var7 = var1[var4.OB.nH()];
            float var8 = this.method9103(this.NU[var5], -var6[1] * var2);
            var7.method8678(this.NO[var5], this.NG[var5], this.NU[var5], -var6[0] * var2, var8, 0.0F);
            var7.method8679(this.NG[var5], this.NU[var5], -var6[0] * var2, var8, 0.0F);
            var7.method8680(var6[2] * var2);
         }
      }

      for (int var15 = 0; var15 < this.Nt.length; var15++) {
         if (!this.Nt[var15].OS && var15 != this.Nu) {
            int var22 = this.Ny[var15];
            float[] var28 = var22 >= 0 && this.Nt[var22].OS ? this.Nz[var22] : null;
            float[] var32 = this.Nz[var15];
            var1[this.Nt[var15].OB.nH()]
               .method8675(
                  (var32[0] - (var28 == null ? 0.0F : var28[0])) * var2,
                  -(var32[1] - (var28 == null ? 0.0F : var28[1])) * var2,
                  (this.Nx != null && var15 == this.Nx.Ow ? 0.0F : var32[2] - (var28 == null ? 0.0F : var28[2])) * var2
               );
         }
      }

      if (this.Nu >= 0) {
         TransformBuilder var16 = var1[this.Nt[this.Nu].OB.nH()];
         var16.method8687(this.NX).getTranslation(this.Oc);
         float[] var23 = this.Nz[this.Nu];
         this.NY.rotationZYX(-var23[2] * var2, var23[1] * var2, -var23[0] * var2);
         this.NY.transform(this.Oc);
         var16.method8682(this.Oc, this.NY);
      }

      for (int var17 = 0; var17 < this.NE.length; var17++) {
         CustomEntityDefinition var24 = this.NE[var17];
         var1[var24.nH()].method8687(this.NK[var17]);
         if (!this.NT[var17]) {
            this.NO[var17].set(this.NK[var17]);
         }

         int var29 = this.NF[var17];
         if (var29 < 0) {
            this.NL[var17].set(this.NK[var17]);
         } else {
            this.NL[var17].set(this.NL[var29]).mul(this.NK[var17]);
         }

         Matrix4f var33 = this.NT[var17] ? this.NJ[var17] : this.NK[var17];
         if (var29 < 0) {
            this.NM[var17].set(var33);
            this.field125[var17].set(this.NK[var17]);
            this.NP[var17].set(this.NO[var17]);
         } else {
            this.NM[var17].set(this.NM[var29]).mul(var33);
            this.field125[var17].set(this.NM[var29]).mul(this.NK[var17]);
            this.NP[var17].set(this.NM[var29]).mul(this.NO[var17]);
         }
      }

      Arrays.fill(this.NS, false);

      for (IntArrayHolder var34 : this.Nv) {
         int var36 = (Integer)this.NH.get(this.Nt[var34.data[0]].OB);
         this.field125[var36].getTranslation(this.Oc);

         for (int var38 = 0; var38 < var34.data.length; var38++) {
            int var9 = var34.data[var38];
            EntityPlacementSpec var10 = this.Nt[var9];
            int var11 = (Integer)this.NH.get(var10.OB);
            float[] var12 = this.Nz[var9];
            this.NR[var11].set(this.field125[var11]).setTranslation(this.Oc);
            this.NS[var11] = true;
            if (var38 + 1 < var34.data.length) {
               int var13 = (Integer)this.NH.get(this.Nt[var34.data[var38 + 1]].OB);
               this.NI[var13].getTranslation(this.Of);
               this.NI[var11].getTranslation(this.Oe);
               this.Od.set(this.Of).sub(this.Oe);
               this.NX.set(this.NP[var11]).setTranslation(this.Oc).mul(this.NV.set(this.NI[var11]).invert()).transformDirection(this.Od);
               this.Oc.add(this.Od);
            }
         }
      }

      if (this.Nw != null) {
         this.NR[this.Nw.first].getTranslation(this.Oc);
         this.NI[this.Nw.second].getTranslation(this.Of);
         this.NI[this.Nw.first].getTranslation(this.Oe);
         this.Od.set(this.Of).sub(this.Oe);
         this.NX.set(this.NP[this.Nw.first]).setTranslation(this.Oc).mul(this.NV.set(this.NI[this.Nw.first]).invert()).transformDirection(this.Od);
         this.Oc.add(this.Od);
         this.NR[this.Nw.second].set(this.NL[this.Nw.second]).setTranslation(this.Oc);
         this.NL[this.Nw.second].getTranslation(this.Oe);
         this.NR[this.Nw.second].getTranslation(this.Of);
         this.Oi = this.Oe.distance(this.Of);
         this.NS[this.Nw.second] = true;
      }

      if (this.Nx != null) {
         Matrix4f var19 = this.NL[this.Nx.Ou];
         float var26 = this.Nz[this.Nx.Ow][2] * var2;
         this.NX
            .set(var19)
            .translate(this.Nx.Ox, this.Nx.Oy, this.Nx.Oz)
            .rotateZ(var26)
            .translate(-this.Nx.Ox, -this.Nx.Oy, -this.Nx.Oz)
            .mul(this.NV.set(var19).invert());

         for (int var39 : this.Nx.Ov) {
            this.NR[var39].set(this.NX).mul(this.NL[var39]);
            this.NS[var39] = true;
         }
      }

      for (int var20 = 0; var20 < this.NE.length; var20++) {
         int var27 = this.NF[var20];
         if (this.NS[var20]) {
            this.NW.set(this.NR[var20]);
            if (var27 >= 0) {
               this.NW.set(this.NV.set(this.NQ[var27]).invert()).mul(this.NR[var20]);
            }

            this.NX.set(this.NW).mul(this.NV.set(this.NK[var20]).invert()).getNormalizedRotation(this.NZ);
            this.NW.getTranslation(this.Oc);
            var1[this.NE[var20].nH()].method8682(this.Oc, this.NZ);
            var1[this.NE[var20].nH()].method8687(this.NK[var20]);
         }

         if (var27 < 0) {
            this.NQ[var20].set(this.NK[var20]);
         } else {
            this.NQ[var20].set(this.NQ[var27]).mul(this.NK[var20]);
         }
      }

      if (this.Nw != null) {
         this.NR[this.Nw.second].get(this.Og);
         this.NQ[this.Nw.second].get(this.Oh);
         this.Oj = 0.0F;

         for (int var21 = 0; var21 < this.Og.length; var21++) {
            this.Oj = Math.max(this.Oj, Math.abs(this.Og[var21] - this.Oh[var21]));
         }
      }
   }

   private float method9103(Quaternionf var1, float var2) {
      if (!(Math.abs(var1.x) > 1.0E-6F) && !(Math.abs(var1.z) > 1.0E-6F) && !(Math.abs(var1.y) < 1.0E-6F)) {
         float var3 = 2.0F * (float)Math.atan2(var1.y, var1.w);
         this.Oa.rotationZYX(0.0F, var3, 0.0F);
         this.Ob.rotationZYX(0.0F, var3 + var2, 0.0F).mul(this.Oa.conjugate());
         return 2.0F * (float)Math.atan2(this.Ob.y, this.Ob.w);
      } else {
         return var2;
      }
   }

   float[] ai(int var1) {
      return (float[])this.Nz[var1].clone();
   }

   float aj(int var1) {
      return this.Nt[var1].OC;
   }

   float ak(int var1) {
      return this.Nt[var1].OD;
   }

   float al(int var1) {
      return this.field126 ? this.NB[var1 * 4] : this.ND.sampleAngle(this.Nt[var1].OC);
   }

   float am(int var1) {
      return this.field126 ? this.NB[var1 * 4 + 1] : this.ND.sampleAngle(this.Nt[var1].OD);
   }

   float an(int var1) {
      return this.field126 ? this.NC[var1 * 4 + 2] : this.ND.sampleFloat(this.Nt[var1].OF);
   }

   float ao(int var1) {
      return this.field126 ? this.NC[var1 * 4 + 3] : this.ND.sampleFloat(this.Nt[var1].OG);
   }

   int size() {
      return this.Nt.length;
   }

   boolean tH() {
      return this.field126;
   }

   double method9104(double var1) {
      return Float.isFinite(this.On) ? this.On : var1;
   }

   float tI() {
      return this.Oi;
   }

   float tJ() {
      return this.Oj;
   }

   private static boolean method9105(float[] var0, int var1, float var2, float var3) {
      float var4 = var0[var1] + method9106(var2 - var0[var1]) * var3;
      if (Math.abs(var4 - var2) < 1.0E-5F) {
         var4 = var2;
      }

      boolean var5 = var4 != var0[var1];
      var0[var1] = var4;
      return var5;
   }

   static float method9106(float var0) {
      var0 %= 360.0F;
      if (var0 >= 180.0F) {
         var0 -= 360.0F;
      }

      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }

   static float method9107(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }
}
