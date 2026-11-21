package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.anim.AnimationPlayback;
import dev.lone.itemsadder.anim.BlendState;
import dev.lone.itemsadder.anim.BoneSpec;
import dev.lone.itemsadder.anim.EntityAnimationDriver;
import dev.lone.itemsadder.anim.LocomotionSample;
import dev.lone.itemsadder.anim.LocomotionTracker;
import dev.lone.itemsadder.anim.PlaybackCursor;
import dev.lone.itemsadder.anim.PlaybackRequest;
import dev.lone.itemsadder.anim.SkeletonPose;
import dev.lone.itemsadder.anim.TimedSequenceCursor;
import dev.lone.itemsadder.api.AnimationPlaybackOptions;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.particle.EntityParticleEmitter;
import dev.lone.itemsadder.particle.ParticleOffsetEmitter;
import dev.lone.itemsadder.util.IntLookupTables;
import dev.lone.itemsadder.util.IntPair;
import dev.lone.itemsadder.util.PackedSampleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

final class ModelPoseRuntime {
   static final int Jm = 16;
   private final ModelInstance Jn;
   private final CustomEntityImpl Jo;
   private final List Jp;
   private final ModelPartDisplay[] Jq;
   private final TransformScratch Jr = new TransformScratch();
   private final TransformBuilder[] Js;
   private final TransformBuilder[] Jt;
   private final TransformBuilder[] Ju;
   private TransformBuilder[] Jv;
   private TransformBuilder[] Jw;
   private final TransformBuilder[] Jx;
   private final TransformBuilder[] Jy;
   private final TransformBuilder[] Jz;
   private final TransformBuilder[] JA;
   private final TransformBuilder[] JB;
   private final TransformBuilder[] JC;
   private final Matrix4f[] JD;
   private final Matrix4f[] JE;
   private final Matrix4f[] JF;
   private final TransformDecomposition[] JG;
   private final byte[] JH;
   private final boolean[] JI;
   private final boolean[] field122;
   private final Matrix4f JK = new Matrix4f();
   private boolean JL;
   private final Matrix4f JM = new Matrix4f().scaling(-1.0F, 1.0F, -1.0F);
   @Nullable
   private final BlendState JN;
   @Nullable
   private final SkeletonPose JO;
   @Nullable
   final EntityAnimationDriver JP;
   private final TransformBuilder[] JQ = new TransformBuilder[3];
   private final int[] JR = new int[3];
   private final float[] JS = new float[3];
   private final byte[] JT;
   private final byte[] JU;
   private final byte[] JV;
   private final byte[] JW;
   private final int[] JX;
   private final int[] JY;
   private final int[] JZ;
   private final int[] Ka;
   private final int[] Kb;
   private final int[] Kc;
   private final int[] Kd;
   private final int[] Ke;
   private final LinkedHashMap Kf = new LinkedHashMap();
   private final ArrayList Kg = new ArrayList();
   private volatile Set Fr = Set.of();
   private final ArrayList Kh = new ArrayList();
   private final LocomotionTracker Ki = new LocomotionTracker();
   private boolean Kj;
   private float Kk;
   private float Kl;
   private double Fv;
   private double Km = -1.0;
   private final AnimationPlayback[] Kn = new AnimationPlayback[16];
   @Nullable
   private AnimationPlayback Ko;
   private long Kp;
   private int Fc = -1;
   private int Kq;
   private int Kr;
   private boolean Ks = true;
   private float un = 1.0F;
   private boolean Kt;
   @Nullable
   private UUID Ku;
   private double Kv;
   private double Kw;
   private double Kx;
   private double Ky;
   private double Kz;
   private double KA;
   private int KB;
   private boolean KC;
   private float KD;
   private volatile BoneSpec KE;
   private final TimedSequenceCursor[] KF;
   private final long[] KG;
   private final int[] KH;
   private final boolean[] KI;
   private boolean KJ;
   @Nullable
   private BoneMatrixView field123;
   @Nullable
   private Matrix4fc KL;
   private float KM = Float.NaN;
   private long KN;
   private long KO;
   private boolean KP;
   private long pS;
   private long KQ;
   private long KR;
   private long KS;
   private long KT;
   private long KU;
   private final Matrix4f KV = new Matrix4f();
   @Nullable
   private AnimationPlayback KW;
   @Nullable
   private AnimationPlayback KX;
   @Nullable
   private BoneTransformTable KY;
   private boolean KZ;
   private boolean La = true;
   private long Lb;
   private int Lc;
   private boolean Ld;
   private int[] Le;
   private int[] Lf;
   private int[] Lg;
   private int[] Lh;
   private long[] Li;
   private boolean[] Lj;
   private boolean[] Lk;
   private boolean[] Ll;
   private int[] Lm;
   private Matrix4f[][] Ln;
   private TransformBuilder Lo;
   private long Lp = Long.MIN_VALUE;
   private int Lq;
   private double Lr = Double.NaN;
   private float Ls = Float.NaN;
   private double Lt;
   private long Lu;
   private boolean Lv;
   @Nullable
   private BoneTransformTable Lw;
   private int Lx = -1;

   ModelPoseRuntime(ModelInstance var1, @Nullable ModelDefinitionData var2) {
      this.Jn = var1;
      this.Jo = var1.ER;
      Location var3 = this.Jo.getBaseLocation();
      this.Ku = this.Jo.getEntityUuid();
      this.Kv = var3.getX();
      this.Kw = var3.getY();
      this.Kx = var3.getZ();
      this.Kt = true;
      this.Jp = this.Jo.getModel().og();
      this.JN = BlendState.method9063(this.Jo.getModel().oh());
      this.JO = this.Jo.getModel().oi() == null ? null : new SkeletonPose(this.Jo.getModel().oi(), this.Jp);
      this.JP = !this.Jo.ha() && this.Jo.getModel().nW() != null ? new EntityAnimationDriver(this.Jo, this.Jo.getModel().nW(), this.Jp) : null;

      for (ModelDefinitionData var5 : this.Jo.getModel().AK.values()) {
         if (var5.getVariable() != AnimationVariable.NONE) {
            this.Kg.add(var5);
         }
      }

      this.Kg.sort(Comparator.comparingInt(ModelDefinitionData::getPriority));
      int var8 = this.Jp.size();
      this.Jq = new ModelPartDisplay[var8];
      this.KF = new TimedSequenceCursor[var8];
      this.KG = new long[var8];
      this.KH = new int[var8];
      this.KI = new boolean[var8];

      for (int var9 = 0; var9 < var8; var9++) {
         this.KF[var9] = new TimedSequenceCursor();
      }

      this.Js = this.createTransformBuilders(var8);
      this.Jt = this.createTransformBuilders(var8);
      this.Ju = this.createTransformBuilders(var8);
      this.Jv = this.Jt;
      this.Jw = this.Jt;
      this.Jx = this.createTransformBuilders(var8);
      this.Jy = this.createTransformBuilders(var8);
      this.Jz = this.createTransformBuilders(var8);
      this.JA = this.createTransformBuilders(var8);
      this.JB = this.createTransformBuilders(var8);
      this.JC = this.createTransformBuilders(var8);
      this.JD = createMatrixArray(var8);
      this.JE = createMatrixArray(var8);
      this.JF = createMatrixArray(var8);
      this.JG = new TransformDecomposition[var8];
      this.JH = new byte[var8];
      this.JI = new boolean[var8];
      this.field122 = new boolean[var8];
      this.JT = new byte[var8];
      this.JU = new byte[var8];
      this.JV = new byte[var8];
      this.JW = new byte[var8];
      Arrays.fill(this.JW, (byte)-2);
      this.JX = new int[var8];
      this.JY = new int[var8];
      this.JZ = new int[var8];
      this.Ka = new int[var8];
      Arrays.fill(this.Ka, Integer.MIN_VALUE);
      int var10 = this.Jo.getModel().od();
      this.Kb = new int[var10];
      this.Kc = new int[var10];
      this.Kd = new int[var10];
      this.Ke = new int[var10];
      Arrays.fill(this.Ke, -1);

      for (int var6 = 0; var6 < var8; var6++) {
         CustomEntityDefinition var7 = (CustomEntityDefinition)this.Jp.get(var6);
         this.Kj = this.Kj | var7.Ao;
         this.Jq[var6] = this.Jo.getModelPart(var7.getId());
         var7.getDecomposition().applyTo(this.Jt[var6]);
      }

      if (var2 != null) {
         this.Ko = new AnimationPlayback(var1, this.Jp.size(), var2, 0, null, ++this.Kp, null, 0, null, null, null, true);
      }

      this.applyPose(false);
      this.sN();
      this.updateBoneSpec();
   }

   void update(long var1) {
      Location var3 = this.Jo.getBaseLocation();
      UUID var4 = this.Jo.getEntityUuid();
      boolean var5 = !this.Kt || !Objects.equals(var4, this.Ku);
      if (var5) {
         this.Kt = true;
         this.Ky = this.Kz = this.KA = 0.0;
      } else {
         this.Ky = (var3.getX() - this.Kv) / var1;
         this.Kz = (var3.getY() - this.Kw) / var1;
         this.KA = (var3.getZ() - this.Kx) / var1;
      }

      this.Ku = var4;
      this.Kv = var3.getX();
      this.Kw = var3.getY();
      this.Kx = var3.getZ();
      if (this.JN != null) {
         this.JN.method9065((float)Math.toDegrees(this.Jo.getBodyYaw()), var1);
      }

      if (this.JO != null) {
         this.JO.method9101((float)Math.toDegrees(this.Jo.getBodyYaw()), (float)var3.getY(), var1, var5, this.Jo.getBukkitEntity());
      }
   }

