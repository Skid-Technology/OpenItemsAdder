package dev.lone.itemsadder.physics;

import com.github.stephengold.joltjni.Body;
import com.github.stephengold.joltjni.BodyCreationSettings;
import com.github.stephengold.joltjni.BodyInterface;
import com.github.stephengold.joltjni.BoxShape;
import com.github.stephengold.joltjni.BroadPhaseLayerInterfaceTable;
import com.github.stephengold.joltjni.CapsuleShape;
import com.github.stephengold.joltjni.CollisionGroup;
import com.github.stephengold.joltjni.FixedConstraintSettings;
import com.github.stephengold.joltjni.GroupFilterTable;
import com.github.stephengold.joltjni.HingeConstraintSettings;
import com.github.stephengold.joltjni.JobSystemSingleThreaded;
import com.github.stephengold.joltjni.ObjectLayerPairFilterTable;
import com.github.stephengold.joltjni.ObjectVsBroadPhaseLayerFilterTable;
import com.github.stephengold.joltjni.PhysicsSettings;
import com.github.stephengold.joltjni.PhysicsSystem;
import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.RotatedTranslatedShape;
import com.github.stephengold.joltjni.Shape;
import com.github.stephengold.joltjni.ShapeRefC;
import com.github.stephengold.joltjni.ShapeResult;
import com.github.stephengold.joltjni.SphereShape;
import com.github.stephengold.joltjni.StaticCompoundShapeSettings;
import com.github.stephengold.joltjni.SwingTwistConstraintSettings;
import com.github.stephengold.joltjni.TempAllocatorMalloc;
import com.github.stephengold.joltjni.TwoBodyConstraint;
import com.github.stephengold.joltjni.Vec3;
import com.github.stephengold.joltjni.enumerate.EActivation;
import com.github.stephengold.joltjni.enumerate.EConstraintSpace;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.enumerate.EOverrideMassProperties;
import com.github.stephengold.joltjni.readonly.ConstAaBox;
import com.github.stephengold.joltjni.readonly.ConstShape;
import dev.lone.itemsadder.anim.AnimationClip;
import dev.lone.itemsadder.anim.BoneAnimation;
import dev.lone.itemsadder.anim.BoneBinding;
import dev.lone.itemsadder.anim.BoneKeyframe;
import dev.lone.itemsadder.anim.KeyframeEntry;
import dev.lone.itemsadder.entity.ModelPhysicsLoop;
import dev.lone.itemsadder.util.DoubleQuaternion;
import dev.lone.itemsadder.util.DoubleVector3;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class RagdollSimulation implements AutoCloseable {
   private static final float FL = 0.05F;
   private static final int FM = 6;
   private final BoneBindPose[] FN;
   private final BoneRigidBody[] FO;
   private final BoneConstraintLink[] FP;
   private final AnimationClip FQ;
   private final Map FR = new HashMap();
   private final float[] FS;
   private boolean FT;
   private final PhysicsSystem FU;
   private final BodyInterface FV;
   private final TempAllocatorMalloc FW;
   private final JobSystemSingleThreaded FX;
   private final ObjectLayerPairFilterTable FY;
   private final BroadPhaseLayerInterfaceTable FZ;
   private final ObjectVsBroadPhaseLayerFilterTable Ga;
   private final GroupFilterTable Gb;
   private final BitSet Gc = new BitSet();
   private final Vector3f Gd = new Vector3f();
   private final Quaternionf Ge = new Quaternionf();
   private final double Gf;
   private final double Gg;
   private final double Gh;
   private final int Gi;
   private Body Gj;
   private VoxelCollisionShape Gk;
   private float Gl;
   private float Gm;
   private float Gn;
   private float Go;
   private float Gp;
   private float Gq;
   private float Gr;
   private float Gs;
   private final float Gt;
   private final Vector3f Gu = new Vector3f();
   private float Gv;
   private float Gw;
   private float Gx;
   private float Gy;
   private int Gz;
   private int GA;
   private int GB;
   private int GC;
   private volatile boolean GD;
   private boolean GE = true;
   private boolean cz;
   private boolean GF;
   private boolean field32;
   private final double GH;
   private final double GI;
   private final double GJ;

   RagdollSimulation(
      AnimationClip var1,
      String[] var2,
      Matrix4f[] var3,
      float[][] var4,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      float var17
   ) {
      this(var1, var2, var3, var4, var5, var7, var9, var11, var13, var15, var17, 0.0, 0.0);
   }

   RagdollSimulation(
      AnimationClip var1,
      String[] var2,
      Matrix4f[] var3,
      float[][] var4,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      float var17,
      double var18,
      double var20
   ) {
      this(var1, var2, var3, var4, var5, var7, var9, var11, var13, var15, var17, var18, var20, null);
   }

   RagdollSimulation(
      AnimationClip var1,
      String[] var2,
      Matrix4f[] var3,
      float[][] var4,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      float var17,
      double var18,
      double var20,
      float[] var22
   ) {
      this(var1, var2, var3, var4, var5, var7, var9, var11, var13, var15, var17, var18, var20, var22, false);
   }

   RagdollSimulation(
      AnimationClip var1,
      String[] var2,
      Matrix4f[] var3,
      float[][] var4,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      float var17,
      double var18,
      double var20,
      float[] var22,
      boolean var23
   ) {
      this.FQ = var1;
      this.FS = var22;
      this.FT = var23;
      this.Gf = var5;
      this.Gg = var7;
      this.Gh = var9;
      if (var2.length == var3.length && var4.length == var3.length && (var22 == null || var22.length == var3.length)) {
         HashMap var24 = new HashMap();
         this.FN = new BoneBindPose[var3.length];
         Matrix4f[] var25 = new Matrix4f[var3.length];

         for (int var26 = 0; var26 < this.FN.length; var26++) {
            if (var24.put(var2[var26], var26) != null) {
               throw new IllegalArgumentException("duplicate visual bone " + var2[var26]);
            }

            this.FN[var26] = new BoneBindPose(new Matrix4f(var3[var26]), var4[var26]);
            var25[var26] = new Matrix4f(var3[var26]);
            if (var22 != null) {
               float var54 = var22[var26];
               if (!Float.isFinite(var54) || var54 <= 0.0F || !Float.isFinite(1.0F / var54)) {
                  throw new IllegalArgumentException("invalid ragdoll geometry scale");
               }

               var25[var26].scale(1.0F / var54);
            }
         }

         HashMap var52 = new HashMap();

         for (KeyframeEntry var28 : var1.pK()) {
            if (!var24.containsKey(var28.bone())) {
               throw new IllegalArgumentException("ragdoll body bone missing: " + var28.bone());
            }

            var52.put(var28.id(), var28);
         }

         HashMap var53 = new HashMap();
         var53.put(var1.pJ(), new Vector3f());

         for (KeyframeEntry var29 : var1.pK()) {
            if (var29.pS()) {
               var53.put(var29.id(), new Vector3f());
            }
         }

         int var56 = var23 ? 0 : var1.pL().size();

         while (var56 > 0) {
            int var57 = var56;

            for (BoneAnimation var31 : var1.pL()) {
               if (!var53.containsKey(var31.qc()) && var53.containsKey(var31.qb())) {
                  KeyframeEntry var32 = (KeyframeEntry)var52.get(var31.qb());
                  KeyframeEntry var33 = (KeyframeEntry)var52.get(var31.qc());
                  if (var32 != null && var33 != null) {
                     int var34 = (Integer)var24.get(var32.bone());
                     int var35 = (Integer)var24.get(var33.bone());
                     DoubleVector3 var36 = var31.qd();
                     DoubleVector3 var37 = var31.qe();
                     Vector3f var38 = var25[var34]
                        .transformPosition((float)var36.getX(), (float)var36.getY(), (float)var36.getZ(), new Vector3f())
                        .sub(var25[var35].transformPosition((float)var37.getX(), (float)var37.getY(), (float)var37.getZ(), new Vector3f()));
                     if (var38.isFinite() && !(var38.length() > 2.0F)) {
                        method1782(var25[var35], var38);
                        method1782(this.FN[var35].Hb, var38);
                        var53.put(var31.qc(), var38);
                        var56--;
                        continue;
                     }

                     throw new IllegalArgumentException("ragdoll joint " + var31.qc() + " has an invalid death pose");
                  }

                  throw new IllegalArgumentException("ragdoll joint references a missing body");
               }
            }

            if (var56 == var57) {
               throw new IllegalArgumentException("ragdoll joints are disconnected");
            }
         }

         if (!var23) {
            for (BoneBinding var60 : var1.pM()) {
               method1782(this.FN[var24.get(var60.bone())].Hb, (Vector3f)var53.get(var60.qa()));
            }
         }

         this.FY = new ObjectLayerPairFilterTable(2).enableCollision(0, 0).enableCollision(0, 1);
         this.FZ = new BroadPhaseLayerInterfaceTable(2, 2).mapObjectToBroadPhaseLayer(0, 0).mapObjectToBroadPhaseLayer(1, 1);
         this.Ga = new ObjectVsBroadPhaseLayerFilterTable(this.FZ, 2, this.FY, 2);
         int var59 = var1.pK().size();
         int var61 = Math.max(128, var59 * (var59 + 1) / 2 + 32);
         this.FU = new PhysicsSystem().init(Math.max(32, var59 + 8), 0, var61, var61, this.FZ, this.Ga, this.FY);
         this.FU.setGravity(0.0F, -32.0F, 0.0F);
         PhysicsSettings var62 = this.FU.getPhysicsSettings();

         try {
            var62.setNumPositionSteps(8);
            var62.setNumVelocitySteps(4);
            var62.setBaumgarte(0.8F);
            var62.setPenetrationSlop(0.001F);
            this.FU.setPhysicsSettings(var62);
         } catch (Throwable var47) {
            if (var62 != null) {
               try {
                  var62.close();
               } catch (Throwable var46) {
                  var47.addSuppressed(var46);
               }
            }

            throw var47;
         }

         if (var62 != null) {
            var62.close();
         }

         this.FV = this.FU.getBodyInterface();
         this.FW = new TempAllocatorMalloc();
         this.FX = new JobSystemSingleThreaded(2048);
         this.Gb = new GroupFilterTable(var1.pK().size());
         this.FO = new BoneRigidBody[var1.pK().size()];
         this.FP = new BoneConstraintLink[var1.pL().size()];

         try {
            for (int var63 = 0; var63 < this.FO.length; var63++) {
               KeyframeEntry var65 = (KeyframeEntry)var1.pK().get(var63);
               int var67 = (Integer)var24.get(var65.bone());
               this.FR.put(var65.id(), var63);
               this.FN[var67].He = var63;
               BoneKeyframe var76 = var65.pR();
               Shape var84 = method1777(var76);

               try {
                  BodyCreationSettings var90 = new BodyCreationSettings();

                  try {
                     CollisionGroup var94 = new CollisionGroup(this.Gb, 1, var63);

                     try {
                        Matrix4f var97 = var25[var67];
                        Quaternionf var39 = var97.getUnnormalizedRotation(new Quaternionf()).normalize();
                        Vector3f var40 = var97.transformPosition((float)var76.pV().getX(), (float)var76.pV().getY(), (float)var76.pV().getZ(), new Vector3f());
                        var90.setShape(var84)
                           .setPosition(var40.x, var40.y, var40.z)
                           .setRotation(var39.x, var39.y, var39.z, var39.w)
                           .setAllowDynamicOrKinematic(true)
                           .setMotionType(var23 ? EMotionType.Kinematic : EMotionType.Dynamic)
                           .setObjectLayer(0)
                           .setLinearVelocity(var23 ? 0.0F : (float)(var11 * 20.0), var23 ? 0.0F : (float)(var13 * 20.0), var23 ? 0.0F : (float)(var15 * 20.0))
                           .setLinearDamping(0.02F)
                           .setAngularDamping(0.2F)
                           .setFriction(1.0F)
                           .setCollisionGroup(var94)
                           .setOverrideMassProperties(EOverrideMassProperties.CalculateInertia);
                        var90.getMassPropertiesOverride().setMass((float)var65.pQ());
                        Body var41 = this.FV.createBody(var90);
                        if (var41 == null) {
                           throw new IllegalStateException("Jolt could not create ragdoll body " + var65.id());
                        }

                        this.FV.addBody(var41, EActivation.Activate);
                        BoneRigidBody var42 = this.FO[var63] = new BoneRigidBody(var67, var65.pQ(), var41);
                        var42.GY = var23;
                        var42.GS.set((float)var76.pV().getX(), (float)var76.pV().getY(), (float)var76.pV().getZ());
                        var42.GU.set(var39);
                        var42.GV.set(var39).conjugate();
                        var42.GT.set(var97.m30(), var97.m31(), var97.m32());
                     } catch (Throwable var48) {
                        try {
                           var94.close();
                        } catch (Throwable var45) {
                           var48.addSuppressed(var45);
                        }

                        throw var48;
                     }

                     var94.close();
                  } catch (Throwable var49) {
                     try {
                        var90.close();
                     } catch (Throwable var44) {
                        var49.addSuppressed(var44);
                     }

                     throw var49;
                  }

                  var90.close();
               } catch (Throwable var50) {
                  if (var84 != null) {
                     try {
                        var84.close();
                     } catch (Throwable var43) {
                        var50.addSuppressed(var43);
                     }
                  }

                  throw var50;
               }

               if (var84 != null) {
                  var84.close();
               }
            }

            Integer var64 = (Integer)this.FR.get(var1.pJ());
            if (var64 == null) {
               throw new IllegalArgumentException("ragdoll root body missing");
            }

            this.Gi = var64;
            HashMap var66 = new HashMap();
            var1.pL().forEach(var1x -> var66.put(var1x.qc(), var1x.qb()));

            for (int var68 = 0; var68 < this.FO.length; var68++) {
               String var77 = ((KeyframeEntry)var1.pK().get(var68)).id();

               while (var66.containsKey(var77)) {
                  var77 = (String)var66.get(var77);
               }

               this.FO[var68].GX = (Integer)this.FR.get(var77);
            }

            for (int var69 = 0; var69 < this.FO.length; var69++) {
               if (((KeyframeEntry)var1.pK().get(this.FO[var69].GX)).pS()) {
                  for (int var78 = 0; var78 < this.FO.length; var78++) {
                     if (var69 != var78) {
                        this.method1780(var69, var78);
                     }
                  }
               }
            }

            for (BoneBinding var79 : var1.pM()) {
               this.FN[var24.get(var79.bone())].He = (Integer)this.FR.get(var79.qa());
            }

            for (String var80 : var1.pN()) {
               Integer var85 = (Integer)var24.get(var80);
               if (var85 != null) {
                  this.FN[var85].He = this.Gi;
               }
            }

            for (BoneBindPose var91 : this.FN) {
               if (var91.He < 0) {
                  var91.He = this.Gi;
               }
            }

            this.method1779(var1, this.FR);

            for (BoneRigidBody var92 : this.FO) {
               this.FO[var92.GX].GR = this.FO[var92.GX].GR + var92.GQ;
            }

            for (BoneRigidBody var93 : this.FO) {
               var93.GR = this.FO[var93.GX].GR;
            }

            double var75 = var18;
            double var89 = var20;
            if (Math.hypot(var75, var89) < 0.1) {
               var75 = var11;
               var89 = var15;
               if (Math.hypot(var75, var89) < 0.02) {
                  double var95 = ThreadLocalRandom.current().nextDouble(Math.PI * 2);
                  var75 = Math.cos(var95);
                  var89 = Math.sin(var95);
                  this.GJ = 0.2;
               } else {
                  this.GJ = 0.3;
               }
            } else {
               this.GJ = 0.3;
            }

            double var96 = Math.hypot(var75, var89);
            this.GH = var75 / var96;
            this.GI = var89 / var96;
            this.method1792();
            this.Gt = (float)(this.Gg + this.Gm - this.Gs);
            this.FO[this.Gi].GU.transform(new Vector3f(0.0F, 1.0F, 0.0F), this.Gu);
         } catch (RuntimeException | Error var51) {
            this.close();
            throw var51;
         }
      } else {
         throw new IllegalArgumentException("ragdoll pose size mismatch");
      }
   }

   private static Shape method1777(BoneKeyframe var0) {
      Object var1 = switch (var0.pU()) {
         case "box" -> {
            DoubleVector3 var4 = var0.pX();
            yield method1778((float)var4.getX() * 0.5F, (float)var4.getY() * 0.5F, (float)var4.getZ() * 0.5F);
         }
         case "sphere" -> new SphereShape(var0.pY().floatValue());
         case "capsule" -> new CapsuleShape(var0.pZ().floatValue() * 0.5F, var0.pY().floatValue());
         default -> throw new IllegalArgumentException("unknown ragdoll collider");
      };
      DoubleQuaternion var8 = var0.pW();
      if (Math.abs(var8.getX()) + Math.abs(var8.getY()) + Math.abs(var8.getZ()) < 1.0E-6 && Math.abs(var8.getW() - 1.0) < 1.0E-6) {
         return (Shape)var1;
      }

      try {
         return new RotatedTranslatedShape(
            new Vec3(), new Quat((float)var8.getX(), (float)var8.getY(), (float)var8.getZ(), (float)var8.getW()), (ConstShape)var1
         );
      } finally {
         var1.close();
      }
   }

   private static BoxShape method1778(float var0, float var1, float var2) {
      return new BoxShape(var0, var1, var2, Math.min(0.02F, Math.min(var0, Math.min(var1, var2)) * 0.5F));
   }

   private void method1779(AnimationClip var1, Map var2) {
      for (int var3 = 0; var3 < this.FP.length; var3++) {
         BoneAnimation var4 = (BoneAnimation)var1.pL().get(var3);
         int var5 = (Integer)var2.get(var4.qb());
         int var6 = (Integer)var2.get(var4.qc());
         BoneRigidBody var7 = this.FO[var5];
         BoneRigidBody var8 = this.FO[var6];
         if (this.FP[var3] == null && !var7.GY && !var8.GY) {
            Vector3f var9 = method1783(var7, var4.qd(), new Vector3f()).add(method1783(var8, var4.qe(), new Vector3f())).mul(0.5F);

            Object var10 = switch (var4.pU()) {
               case "fixed" -> {
                  FixedConstraintSettings var23 = new FixedConstraintSettings();
                  var23.setAutoDetectPoint(false);
                  var23.setSpace(EConstraintSpace.WorldSpace);
                  var23.setPoint1(var9.x, var9.y, var9.z);
                  var23.setPoint2(var9.x, var9.y, var9.z);
                  yield var23;
               }
               case "hinge" -> {
                  HingeConstraintSettings var22 = new HingeConstraintSettings();
                  var22.setSpace(EConstraintSpace.WorldSpace);
                  var22.setPoint1(var9.x, var9.y, var9.z);
                  var22.setPoint2(var9.x, var9.y, var9.z);
                  Vector3f var25 = method1784(var7, var4.qf(), new Vector3f()).normalize();
                  Vector3f var27 = method1781(var25);
                  var22.setHingeAxis1(var25.x, var25.y, var25.z);
                  var22.setHingeAxis2(var25.x, var25.y, var25.z);
                  var22.setNormalAxis1(var27.x, var27.y, var27.z);
                  var22.setNormalAxis2(var27.x, var27.y, var27.z);
                  var22.setLimitsMin(var4.qj().floatValue());
                  var22.setLimitsMax(var4.qk().floatValue());
                  yield var22;
               }
               case "ball" -> {
                  SwingTwistConstraintSettings var13 = new SwingTwistConstraintSettings();
                  var13.setSpace(EConstraintSpace.WorldSpace);
                  var13.setPosition1(var9.x, var9.y, var9.z);
                  var13.setPosition2(var9.x, var9.y, var9.z);
                  Vector3f var14 = method1784(var7, var4.qf(), new Vector3f()).normalize();
                  Vector3f var15 = method1784(var8, var4.qg(), new Vector3f()).normalize();
                  Vector3f var16 = method1781(var14);
                  Vector3f var17 = method1781(var15);
                  var13.setTwistAxis1(var14.x, var14.y, var14.z);
                  var13.setTwistAxis2(var15.x, var15.y, var15.z);
                  var13.setPlaneAxis1(var16.x, var16.y, var16.z);
                  var13.setPlaneAxis2(var17.x, var17.y, var17.z);
                  var13.setNormalHalfConeAngle(var4.qh().floatValue());
                  var13.setPlaneHalfConeAngle(var4.qh().floatValue());
                  var13.setTwistMinAngle((float)(-var4.qi()));
                  var13.setTwistMaxAngle(var4.qi().floatValue());
                  yield var13;
               }
               default -> throw new IllegalArgumentException("unknown ragdoll joint");
            };
            Object var20 = var10;

            try {
               TwoBodyConstraint var21 = var10.create(var7.GW, var8.GW);
               this.FU.addConstraint(var21);
               BoneConstraintLink var24 = this.FP[var3] = new BoneConstraintLink(var5, var6, var21);
               RVec3 var26 = var7.GW.getPosition();
               var24.GM.set(var9).sub((float)var26.xx(), (float)var26.yy(), (float)var26.zz());
               var7.GV.transform(var24.GM);
               RVec3 var28 = var8.GW.getPosition();
               var24.GN.set(var9).sub((float)var28.xx(), (float)var28.yy(), (float)var28.zz());
               var8.GV.transform(var24.GN);
            } catch (Throwable var19) {
               if (var20 != null) {
                  try {
                     var20.close();
                  } catch (Throwable var18) {
                     var19.addSuppressed(var18);
                  }
               }

               throw var19;
            }

            if (var20 != null) {
               var20.close();
            }

            this.method1780(var5, var6);
         }
      }
   }

   private void method1780(int var1, int var2) {
      int var3 = Math.min(var1, var2) * this.FO.length + Math.max(var1, var2);
      if (!this.Gc.get(var3)) {
         this.Gc.set(var3);
         this.Gb.disableCollision(var1, var2);
         this.GC++;
      }
   }

   private static Vector3f method1781(Vector3f var0) {
      return Math.abs(var0.y) < 0.8F ? var0.cross(0.0F, 1.0F, 0.0F, new Vector3f()).normalize() : var0.cross(1.0F, 0.0F, 0.0F, new Vector3f()).normalize();
   }

   private static void method1782(Matrix4f var0, Vector3f var1) {
      var0.m30(var0.m30() + var1.x);
      var0.m31(var0.m31() + var1.y);
      var0.m32(var0.m32() + var1.z);
   }

   private static Vector3f method1783(BoneRigidBody var0, DoubleVector3 var1, Vector3f var2) {
      var2.set((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
      return var0.GU.transform(var2).add(var0.GT);
   }

   private static Vector3f method1784(BoneRigidBody var0, DoubleVector3 var1, Vector3f var2) {
      var2.set((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
      return var0.GU.transform(var2);
   }

   void method1785(Matrix4f[] var1, double var2, boolean var4) {
      if (this.FT && !this.cz) {
         if (var1.length != this.FN.length) {
            throw new IllegalArgumentException("ragdoll animation pose size mismatch");
         }

         for (int var5 = 0; var5 < this.FN.length; var5++) {
            if (this.FO[this.FN[var5].He].GY) {
               if (!var1[var5].isFinite()) {
                  throw new IllegalArgumentException("non-finite ragdoll animation pose");
               }

               this.FN[var5].Hb.set(var1[var5]);
            }
         }

         for (BoneRigidBody var8 : this.FO) {
            if (var8.GY) {
               Matrix4f var9 = new Matrix4f(var1[var8.GP]);
               if (this.FS != null) {
                  var9.scale(1.0F / this.FS[var8.GP]);
               }

               Quaternionf var10 = var9.getUnnormalizedRotation(new Quaternionf()).normalize();
               Vector3f var11 = var9.transformPosition(var8.GS, new Vector3f());
               RVec3 var12 = var8.GW.getPosition();
               var8.GZ.set(var11).sub((float)var12.xx(), (float)var12.yy(), (float)var12.zz()).mul(20.0F);
               if (var8.GZ.lengthSquared() > 900.0F) {
                  var8.GZ.normalize(30.0F);
               }

               Quaternionf var13 = new Quaternionf(var10).mul(new Quaternionf(var8.GU).conjugate()).normalize();
               if (var13.w < 0.0F) {
                  var13.mul(-1.0F);
               }

               float var14 = (float)Math.sqrt(var13.x * var13.x + var13.y * var13.y + var13.z * var13.z);
               var8.Ha.set(var13.x, var13.y, var13.z);
               if (var14 > 1.0E-6F) {
                  var8.Ha.mul((float)(2.0 * Math.atan2(var14, var13.w) * 20.0 / var14));
               } else {
                  var8.Ha.zero();
               }

               if (var8.Ha.lengthSquared() > 400.0F) {
                  var8.Ha.normalize(20.0F);
               }

               var8.GU.set(var10);
               var8.GV.set(var10).conjugate();
               var8.GT.set(var9.m30(), var9.m31(), var9.m32());
               this.FV.setPositionAndRotation(var8.GW.getId(), var11.x, var11.y, var11.z, var10.x, var10.y, var10.z, var10.w, EActivation.Activate);
            }
         }

         for (BoneRigidBody var19 : this.FO) {
            if (var19.GY) {
               KeyframeEntry var20 = (KeyframeEntry)this.FQ.pK().get(var19.GX);
               if (var4 || var20.pT() != null && !(var2 + 1.0E-9 < var20.pT())) {
                  var19.GY = false;
                  this.FV.setMotionType(var19.GW.getId(), EMotionType.Dynamic, EActivation.Activate);
                  this.FV.setLinearAndAngularVelocity(var19.GW.getId(), var19.GZ.x, var19.GZ.y, var19.GZ.z, var19.Ha.x, var19.Ha.y, var19.Ha.z);
               }
            }
         }

         this.method1779(this.FQ, this.FR);
         if (var4) {
            this.FT = false;
            this.Gz = this.GA = 0;
            this.GF = false;
         }

         this.method1792();
      }
   }

   boolean method1786(EntityBoundsSnapshot var1) {
      double var2 = var1.sv() * var1.sv() + var1.sw() * var1.sw();
      if (!this.cz
         && !(var2 < 2.25E-4)
         && !(var2 > 0.6400000000000001)
         && !(var1.ka() < this.Gf + this.Go - 0.35)
         && !(var1.ka() > this.Gf + this.Gp + 0.35)
         && !(var1.kc() < this.Gh + this.Gq - 0.35)
         && !(var1.kc() > this.Gh + this.Gr + 0.35)) {
         double var4 = var1.ka() - this.Gf;
         double var6 = var1.kb() - this.Gg;
         double var8 = var1.kc() - this.Gh;
         BoneRigidBody var10 = null;
         double var11 = Double.POSITIVE_INFINITY;

         for (BoneRigidBody var16 : this.FO) {
            if (!var16.GY) {
               ConstAaBox var17 = var16.GW.getWorldSpaceBounds();
               Vec3 var18 = var17.getMin();
               Vec3 var19 = var17.getMax();
               if (!(var4 < var18.getX() - 0.35)
                  && !(var4 > var19.getX() + 0.35)
                  && !(var8 < var18.getZ() - 0.35)
                  && !(var8 > var19.getZ() + 0.35)
                  && !(var6 > var19.getY() + 0.1)
                  && !(var6 + var1.su() < var18.getY() - 0.1)) {
                  RVec3 var20 = var16.GW.getPosition();
                  double var21 = var20.xx() - var4;
                  double var23 = var20.zz() - var8;
                  double var25 = var21 * var21 + var23 * var23;
                  if (var25 < var11) {
                     var10 = var16;
                     var11 = var25;
                  }
               }
            }
         }

         if (var10 == null) {
            return false;
         }

         double var27 = Math.sqrt(var2);
         double var28 = var10.GR * Math.min(var27, 0.35) * 7.0;
         RVec3 var29 = var10.GW.getPosition();
         var10.GW
            .addImpulse(
               new Vec3((float)(var1.sv() / var27 * var28), 0.0F, (float)(var1.sw() / var27 * var28)),
               new RVec3(
                  var29.xx() + Math.max(-0.25, Math.min(0.25, var4 - var29.xx())),
                  var29.yy() + Math.max(-0.35, Math.min(0.35, var6 + 0.55 - var29.yy())),
                  var29.zz() + Math.max(-0.25, Math.min(0.25, var8 - var29.zz()))
               )
            );
         if (this.GD) {
            this.GD = false;
            this.GF = false;
            this.Gz = this.GA = 0;

            for (BoneRigidBody var33 : this.FO) {
               var33.GW.getMotionProperties().setLinearDamping(0.02F);
               var33.GW.getMotionProperties().setAngularDamping(0.2F);
            }

            this.field32 = false;
         }

         return true;
      } else {
         return false;
      }
   }

   void rq() {
      if (!this.cz) {
         this.GD = false;
         this.Gz = this.GA = 0;
         this.GF = this.field32 = false;

         for (BoneRigidBody var4 : this.FO) {
            var4.GW.getMotionProperties().setLinearDamping(0.02F);
            var4.GW.getMotionProperties().setAngularDamping(0.2F);
            RVec3 var5 = var4.GW.getPosition();
            Quat var6 = var4.GW.getRotation();
            this.FV
               .setPositionAndRotation(
                  var4.GW.getId(), var5.xx(), var5.yy(), var5.zz(), var6.getX(), var6.getY(), var6.getZ(), var6.getW(), EActivation.Activate
               );
         }
      }
   }

   void method1787(VoxelCollisionShape var1) {
      this.method1789(var1, 1, false);
   }

   void method1788(VoxelCollisionShape var1, int var2) {
      this.method1789(var1, var2, false);
   }

   void method1789(VoxelCollisionShape var1, int var2, boolean var3) {
      if (!this.cz && !this.GD) {
         if (var1 != this.Gk) {
            this.method1790(var1);
         }

         if (this.Gj != null) {
            float var4 = (float)ModelPhysicsLoop.getGroundFriction();
            this.Gj.setFriction(var4 * var4);
         }

         if (this.GE && !this.FT) {
            RVec3 var16 = this.FO[this.Gi].GW.getPosition();
            this.FO[this.Gi]
               .GW
               .addImpulse(
                  new Vec3((float)(this.GH * this.FO[this.Gi].GR * this.GJ), 0.0F, (float)(this.GI * this.FO[this.Gi].GR * this.GJ)),
                  new RVec3(var16.xx(), var16.yy() + 0.3, var16.zz())
               );
            this.GE = false;
         }

         if (var3 != this.field32) {
            this.field32 = var3;

            for (BoneRigidBody var7 : this.FO) {
               var7.GW.getMotionProperties().setLinearDamping(var3 ? 1.2F : (this.GF ? 0.3F : 0.02F));
               var7.GW.getMotionProperties().setAngularDamping(var3 ? 1.8F : (this.GF ? 0.8F : 0.2F));
            }
         }

         int var18 = this.FU.update(0.05F * var2, 6 * var2, this.FW, this.FX);
         if (var18 != 0) {
            throw new IllegalStateException("Jolt physics update failed: " + var18);
         }

         double var19 = 0.0;
         double var20 = 0.0;

         for (BoneRigidBody var12 : this.FO) {
            var19 = Math.max(var19, var12.GW.getLinearVelocity().length());
            var20 = Math.max(var20, var12.GW.getAngularVelocity().length());
            RVec3 var13 = var12.GW.getPosition();
            if (!Double.isFinite(var13.xx() + var13.yy() + var13.zz()) || var13.lengthSq() > 16384.0) {
               throw new IllegalStateException("non-finite or explosive ragdoll state");
            }
         }

         this.Gw = (float)(var19 / 20.0);
         this.Gx = (float)(var20 / 20.0);
         this.Gv = Math.max(this.Gw, this.Gx);
         this.method1792();
         this.GB = 0;
         this.Gy = 0.0F;

         for (BoneRigidBody var27 : this.FO) {
            Vec3 var29 = var27.GW.getWorldSpaceBounds().getMin();
            double var14 = var1.method8996(this.Gf + var27.GW.getPosition().xx(), this.Gh + var27.GW.getPosition().zz()) - this.Gg;
            if (Double.isFinite(var14) && var29.getY() <= var14 + 0.03) {
               this.GB++;
               this.Gy = Math.max(this.Gy, (float)(var14 - var29.getY()));
            }
         }

         this.GA = this.GB > 0 ? this.GA + var2 : 0;
         if (!this.GF && this.GA >= 8 && this.rs()) {
            this.GF = true;
            if (!var3) {
               for (BoneRigidBody var28 : this.FO) {
                  var28.GW.getMotionProperties().setLinearDamping(0.3F);
                  var28.GW.getMotionProperties().setAngularDamping(0.8F);
               }
            }
         }

         if (!this.FT && this.GA >= 8 && (this.rs() && var19 < 0.3 && var20 < 0.5 || var19 < 0.03 && var20 < 0.05)) {
            this.GD = (this.Gz += var2) >= 8;
         } else {
            this.Gz = 0;
         }
      }
   }

   private void method1790(VoxelCollisionShape var1) {
      if (this.Gj != null) {
         this.FV.removeBody(this.Gj.getId());
         this.FV.destroyBody(this.Gj.getId());
         this.Gj = null;
      }

      if (var1.st()) {
         BoxShape var16 = method1778(64.0F, 0.5F, 64.0F);

         try {
            this.method1791(var16, 0.0, var1.method8996(this.Gf, this.Gh) - this.Gg - 0.5, 0.0);
         } catch (Throwable var13) {
            if (var16 != null) {
               try {
                  var16.close();
               } catch (Throwable var11) {
                  var13.addSuppressed(var11);
               }
            }

            throw var13;
         }

         if (var16 != null) {
            var16.close();
         }
      } else {
         StaticCompoundShapeSettings var2 = new StaticCompoundShapeSettings();

         try {
            int[] var3 = new int[]{0};
            var1.method8994(
               var3x -> {
                  float var4x = (float)((var3x.IU - var3x.IR) * 0.5);
                  float var5x = (float)((var3x.IV - var3x.IS) * 0.5);
                  float var6 = (float)((var3x.IW - var3x.IT) * 0.5);
                  if (!(var4x <= 0.0F) && !(var5x <= 0.0F) && !(var6 <= 0.0F)) {
                     BoxShape var7 = method1778(var4x, var5x, var6);

                     try {
                        var2.addShape(
                           (float)((var3x.IR + var3x.IU) * 0.5 - this.Gf),
                           (float)((var3x.IS + var3x.IV) * 0.5 - this.Gg),
                           (float)((var3x.IT + var3x.IW) * 0.5 - this.Gh),
                           var7
                        );
                        var3[0]++;
                     } catch (Throwable var11x) {
                        if (var7 != null) {
                           try {
                              var7.close();
                           } catch (Throwable var10x) {
                              var11x.addSuppressed(var10x);
                           }
                        }

                        throw var11x;
                     }

                     if (var7 != null) {
                        var7.close();
                     }
                  }
               }
            );
            if (var3[0] > 0) {
               ShapeResult var4 = var2.create();

               try {
                  if (var4.hasError()) {
                     throw new IllegalStateException(var4.getError());
                  }

                  ShapeRefC var5 = var4.get();

                  try {
                     this.method1791(var5, 0.0, 0.0, 0.0);
                  } catch (Throwable var12) {
                     if (var5 != null) {
                        try {
                           var5.close();
                        } catch (Throwable var10) {
                           var12.addSuppressed(var10);
                        }
                     }

                     throw var12;
                  }

                  if (var5 != null) {
                     var5.close();
                  }
               } catch (Throwable var14) {
                  if (var4 != null) {
                     try {
                        var4.close();
                     } catch (Throwable var9) {
                        var14.addSuppressed(var9);
                     }
                  }

                  throw var14;
               }

               if (var4 != null) {
                  var4.close();
               }
            }
         } catch (Throwable var15) {
            try {
               var2.close();
            } catch (Throwable var8) {
               var15.addSuppressed(var8);
            }

            throw var15;
         }

         var2.close();
      }

      this.Gk = var1;
   }

   private void method1791(ConstShape var1, double var2, double var4, double var6) {
      BodyCreationSettings var8 = new BodyCreationSettings();

      try {
         var8.setShape(var1).setPosition(var2, var4, var6).setMotionType(EMotionType.Static).setObjectLayer(1).setFriction(1.0F);
         this.Gj = this.FV.createBody(var8);
         if (this.Gj == null) {
            throw new IllegalStateException("Jolt could not create terrain body");
         }

         this.FV.addBody(this.Gj, EActivation.DontActivate);
      } catch (Throwable var12) {
         try {
            var8.close();
         } catch (Throwable var11) {
            var12.addSuppressed(var11);
         }

         throw var12;
      }

      var8.close();
   }

   private void method1792() {
      double var1 = 0.0;
      double var3 = 0.0;
      double var5 = 0.0;
      double var7 = 0.0;

      for (BoneRigidBody var12 : this.FO) {
         RVec3 var13 = var12.GW.getPosition();
         var1 += var12.GQ;
         var3 += var13.xx() * var12.GQ;
         var5 += var13.yy() * var12.GQ;
         var7 += var13.zz() * var12.GQ;
      }

      this.Gl = (float)(var3 / var1);
      this.Gm = (float)(var5 / var1);
      this.Gn = (float)(var7 / var1);
      this.Gs = Float.POSITIVE_INFINITY;
      this.Go = this.Gq = Float.POSITIVE_INFINITY;
      this.Gp = this.Gr = Float.NEGATIVE_INFINITY;

      for (BoneRigidBody var23 : this.FO) {
         ConstAaBox var25 = var23.GW.getWorldSpaceBounds();
         Vec3 var14 = var25.getMin();
         Vec3 var15 = var25.getMax();
         this.Gs = Math.min(this.Gs, var14.getY() + (float)this.Gg);
         this.Go = Math.min(this.Go, var14.getX());
         this.Gp = Math.max(this.Gp, var15.getX());
         this.Gq = Math.min(this.Gq, var14.getZ());
         this.Gr = Math.max(this.Gr, var15.getZ());
      }

      for (BoneBindPose var24 : this.FN) {
         BoneRigidBody var26 = this.FO[var24.He];
         RVec3 var27 = var26.GW.getPosition();
         Quat var28 = var26.GW.getRotation();
         this.Ge.set(var28.getX(), var28.getY(), var28.getZ(), var28.getW());
         this.Gd.set(var26.GS).negate();
         this.Ge.transform(this.Gd);
         var24.Hc
            .translation((float)var27.xx() + this.Gd.x, (float)var27.yy() + this.Gd.y, (float)var27.zz() + this.Gd.z)
            .rotate(this.Ge)
            .rotate(var26.GV)
            .translate(-var26.GT.x, -var26.GT.y, -var26.GT.z)
            .mul(var24.Hb);
         var24.Hc.m30(var24.Hc.m30() - this.Gl);
         var24.Hc.m31(var24.Hc.m31() - this.Gm);
         var24.Hc.m32(var24.Hc.m32() - this.Gn);

         for (byte var16 = 0; var16 < var24.Hd.length; var16 += 3) {
            var24.Hc.transformPosition(var24.Hd[var16], var24.Hd[var16 + 1], var24.Hd[var16 + 2], this.Gd);
            this.Gs = Math.min(this.Gs, this.Gd.y + this.Gm + (float)this.Gg);
            this.Go = Math.min(this.Go, this.Gd.x + this.Gl);
            this.Gp = Math.max(this.Gp, this.Gd.x + this.Gl);
            this.Gq = Math.min(this.Gq, this.Gd.z + this.Gn);
            this.Gr = Math.max(this.Gr, this.Gd.z + this.Gn);
         }
      }
   }

   Matrix4f method1793(int var1) {
      return this.FN[var1].Hc;
   }

   private boolean rs() {
      if (this.Gg + this.Gm - this.Gs < this.Gt * 0.4F) {
         return true;
      }

      Quat var1 = this.FO[this.Gi].GW.getRotation();
      this.Ge.set(var1.getX(), var1.getY(), var1.getZ(), var1.getW());
      this.Ge.transform(0.0F, 1.0F, 0.0F, this.Gd);
      return this.Gu.dot(this.Gd) < 0.26;
   }

   float rt() {
      float var1 = 0.0F;

      for (BoneConstraintLink var5 : this.FP) {
         if (var5 != null) {
            BoneRigidBody var6 = this.FO[var5.GK];
            BoneRigidBody var7 = this.FO[var5.GL];
            RVec3 var8 = var6.GW.getPosition();
            RVec3 var9 = var7.GW.getPosition();
            Quat var10 = var6.GW.getRotation();
            Quat var11 = var7.GW.getRotation();
            Vector3f var12 = new Quaternionf(var10.getX(), var10.getY(), var10.getZ(), var10.getW())
               .transform(var5.GM, new Vector3f())
               .add((float)var8.xx(), (float)var8.yy(), (float)var8.zz());
            Vector3f var13 = new Quaternionf(var11.getX(), var11.getY(), var11.getZ(), var11.getW())
               .transform(var5.GN, new Vector3f())
               .add((float)var9.xx(), (float)var9.yy(), (float)var9.zz());
            var1 = Math.max(var1, var12.distance(var13));
         }
      }

      return var1;
   }

   int ru() {
      return this.FO[this.Gi].GP;
   }

   int rv() {
      return this.FO.length;
   }

   int rw() {
      return this.FP.length;
   }

   boolean rx() {
      return this.GD;
   }

   float ry() {
      return this.Gv;
   }

   float rz() {
      return this.Gw;
   }

   float rA() {
      return this.Gx;
   }

   float rB() {
      return this.Gy;
   }

   int rC() {
      return this.GB;
   }

   int rD() {
      return this.GC;
   }

   float rE() {
      return this.Go;
   }

   float rF() {
      return this.Gp;
   }

   float rG() {
      return this.Gq;
   }

   float rH() {
      return this.Gr;
   }

   double rI() {
      return this.Gf + this.Gl;
   }

   double rJ() {
      return this.Gg + this.Gm;
   }

   double rK() {
      return this.Gh + this.Gn;
   }

   float rL() {
      return this.Gs;
   }

   @Override
   public void close() {
      if (!this.cz) {
         this.cz = true;
         if (this.Gj != null) {
            this.FV.removeBody(this.Gj.getId());
            this.FV.destroyBody(this.Gj.getId());
         }

         for (BoneConstraintLink var4 : this.FP) {
            if (var4 != null) {
               this.FU.removeConstraint(var4.GO);
               var4.GO.close();
            }
         }

         for (BoneRigidBody var8 : this.FO) {
            if (var8 != null) {
               this.FV.removeBody(var8.GW.getId());
               this.FV.destroyBody(var8.GW.getId());
            }
         }

         this.FU.close();
         this.FX.close();
         this.FW.close();
         this.Gb.close();
         this.Ga.close();
         this.FZ.close();
         this.FY.close();
      }
   }
}
