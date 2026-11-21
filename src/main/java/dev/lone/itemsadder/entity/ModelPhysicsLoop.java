package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.anim.AnimationClip;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.physics.EntityBoundsSnapshot;
import dev.lone.itemsadder.physics.RagdollSimulation;
import dev.lone.itemsadder.physics.VoxelCollisionShape;
import dev.lone.itemsadder.util.DoubleVector3Dup2;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.World;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class ModelPhysicsLoop {
   private static volatile double HR = 0.2;
   public static final int HS = 120;
   public static final int HT = 128;
   public static final int HU = 256;
   public static final EntityBoundsSnapshot[] HV = new EntityBoundsSnapshot[0];
   private static final RotationPolicy[] HW = RotationPolicy.values();
   private static final boolean HX = Boolean.getBoolean("itemsadder.ragdoll.profile");
   private static final ThreadMXBean HY = HX ? ManagementFactory.getThreadMXBean() : null;
   private static final com.sun.management.ThreadMXBean HZ;
   private final ModelPartDisplay[] Ia;
   private static final int Ib = 20;
   private final RagdollSimulation Ic;
   private ModelInstance Id;
   private Matrix4f[] Ie;
   private int If;
   private int Ig;
   private final double Ih;
   private final double Ii;
   private final double Ij;
   private volatile VoxelCollisionShape Ik;
   private volatile double field119;
   private volatile double mB;
   private volatile double mE;
   private volatile double mD;
   private volatile double mG;
   private volatile double Im;
   private volatile double In;
   private volatile double field120;
   private volatile boolean Ip;
   private volatile boolean Iq;
   private volatile boolean Ir;
   private volatile boolean Is;
   private volatile boolean It;
   private int Iu;
   private EntityBoundsSnapshot[] Iv = HV;
   private int Iw;
   private int ms;
   private int Ix;
   private long Iy;
   private long Iz;
   private int IA;
   private boolean IB;
   private double IC;
   private float ID;
   private float IE;
   private long IF;

   public static double getGroundFriction() {
      return HR;
   }

   public static void setGroundFriction(double var0) {
      if (Double.isFinite(var0) && !(var0 < 0.0) && !(var0 > 2.0)) {
         HR = var0;
      } else {
         throw new IllegalArgumentException("ragdoll ground friction must be between 0 and 2");
      }
   }

   public ModelPhysicsLoop(CustomEntityImpl var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      this(var1, var2, var4, var6, var8, var10, var12, VoxelCollisionShape.method8990(var14));
   }

   public ModelPhysicsLoop(CustomEntityImpl var1, double var2, double var4, double var6, double var8, double var10, double var12, VoxelCollisionShape var14) {
      this(var1, var2, var4, var6, var8, var10, var12, var14, 0.0F);
   }

   public ModelPhysicsLoop(
      CustomEntityImpl var1, double var2, double var4, double var6, double var8, double var10, double var12, VoxelCollisionShape var14, float var15
   ) {
      this(var1, var2, var4, var6, var8, var10, var12, var14, var15, 0.0, 0.0);
   }

   public ModelPhysicsLoop(
      CustomEntityImpl var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      VoxelCollisionShape var14,
      float var15,
      double var16,
      double var18
   ) {
      AnimationClip var20 = var1.getModel().nY();
      if (var20 == null) {
         throw new IllegalArgumentException("V2 bundle has no authored ragdoll physics profile");
      }

      if (var20.pP() && !var1.getModel().AM && var1.getDeathAnimation() != null) {
         this.Id = var1.getModelInstance();
         ModelDefinitionData var21 = var1.getModel().method475(var1.getDeathAnimation());
         this.If = Math.max(1, (int)Math.ceil(var21.yQ * 20.0));
         this.Id.method8909(var21);
      }

      int var32 = var1.getModel().AL.size();
      if (var32 < 1 || var32 > 128) {
         throw new IllegalArgumentException("unsupported ragdoll bone count");
      }

      if (Double.isFinite(var2)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var12)
         && Double.isFinite(var16)
         && Double.isFinite(var18)) {
         int[] var22 = new int[HW.length];
         this.Ia = new ModelPartDisplay[var32];

         for (ModelPartDisplay var24 : var1.rQ.values()) {
            CustomEntityDefinition var25 = var24.Ho;
            int var26 = var25.nH();
            if (var26 < 0 || var26 >= var32 || this.Ia[var26] != null) {
               throw new IllegalArgumentException("unsupported ragdoll hierarchy");
            }

            this.Ia[var26] = var24;
            if (var25.Aq) {
               for (RotationPolicy var30 : HW) {
                  if ((var22[var30.ordinal()] += var25.nJ() ? var25.method8741(var30).length : 1) > 256) {
                     throw new IllegalArgumentException("unsupported ragdoll display count");
                  }
               }
            }
         }

         Matrix4f[] var33 = new Matrix4f[var32];
         float[][] var34 = new float[var32][];
         float[] var35 = new float[var32];
         String[] var36 = new String[var32];

         for (int var37 = 0; var37 < var32; var37++) {
            ModelPartDisplay var38 = this.Ia[var37];
            if (var38 == null) {
               throw new IllegalArgumentException("missing ragdoll bone");
            }

            CustomEntityDefinition var39 = var38.Ho;
            var36[var37] = var39.getId();
            var33[var37] = this.Id == null ? var38.getPartMatrix(new Matrix4f()) : var38.computeBoneMatrix(this.Id.bq(var39.getId()), new Matrix4f());
            var35[var37] = var39.getScaleFactor();
            if (!var33[var37].isFinite()
               || Math.abs(var33[var37].m30()) > 128.0F
               || Math.abs(var33[var37].m31()) > 128.0F
               || Math.abs(var33[var37].m32()) > 128.0F) {
               throw new IllegalArgumentException("missing or non-finite ragdoll pose");
            }

            var34[var37] = var39.getBoneTransforms();
         }

         this.Ic = new RagdollSimulation(
            var20,
            var36,
            var33,
            var34,
            var2,
            var4,
            var6,
            Math.max(-1.5, Math.min(1.5, var8)),
            Math.max(-1.5, Math.min(1.5, var10)),
            Math.max(-1.5, Math.min(1.5, var12)),
            var15,
            var16,
            var18,
            var35,
            this.Id != null
         );

         try {
            if (this.Id != null) {
               this.Ie = var33;
               this.Ic.method1785(this.Ie, 0.0, false);
            }
         } catch (RuntimeException | Error var31) {
            this.Ic.close();
            throw var31;
         }

         this.Ik = var14;
         this.Ih = var2;
         this.Ii = var6;
         this.Im = var2;
         this.In = var4 + 1.0;
         this.field120 = var6;
         this.Ij = this.IC = var4;
         this.field119 = var4;
         this.mB = var2 + this.Ic.rE();
         this.mE = var2 + this.Ic.rF();
         this.mD = var6 + this.Ic.rG();
         this.mG = var6 + this.Ic.rH();
      } else {
         throw new IllegalArgumentException("non-finite ragdoll motion");
      }
   }

   public void updateCollisionShape(World var1) {
      if (!this.Ic.rx() || this.Iq) {
         if (this.Iq) {
            boolean var13 = this.Is;
            if (var13 || ++this.Iu >= 20) {
               this.Iu = 0;
               VoxelCollisionShape var3 = VoxelCollisionShape.method8992(var1, this.mB, this.mE, this.mD, this.mG, this.field119);
               if (!this.Ik.method8998(var3)) {
                  this.Ik = var3;
                  this.Ir = true;
               }

               if (var13) {
                  this.It = true;
               }
            }
         } else {
            double var2 = this.field119;
            double var4 = this.mB;
            double var6 = this.mE;
            double var8 = this.mD;
            double var10 = this.mG;
            boolean var12 = ++this.Iw % 5 == 0;
            if (var12) {
               this.Ip = var1.getBlockAt((int)Math.floor(this.Im), (int)Math.floor(this.In), (int)Math.floor(this.field120)).getType() == Material.WATER;
            }

            if (var12 || !this.Ik.method8997(var4, var6, var8, var10)) {
               this.Ik = VoxelCollisionShape.method8992(var1, var4, var6, var8, var10, var2);
            }
         }
      }
   }

   public boolean isSettled() {
      return this.Ic.rx();
   }

   public boolean isParked() {
      return this.Iq;
   }

   public boolean tickSimulation(CustomEntityImpl var1, long var2) {
      return this.tickSimulationWithBounds(var1, var2, HV, null);
   }

   public boolean tickSimulationWithBounds(CustomEntityImpl var1, long var2, EntityBoundsSnapshot[] var4, UUID var5) {
      int var6 = this.ms;
      long var7 = var2;
      if (this.Id != null) {
         int var9 = (int)Math.min(var2, this.If - this.Ig);

         for (int var10 = 0; var10 < var9; var10++) {
            this.Id.qG();

            for (int var11 = 0; var11 < this.Ia.length; var11++) {
               this.Ia[var11].computeBoneMatrix(this.Id.bq(this.Ia[var11].Ho.getId()), this.Ie[var11]);
            }

            this.Ig++;
            this.Ic.method1785(this.Ie, this.Ig / 20.0, this.Ig >= this.If);
            if (this.Ig < this.If) {
               this.Ic.method1789(this.Ik, 1, this.Ip);
            }
         }

         var7 -= var9;
         if (this.Ig >= this.If) {
            this.Id.qH();
            this.Id = null;
         }
      }

      this.ms = this.ms + (int)Math.min(var7, 120L);
      List var43 = var1.getViewers();
      if (this.ms < 120 && !var43.isEmpty()) {
         boolean var44 = var1.hasVisibleViewerWithin(Settings.get().customEntityRagdollNearDetailDistance);
         if (this.Iq) {
            if (!var44 && !this.Ir) {
               return true;
            }

            if (var44 && !this.It) {
               this.Is = true;
               return true;
            }

            if (var44) {
               this.Is = false;
            }

            this.It = false;
            this.Ir = false;
            this.Iq = false;
            this.Ic.rq();
         } else if (!var44 && this.Ic.rx()) {
            this.Iq = true;
            this.Iu = 0;
            return true;
         }

         if (var4 != this.Iv) {
            this.Iv = var4;
            if (var5 != null) {
               int var45 = 0;

               for (EntityBoundsSnapshot var15 : var4) {
                  if (var5.equals(var15.method20()) && this.Ic.method1786(var15)) {
                     if (++var45 == 4) {
                        break;
                     }
                  }
               }
            }
         }

         int var46 = 4;

         for (EntityViewer var49 : var43) {
            if (var49.kW()) {
               int var52 = var49.li();
               var46 = Math.min(var46, var52 >= 10 ? 4 : (var52 >= 4 ? 3 : 2));
            }
         }

         this.Ix = this.Ix + (int)Math.min(var7, 120L);
         if (var6 == 0 && var7 > 0L) {
            this.Ix += var46 - 1;
         }

         int var48 = Math.min(this.Ix / var46, 4);
         this.Ix -= var48 * var46;

         for (int var50 = 0; var50 < var48; var50++) {
            long var53 = HX ? HY.getCurrentThreadCpuTime() : 0L;
            long var16 = HZ == null ? 0L : HZ.getThreadAllocatedBytes(Thread.currentThread().threadId());
            this.Ic.method1789(this.Ik, var6 == 0 && var50 == 0 ? 1 : var46, this.Ip);
            if (HX) {
               this.Iy = this.Iy + (HY.getCurrentThreadCpuTime() - var53);
               if (HZ != null) {
                  this.Iz = this.Iz + (HZ.getThreadAllocatedBytes(Thread.currentThread().threadId()) - var16);
               }

               this.IA++;
               this.ID = Math.max(this.ID, this.Ic.rt());
               this.IE = Math.max(this.IE, this.Ic.rB());
               this.IF = this.IF + this.Ic.rC();
            }
         }

         double var51 = this.Ic.rI();
         double var54 = this.Ic.rJ();
         double var17 = this.Ic.rK();
         this.Im = var51;
         this.In = var54;
         this.field120 = var17;
         if (HX) {
            this.IC = Math.min(this.IC, var54);
         }

         this.field119 = var54;
         this.mB = this.Ih + this.Ic.rE();
         this.mE = this.Ih + this.Ic.rF();
         this.mD = this.Ii + this.Ic.rG();
         this.mG = this.Ii + this.Ic.rH();
         boolean var19 = false;

         for (EntityViewer var21 : var43) {
            if (var21.kW() && (var21.kX() || var21.kZ())) {
               var19 = true;
               break;
            }
         }

         if (!var19) {
            var1.tickEquipment();
            return true;
         }

         var43.forEach(EntityViewer::kS);
         boolean var55 = false;

         try {
            for (int var56 = 0; var56 < this.Ia.length; var56++) {
               if (this.Ia[var56].Ho.Aq) {
                  this.Ia[var56].applyPhysicsTransform(var51, var54, var17, this.Ic.method1793(var56));
               }
            }

            var1.applyPositionUpdate(var51, var54, var17);
            var1.tickEquipment();
            var55 = true;
         } finally {
            try {
               for (EntityViewer var28 : var43) {
                  var28.method8552(var55);
               }
            } finally {
               for (EntityViewer var31 : var43) {
                  var31.method8552(false);
               }
            }
         }

         return true;
      } else {
         if (HX && !this.IB) {
            this.IB = true;
            System.out
               .println(
                  "IA_RAGDOLL_PROFILE|bones="
                     + this.Ia.length
                     + "|physical="
                     + this.Ic.rv()
                     + "|joints="
                     + this.Ic.rw()
                     + "|root="
                     + this.Ic.ru()
                     + "|steps="
                     + this.IA
                     + "|excludedSelfPairs="
                     + this.Ic.rD()
                     + "|cpuNanos="
                     + this.Iy
                     + "|allocatedBytes="
                     + this.Iz
                     + "|settled="
                     + this.Ic.rx()
                     + "|lastMotion="
                     + this.Ic.ry()
                     + "|lastLinear="
                     + this.Ic.rz()
                     + "|lastAngular="
                     + this.Ic.rA()
                     + "|lastPenetration="
                     + this.Ic.rB()
                     + "|maxJointError="
                     + this.ID
                     + "|maxPenetration="
                     + this.IE
                     + "|groundContacts="
                     + this.IF
                     + "|startY="
                     + this.Ij
                     + "|minY="
                     + this.IC
                     + "|lastY="
                     + this.Ic.rJ()
               );
         }

         return false;
      }
   }

   public int getElapsedTicks() {
      return this.ms;
   }

   public boolean isSettledDup2() {
      return this.Ic.rx();
   }

   public DoubleVector3Dup2 getPosition() {
      return new DoubleVector3Dup2(this.Im, this.In, this.field120);
   }

   public Matrix4fc getBoneMatrix(int var1) {
      return this.Ic.method1793(var1);
   }

   public float getJointError() {
      return this.Ic.rt();
   }

   public double getRagdollEnergy() {
      return this.Ic.rL();
   }

   public double getRootY() {
      return this.Ic.rJ();
   }

   public double getRootX() {
      return this.Ic.rI();
   }

   public void close() {
      this.Ic.close();
   }

   static {
      HZ = HX && HY instanceof com.sun.management.ThreadMXBean var0 && var0.isThreadAllocatedMemorySupported() ? var0 : null;
      if (HZ != null && !HZ.isThreadAllocatedMemoryEnabled()) {
         HZ.setThreadAllocatedMemoryEnabled(true);
      }
   }
}