   boolean isLocomotionPlaying() {
      return this.Ki.tl() || this.Ko != null && this.Ko.LM.isLocomotionDriven() && this.Ki.tk();
   }

   void resetLocomotion() {
      this.Ki.qJ();
      this.Ks = true;
   }

   boolean tryStopBaseAnimation() {
      if (this.Ko != null && !this.Ko.Mm && this.Ko.LM.isStoppable()) {
         this.Ko.qB();
         this.Ko.EX = null;
         this.Ko.FE = false;
         this.Ko.Mm = true;
         this.Ks = true;
         return true;
      } else {
         return false;
      }
   }

   void refreshDisplays() {
      for (int var1 = 0; var1 < this.Jp.size(); var1++) {
         this.Jq[var1] = this.Jo.getModelPart(((CustomEntityDefinition)this.Jp.get(var1)).getId());
      }

      this.Fc = -1;
      Arrays.fill(this.Ke, -1);
      this.resetPoseCaches();
      if (this.Jo.getModel().AK.isEmpty()) {
         this.applyPose(true);
      }
   }

   void applyPartState(ModelPartDisplay var1) {
      int var2 = var1.Ho.nH();
      if (var2 >= 0 && var2 < this.Jq.length) {
         byte var3 = this.JT[var2];
         int var4 = this.JX[var2];
         var1.applyCustomModelOverrides(var3 < 0 ? null : var3 != 0, var4 < 0 ? null : var4, this.Kq);
         var1.updateAnimatedIndices(this.Kc);
      }
   }

   boolean playAnimation(ModelDefinitionData var1, int var2, @Nullable Runnable var3) {
      return this.playAnimationWithOptions(var1, var2, var3, AnimationPlaybackOptions.DEFAULT);
   }

   void replaceBaseAnimation(ModelDefinitionData var1) {
      this.La = true;
      if (this.Ko != null) {
         this.Ko.qB();
      }

      this.Kf.values().forEach(AnimationPlayback::qB);
      this.Kf.clear();
      this.Fr = Set.of();
      this.sM();
      this.Ko = new AnimationPlayback(this.Jn, this.Jp.size(), var1, 1, null, ++this.Kp, null, 0, null, null, null, true);
      this.Ks = true;
      this.applyPose(false);
      this.sN();
   }

   void refreshPose() {
      this.applyPose(false);
   }

   void setAnimationVariable(int var1, int var2) {
      if (var1 >= 0 && var1 < this.Kb.length && var2 >= 0 && var2 < ((List)this.Jo.getModel().ob().get(this.Jo.getModel().oc().get(var1))).size()) {
         this.Kb[var1] = var2;
         this.invalidateTransformCaches();
         this.resetPoseCaches();
      }
   }

   void finishBaseAnimation(List var1) {
      if (this.Ko != null) {
         if (this.Ko.LM.getMaxPartCount() > 1) {
            this.fireFrameTriggers(this.Ko, this.Jo.getBaseLocation(), var1, true);
         }

         this.Ko.FE = false;
         this.sN();
      }
   }

   boolean playAnimationWithOptions(ModelDefinitionData var1, int var2, @Nullable Runnable var3, AnimationPlaybackOptions var4) {
      if (var1.getVariable() != AnimationVariable.NONE) {
         return false;
      }

      if (!var4.equals(AnimationPlaybackOptions.DEFAULT) && !this.canPlayAnimation(var1)) {
         return false;
      }

      if ("base".equals(var1.getLayer())) {
         this.La = true;
         AnimationPlayback var12 = this.Ko;
         int var13 = resolveBlendMode(var1, var12);
         TransformBuilder[] var14 = var13 == 0 ? null : this.cloneTransformBuilders(this.Jt);
         this.Ko = new AnimationPlayback(
            this.Jn,
            this.Jp.size(),
            var1,
            var2,
            var3,
            ++this.Kp,
            var14,
            var13,
            var13 == 0 ? null : (byte[])this.JU.clone(),
            var13 == 0 ? null : (int[])this.JY.clone(),
            var13 == 0 ? null : this.Kr,
            true
         );
         this.Ko.LY = var13 == 0 ? null : (var12 == null ? this.allocateLayerIndices() : this.method9045(var12));
         if (var12 != null) {
            var12.qB();
         }

         this.Ko.method9068(var4);
         this.Ko.method9070(var14 != null && var12 != null ? var12.LM.getPackedBytes() : null);
         this.Ks = true;
         this.sN();
         return true;
      } else {
         AnimationPlayback var5 = (AnimationPlayback)this.Kf.get(var1.getLayer());
         if (!canAddLayer(this.Kf.size(), var5 != null)) {
            return false;
         }

         int var6 = resolveBlendMode(var1, var5);
         long var7 = ++this.Kp;
         TransformBuilder[] var9 = var6 == 0 ? null : this.method9046(var5, var1.getPriority(), var7, false);
         PackedSampleBuffer var10 = var6 == 0 ? null : this.method9047(var5, var1.getPriority(), var7);
         AnimationPlayback var11 = new AnimationPlayback(
            this.Jn,
            this.Jp.size(),
            var1,
            var2,
            var3,
            var7,
            var9,
            var6,
            var10 == null ? null : var10.bytes(),
            var10 == null ? null : var10.offsets(),
            var10 == null ? null : var10.count(),
            false
         );
         var11.LY = var10 == null ? null : var10.lengths();
         var11.method9068(var4);
         var11.method9070(var9 != null && var5 != null ? var5.LM.getPackedBytes() : null);
         if (var5 != null) {
            var5.qB();
         }

         this.Kf.put(var1.getLayer(), var11);
         this.La = true;
         this.Fr = Set.copyOf(this.Kf.keySet());
         this.sM();
         this.Ks = true;
         return true;
      }
   }

   boolean stopLayer(String var1) {
      AnimationPlayback var2 = (AnimationPlayback)this.Kf.get(var1);
      if (var2 == null) {
         return false;
      }

      if (var2.Mm) {
         return true;
      }

      this.La = true;
      if (var2.LM.getAuxDurationTicks() > 0) {
         var2.Mn = this.method9046(var2, var2.LM.getPriority(), var2.LR, true);
         var2.Mo = new byte[this.Jp.size()];

         for (int var3 = 0; var3 < var2.mW(); var3++) {
            int var4 = var2.method9071(var3);
            if (var4 >= this.Jp.size()) {
               break;
            }

            var2.Mo[var4] = (byte)(var2.LM.getBoneFlag(var4) | var2.ah(var4));
         }
      }

      var2.Fa = var2.Mg;
      var2.Me = var2.Mh;
      var2.FE = false;
      var2.qB();
      var2.EX = null;
      var2.Mm = true;
      this.Ks = true;
      if (var2.LM.getAuxDurationTicks() == 0) {
         this.Kf.remove(var1);
         this.Fr = Set.copyOf(this.Kf.keySet());
         this.sM();
      }

      return true;
   }

   boolean isLayerActive(String var1) {
      return this.Fr.contains(var1);
   }

   boolean isAnimationActive(String var1) {
      if (this.Ko != null && !this.Ko.Mm && this.Ko.LM.yK.equals(var1)) {
         return true;
      }

      for (AnimationPlayback var3 : this.Kh) {
         if (var3.LM.yK.equals(var1) && !var3.td()) {
            return true;
         }
      }

      return false;
   }

   void stopBaseAnimation() {
      this.La = true;
      if (this.Ko != null) {
         this.Ko.qB();
         this.Ko.FE = false;
      }

      this.sN();
   }

   void setLayerPaused(String var1, boolean var2) {
      AnimationPlayback var3 = "base".equals(var1) ? this.Ko : (AnimationPlayback)this.Kf.get(var1);
      if (var3 != null && var3.FE && !var3.Mm && this.canPlayAnimation(var3.LM)) {
         if (var2) {
            var3.qB();
         }

         this.La = this.La | var3.Mk != var2;
         var3.Mk = var2;
      }

      this.sN();
   }

   boolean canPlayAnimation(ModelDefinitionData var1) {
      if (var1.isLocomotionDriven() || var1.getVariable() != AnimationVariable.NONE) {
         return false;
      }

      if (!"base".equals(var1.getLayer())) {
         return true;
      }

      AnimationSetup var2 = this.Jo.getModel().oi();
      return (var2 == null || !"native".equals(var2.oW())) && (this.JN == null || !this.JN.method9064(var1));
   }

   boolean isBaseAnimationPaused() {
      return this.Ko != null && this.Ko.Mk;
   }

   long getAnimationStartTick(ModelDefinitionData var1) {
      return this.Ko != null && this.Ko.LM == var1 ? this.Ko.LN : -1L;
   }

   boolean isBaseAnimationFinished() {
      return this.Ko != null && (this.Ko.Ml == null ? this.Ko.pB >= this.Ko.LM.yO : this.Ko.Ml.QS);
   }

   BoneSpec getBoneSpec() {
      return this.KE;
   }

   PlaybackRequest getPlaybackRequest() {
      AnimationPlayback var1 = this.Ko;
      return new PlaybackRequest(
         var1 == null ? "" : var1.LM.yK, this.Lt, this.Lt / (var1 == null ? 20 : var1.LM.getFrameRate()), this.Lu, this.Lv, var1 == null ? 1.0F : var1.sV()
      );
   }

