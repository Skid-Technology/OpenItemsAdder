package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.EulerRotation;
import dev.lone.itemsadder.util.FloatVector3;
import dev.lone.itemsadder.util.FloatVector3Base;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MathUtils;
import dev.lone.itemsadder.util.Rotation3D;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class ModelPartDisplay {
   private static final double Hj = 0.2;
   private static final double Hk = 0.3;
   private static final double Hl = 0.5;
   public final CustomEntityImpl Hm;
   public final AbstractFakeEntity Hn;
   public final CustomEntityDefinition Ho;
   @Nullable
   ModelRiderController Hp;
   double[] Hq;
   double Hr;
   double Hs;
   double Ht;
   private double Hu;
   double Hv;
   double Hw;
   private final EulerRotation Hx;
   private final EulerRotation Hy;
   protected int Hz = -1;
   private boolean HA;
   private volatile boolean HB = true;
   private boolean HC = true;
   private boolean HD = true;
   @Nullable
   private Boolean HE;
   private volatile int HF = 16777215;
   private volatile boolean HG;
   @Nullable
   private Integer field118;
   @Nullable
   protected Boolean HI;
   long HJ;
   boolean HK;
   protected ModelPartDisplay HL;
   boolean HM;
   private final ModelInstance HN;
   private final EquipmentSlot HO;
   private final Matrix4f HP = new Matrix4f();
   private final Matrix4f HQ = new Matrix4f();

   public Matrix4f getPartMatrix(Matrix4f var1) {
      return this.Hn instanceof DisplayEntity var2 ? var2.method3064(var1) : var1.set(this.HP);
   }

   Matrix4f computeBoneMatrix(Matrix4fc var1, Matrix4f var2) {
      this.applyMatrixInternal(this.Hm.getBaseLocation(), var1, false);
      return this.getPartMatrix(var2);
   }

   public Matrix4f getScaledPartMatrix(Matrix4f var1) {
      this.getPartMatrix(var1).scale(1.0F / this.Ho.getScaleFactor());
      if (this.Ho.An == BonePurpose.MOUNT_PIVOT) {
         Location var2 = this.getPartLocation();
         Location var3 = this.Hm.getBaseLocation();
         var1.setTranslation((float)(var2.getX() - var3.getX()), (float)(var2.getY() - var3.getY()), (float)(var2.getZ() - var3.getZ()));
      }

      return var1;
   }

   public void applyPhysicsTransform(double var1, double var3, double var5, Matrix4f var7) {
      if (this.HC) {
         this.HP.set(var7);
         DisplayEntity var8 = (DisplayEntity)this.Hn;
         if (!this.Hm.hasRootDisplay()) {
            var8.method2998(var1, var3, var5);
         }

         var8.method3060(var7, 1);
         this.sendMetadataPackets();
         this.sendEquipmentPackets();
      }
   }

   public ModelPartDisplay(CustomEntityImpl var1, CustomEntityDefinition var2, ModelInstance var3) {
      this.Hm = var1;
      this.Ho = var2;
      this.HM = var2.field115;
      this.HN = var3;
      this.Hs = var1.getBodyYaw();
      this.Hr = this.Hs;
      this.Hu = this.Hs;
      if (var2.field115 && var2.An != BonePurpose.HAT_PIVOT) {
         this.HO = EquipmentSlot.HAND;
      } else {
         this.HO = var2.An != BonePurpose.LEFT_HAND_PIVOT && var2.An != BonePurpose.RIGHT_HAND_PIVOT ? EquipmentSlot.HEAD : EquipmentSlot.HAND;
      }

      this.Hx = new EulerRotation(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR, 0.0, 0.0, 0.0);
      this.Hy = new EulerRotation(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR, 0.0, 0.0, 0.0);
      var1.applyStateSnapshot();
      Location var4 = var1.getLocation().clone();
      Location var5 = var4.clone().add(var2.getOffset().toBukkitVector());
      double[] var6 = new double[]{var5.getX() + (this.HM ? 0.3F : 0.0), var5.getY(), var5.getZ()};
      if (var2.An.nN()) {
         this.computePartPosition(var4, var6, var1.iH(), var1.getBodyYaw());
      } else {
         this.computePartPosition(var4, var6, 0.0, var1.iG());
      }

      var5.setX(var6[0]);
      var5.setY(var6[1]);
      var5.setZ(var6[2]);
      if (var1.qK.vn.AM) {
         var5.add(0.0, -0.03, 0.0);
      }

      this.Hn = var1.qK.vn.AN
         ? (
            !var1.qK.vn.AM || !CustomEntityDefinition.aW(var2.getId()) && !var1.qK.vn.ba(var2.getId())
               ? (
                  var2.nJ()
                     ? new AnimatedDisplayEntity(var4, var1.qK.vn, var2, var1)
                     : new DisplayEntity(var1.qK.vn.AO ? var5 : var4, var1.qK.vn.AO, var1.qK.vn, var1)
               )
               : new CachedDisplayEntity(var4, var1.qK.vn, var1)
         )
         : AbstractFakeEntity.create(var5, this.HO, var2.field115, var1.qQ, var1.qR);
      if (this.Hn == null) {
         System.err.println("Failed to get constructor for the client entity.");
      } else {
         if (this.Hn instanceof DisplayEntity var7 && !var1.qK.vn.AO) {
            if (var2.An == BonePurpose.LEFT_HAND_PIVOT) {
               var7.method3059((byte)1);
            } else if (var2.An == BonePurpose.RIGHT_HAND_PIVOT) {
               var7.method3059((byte)2);
            } else if (var2.An == BonePurpose.HAT_PIVOT) {
               var7.method3059((byte)5);
            }
         }

         if (!(this.Hn instanceof DisplayEntity) || var1.qK.vn.AO) {
            this.Hn.method2999(var5.getX(), var5.getY(), var5.getZ(), true);
         }

         this.Hq = this.Hn.Ns();
         if (var1.hasBukkitEntity() && (!(this.Hn instanceof DisplayEntity) || var1.qK.vn.AO)) {
            if (var2.An.nN()) {
               this.Hs = var1.iG();
               this.Ht = var1.iH();
            } else {
               this.Hs = var1.getBodyYaw();
               this.Ht = 0.0;
            }

            this.Hr = var1.getBodyYaw();
            if (var1.qK.vn.Bn == InterpolationMode.PRECISE) {
               this.setRotationAngles(this.Ht, this.Hs);
               Rotation3D var10 = var2.getRotation().compose(this.Hx);
               double[] var13 = var10.getAnglesInDegrees(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR);
               this.setOffset(var13[0], var13[1], var13[2]);
               this.Hy.assignFromCopy(var10);
            } else {
               this.setRotationAngles(this.Ht, 0.0);
               Rotation3D var9 = var2.getRotation().compose(this.Hx);
               double[] var12 = var9.getAnglesInDegrees(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR);
               this.setOffset(var12[0], 0.0, var12[2]);
               if (!var2.An.nN() && !var2.nL()) {
                  this.Hn.method2994((float)Math.toDegrees(this.Hu));
               } else {
                  this.Hn.method2994((float)Math.toDegrees(this.Hs));
               }

               this.Hy.assignFromCopy(var9);
            }

            this.Hw = this.Ht;
            this.Hv = this.Hs;
         }

         this.Hn.Nz();
         this.Hn.eS(var1.getPlayerSkin());
         this.Hz = var2.nz();
         var3.method8929(this);
         if (var2.zZ != null) {
            Matrix4fc var11 = var3.bq(var2.getId());
            this.applyMatrixInternal(var4, var11 == null ? var2.zZ : var11, false);
         }

         if (Settings.get().logBonesSpawning) {
            Log.log("Spawning bone: " + var2.getNamespacedID());
         }

         if (var2.An != BonePurpose.LOCATOR_PIVOT) {
            this.sendSpawnPackets();
         }

         if (var2.An == BonePurpose.MOUNT_PIVOT && !var1.ha()) {
            this.Hp = new ModelRiderController(this, var1.rP.isEmpty());
            var1.rP.add(this.Hp);
         }

         this.sendUpdatePackets();
      }
   }

   public void spawnFor(EntityViewer var1) {
      this.updateFor(var1, true);
   }

   public void updateFor(EntityViewer var1, boolean var2) {
      if (var1.kW() && this.HN.qF() && this.Hn instanceof DisplayEntity var3) {
         var3.method3064(this.HP);
         var3.method3060(this.HP, 0);
         var1.lb();
         var1.le();
      }

      if (var2) {
         this.despawnForViewer(var1);
      }

      this.spawnForViewer(var1);
      this.updateForViewer(var1);
      if (this.Ho.An == BonePurpose.MOUNT_PIVOT && this.Hp != null) {
         this.Hp.sendPassengersUpdate();
      }
   }

   public void setItemModel(String var1, boolean var2) {
      this.Hn.eS(var1);
      this.refreshDisplayEntity(var2);
   }

   public void refreshDisplayEntity(boolean var1) {
      if (this.Ho.Ar) {
         this.Hn.Nr();
         if (var1) {
            this.sendUpdatePackets();
         }
      }
   }

   public void setAnimationFrame(int var1) {
      if (this.Ho.Ar) {
         if (this.Hn instanceof AnimatedDisplayEntity var2) {
            var2.method3132(var1);
         }

         int var4 = this.Ho.method8742(var1);
         if (this.Hz != var4 || this.Hn instanceof AnimatedDisplayEntity) {
            this.Hz = var4;
            if (this.HA) {
               this.Hn.addTickPackets(this.Hm.getViewers(), this.Hz);
            } else {
               this.sendUpdatePackets();
            }
         }
      }
   }

   public void setAnimationIndices(int[] var1) {
      if (this.Ho.Ar && this.Hn instanceof AnimatedDisplayEntity var2 && var2.method3133(var1)) {
         if (this.HA) {
            this.Hn.addTickPackets(this.Hm.getViewers(), this.Hz);
         } else {
            this.sendUpdatePackets();
         }
      }
   }

   void updateAnimatedIndices(int[] var1) {
      if (this.Hn instanceof AnimatedDisplayEntity var2) {
         var2.method3133(var1);
      }
   }

   private double[] getCachedPosition() {
      if (this.Hq == null) {
         this.Hq = this.Hn.Ns();
      }

      return this.Hq;
   }

   public void applyTransform(Location var1, ModelPartTransform var2) {
      if (var2.Da != null) {
         this.applyMatrixInternal(var1, var2.Da, true);
      } else {
         if (this.Ho.Ar) {
            int var3 = this.Ho.method8735(var2.Dh);
            if ((this.Hz != -1 || !var2.Dg.isZero()) && this.Hz != var3) {
               this.Hz = var3;
               if (!this.HM && var2.Dg.isZero()) {
                  this.Hz = -1;
               }

               this.sendUpdatePackets();
            }

            if (!this.HK && System.currentTimeMillis() - this.HJ > (this.HM ? 0 : 40)) {
               this.HK = true;
               this.Hn.Nz();
               if (!this.Hm.sY) {
                  this.Hm.sY = true;
                  this.Hm.scheduleEquipmentSync();
               }
            }
         }

         this.applyOffsetVector(var1, var2.Dd);
         this.updateRotationAngles();
         this.Hq = this.Hn.Ns();
         if (!this.Hm.jE() && this.Hm.sZ && !this.Hm.qK.vn.AM) {
            if (this.Hm.hS()) {
               this.Hq[1] = this.Hq[1] + 0.0;
               this.Hq[2] = this.Hq[2] - 0.3;
            } else if (this.Hm.hT()) {
               this.Hq[1] = this.Hq[1] + 1.3;
               this.Hq[2] = this.Hq[2] - 0.05;
            } else {
               this.Hq[1] = this.Hq[1] + 1.2;
            }
         }

         if (this.Hm.qK.vn.AM) {
            this.Hq[1] = this.Hq[1] - 0.03;
         }

         if (this.Ho.Aq && !var2.Dg.isZero()) {
            this.applyRotation(this.Hs, this.Ht, var2.Df, true);
         }

         if (this.Ho.Az != null) {
            ModelPartDisplay var5 = this.Hm.getModelPart(this.Ho.Az.getId());
            if (var5 != null && var5.Ho.An.nN() && var5.Hx != null) {
               MathUtils.rotateXThenY(this.Hq, var1.getX(), 0.0, var1.getZ(), 0.0, this.Hm.getBodyYaw());
               double[] var4 = var5.getCachedPosition();
               MathUtils.rotateXThenY(this.Hq, var4[0] + (this.HM ? 0.3F : 0.0), var4[1], var4[2], var5.Ht, var5.Hs);
               this.Hs = var5.Hs;
               this.Ht = var5.Ht;
               this.setRotationAngles(this.Ht, this.Hs);
            }
         }

         this.computePartPosition(var1, this.Hq, this.Ht, this.Hs);
         if (this.Ho.An == BonePurpose.MOUNT_PIVOT) {
            this.Hq[1] = this.Hq[1] - 0.562;
         }

         this.teleportToPosition(this.Hq);
         if (this.Ho.An == BonePurpose.MOUNT_PIVOT && this.Hp != null && this.Hp.isMounted()) {
            this.Hp.snapshotSeatPosition();
         }

         this.sendEquipmentPackets();
         this.Hv = this.Hs;
         this.Hw = this.Ht;
      }
   }

   public void applyMatrix(Location var1, Matrix4fc var2) {
      this.applyMatrixInternal(var1, var2, true);
   }

   public void applyMatrixInterpolated(Location var1, Matrix4fc var2, Matrix4fc var3, int var4) {
      this.applyMatrixFull(var1, var2, true, var4, var3);
   }

   public boolean isDisplayReady() {
      return this.Hn instanceof DisplayEntity var1 && var1.NI();
   }

   boolean isTransformRequired() {
      return (this.Ho.An == BonePurpose.LEFT_HAND_PIVOT || this.Ho.An == BonePurpose.RIGHT_HAND_PIVOT || this.Ho.An == BonePurpose.HAT_PIVOT)
            && Boolean.TRUE.equals(this.HI)
         ? true
         : (this.Ho.An.nN() || this.Ho.Az != null)
            && this.isBoneRotationEnabled()
            && (this.Hm.iH() != 0.0F || Location.normalizeYaw((float)Math.toDegrees(this.Hm.iG() - this.Hm.getBodyYaw())) != 0.0F);
   }

   public void applyMatrixImmediate(Location var1, Matrix4fc var2) {
      this.applyMatrixFull(var1, var2, true, 0, null);
   }

   boolean isFastPathEligible() {
      return this.HK && this.isStaticPathEligible();
   }

   boolean isStaticPathEligible() {
      return this.Hn instanceof DisplayEntity
         && this.HD
         && this.HC
         && this.Hp == null
         && this.Ho.An != BonePurpose.MOUNT_PIVOT
         && !this.Ho.An.nN()
         && this.Ho.Az == null
         && this.isBoneTransformEnabled()
         && !Boolean.TRUE.equals(this.HI)
         && !this.Hm.hasModelPart(this.Ho.getId())
         && this.Hm.rq == null
         && !this.Hm.isDying()
         && this.Hm.getDeathFadeAngle() == 0.0F;
   }

   void applyMatrixUnforced(Location var1, Matrix4fc var2) {
      this.applyMatrixFull(var1, var2, false, 0, null);
      if (this.Hp != null && this.Hp.isMounted()) {
         this.Hp.snapshotSeatPosition();
      }
   }

   void applyBoneMatrixView(Location var1, BoneMatrixView var2, int var3) {
      ((DisplayEntity)this.Hn).method3062(var2, var3);
      if (!this.Hm.hasRootDisplay()) {
         this.Hn.method2998(var1.getX(), var1.getY(), var1.getZ());
      }
   }

   void attachBoneMatrixView(BoneMatrixView var1, int var2) {
      ((DisplayEntity)this.Hn).method3062(var1, var2);
      this.sendMetadataPackets();
      this.sendEquipmentPackets();
   }

   private void applyMatrixInternal(Location var1, Matrix4fc var2, boolean var3) {
      this.applyMatrixFull(var1, var2, var3, -1, null);
   }

   private void applyMatrixFull(Location var1, Matrix4fc var2, boolean var3, int var4, @Nullable Matrix4fc var5) {
      boolean var6 = !TransformDecomposition.method8657(var2);
      if (!var3) {
         this.HD = var6;
         this.HC = var6 && !this.Hm.hasModelPart(this.Ho.getId()) && (this.HE == null ? this.HB : this.HE);
      } else if (this.HD != var6) {
         this.HD = var6;
         if (!var6) {
            this.updateVisibility();
         }
      }

      if (var3) {
         this.updateVisibility();
      }

      if (var6) {
         DisplayEntity var7 = (DisplayEntity)this.Hn;
         this.updateRotationAngles();
         float var8 = (float)this.Hr;
         this.HP.rotationY(-var8);
         this.HP.rotateZ(this.Hm.getDeathFadeAngle());
         CustomEntityDefinition var9 = this.Ho.An == BonePurpose.HAT_PIVOT && this.Ho.Az != null && this.Hm.qK.vn.AN && !this.Hm.qK.vn.AO
            ? this.Ho.Az
            : (this.Ho.An.nN() ? this.Ho : this.Ho.Az);
         if (var9 != null && this.isBoneRotationEnabled()) {
            Matrix4fc var10 = this.HN.bq(var9.getId());
            if (var10 == null) {
               var10 = var9.zZ;
               ModelBinding var11 = this.HN.EU == null ? null : this.HN.EU.getBindingById(var9.getId());
               if (var11 != null) {
                  ModelPartTransform var12 = var11.getPart(Math.min(Math.max(0, this.HN.Fa), var11.getPartCount() - 1));
                  if (var12 != null && var12.Da != null) {
                     var10 = var12.Da;
                  }
               }
            }

            float var15 = (float)Math.toRadians(Location.normalizeYaw((float)Math.toDegrees(this.Hm.iG() - var8)));
            float var17 = (float)Math.toRadians(this.Hm.qK.vn.Bm);
            float var13 = (float)Math.toRadians(this.Hm.qK.vn.Bl);
            float var14 = Math.max(-var13, Math.min(var13, this.Hm.iH()));
            var15 = Math.max(-var17, Math.min(var17, var15));
            this.HP.translate(var10.m30(), var10.m31(), var10.m32()).rotateY(-var15).rotateX(var14).translate(-var10.m30(), -var10.m31(), -var10.m32());
         }

         if (var5 != null) {
            this.HQ.set(this.HP).mul(var5);
         }

         this.HP.mul(var2);
         if (this.Hm.rq != null) {
            this.Hm.rq.setTransform(this.HP);
         }

         if (this.Ho.An == BonePurpose.MOUNT_PIVOT) {
            var7.method2998(var1.getX() + this.HP.m30(), var1.getY() + this.HP.m31(), var1.getZ() + this.HP.m32());
            this.HP.setTranslation(0.0F, 0.0F, 0.0F);
         } else if (!this.Hm.hasRootDisplay()) {
            var7.method2998(var1.getX(), var1.getY(), var1.getZ());
         }

         if (var5 == var2) {
            this.HQ.set(this.HP);
         }

         if (var4 >= 0) {
            var7.method3061(this.HP);
            if (var4 > 0) {
               var7.method3065(this.HQ, var4);
            }
         } else {
            var7.method3060(this.HP, var3 ? 1 : 0);
         }

         if (var3) {
            if (this.HD != this.HC) {
               this.updateVisibility();
            }

            if (!this.HK) {
               this.HK = true;
               if (!this.Hm.sY) {
                  this.Hm.sY = true;
                  this.Hm.scheduleEquipmentSync();
               }
            }

            this.sendMetadataPackets();
            this.sendEquipmentPackets();
            if (this.Hp != null && this.Hp.isMounted()) {
               this.Hp.snapshotSeatPosition();
            }
         }
      }
   }

   private void computePartPosition(Location var1, double[] var2, double var3, double var5) {
      if (this.Ho.Az == null) {
         if (this.HN.EU != null) {
            if (this.HO != EquipmentSlot.HEAD) {
               if (this.Ho.An.nN()) {
                  MathUtils.rotateXThenY(var2, var1.getX() + 0.3F, var2[1], var1.getZ(), 0.0, this.Hr);
               } else {
                  MathUtils.rotateXThenY(var2, var1.getX() + 0.3F, var2[1], var1.getZ(), var3, var5);
               }
            } else if (this.Ho.An.nN()) {
               MathUtils.rotateXThenY(var2, var1.getX(), var2[1], var1.getZ(), 0.0, this.Hr);
            } else if (!this.Hm.jE() && this.Hm.sZ && !this.Hm.qK.vn.AM) {
               double var7 = var1.getX();
               double var9 = var1.getZ();
               double var11 = var1.getY() - 1.413 - 0.4;
               MathUtils.rotateXThenY(var2, var7, var11, var9, var3, var5);
            } else {
               MathUtils.rotateXThenY(var2, var1.getX(), var2[1], var1.getZ(), var3, var5);
            }
         }
      }
   }

   void updateRotationAngles() {
      if (this.Ho.Az != null) {
         if (this.isBoneTransformEnabled()) {
            this.Hr = this.Hm.getBodyYaw();
         }
      } else if (this.HN.EU != null || this.Hm.getModel().nZ()) {
         if (!this.Hm.isDying()) {
            if (this.Hm.jE() || !this.Hm.sZ || this.Hm.qK.vn.AM || !this.Hm.hS() && !this.Hm.hT() || this.HN.EU != null && this.HN.EU.yM == AnimationState.SWIM
               )
             {
               if (this.Ho.An.nN()) {
                  if (this.isBoneRotationEnabled()) {
                     this.Hs = this.Hm.iG();
                     this.Ht = this.Hm.iH();
                  }
               } else if (this.isBoneTransformEnabled()) {
                  this.Hs = this.Hm.getBodyYaw();
                  this.Ht = 0.0;
               }

               if (this.isBoneTransformEnabled()) {
                  this.Hr = this.Hm.getBodyYaw();
               }
            } else {
               this.Ht = this.Hm.iH() + Math.toRadians(90.0);
               this.Hs = this.Hm.getBodyYaw();
            }
         }
      }
   }

   private boolean isBoneRotationEnabled() {
      return this.HN.EU == null ? this.Hm.getModel().nZ() : this.HN.EU.nh();
   }

   private boolean isBoneTransformEnabled() {
      return this.HN.EU == null ? this.Hm.getModel().nZ() : this.HN.EU.ni();
   }

   void applyRotation(double var1, double var3, Rotation3D var5, boolean var6) {
      if (this.Ho.An.nN() && !this.Hm.getModel().AM) {
         FloatVector3 var7 = this.Ho.getOffset();
         if (var7 != null && (var7.x != 0.0F || var7.y != 0.0F || var7.z != 0.0F)) {
            Location var8 = this.Hm.getLastKnownPosition();
            if (var8 != null) {
               Location var9 = this.Hn.getLocation().clone();
               var9.add(0.0, this.Hm.qK.wg, 0.0);
               var9 = MathUtils.lookAt(var9, var8);
               double var10 = var9.getPitch();
               var3 = Math.toRadians(var10);
            }
         }
      }

      if (this.Hm.qK.vn.Bn == InterpolationMode.PRECISE) {
         if (this.Hx == null || this.Hv != var1 || this.Hw != var3) {
            this.setRotationAngles(var3, var1);
         }

         Rotation3D var13 = var5.compose(this.Hx);
         var13 = Rotation3D.slerp(this.Hy, var13, 0.7);
         double[] var17 = var13.getAnglesInDegrees(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR);
         this.setOffset(var17[0], var17[1], var17[2]);
         this.Hy.assignFromCopy(var13);
      } else {
         if (this.Hx == null || this.Hw != var3) {
            this.setRotationAngles(var3, 0.0);
         }

         Rotation3D var15 = var5.compose(this.Hx);
         var15 = Rotation3D.slerp(this.Hy, var15, 0.5);
         double[] var18 = var15.getAnglesInDegrees(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR);
         this.setOffset(var18[0], 0.0, var18[2]);
         if (!this.Ho.An.nN() && !this.Ho.nL()) {
            double var20 = MathUtils.normalizeAngle(var1 - this.Hu);
            this.Hu = var1;
            double var11 = this.Hu;
            if (Math.abs(var20) > 0.01F) {
               var11 += Math.signum(var20) * 0.3;
            }

            var11 = MathUtils.normalizeAngle(var11);
            this.Hn.method2994((float)Math.toDegrees(var11));
         } else {
            this.Hn.method2994((float)Math.toDegrees(var1));
         }

         this.Hy.assignFromCopy(var15);
      }

      if (var6) {
         this.sendMetadataPackets();
      }
   }

   void updateRotationBase() {
      if (this.Ho.Az == null) {
         if (!this.Hm.isDying()) {
            float var1;
            float var2;
            if (this.Ho.An.nN()) {
               var1 = this.Hm.iG();
               var2 = this.Hm.iH();
            } else {
               var1 = this.Hm.getBodyYaw();
               var2 = 0.0F;
            }

            this.Hx.setAngles(var2, var1, 0.0);
         }
      }
   }

   private void setRotationAngles(double var1, double var3) {
      this.Hx.setAngles(var1, var3, 0.0);
   }

   public void setEquipment(ItemStack var1, EquipmentSlot var2) {
      if (this.Hm.sl.isAttached()) {
         if (this.Ho.An != BonePurpose.LEFT_HAND_PIVOT || var2 == EquipmentSlot.OFF_HAND) {
            if (this.Ho.An != BonePurpose.RIGHT_HAND_PIVOT || var2 == EquipmentSlot.HAND) {
               if (this.Ho.An != BonePurpose.HAT_PIVOT || var2 == EquipmentSlot.HEAD) {
                  this.HI = var1 != null && var1.getType() != Material.AIR;
                  this.Hn.sendEquipment(this.Hm.getBukkitEntity() instanceof Player ? (Player)this.Hm.getBukkitEntity() : null, this.Hm.getViewers(), var1);
                  this.Hn.Nz();
               }
            }
         }
      }
   }

   public void sendNativeValue(NativeValue var1, boolean var2) {
      if (this.Hn instanceof DisplayEntity var3) {
         this.HI = var2;
         var3.method3072(this.Hm.getViewers(), var1);
         var3.Nz();
      }
   }

   public void resolveLinkedPart(HashMap var1) {
      if (this.Ho.An.nN()) {
         for (String var5 : this.Ho.Am) {
            ModelPartDisplay var6 = (ModelPartDisplay)var1.get(var5);
            if (var6 != null && var6.Ho.An.nN()) {
               this.HL = var6;
            }
         }

         if (this.HL == null) {
            this.HL = this;
         }
      }
   }

   public void setOffset(double var1, double var3, double var5) {
      this.Hn.setAngles((float)var1, (float)var3, (float)var5);
   }

   public void applyOffsetVector(Location var1, FloatVector3Base var2) {
      if (this.Hn.aQv != EquipmentSlot.HEAD) {
         this.Hn.method2995(var1.getX() + this.Hm.tl + 0.3F, var1.getY() + this.Hm.tj, var1.getZ() + this.Hm.tk, var2);
      } else {
         this.Hn.method2995(var1.getX() + this.Hm.tl, var1.getY() + this.Hm.tj, var1.getZ() + this.Hm.tk, var2);
      }
   }

   public void teleportToPosition(double[] var1) {
      this.Hn.method2998(var1[0], var1[1], var1[2]);
   }

   public void sendSpawnPackets() {
      if (this.HC && this.Ho.An != BonePurpose.LOCATOR_PIVOT) {
         this.Hn.addSpawnPackets(this.Hm.getViewers());
      }

      this.HJ = System.currentTimeMillis();
   }

   public void spawnForViewer(EntityViewer var1) {
      if (this.HC && this.Ho.An != BonePurpose.LOCATOR_PIVOT) {
         this.Hn.spawnFor(var1);
      }

      this.HJ = System.currentTimeMillis();
   }

   void despawnForViewer(EntityViewer player) {
      if (this.Ho.An != BonePurpose.LOCATOR_PIVOT) {
         this.Hn.despawnFor(player);
      }
   }

   public void sendEquipmentPackets() {
      if (!this.Hm.hasRootDisplay() || this.Ho.An == BonePurpose.MOUNT_PIVOT || !(this.Hn instanceof DisplayEntity)) {
         if (this.HC && this.Ho.An != BonePurpose.LOCATOR_PIVOT) {
            this.Hn.addEquipmentPackets(!this.Hm.jE() && this.Hm.sZ && !this.Hm.jD(), null, this.Hm.getViewers());
         }
      }
   }

   public void sendMetadataPackets() {
      if (this.HC && this.Ho.An != BonePurpose.MOUNT_PIVOT && this.Ho.An != BonePurpose.LOCATOR_PIVOT && (this.HI == null || this.HI)) {
         if (!this.Hm.getModel().AN && this.Hm.getViewers().stream().anyMatch(EntityViewer::kY)) {
            this.Hn.NA();
         }

         this.Hn.addMetadataPackets(this.Hm.getViewers());
      }
   }

   public void updateForViewer(EntityViewer var1) {
      if (this.HC && this.Ho.Ar) {
         if (this.HA) {
            this.Hn.addTickPackets(List.of(var1), this.Hz);
         } else {
            this.Hn.updateFor(var1, this.Hz);
         }
      }
   }

   public void sendUpdatePackets() {
      if (this.HC && this.Ho.Ar) {
         if (this.HA) {
            this.Hn.addTickPackets(this.Hm.getViewers(), this.Hz);
         } else {
            this.Hn.addUpdatePackets(this.Hm.getViewers(), this.Hz);
         }
      }
   }

   public void enableAnimationTicks(List var1) {
      if (!"none".equals(this.Hm.getModel().nT())) {
         if (this.Ho.Ar) {
            this.HA = true;
            this.Hn.method3000(var1, this.Hz);
         }
      }
   }

   public void disableAnimationTicks(List var1) {
      if (this.Ho.Ar) {
         this.HA = false;
         this.Hn.method3001(var1, this.Hz);
      }
   }

   public Location getPartLocation() {
      return this.Hn.Nv();
   }

   @Nullable
   public ModelRiderController getRider() {
      return this.Hp;
   }

   public void setEnchantedWithViewers(List var1, boolean var2) {
      this.HB = var2;
      this.updateVisibility();
   }

   public void setEnchantedFlag(boolean var1) {
      this.HB = var1;
   }

   public void refreshVisibility() {
      this.updateVisibility();
   }

   public void setGlow(@Nullable Boolean var1) {
      if (!Objects.equals(this.HE, var1)) {
         this.HE = var1;
         this.updateVisibility();
      }
   }

   private void updateVisibility() {
      boolean var1 = this.HD && !this.Hm.hasModelPart(this.Ho.getId()) && (this.HE == null ? this.HB : this.HE);
      if (this.HC != var1) {
         this.HC = var1;
         if (var1) {
            this.spawnAll();
         } else {
            this.despawnAll();
         }

         this.Hm.refreshPassengerPackets();
      }
   }

   public boolean isVisibleToViewers() {
      return this.HC;
   }

   public void despawnAll() {
      List var1 = this.Hm.getViewers();
      int var2 = 0;

      for (int var3 = var1.size(); var2 < var3; var2++) {
         EntityViewer var4 = (EntityViewer)var1.get(var2);
         this.Hn.despawnFor(var4);
      }
   }

   public void spawnAll() {
      this.sendSpawnPackets();
      if (this.Ho.zZ != null) {
         this.sendUpdatePackets();
      }
   }

   public void setEnchanted(boolean enchanted) {
      this.HG = enchanted;
      this.Hn.setEnchanted(enchanted);
   }

   public void setEnchantGlintSilently(boolean var1) {
      this.HG = var1;
   }

   public boolean hasEnchantGlint() {
      return this.HG;
   }

   public void refreshEnchantGlint() {
      this.Hn.setEnchanted(this.HG);
      this.sendUpdatePackets();
   }

   public void setColor(int color) {
      this.HF = color;
      this.applyColorToEntity();
   }

   public void setColorSilently(int var1) {
      this.HF = var1;
   }

   public void refreshColor() {
      this.applyColorToEntity();
   }

   public int getColorValue() {
      return this.HF;
   }

   public boolean isEnchanted() {
      return this.HB;
   }

   public void setCustomModelData(@Nullable Integer var1) {
      if (!Objects.equals(this.field118, var1)) {
         this.field118 = var1;
         this.applyColorToEntity();
      }
   }

   void applyCustomModelOverrides(@Nullable Boolean var1, @Nullable Integer var2, int var3) {
      this.HE = var1;
      this.HC = this.HD && !this.Hm.hasModelPart(this.Ho.getId()) && (var1 == null ? this.HB : var1);
      this.field118 = var2;
      this.Hn.setColor(var2 == null ? this.HF : var2);
      if (this.Ho.Ar) {
         if (this.Hn instanceof AnimatedDisplayEntity var4) {
            var4.method3132(var3);
         }

         this.Hz = this.Ho.method8742(var3);
      }
   }

   private void applyColorToEntity() {
      int var1 = this.field118 == null ? this.HF : this.field118;
      if (this.Hn.getColor() != var1) {
         this.Hn.setColor(var1);
         this.sendUpdatePackets();
      }
   }
}