   private void updateBoneSpec() {
      LocomotionSample var1 = this.Ki.ti();
      this.KE = new BoneSpec(
         this.Ko == null ? "" : this.Ko.LM.yK,
         this.Fr,
         var1.rc(),
         var1.rd(),
         var1.re(),
         this.getPlaybackTime(),
         var1.rg(),
         var1.rh(),
         var1.ri(),
         var1.tp().name()
      );
   }

   void refreshBasePlayback(boolean var1, boolean var2) {
      this.La = true;
      if (this.Ko != null) {
         if (var2) {
            this.Ko.qB();
            this.Ko.FE = false;
         }

         if (var1) {
            this.Ko.tc();
         }

         this.sN();
      }
   }

   void tickAnimations(boolean var1, List var2) {
      this.pS++;
      this.syncEntityState();
      if (this.JN != null) {
         this.Ks = this.Ks | this.JN.aq(this.Ko != null && this.JN.method9064(this.Ko.LM));
      }

      boolean var3 = this.Ko != null && this.Ko.LM.yM == AnimationState.FLY;
      boolean var4 = this.Ko != null && this.Ko.LM.yM == AnimationState.SWIM;
      if (this.JO != null) {
         this.Ks = this.Ks | this.JO.aq(var3);
      }

      LocomotionSample var5 = this.Ki.ti();
      LocomotionSample var6 = this.Ki.method9078(this.Ky, this.Kz, this.KA, var3, this.Jo.isAirborneAttached() && (!this.Jo.qK.vL || !this.Jo.jC()), var4);
      if (this.Kj) {
         float var7 = Math.min(1.0F, (float)this.Ki.tj() * 32.0F);
         float var8 = this.Kk + (var7 - this.Kk) * 0.4F;
         if (Math.abs(var8 - var7) < 1.0E-4F) {
            var8 = var7;
         }

         this.Ks = this.Ks | (var8 != this.Kk && !this.Kh.isEmpty());
         this.Kk = var8;
      }

      double var15 = this.Jo.getBodyYaw();
      boolean var9 = !var3 && -Math.sin(var15) * var6.tq() + Math.cos(var15) * var6.ts() < -1.0E-6;
      if (this.Ko != null && this.Ko.LM.isLocomotionDriven()) {
         this.Km = this.Ko.LM.getLength();
      } else {
         this.Km = -1.0;
      }

      if (this.Km > 0.0) {
         this.Fv = this.Fv + (var9 ? -1 : 1) * (var6.rc() / this.Km + (this.Ki.tl() ? Math.max(0.0, var6.to() - var6.rg()) * 0.6662 / (Math.PI * 2) : 0.0));
      }

      if (this.Ko != null && this.Ko.LM.isLocomotionDriven()) {
         this.Ko.method9069(this.Ko.LM.wrapTime(this.getPlaybackTime()));
         long var10 = (long)Math.floor(this.getPlaybackTime());
         if (this.Ko.LP && this.Ko.LO != var10) {
            this.Ko.LN++;
         }

         this.Ko.LO = var10;
         this.Ko.LP = true;
         this.Ks = this.Ks | (var5.re() != var6.re() || var5.rh() != var6.rh());
      }

      if (this.Ko != null) {
         this.Ks = this.Ks | this.Ko.tb();
      }

      for (int var16 = 0; var16 < this.Kh.size(); var16++) {
         this.Ks = this.Ks | ((AnimationPlayback)this.Kh.get(var16)).tb();
      }

      if (var1) {
         this.applyPose(true);
      }

      Location var17 = this.Jo.getBaseLocation();
      this.fireTriggers(this.Ko, var17, var2);

      for (int var11 = 0; var11 < this.Kh.size(); var11++) {
         this.fireTriggers((AnimationPlayback)this.Kh.get(var11), var17, var2);
      }

      if (this.Ko != null) {
         boolean var18 = this.Ko.FE;
         long var12 = this.Ko.LN;
         if (this.Ko.LM.isLocomotionDriven()) {
            this.Ko.pB++;
         } else {
            this.Ks = this.Ks | this.Ko.method9074(false, this.Jn.Fk, var9);
         }

         if (this.Ko.LN != var12 && this.Jn.method8905(this.Ko.LM)) {
            if (this.Ko.Ml != null) {
               this.Ko.Ml.method9135((var3x, var5x, var7x, var8x) -> this.fireTriggersInRange(this.Ko, var17, var2, var3x, var5x, var7x, var8x));
            } else {
               int var14 = this.Ko.ta();
               if (var14 >= 0) {
                  this.fireFrameEmitters(this.Ko, var14, var17, var2);
               }
            }
         }

         this.La = this.La | (var18 && !this.Ko.FE);
      }

      int var19 = this.Kh.size();

      for (int var20 = 0; var20 < var19; var20++) {
         this.Kn[var20] = (AnimationPlayback)this.Kh.get(var20);
      }

      for (int var21 = 0; var21 < var19; var21++) {
         boolean var13 = this.Kn[var21].FE;
         this.Ks = this.Ks | this.Kn[var21].method9073(true, false);
         this.La = this.La | (var13 && !this.Kn[var21].FE);
         this.Kn[var21] = null;
      }

      boolean var22 = this.Kf.entrySet().removeIf(var0 -> {
         if (!((AnimationPlayback)var0.getValue()).td()) {
            return false;
         }

         ((AnimationPlayback)var0.getValue()).qB();
         return true;
      });
      if (var22) {
         this.La = true;
         this.Fr = Set.copyOf(this.Kf.keySet());
         this.sM();
         this.Ks = true;
      }

      if (this.Ko != null && this.Ko.td()) {
         this.La = true;
         this.Ko.qB();
         this.Ko = null;
         this.Ks = true;
      }

      this.sN();
      this.updateBoneSpec();
   }

   void forceRefresh() {
      this.Jo.applyStateSnapshot();
      this.applyPose(true);
      this.sN();
   }

   private void fireTriggers(@Nullable AnimationPlayback var1, Location var2, List var3) {
      this.fireFrameTriggers(var1, var2, var3, false);
   }

   private void fireFrameTriggers(@Nullable AnimationPlayback var1, Location var2, List var3, boolean var4) {
      if (var1 != null && (var4 || var1.sZ())) {
         if (var1.Ml != null) {
            if (!var1.LM.getTriggers().isEmpty()) {
               var1.Ml.method9134((var4x, var6, var8, var9) -> this.fireTriggersInRange(var1, var2, var3, var4x, var6, var8, var9));
            } else {
               var1.Ml.method9134(PlaybackCursor.QT);
            }
         } else {
            int var5 = var1.ta();
            if (var5 >= 0) {
               this.fireFrameEmitters(var1, var5, var2, var3);
            }

            this.fireFrameEmitters(var1, var1.Fa, var2, var3);

            for (TriggerRecord var7 : var1.LM.getTriggers()) {
               if (var7.tag() != null && (int)Math.ceil(var7.time() * var1.LM.getFrameRate() - 1.0E-9) == var1.Fa) {
                  this.fireTrigger(var1, var7, var2, var3);
               }
            }
         }
      }
   }

   private void fireTriggersInRange(AnimationPlayback var1, Location var2, List var3, double var4, double var6, boolean var8, boolean var9) {
      List var10 = var1.LM.getTriggers();
      int var11 = var6 < var4 ? -1 : 1;
      int var12 = var1.LM.findTriggerIndex(Math.min(var4, var6) - 1.0E-9);
      int var13 = var1.LM.findTriggerIndex(Math.max(var4, var6) + 2.0E-9);

      for (int var14 = var11 == 1 ? var12 : var13 - 1; var14 >= var12 && var14 < var13; var14 += var11) {
         TriggerRecord var15 = (TriggerRecord)var10.get(var14);
         if (PlaybackCursor.method9137(var15.time(), var4, var6, var8, var9)) {
            this.fireTrigger(var1, var15, var2, var3);
         }
      }
   }

   private void fireTrigger(AnimationPlayback var1, TriggerRecord var2, Location var3, List var4) {
      if (var2.tag() != null) {
         var4.add(() -> CustomEntity.internal_fireAnimationSignal(this.Jo, var1.LM.yK, var1.LM.getLayer(), var1.LR, var2.tag()));
      } else if (var2.emitter() instanceof ParticleOffsetEmitter var5) {
         UUID var14 = this.Jo.getEntityUuid();
         double var8 = var3.getX();
         double var10 = var3.getY();
         double var12 = var3.getZ();
         var4.add(() -> {
            World var8x = var14 == null ? null : Bukkit.getWorld(var14);
            if (var8x != null) {
               var5.method8878(new Location(var8x, var8, var10, var12));
            }
         });
      } else if (var2.emitter() instanceof EntityParticleEmitter var6) {
         if (this.Jo.isDormant()) {
            return;
         }

         ModelPartDisplay var16 = this.Jo.getModelPart(var6.Dt.getId());
         if (var16 == null) {
            return;
         }

         this.emitFromPartDisplay(var1, var6, var16, var4);
      }
   }

   private void fireFrameEmitters(AnimationPlayback var1, int var2, Location var3, List var4) {
      if (var1.LM.zw != null && var2 < var1.LM.zw.size()) {
         for (ParticleOffsetEmitter var6 : (List)var1.LM.zw.get(var2)) {
            UUID var7 = this.Jo.getEntityUuid();
            double var8 = var3.getX();
            double var10 = var3.getY();
            double var12 = var3.getZ();
            var4.add(() -> {
               World var8x = var7 == null ? null : Bukkit.getWorld(var7);
               if (var8x != null) {
                  var6.method8878(new Location(var8x, var8, var10, var12));
               }
            });
         }
      }

      if (var1.LM.zx != null && var2 < var1.LM.zx.size()) {
         if (!this.Jo.isDormant()) {
            for (EntityParticleEmitter var15 : (List)var1.LM.zx.get(var2)) {
               ModelPartDisplay var16 = this.Jo.getModelPart(var15.Dt.getId());
               if (var16 != null) {
                  this.emitFromPartDisplay(var1, var15, var16, var4);
               }
            }
         }
      }
   }

   private void emitFromPartDisplay(AnimationPlayback var1, EntityParticleEmitter var2, ModelPartDisplay var3, List var4) {
      TrackedDisplayEntity var5 = null;
      if (var2.Dx) {
         LivingEntity var6 = this.Jo.getRootBukkitEntity();
         if (var6 == null) {
            throw new IllegalStateException("Following particle locator has no scheduler owner");
         }

         var5 = new TrackedDisplayEntity(var6);
         TrackedDisplayRef var7 = new TrackedDisplayRef(var3, var5);
         var7.sP();
         var1.LQ.add(var7);
      }

      Matrix4f var8 = var3.getScaledPartMatrix(new Matrix4f());
      Location var9 = this.Jo.getBaseLocation().clone().add(var8.m30(), var8.m31(), var8.m32());
      var2.method8883(var1.qA(), var9, this.Jo.getEntityUuid(), this.Jo.getViewers().stream().map(var0 -> var0.li).toList(), var4, var5);
   }

   private void pruneTrackedDisplays(@Nullable AnimationPlayback var1) {
      if (var1 != null) {
         var1.LQ.removeIf(var0 -> var0.LK.isActive());
         var1.LQ.forEach(TrackedDisplayRef::sP);
      }
   }

   void clearAnimations() {
      if (this.Ko != null) {
         this.Ko.qB();
      }

      this.Kf.values().forEach(AnimationPlayback::qB);
      this.Kf.clear();
      this.Fr = Set.of();
      this.Kh.clear();
      this.Ko = null;

      for (int var1 = 0; var1 < this.Jq.length; var1++) {
         if (this.Jq[var1] != null) {
            this.Jq[var1].setGlow(null);
            this.Jq[var1].setCustomModelData(null);
         }
      }
   }

   @Nullable
   Matrix4fc getBoneTransform(String var1) {
      CustomEntityDefinition var2 = (CustomEntityDefinition)this.Jo.getModel().AL.get(var1);
      if (var2 != null && var2.nH() >= 0) {
         int var3 = var2.nH();
         return (Matrix4fc)(this.KI != null && this.KI[var3] ? this.field123.getMatrix(var3) : this.JF[var3]);
      } else {
         return null;
      }
   }

   boolean isPoseChanged() {
      return this.KP;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void applyPose(boolean var1) {
      boolean var2 = var1 && this.Jo.isDormant();
      boolean var3 = var1 && this.KP && !var2;
      long var4 = var3 ? System.nanoTime() : 0L;
      if (var3) {
         this.flushAllBoneMatrixViews();
         this.Ks = true;
         this.Lw = null;
         this.KW = null;
         this.La = true;

         for (TimedSequenceCursor var9 : this.KF) {
            var9.reset();
         }

         for (EntityViewer var42 : this.Jo.getViewers()) {
            var42.lb();
            var42.le();
            var42.lc();
         }

         this.KT++;
      }

      if (var1) {
         this.KP = var2;
      }

      if (var2) {
         this.KQ++;
      }

      boolean var43;
      boolean var44;
      boolean var45;
      boolean var10000;
      label831: {
         label830: {
            float var41 = this.Jo.getYawOffset();
            var43 = this.un != var41;
            var44 = this.Ks || var43;
            this.un = var41;
            var45 = this.JP != null && this.JP.tX();
            if (this.Kh.isEmpty() && this.KD == 0.0F) {
               if (this.Ko == null) {
                  break label830;
               }

               switch (IntLookupTables.SWITCH_ANIMATION_STATE[this.Ko.LM.yM.ordinal()]) {
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                     break label830;
               }
            }

            var10000 = false;
            break label831;
         }

         var10000 = true;
      }

      boolean var10 = var10000;
      boolean var11 = this.JP != null && (var45 && var10 || this.JP.tW());
      if (var44) {
         this.invalidateTransformCaches();
         this.resetPoseCaches();
      }

      List var12 = var1 && !var2 ? this.Jo.getViewers() : List.of();
      boolean var13 = this.hasAutoRotation();
      boolean var14 = var1 && !var2 && !var13 && !var11 && this.Ko != null && this.Ko.LS == null && this.getActiveBoneTable() != null;
      this.KJ = (var14 || var2 && !var11) && this.un == 1.0F;
      if (var14) {
         this.updateSampleCursors(var12);
      }

      if (!var1 || var11) {
         this.flushAllBoneMatrixViews();
      }

      if (var44) {
         if (!this.applyBoneTable(var43)) {
            this.flushAllBoneMatrixViews();
            this.Lw = null;
            this.rebuildTransforms();
            this.updateWorldMatrices(var43);
         }

         this.Ks = false;
      }

      if (this.JP != null) {
         this.JP.method9118(this.JE, this.JF, this.JI, var44, var10, var45);
      }

      Location var15 = this.Jo.getBaseLocation();
      if (var2) {
         for (int var16 = 0; var16 < this.Jq.length; var16++) {
            if (this.Jq[var16] != null) {
               if (this.KI[var16]) {
                  this.Jq[var16].applyBoneMatrixView(var15, this.field123, var16);
                  this.KS++;
               } else {
                  this.Jq[var16].applyMatrixUnforced(var15, this.JF[var16]);
               }
            }
         }
      } else if (var1) {
         var12.forEach(EntityViewer::kS);
         boolean var46 = false;

         try {
            int var17 = this.computeUpdateInterval(var12);
            if (var17 == 0 && !var14) {
               this.updateSampleCursors(var12);
            }

            for (int var18 = 0; var18 < this.Jq.length; var18++) {
               if (this.Jq[var18] != null) {
                  if (var17 == 0) {
                     this.applyPartTransform(var18, var15);
                  } else {
                     this.flushBoneMatrixView(var18);
                     ModelPartDisplay var19 = this.Jq[var18];
                     if (!this.Lj[var18] && var19.isDisplayReady()) {
                        var19.applyMatrixImmediate(var15, this.JF[var18]);
                     } else {
                        var19.applyMatrixInterpolated(var15, this.JF[var18], this.JF[var18], this.Le[var18]);
                     }
                  }
               }
            }

            this.Jo.tickEquipment();
            var46 = true;
         } finally {
            try {
               for (EntityViewer var25 : var12) {
                  var25.method8552(var46);
               }
            } finally {
               for (EntityViewer var28 : var12) {
                  var28.method8552(false);
               }
            }
         }
      }

      if (var3) {
         this.KU = this.KU + (System.nanoTime() - var4);
      }

      if (var1) {
         this.pruneTrackedDisplays(this.Ko);

         for (AnimationPlayback var48 : this.Kh) {
            this.pruneTrackedDisplays(var48);
         }
      }
   }

   @Nullable
   private AnimationPlayback getSoleActivePlayback() {
      if (this.Kh.isEmpty()) {
         return this.Ko;
      }

      if (this.Kh.size() != 1) {
         return null;
      }

      AnimationPlayback var1 = (AnimationPlayback)this.Kh.getFirst();
      if (var1.rp() == 1.0F && !var1.Mm) {
         if (this.Kj && this.Kl > 0.0F && var1.LM.yM == AnimationState.ATTACK) {
            return null;
         }

         for (int var2 = 0; var2 < this.Jp.size(); var2++) {
            if (var1.LM.getMatrixTable(var2) != null) {
               for (CustomEntityDefinition var3 = (CustomEntityDefinition)this.Jp.get(var2); var3 != null; var3 = var3.getParent()) {
                  if (var1.LM.getBoneFlag(var3.nH()) != 7) {
                     return null;
                  }
               }
            }
         }

         return var1;
      } else {
         return null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean isAutoRotationEnabled(@Nullable AnimationPlayback var1) {
      boolean var10000;
      if (var1 == null) {
         var10000 = this.Jo.qK.wf.lL();
      } else {
         switch (IntLookupTables.SWITCH_AUTO_ROTATION_MODE[var1.LM.getAutoRotation().ordinal()]) {
            case 1:
               var10000 = true;
               break;
            case 2:
               var10000 = false;
               break;
            case 3:
               var10000 = this.Jo.qK.wf.lL();
               break;
            default:
               throw new MatchException(null, null);
         }
      }

      return var10000;
   }

   private boolean hasAutoRotation() {
      if (!this.Jo.qK.wf.enabled()) {
         return false;
      }

      if (this.isAutoRotationEnabled(this.Ko)) {
         return true;
      }

      for (AnimationPlayback var2 : this.Kh) {
         if (this.isAutoRotationEnabled(var2)) {
            return true;
         }
      }

      return false;
   }

   private int resolveFrameCount(@Nullable AnimationPlayback var1, int var2) {
      if (!this.isAutoRotationEnabled(var1)) {
         return 1;
      } else {
         return var1 != null && var1.LM.getAutoRotation() != AutoRotationMode.INHERIT ? var1.LM.getFrameCount() : var2;
      }
   }

   private int computeUpdateInterval(List var1) {
      SpawnLimits var2 = this.Jo.qK.wf;
      if (!this.hasAutoRotation()) {
         this.Lc = 0;
         this.La = true;
         return 0;
      }

      for (EntityViewer var4 : var1) {
         if (!var4.kW() || !var4.kX() || !var4.kZ() || var4.li() != 1 || var4.la() || var4.ld()) {
            this.La = true;
            return 0;
         }
      }

      int var12 = this.Ko == null || !this.Kh.isEmpty() || this.Ko.LM.yM != AnimationState.IDLE && this.Ko.LM.yM != AnimationState.IDLE_ANGRY
         ? var2.lM()
         : var2.lN();
      if (this.Le == null) {
         int var13 = this.Jp.size();
         this.Le = new int[var13];
         this.Lf = new int[var13];
         this.Lg = new int[var13];
         this.Lh = new int[var13];
         this.Li = new long[var13];
         this.Lj = new boolean[var13];
         this.Lk = new boolean[var13];
         this.Ll = new boolean[var13];
      }

      Arrays.fill(this.Lh, 8);
      boolean var14 = this.Ko != null && (this.Ko.Mm || this.Ko.LS != null && this.Ko.rp() < 1.0F);

      for (AnimationPlayback var6 : this.Kh) {
         var14 |= var6.Mm || var6.rp() < 1.0F;
      }

      for (int var15 = 0; var15 < this.Jp.size(); var15++) {
         CustomEntityDefinition var17 = ((CustomEntityDefinition)this.Jp.get(var15)).getParent();
         int var7 = var17 == null ? -1 : var17.nH();
         this.Ll[var15] = this.Ko != null && this.Ko.LM.isMultiPartBone(var15) || var7 >= 0 && this.Ll[var7];
         int var8 = this.isAutoRotationEnabled(this.Ko) ? this.resolveFrameCount(this.Ko, var12) : 8;
         if (!this.isAutoRotationEnabled(this.Ko) && this.Ll[var15]) {
            var8 = 1;
         }

         if (var7 >= 0) {
            var8 = Math.min(var8, this.Le[var7]);
         }

         boolean var9 = this.Ko != null && this.Ko.LM.isMultiPartBone(var15);

         for (AnimationPlayback var11 : this.Kh) {
            var9 |= var11.LM.isMultiPartBone(var15);
            if (var11.LM.isBoneAnimated(var15)) {
               var8 = Math.min(var8, this.resolveFrameCount(var11, var12));
            }
         }

         if (var14) {
            var8 = 1;
         }

         this.Le[var15] = var8;
         int var20 = var7 >= 0 && this.Lk[var7] ? this.Lg[var7] : var15;
         this.Lg[var15] = var20;
         this.Lk[var15] = var9 || var7 >= 0 && this.Lk[var7];
         this.Lh[var20] = Math.min(this.Lh[var20], var8);
      }

      for (int var16 = 0; var16 < this.Jp.size(); var16++) {
         int var18 = this.Lh[this.Lg[var16]];
         this.Le[var16] = var18;
         boolean var19 = this.La || var18 != this.Lf[var16] || this.pS >= this.Li[var16];
         this.Lj[var16] = var19;
         if (var19) {
            this.Li[var16] = this.pS + var18;
         }

         this.Lf[var16] = var18;
      }

      this.Ld = this.La || var12 != this.Lc || this.pS >= this.Lb;
      if (this.Ld) {
         this.Lb = this.pS + var12;
         this.La = false;
      }

      this.Lc = var12;
      return var12;
   }

   private boolean canUseCachedBoneTable(List var1) {
      this.KY = null;
      this.KZ = false;
      if (this.Lm != null) {
         Arrays.fill(this.Lm, -1);
      }

      AnimationPlayback var2 = this.KX = this.getSoleActivePlayback();
      double var3 = this.Jo.getBodyYaw();
      boolean var5 = this.KW == var2
         && var2 != null
         && var2.pB >= this.Lp
         && var2.pB <= this.Lp + 1L
         && var2.sT() >= this.Lq
         && var2.sT() <= this.Lq + 1
         && var3 == this.Lr
         && this.un == this.Ls;
      if (!var5) {
         for (int var6 = 0; var6 < this.KF.length; var6++) {
            this.KF[var6].reset();
            this.KG[var6] = (var2 == null ? 0L : var2.pB) + this.Jo.qK.wf.perChunk();
         }
      }

      this.KW = var2;
      this.Lp = var2 == null ? Long.MIN_VALUE : var2.pB;
      this.Lq = var2 == null ? 0 : var2.sT();
      this.Lr = var3;
      this.Ls = this.un;
      if (this.Jo.qK.wf.enabled()
         && var2 != null
         && (var2.FE || var2.Fa == var2.LM.getMaxPartCount() - 1)
         && !var2.Mk
         && !var2.Mm
         && var2.LS == null
         && (var2.Ml == null || var2.Ms && var2.Ml.um())
         && !var2.LM.isLocomotionDriven()
         && (!this.Jn.Fk || var2.LM.yM == AnimationState.DEATH)
         && (this.JP == null || !this.JP.tW())
         && this.JO == null
         && (this.JN == null || this.Ko != null && !this.JN.method9064(this.Ko.LM))
         && this.Jo.rq == null
         && this.Jo.hasRootDisplay()
         && this.Jo.getDeathFadeAngle() == 0.0F
         && this.Jo.getModel().nX() == null
         && !(this.un <= 0.0F)
         && !(this.un > 1.0F)) {
         for (int var9 = 0; var9 < this.Jq.length; var9++) {
            if (this.Jq[var9] != null && (this.Jq[var9].isTransformRequired() || var2.LM.getMatrixTable(var9) != null && !this.Jq[var9].isDisplayReady())) {
               return false;
            }
         }

         for (EntityViewer var7 : var1) {
            if (!var7.kW() || !var7.kX() || !var7.kZ() || var7.li() != 1 || var7.la() || var7.ld()) {
               return false;
            }
         }

         boolean var11 = true;

         for (CustomEntityDefinition var8 : this.Jp) {
            var11 &= var8.An != BonePurpose.MOUNT_PIVOT;
         }

         if (var11 && this.Kg.isEmpty()) {
            if (var2 == this.Ko) {
               this.KY = this.getActiveBoneTable();
               this.KZ = var2.Ml != null;
            } else {
               boolean var13 = true;

               for (int var14 = 0; var14 < this.Jp.size(); var14++) {
                  var13 &= var2.LM.getBoneFlag(var14) == 7;
               }

               if (var13) {
                  this.KY = var2.LM.getBoneTable();
                  this.KZ = var2.Ml != null;
               }
            }
         }

         return var2.Ml == null || this.KZ;
      } else {
         return false;
      }
   }

   private void updateSampleCursors(List var1) {
      boolean var2 = this.canUseCachedBoneTable(var1);

      for (int var3 = 0; var3 < this.Jq.length; var3++) {
         if (this.Jq[var3] != null) {
            this.KH[var3] = this.sampleBoneFrame(var3, var2);
         }
      }
   }

   private int sampleBoneFrame(int var1, boolean var2) {
      AnimationPlayback var3 = this.KX;
      MatrixTable var4 = var3 == null ? null : var3.LM.getMatrixTable(var1);
      TimedSequenceCursor var5 = this.KF[var1];
      long var6 = var3 == null ? 0L : var3.pB;
      if (!var2) {
         this.KG[var1] = var6 + this.Jo.qK.wf.perChunk();
      }

      return var5.method9001(
         var4,
         var3 == null ? 0 : var3.sT(),
         var6,
         var3 != null && var3.LM.yN,
         var2 && var6 >= this.KG[var1],
         var3 != null && var3.LM.getLoopFrames() != null ? (int)Math.ceil(var3.LM.getLoopEnd() * 20.0 - 1.0E-9) : Integer.MAX_VALUE,
         this.KY != null || this.KZ
      );
   }

   private void computeCachedBoneMatrix(int var1, int var2) {
      AnimationPlayback var3 = this.KX;
      if (this.Lm == null) {
         this.Lm = new int[9];
         Arrays.fill(this.Lm, -1);
         this.Ln = new Matrix4f[9][];
         this.Lo = new TransformBuilder(this.Jr);
      }

      if (this.Lm[var2] < 0) {
         int var4 = var3.LM.timeToFrameIndex(var3.Ml.method9133(var2, var3.LM.yN && !this.Jn.Fk));
         this.Lm[var2] = var4;
         if (this.KY == null) {
            Matrix4f[] var5 = this.Ln[var2];
            if (var5 == null) {
               this.Ln[var2] = var5 = createMatrixArray(this.Jp.size());
            }

            for (int var6 = 0; var6 < this.Jp.size(); var6++) {
               CustomEntityDefinition var7 = (CustomEntityDefinition)this.Jp.get(var6);
               var7.getDecomposition().applyTo(this.Lo);
               TransformDecomposition var8 = var3.LM.isBoneAnimated(var6) ? var3.LM.getPartDecomposition(var6, var4, false) : null;
               if (var8 != null) {
                  this.Lo.method8668(var8, var3.LM.getBoneFlag(var6), 1.0F);
               }

               this.Lo.method8687(var5[var6]);
               if (var7.getParent() != null) {
                  var5[var7.getParent().nH()].mul(var5[var6], var5[var6]);
               }
            }
         }
      }

      if (this.KY != null) {
         this.KY.method8903(this.Lm[var2], var1, this.KV);
         if (this.un != 1.0F) {
            this.KV.scaleLocal(this.un);
         }
      } else {
         this.KV.set(this.JM).scale(this.un).mul(this.Ln[var2][var1]).scale(((CustomEntityDefinition)this.Jp.get(var1)).getScaleFactor());
      }
   }

   private void applyPartTransform(int var1, Location var2) {
      ModelPartDisplay var3 = this.Jq[var1];
      int var4 = this.KH[var1];
      if (var4 != -2 && !var3.isDisplayReady()) {
         var4 = this.sampleBoneFrame(var1, false);
      }

      if (var4 == -1 && this.KI[var1] && var3.isFastPathEligible()) {
         var3.attachBoneMatrixView(this.field123, var1);
         this.KO++;
      } else {
         this.flushBoneMatrixView(var1);
         if (var4 == -1) {
            var3.applyMatrixImmediate(var2, this.JF[var1]);
         } else if (var4 == 0) {
            var3.applyMatrixInterpolated(var2, this.JF[var1], this.JF[var1], 2);
         } else if (var4 > 0) {
            MatrixTable var5 = this.KX.LM.getMatrixTable(var1);
            if (this.KZ) {
               this.computeCachedBoneMatrix(var1, this.KF[var1].method9003(var4));
            } else {
               this.KV.set(this.JM).scale(this.un).mul(var5.getMatrix(var4)).scale(((CustomEntityDefinition)this.Jp.get(var1)).getScaleFactor());
            }

            var3.applyMatrixInterpolated(var2, this.JF[var1], this.KV, this.KF[var1].method9003(var4));
         } else if (this.KZ) {
            this.computeCachedBoneMatrix(var1, 1);
            var3.applyMatrixInterpolated(var2, this.JF[var1], this.KV, 1);
         } else if (this.KY != null) {
            int var8 = this.KX.Fa;
            int var6 = Math.min(var8 + 1, this.KX.LM.getMaxPartCount() - 1);
            IntPair var7 = this.KX.LM.getLoopFrames();
            if (var7 != null && var6 >= var7.zV) {
               var6 = var8;
            }

            this.KY.method8903(this.KY.method8898(var6, this.KX.sY()), var1, this.KV);
            if (this.un != 1.0F) {
               this.KV.scaleLocal(this.un);
            }

            var3.applyMatrixInterpolated(var2, this.JF[var1], this.KV, 1);
         } else {
            var3.applyMatrix(var2, this.JF[var1]);
         }
      }
   }

   private void updateWorldMatrices(boolean var1) {
      for (int var2 = 0; var2 < this.Jp.size(); var2++) {
         CustomEntityDefinition var3 = (CustomEntityDefinition)this.Jp.get(var2);
         CustomEntityDefinition var4 = var3.getParent();
         boolean var5 = false;
         if (this.JI[var2]) {
            this.Jw[var2].method8687(this.JK);
            var5 = !this.JK.equals(this.JD[var2]);
            if (var5) {
               this.JD[var2].set(this.JK);
            }
         }

         this.JI[var2] = !this.JL || var5 || var1 || var4 != null && this.JI[var4.nH()];
         if (this.JI[var2]) {
            if (var4 == null) {
               this.JE[var2].set(this.JD[var2]);
            } else {
               this.JE[var2].set(this.JE[var4.nH()]).mul(this.JD[var2]);
            }

            this.JF[var2].set(this.JM).scale(this.un).mul(this.JE[var2]).scale(var3.getScaleFactor());
         }
      }

      this.JL = true;
   }

   @Nullable
   private BoneTransformTable getActiveBoneTable() {
      return this.Ko != null
            && !this.Ko.Mm
            && (this.Ko.LS == null || !(this.Ko.rp() < 1.0F))
            && this.JN == null
            && this.JO == null
            && this.Kg.isEmpty()
            && this.Kh.isEmpty()
            && !this.Ko.LM.isLocomotionDriven()
         ? this.Ko.LM.getBoneTable()
         : null;
   }

   private boolean applyBoneTable(boolean var1) {
      BoneTransformTable var2 = this.getActiveBoneTable();
      if (var2 == null) {
         return false;
      }

      int var3 = var2.method8898(this.Ko.Fa, this.Ko.sY());
      BoneMatrixView var4 = null;
      this.Lt = this.Ko.Fa;
      this.Lu = this.Ko.pB;
      this.Lv = this.Ko.FE && !this.Ko.Mk;

      for (int var5 = 0; var5 < this.Jp.size(); var5++) {
         if (!this.KJ
            || !this.KP && this.KH[var5] != -1
            || !var2.method8900(var3, var5)
            || this.Jq[var5] == null
            || (this.KP ? !this.Jq[var5].isStaticPathEligible() : !this.Jq[var5].isFastPathEligible())) {
            boolean var7 = this.KI != null && this.KI[var5] || this.Lw != var2 || !var2.method8899(var5, this.Lx, var3);
            if (this.KI != null) {
               this.KI[var5] = false;
            }

            if (var7) {
               var2.method8901(var3, var5, this.Js[var5]);
               this.Jt[var5].copyOf(this.Js[var5]);
               this.Ju[var5].copyOf(this.Js[var5]);
            }

            this.JI[var5] = var1 || var7;
            this.JG[var5] = null;
            this.JH[var5] = -1;
            if (this.JI[var5]) {
               var2.method8902(var3, var5, this.JE[var5]);
               if (this.un == 1.0F) {
                  var2.method8903(var3, var5, this.JF[var5]);
               } else {
                  this.JF[var5].set(this.JM).scale(this.un).mul(this.JE[var5]).scale(((CustomEntityDefinition)this.Jp.get(var5)).getScaleFactor());
               }
            }
         } else {
            if (var4 == null) {
               float var6 = this.Jo.getBodyYaw();
               if (this.KL == null || var6 != this.KM) {
                  this.KL = new Matrix4f().rotationY(-var6);
                  this.KM = var6;
               }

               var4 = new BoneMatrixView(var2, var3, this.KL);
            }

            this.KI[var5] = true;
            if (this.KP) {
               this.KR++;
            } else {
               this.KN++;
            }
         }
      }

      if (var4 != null) {
         this.field123 = var4;
      }

      this.Ko.sX();
      this.Lw = var2;
      this.Lx = var3;
      return true;
   }

   private void flushBoneMatrixView(int var1) {
      if (this.KI != null && this.KI[var1]) {
         BoneTransformTable var2 = this.field123.table();
         int var3 = this.field123.boneIndex();
         var2.method8901(var3, var1, this.Js[var1]);
         this.Jt[var1].copyOf(this.Js[var1]);
         this.Ju[var1].copyOf(this.Js[var1]);
         var2.method8902(var3, var1, this.JE[var1]);
         var2.method8903(var3, var1, this.JF[var1]);
         this.JI[var1] = true;
         this.JG[var1] = null;
         this.JH[var1] = -1;
         this.KI[var1] = false;
      }
   }

   private void flushAllBoneMatrixViews() {
      if (this.KI != null) {
         for (int var1 = 0; var1 < this.KI.length; var1++) {
            this.flushBoneMatrixView(var1);
         }
      }
   }

   private void rebuildTransforms() {
      this.Kl = this.Ko != null && this.Ko.FE && !this.Ko.Mk && !this.Ko.Mm ? this.Kk : 0.0F;
      if (this.Ko != null) {
         double var1 = this.JO == null ? Double.NaN : this.JO.method9104(Double.NaN);
         this.Lt = Double.isFinite(var1)
            ? this.Ko.LM.wrapTime(var1)
            : (this.Ko.LM.isLocomotionDriven() ? this.Ko.LM.wrapTime(this.getPlaybackTime()) : this.Ko.Fa);
         this.Lu = this.Ko.pB;
         this.Lv = this.Ko.FE && !this.Ko.Mk;
      }

      this.applyTransformsAtTime(this.Jt, this.Lt);
      if (this.Ko != null) {
         this.Ko.sX();
      }

      String var5 = this.Ko == null ? "" : this.Ko.LM.getName();
      boolean var2 = this.JO != null;

      for (ModelDefinitionData var4 : this.Kg) {
         var2 |= var4.getName().equals(var5);
      }

      this.Jv = var2 ? this.Ju : this.Jt;
      if (var2) {
         for (int var6 = 0; var6 < this.Ju.length; var6++) {
            this.Ju[var6].copyOf(this.Jt[var6]);
         }
      }

      for (ModelDefinitionData var10 : this.Kg) {
         if (var10.getName().equals(var5)) {
            this.applyVariableTransforms(this.Jv, var10);
         }
      }

      if (this.JO != null) {
         this.JO.method9102(this.Jv);
      }

      this.Jw = this.Kh.isEmpty() ? this.Jv : this.Js;
      if (!this.Kh.isEmpty()) {
         for (int var8 = 0; var8 < this.Js.length; var8++) {
            this.Js[var8].copyOf(this.Jv[var8]);
         }
      }

      for (AnimationPlayback var11 : this.Kh) {
         this.method9034(this.Js, this.Jz, var11, false);
         var11.sX();
      }
   }

   private void syncEntityState() {
      int var1 = this.Jo.getAnimationTicks();
      boolean var2 = this.Jo.jk();
      float var3 = this.Jo.jm();
      if (!this.Kg.isEmpty() && (var1 != this.KB || var2 != this.KC || var3 != this.KD)) {
         this.Ks = true;
      }

      this.KB = var1;
      this.KC = var2;
      this.KD = var3;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void applyVariableTransforms(TransformBuilder[] var1, ModelDefinitionData var2) {
      double var3 = switch (IntLookupTables.SWITCH_ANIMATION_VARIABLE[var2.getVariable().ordinal()]) {
         case 1 -> this.KB;
         case 2 -> this.KC ? 1.0 : 0.0;
         case 3 -> this.KD;
         case 4 -> 0.0;
         default -> throw new MatchException(null, null);
      };

      for (int var5 = 0; var5 < var2.getAnimatedBoneCount(); var5++) {
         int var6 = var2.getAnimatedBoneIndex(var5);
         if (var6 >= this.Jp.size()) {
            break;
         }

         if (var2.applyBoneClamped(var6, var3, this.Jx[var6], this.Jy[var6])) {
            var1[var6].method8672(((CustomEntityDefinition)this.Jp.get(var6)).getDecomposition(), this.Jx[var6], var2.getBoneFlag(var6), 1.0F);
            this.JI[var6] = this.field122[var6] = true;
         }
      }
   }

   private void applyTransformsAtTime(TransformBuilder[] var1, double var2) {
      double var4 = this.JO == null ? Double.NaN : this.JO.method9104(Double.NaN);
      boolean var6 = this.JN == null
         && this.JO == null
         && (this.Ko == null || !this.Ko.Mm && !this.Ko.LM.isLocomotionDriven() && (this.Ko.LS == null || this.Ko.rp() >= 1.0F));

      for (int var7 = 0; var7 < this.Jp.size(); var7++) {
         TransformDecomposition var8 = ((CustomEntityDefinition)this.Jp.get(var7)).getDecomposition();
         int var9 = this.Ko == null ? 0 : this.Ko.LM.getBoneFlag(var7);
         TransformDecomposition var10 = var6 ? this.method9035(this.Ko, var7) : null;
         boolean var11 = !var6 || this.JG[var7] != var10 || this.JH[var7] != var9;
         this.JI[var7] = var11 || this.field122[var7];
         this.field122[var7] = false;
         this.JG[var7] = var10;
         this.JH[var7] = (byte)var9;
         if (var11) {
            var8.applyTo(var1[var7]);
            if (this.Ko != null && this.JN != null && this.JN.method9064(this.Ko.LM)) {
               var9 = this.method9033(var1[var7], var8, var7);
            } else if (this.Ko != null && (this.Ko.LM.isLocomotionDriven() || Double.isFinite(var4))) {
               if (this.Ko.LM.applyBoneAtRaw(var7, var2, this.Jx[var7], this.Jy[var7])) {
                  var1[var7].method8667(this.Jx[var7], var9, this.Ko.LM.isLocomotionDriven() ? (float)this.Ki.ti().rh() : 1.0F);
               }
            } else {
               TransformDecomposition var12 = var6 ? var10 : this.method9035(this.Ko, var7);
               if (var12 != null) {
                  var1[var7].method8668(var12, var9, 1.0F);
               }
            }

            if (this.Ko != null && this.Ko.LS != null && this.Ko.rp() < 1.0F) {
               this.Jx[var7].copyOf(var1[var7]);
               var8.applyTo(this.Jz[var7]);
               var1[var7].method8686(this.Jz[var7], this.Ko.LS[var7], this.Ko.ah(var7), this.Jx[var7], var9, this.Ko.rp());
            }

            if (this.Ko != null && this.Ko.Mm) {
               var1[var7].method8668(var8, var9 | this.Ko.ah(var7), 1.0F - this.Ko.sU());
            }
         }
      }
   }

   private int method9033(TransformBuilder var1, TransformDecomposition var2, int var3) {
      double var4 = this.Ko.LM.isLocomotionDriven() ? this.getPlaybackTime() : (double)this.Ko.pB / Math.max(1, this.Ko.LM.yP);
      if (this.JO != null) {
         var4 = this.JO.method9104(var4);
      }

      ModelDefinitionData[] var6 = this.JN.LB;
      this.JQ[0] = this.Jx[var3];
      this.JQ[1] = this.Jy[var3];
      this.JQ[2] = this.Jz[var3];
      int var7 = 0;

      for (int var8 = 0; var8 < this.JQ.length; var8++) {
         var2.applyTo(this.JQ[var8]);
         if (var6[var8].applyBoneAt(var3, var4, this.JQ[var8], this.JA[var3])) {
            var7 |= this.JR[var8] = var6[var8].getBoneFlag(var3);
         } else {
            this.JR[var8] = 0;
         }
      }

      this.JN.method9066(this.JS);
      var1.method8673(var2, this.JQ, this.JR, this.JS);
      if (this.Ko.LM.isLocomotionDriven()) {
         this.Jx[var3].copyOf(var1);
         var2.applyTo(var1);
         var1.method8667(this.Jx[var3], var7, (float)this.Ki.ti().rh());
      }

      return var7;
   }

   private double getPlaybackTime() {
      return this.JO == null ? this.Fv : this.JO.method9104(this.Fv);
   }

   private void method9034(TransformBuilder[] var1, TransformBuilder[] var2, AnimationPlayback var3, boolean var4) {
      float var5 = var4 ? var3.sV() : var3.rp();
      float var6 = var4 ? var3.sW() : var3.sU();

      for (int var7 = 0; var7 < var3.mW(); var7++) {
         int var8 = var3.method9071(var7);
         if (var8 >= this.Jp.size()) {
            break;
         }

         int var9;
         if (var3.Mn != null) {
            var9 = var3.Mo[var8];
            var2[var8].copyOf(var3.Mn[var8]);
         } else {
            TransformDecomposition var10 = this.method9036(var3, var8, var4 ? var3.Mg : var3.Fa);
            byte var11 = var10 == null ? 0 : var3.LM.getBoneFlag(var8);
            int var12 = var3.ah(var8);
            var9 = var12 | var11;
            if (var9 != 0) {
               var2[var8]
                  .method8685(
                     var1[var8],
                     var3.LS == null ? var1[var8] : var3.LS[var8],
                     var12,
                     var10 == null ? ((CustomEntityDefinition)this.Jp.get(var8)).getDecomposition() : var10,
                     var11,
                     var5
                  );
            }
         }

         if (var9 != 0) {
            if (!var4) {
               this.JI[var8] = this.field122[var8] = true;
            }

            int var13 = this.Kl > 0.0F && this.Ko != null && var3.LM.yM == AnimationState.ATTACK && ((CustomEntityDefinition)this.Jp.get(var8)).Ao
               ? var9 & this.Ko.LM.getBoneFlag(var8)
               : 0;
            var1[var8].method8667(var2[var8], var9 & ~var13, var6);
            if (var13 != 0) {
               var1[var8].method8667(var2[var8], var13, var6 * (1.0F - this.Kl));
            }
         }
      }
   }

   @Nullable
   private TransformDecomposition method9035(@Nullable AnimationPlayback var1, int var2) {
      return this.method9036(var1, var2, var1 == null ? 0 : var1.Fa);
   }

   @Nullable
   private TransformDecomposition method9036(@Nullable AnimationPlayback var1, int var2, int var3) {
      if (var1 != null && var1.LM.isBoneAnimated(var2)) {
         TransformDecomposition var4 = var1.LM.getPartDecomposition(var2, var3, var1.sY());
         return var4 == null && var1 == this.Ko ? ((CustomEntityDefinition)this.Jp.get(var2)).getDecomposition() : var4;
      } else {
         return var1 == this.Ko ? ((CustomEntityDefinition)this.Jp.get(var2)).getDecomposition() : null;
      }
   }

   private void invalidateTransformCaches() {
      this.Kq = this.method9037(this.JT, this.JX);
      System.arraycopy(this.JT, 0, this.JU, 0, this.JT.length);
      System.arraycopy(this.JX, 0, this.JY, 0, this.JX.length);
      this.Kr = this.Kq;
      System.arraycopy(this.Kb, 0, this.Kd, 0, this.Kb.length);
      if (this.Ko != null && this.Ko.sU() >= 0.5F) {
         for (int var1 = 0; var1 < this.Kd.length; var1++) {
            int var2 = this.method9043(this.Ko, var1, false);
            if (var2 >= 0) {
               this.Kd[var1] = var2;
            }
         }
      }

      System.arraycopy(this.Kd, 0, this.Kc, 0, this.Kd.length);

      for (AnimationPlayback var4 : this.Kh) {
         this.Kq = this.method9038(var4, this.JT, this.JX, this.Kq, false);
         this.method9044(var4, this.Kc, false);
      }
   }

   private int method9037(byte[] var1, int[] var2) {
      Arrays.fill(var1, (byte)-1);
      Arrays.fill(var2, -1);
      byte var3 = 0;
      if (this.Ko != null) {
         float var4 = this.Ko.sU();
         boolean var5 = this.Ko.rp() < 0.5F;

         for (int var6 = 0; var6 < this.Jp.size(); var6++) {
            var1[var6] = var4 < 0.5F ? -1 : (var5 && this.Ko.LV != null ? this.Ko.LV[var6] : this.method9039(this.Ko, var6));
            int var7 = this.method9041(this.Ko, var6, -1);
            var2[var6] = var7 >= 0 && !(var4 <= 0.0F) ? method9050(this.ac(var6), var7, var4) : -1;
         }

         Integer var8 = this.Ko.LM.sampleCharTableOrNull(this.Ko.Fa, this.Ko.sY());
         var3 = (byte)(var4 < 0.5F ? 0 : (var5 && this.Ko.LX != null ? this.Ko.LX : (var8 == null ? 0 : var8)));
      }

      return var3;
   }

   private int method9038(AnimationPlayback var1, byte[] var2, int[] var3, int var4, boolean var5) {
      float var6 = var5 ? var1.sV() : var1.rp();
      float var7 = var5 ? var1.sW() : var1.sU();
      int var8 = var5 ? var1.Mg : var1.Fa;

      for (int var9 = 0; var9 < this.Jp.size(); var9++) {
         if (var7 >= 0.5F) {
            byte var10 = var6 < 0.5F && var1.af(var9) ? var1.LV[var9] : this.method9040(var1, var9, var8);
            if (var10 >= 0) {
               var2[var9] = var10;
            }
         }

         int var13 = this.method9042(var1, var9, var3[var9], var8, var6);
         if (var13 >= 0 && var7 > 0.0F) {
            int var11 = var3[var9] < 0 ? this.ac(var9) : var3[var9];
            var3[var9] = method9050(var11, var13, var7);
         }
      }

      if (var7 >= 0.5F) {
         Integer var12 = var6 < 0.5F && var1.Mb ? var1.LX : var1.LM.sampleCharTableOrNull(var8, var1.sY());
         if (var12 != null) {
            var4 = var12;
         }
      }

      return var4;
   }

   private byte method9039(AnimationPlayback var1, int var2) {
      return this.method9040(var1, var2, var1.Fa);
   }

   private byte method9040(AnimationPlayback var1, int var2, int var3) {
      if (!var1.LM.hasBoneFlags(var2)) {
         return -1;
      }

      Boolean var4 = var1.LM.sampleBoneFlag(var2, var3, var1.sY());
      return var4 == null ? -1 : (byte)(var4 ? 1 : 0);
   }

   private int method9041(AnimationPlayback var1, int var2, int var3) {
      return this.method9042(var1, var2, var3, var1.Fa, var1.rp());
   }

   private int method9042(AnimationPlayback var1, int var2, int var3, int var4, float var5) {
      boolean var6 = var1.LM.hasBoneIndices(var2);
      int var7 = var6 ? var1.LM.sampleBoneIndex(var2, var4, var1.sY()) : -1;
      if (var1.LW != null && !(var5 >= 1.0F)) {
         boolean var8 = var1.ag(var2);
         if (!var8 && !var6) {
            return -1;
         }

         int var9 = var3 < 0 ? this.ac(var2) : var3;
         int var10 = var8 && var1.LW[var2] >= 0 ? var1.LW[var2] : var9;
         int var11 = var7 >= 0 ? var7 : var9;
         return method9050(var10, var11, var5);
      } else {
         return var7;
      }
   }

   private int method9043(AnimationPlayback var1, int var2, boolean var3) {
      float var4 = var3 ? var1.sV() : var1.rp();
      float var5 = var3 ? var1.sW() : var1.sU();
      if (var5 < 0.5F) {
         return -1;
      } else {
         return var4 < 0.5F && var1.LY != null ? var1.LY[var2] : var1.LM.sampleShortTable(var2, var3 ? var1.Mg : var1.Fa, var1.sY());
      }
   }

   private void method9044(AnimationPlayback var1, int[] var2, boolean var3) {
      float var4 = var3 ? var1.sW() : var1.sU();
      if (!(var4 < 0.5F)) {
         for (int var5 = 0; var5 < var2.length; var5++) {
            int var6 = this.method9043(var1, var5, var3);
            if (var6 >= 0) {
               var2[var5] = var6;
            }
         }
      }
   }

   private int[] method9045(AnimationPlayback var1) {
      int[] var2 = this.allocateLayerIndices();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var2[var3] = this.method9043(var1, var3, true);
      }

      return var2;
   }

   private int[] allocateLayerIndices() {
      int[] var1 = new int[this.Kb.length];
      Arrays.fill(var1, -1);
      return var1;
   }

   private int ac(int var1) {
      ModelPartDisplay var2 = this.Jq[var1];
      return var2 == null ? 16777215 : var2.getColorValue();
   }

   private void resetPoseCaches() {
      if (this.Kq != this.Fc) {
         this.Fc = this.Kq;

         for (ModelPartDisplay var4 : this.Jq) {
            if (var4 != null) {
               var4.setAnimationFrame(this.Fc);
            }
         }
      }

      if (!Arrays.equals(this.Ke, this.Kc)) {
         System.arraycopy(this.Kc, 0, this.Ke, 0, this.Kc.length);

         for (ModelPartDisplay var10 : this.Jq) {
            if (var10 != null) {
               var10.setAnimationIndices(this.Kc);
            }
         }
      }

      for (int var6 = 0; var6 < this.Jq.length; var6++) {
         ModelPartDisplay var8 = this.Jq[var6];
         if (var8 != null) {
            if (this.JW[var6] != this.JT[var6]) {
               this.JW[var6] = this.JT[var6];
               var8.setGlow(this.JT[var6] < 0 ? null : this.JT[var6] != 0);
            }

            if (this.Ka[var6] != this.JX[var6]) {
               this.Ka[var6] = this.JX[var6];
               var8.setCustomModelData(this.JX[var6] < 0 ? null : this.JX[var6]);
            }
         }
      }
   }

   private TransformBuilder[] method9046(@Nullable AnimationPlayback var1, int var2, long var3, boolean var5) {
      this.flushAllBoneMatrixViews();

      for (int var6 = 0; var6 < this.Jv.length; var6++) {
         this.JB[var6].copyOf(this.Jv[var6]);
      }

      for (AnimationPlayback var7 : this.Kh) {
         if (var1 == null && method9048(var7, var2, var3) >= 0) {
            break;
         }

         this.method9034(this.JB, this.JC, var7, true);
         if (var7 == var1) {
            break;
         }
      }

      return this.cloneTransformBuilders(var5 ? this.JC : this.JB);
   }

   private PackedSampleBuffer method9047(@Nullable AnimationPlayback var1, int var2, long var3) {
      System.arraycopy(this.JU, 0, this.JV, 0, this.JU.length);
      System.arraycopy(this.JY, 0, this.JZ, 0, this.JY.length);
      int var5 = this.Kr;
      int[] var6 = (int[])this.Kd.clone();

      for (AnimationPlayback var8 : this.Kh) {
         if (var1 == null && method9048(var8, var2, var3) >= 0) {
            break;
         }

         var5 = this.method9038(var8, this.JV, this.JZ, var5, true);
         this.method9044(var8, var6, true);
         if (var8 == var1) {
            break;
         }
      }

      int[] var9 = new int[var6.length];
      Arrays.fill(var9, -1);
      if (var1 != null) {
         for (int var10 = 0; var10 < var6.length; var10++) {
            if (this.method9043(var1, var10, true) >= 0) {
               var9[var10] = var6[var10];
            }
         }
      }

      return new PackedSampleBuffer((byte[])this.JV.clone(), (int[])this.JZ.clone(), var5, var9);
   }

   private static int method9048(AnimationPlayback var0, int var1, long var2) {
      int var4 = Integer.compare(var0.LM.getPriority(), var1);
      return var4 != 0 ? var4 : Long.compare(var0.LR, var2);
   }

   private TransformBuilder[] cloneTransformBuilders(TransformBuilder[] var1) {
      this.flushAllBoneMatrixViews();
      TransformBuilder[] var2 = this.createTransformBuilders(var1.length);

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3].copyOf(var1[var3]);
      }

      return var2;
   }

   private void sM() {
      this.Kh.clear();
      this.Kh.addAll(this.Kf.values());
      this.Kh.sort(Comparator.<AnimationPlayback>comparingInt(var0 -> var0.LM.getPriority()).thenComparingLong(var0 -> var0.LR));
   }

   private void sN() {
      if (this.Ko == null) {
         this.Jn.EU = null;
         this.Jn.Fa = 0;
         this.Jn.pB = 0L;
         this.Jn.EY = false;
      } else {
         this.Jn.EU = this.Ko.LM;
         this.Jn.Fa = this.Ko.Fa;
         this.Jn.pB = this.Ko.pB;
         this.Jn.EY = this.Ko.FE && !this.Ko.Mk;
      }
   }

   static int method9050(int var0, int var1, float var2) {
      if (var2 <= 0.0F) {
         return var0;
      }

      if (var2 >= 1.0F) {
         return var1;
      }

      int var3 = Math.round((var0 >> 16 & 0xFF) + ((var1 >> 16 & 0xFF) - (var0 >> 16 & 0xFF)) * var2);
      int var4 = Math.round((var0 >> 8 & 0xFF) + ((var1 >> 8 & 0xFF) - (var0 >> 8 & 0xFF)) * var2);
      int var5 = Math.round((var0 & 0xFF) + ((var1 & 0xFF) - (var0 & 0xFF)) * var2);
      return var3 << 16 | var4 << 8 | var5;
   }

   static boolean canAddLayer(int var0, boolean var1) {
      return var1 || var0 < 16;
   }

   static int resolveBlendMode(ModelDefinitionData var0, @Nullable AnimationPlayback var1) {
      return Math.max(var0.getDurationTicks(), var1 == null ? 0 : var1.LM.getAuxDurationTicks());
   }

   private TransformBuilder[] createTransformBuilders(int var1) {
      TransformBuilder[] var2 = new TransformBuilder[var1];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = new TransformBuilder(this.Jr);
      }

      return var2;
   }

   private static Matrix4f[] createMatrixArray(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }
}
