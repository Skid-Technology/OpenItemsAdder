package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.anim.BoneSpec;
import dev.lone.itemsadder.anim.PlaybackCursor;
import dev.lone.itemsadder.anim.PlaybackRequest;
import dev.lone.itemsadder.anim.TimedKey;
import dev.lone.itemsadder.anim.TimedKeyPayload;
import dev.lone.itemsadder.api.AnimationPlaybackOptions;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.particle.EntityParticleEmitter;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.particle.ParticleOffsetEmitter;
import dev.lone.itemsadder.util.IntTableHolder;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;

public class ModelInstance {
   public final CustomEntityImpl ER;
   private ParticleEmitterManager ES;
   private Map ET;
   @Nullable
   public volatile ModelDefinitionData EU;
   public List EV;
   public ModelDefinitionData EW;
   @Nullable
   private Runnable EX;
   public volatile boolean EY;
   private int EZ;
   public int Fa;
   public long pB;
   public boolean Fb;
   private int Fc = -1;
   @Nullable
   private ModelPoseRuntime Fd;
   private ModelDefinitionData Fe;
   private ModelDefinitionData Ff;
   private List Fg = List.of();
   private long Fh;
   private Consumer Fi = var0 -> SchedulerUtils.runOnMainThread(() -> var0.forEach(Runnable::run));
   AtomicBoolean Fj = new AtomicBoolean(false);
   boolean Fk;
   boolean Fl = false;
   private final AtomicLong Fm = new AtomicLong();
   private final ConcurrentMap Fn = new ConcurrentHashMap();
   private final AtomicReference Fo = new AtomicReference<>(new TimedKey(0L, null));

   private ParticleEmitterManager getParticles() {
      if (this.ES == null) {
         this.ES = new ParticleEmitterManager();
      }

      return this.ES;
   }

   private void qB() {
      if (this.ES != null) {
         this.ES.close();
         this.ES = null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean method8904(ModelDefinitionData var1) {
      if (this.ER.getModel().of() && "base".equals(var1.getLayer()) && var1.getVariable() == AnimationVariable.NONE) {
         switch (IntTableHolder.SWITCH_MAP[var1.yM.ordinal()]) {
            case 1:
            case 2:
            case 3:
               break;
            default:
               return true;
         }
      }

      return false;
   }

   boolean method8905(ModelDefinitionData var1) {
      return this.Fg.size() > 1 && this.Ff == var1;
   }

   public ModelDefinitionData method8906(@Nullable ModelDefinitionData var1) {
      if (var1 == null) {
         this.Fe = null;
         this.Ff = null;
         return null;
      }

      boolean var2 = this.Fe != var1 && this.Ff != var1;
      if (var2) {
         this.Fe = var1;
         this.Ff = var1;
         this.Fg = this.ER.getModel().method474(var1);
      }

      if (this.Fg.size() < 2) {
         return var1;
      }

      long var3 = this.Fd == null ? -1L : this.Fd.getAnimationStartTick(this.Ff);
      if (var2 || var3 >= 0L && var3 != this.Fh) {
         this.Ff = CustomEntityModel.method476(this.Fg);
         this.Fh = this.Fd == null ? -1L : this.Fd.getAnimationStartTick(this.Ff);
         if (this.Fh < 0L) {
            this.Fh = 0L;
         }
      }

      return this.Ff;
   }

   public void method8907(World var1, Location var2, boolean var3) {
      ModelPoseRuntime var4 = this.Fd;
      if (var4 != null && var4.JP != null) {
         var4.JP.method9112(var1, var2, var3 && this.ER.hasVisibleViewerWithin(Math.min(var4.JP.OW.qp(), Settings.get().customEntityTerrainFeetViewDistance)));
      }
   }

   public void qC() {
      ModelPoseRuntime var1 = this.Fd;
      if (var1 != null && var1.JP != null) {
         var1.JP.tU();
      }
   }

   public ModelInstance(CustomEntityImpl var1, @Nullable ModelDefinitionData var2) {
      this.ER = var1;
      boolean var3 = var1.getModel().nZ();
      this.EU = var3 ? null : var2;
      if (var1.getModel().of()) {
         this.Fd = new ModelPoseRuntime(this, var3 ? null : var2);
      }

      var1.initializeModelParts(this);
      this.qD();
      if (this.Fd != null) {
         this.Fd.refreshDisplays();
      }

      this.EY = this.EU != null;
   }

   private void qD() {
      this.ET = Collections.synchronizedMap(new HashMap());
      new LinkedHashMap<>(this.ER.getModel().AK).forEach((var1, var2) -> this.ET.put(var2, var2.resolvePartBindings(this.ER)));
      this.qU();
   }

   public void qE() {
      this.method8908(1L);
   }

   public void method8908(long var1) {
      if (var1 < 1L) {
         throw new IllegalArgumentException("logicalSteps must be positive");
      }

      ArrayList var3 = new ArrayList();
      this.ER.applyStateSnapshot();
      if (this.Fd != null) {
         this.Fd.update(var1);
      }

      for (long var4 = 0L; var4 < var1; var4++) {
         boolean var6 = var4 + 1L == var1;
         if (this.Fd != null) {
            if (this.EW != null && this.method8925(this.EW)) {
               if (this.EX != null && !this.Fl) {
                  this.Fl = true;
                  this.EX.run();
               }

               this.ai(false);
            }

            this.Fd.tickAnimations(var6, var3);
         } else {
            this.method8910(var6, var3);
         }
      }

      if (!var3.isEmpty()) {
         this.Fi.accept(List.copyOf(var3));
      }
   }

   boolean qF() {
      return this.Fd != null && this.Fd.isPoseChanged();
   }

   void method8909(ModelDefinitionData var1) {
      this.EW = null;
      this.Fd.replaceBaseAnimation(var1);
      this.Fk = true;
   }

   void qG() {
      ArrayList var1 = new ArrayList();
      this.Fd.update(1L);
      this.Fd.tickAnimations(false, var1);
      this.Fd.refreshPose();
      if (!var1.isEmpty()) {
         this.Fi.accept(List.copyOf(var1));
      }
   }

   void qH() {
      ArrayList var1 = new ArrayList();
      this.Fd.finishBaseAnimation(var1);
      if (!var1.isEmpty()) {
         this.Fi.accept(List.copyOf(var1));
      }
   }

   public boolean qI() {
      return this.Fd != null && this.Fd.isLocomotionPlaying();
   }

   public void qJ() {
      if (this.Fd != null) {
         this.Fd.resetLocomotion();
      }
   }

   public BoneSpec getBoneSpec() {
      return this.Fd == null ? null : this.Fd.getBoneSpec();
   }

   public PlaybackRequest getPlaybackRequest() {
      if (this.Fd != null) {
         return this.Fd.getPlaybackRequest();
      }

      ModelDefinitionData var1 = this.EU;
      return new PlaybackRequest(var1 == null ? "" : var1.yK, this.Fa, this.Fa / 20.0, this.pB, this.EY, 1.0F);
   }

   private void method8910(boolean var1, ArrayList var2) {
      if (this.EV != null) {
         Location var3 = this.ER.getBaseLocation();
         int var4 = this.EV.size();

         for (int var5 = 0; var5 < var4; var5++) {
            if (this.Fj.get() || this.EU == null) {
               this.Fj.set(false);
               return;
            }

            if (this.EV.size() == 0) {
               return;
            }

            if (this.EW != null && this.EZ <= 0 && this.method8925(this.EW)) {
               if (this.EX != null) {
                  this.EX.run();
               }

               this.ai(false);
               if (var1) {
                  this.qM();
               }

               return;
            }

            if (this.Fa + 1 >= this.EU.yP) {
               this.EZ--;
               if (this.EZ > 0) {
                  this.EW = this.EU;
                  this.ai(false);
                  if (var1) {
                     this.qM();
                  }

                  return;
               }
            }

            ModelPartBinding var6 = (ModelPartBinding)this.EV.get(var5);
            if (!this.Fl && !this.EU.yN && this.qX()) {
               this.EY = false;
               this.qB();
               if (this.EX != null) {
                  this.Fl = true;
                  this.EX.run();
               }
            } else if (this.EY) {
               var6.apply(this);
            }

            if (var1 && var5 == 0) {
               this.qV();
               this.qW();
            }

            if (var1) {
               var6.applyAt(var3, this);
            }

            if (this.EY && this.EU != null && this.Fa < this.EU.zx.size()) {
               List var7 = (List)this.EU.zx.get(this.Fa);
               if (var7.size() > 0) {
                  List var8 = this.ER.getViewers();
                  if (var8.size() > 0) {
                     double[] var9 = var6.Hh.Hn.Nw();

                     for (EntityParticleEmitter var11 : var7) {
                        if (var6.Hh.Ho.equals(var11.Dt)) {
                           var11.method8883(
                              this.getParticles(),
                              new Location(var3.getWorld(), var9[0], var9[1], var9[2]),
                              this.ER.getEntityUuid(),
                              var8.stream().map(var0 -> var0.li).toList(),
                              var2,
                              null
                           );
                           break;
                        }
                     }
                  }
               }
            }
         }

         if (this.EY && this.EU != null && this.EU.hasOffsetEmitters() && this.Fa < this.EU.zw.size()) {
            for (ParticleOffsetEmitter var15 : (List)this.EU.zw.get(this.Fa)) {
               UUID var16 = this.ER.getEntityUuid();
               double var17 = var3.getX();
               double var18 = var3.getY();
               double var12 = var3.getZ();
               var2.add(() -> {
                  World var8x = var16 == null ? null : Bukkit.getWorld(var16);
                  if (var8x != null) {
                     var15.method8878(new Location(var8x, var17, var18, var12));
                  }
               });
            }
         }

         this.qP();
      }
   }

   public void qM() {
      if (this.Fd != null) {
         this.Fd.forceRefresh();
      } else {
         this.ER.applyStateSnapshot();
         Location var1 = this.ER.getBaseLocation();
         this.qV();
         this.qW();
         int var2 = this.EV.size();

         for (int var3 = 0; var3 < var2; var3++) {
            if (this.Fj.get() || this.EU == null) {
               this.Fj.set(false);
               return;
            }

            if (this.EV.size() == 0) {
               return;
            }

            ModelPartBinding var4 = (ModelPartBinding)this.EV.get(var3);
            var4.applyAt(var1, this);
         }
      }
   }

   public void qN() {
      this.qB();
      if (this.Fd != null) {
         this.Fd.stopBaseAnimation();
      } else {
         this.EY = false;
      }
   }

   public boolean qO() {
      return this.Fd != null && this.Fd.tryStopBaseAnimation();
   }

   public void method8911(boolean var1, boolean var2) {
      if (this.Fd != null) {
         this.Fk = true;
         this.Fd.refreshBasePlayback(var1, var2);
      } else {
         if (var2) {
            this.qN();
         }

         this.Fk = true;
         if (var1 && !this.Fl && this.EX != null) {
            this.Fl = true;
            this.EX.run();
         }
      }
   }

   private void qP() {
      if (this.EU != null) {
         this.pB++;
         if (this.Fa + 1 > this.EU.yP - 1) {
            if ("hold".equals(this.EU.yL)) {
               return;
            }

            this.Fa = 0;
         } else {
            this.Fa++;
         }
      }
   }

   public void method8912(ModelDefinitionData var1, int var2, @Nullable Runnable var3) {
      this.method8922(var1, var2, var3, true);
   }

   public long method8913(ModelDefinitionData var1) {
      long var2 = this.qQ();
      this.method8914(var1, var2);
      return var2;
   }

   long qQ() {
      return this.Fm.incrementAndGet();
   }

   void method8914(ModelDefinitionData var1, long var2) {
      TimedKeyPayload var4 = new TimedKeyPayload(var2, var1.yK, true);
      if ("base".equals(var1.getLayer())) {
         this.method8915(var4);
      } else {
         this.Fn.compute(var1.getLayer(), (var3, var4x) -> (TimedKey)(var4x != null && var4x.jN() >= var2 ? var4x : new TimedKey(var2, var4)));
      }
   }

   private void method8915(TimedKeyPayload var1) {
      TimedKey var2;
      do {
         var2 = (TimedKey)this.Fo.get();
         if (var2.jN() >= var1.jN()) {
            return;
         }
      } while (!this.Fo.compareAndSet(var2, new TimedKey(var1.jN(), var1)));
   }

   public boolean method8916(ModelDefinitionData var1, int var2, @Nullable Runnable var3, long var4) {
      return this.method8917(var1, var2, var3, var4, AnimationPlaybackOptions.DEFAULT);
   }

   public boolean method8917(ModelDefinitionData var1, int var2, @Nullable Runnable var3, long var4, AnimationPlaybackOptions var6) {
      TimedKeyPayload var7 = new TimedKeyPayload(var4, var1.yK, true);
      boolean var8;
      if ("base".equals(var1.getLayer())) {
         TimedKey var9 = (TimedKey)this.Fo.get();
         var8 = var7.equals(var9.rk()) && this.Fo.compareAndSet(var9, new TimedKey(var9.jN(), null));
      } else {
         var8 = this.method8928(var1.getLayer(), var7);
      }

      return !var8 ? false : this.method8923(var1, var2, var3, true, var6);
   }

   public boolean method8918(ModelDefinitionData var1, AnimationPlaybackOptions var2) {
      if (this.Fd != null && this.Fd.canPlayAnimation(var1)) {
         new PlaybackCursor(var1.yQ, var1.getLoopStart(), var1.getLoopEnd(), var2);
         return true;
      } else {
         return false;
      }
   }

   public void method8919(String var1, boolean var2) {
      if (this.Fd != null) {
         this.Fd.setLayerPaused(var1, var2);
      }
   }

   public boolean method8920(String var1, String var2) {
      CustomEntityModel var3 = this.ER.getModel();
      ModelPoseRuntime var4 = this.Fd;
      if (var1 != null && var2 != null && var4 != null) {
         int var5 = var3.bd(var1);
         int var6 = var3.method457(var1, var2);
         if (var5 >= 0 && var6 >= 0) {
            this.ER.dispatchToManager(() -> var4.setAnimationVariable(var5, var6));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean bk(String var1) {
      CustomEntityModel var2 = this.ER.getModel();
      ModelPoseRuntime var3 = this.Fd;
      if (var1 != null && var3 != null) {
         int var4 = var2.bd(var1);
         if (var4 < 0) {
            return false;
         }

         this.ER.dispatchToManager(() -> var3.setAnimationVariable(var4, 0));
         return true;
      } else {
         return false;
      }
   }

   public long qR() {
      long var1 = this.qQ();
      TimedKeyPayload var3 = new TimedKeyPayload(var1, null, false);
      this.method8915(var3);
      return var1;
   }

   public boolean method8921(long var1) {
      TimedKeyPayload var3 = new TimedKeyPayload(var1, null, false);
      TimedKey var4 = (TimedKey)this.Fo.get();
      if (var3.equals(var4.rk()) && this.Fo.compareAndSet(var4, new TimedKey(var4.jN(), null))) {
         this.qN();
         return true;
      } else {
         return false;
      }
   }

   private boolean method8922(ModelDefinitionData var1, int var2, @Nullable Runnable var3, boolean var4) {
      return this.method8923(var1, var2, var3, var4, AnimationPlaybackOptions.DEFAULT);
   }

   private boolean method8923(ModelDefinitionData var1, int var2, @Nullable Runnable var3, boolean var4, AnimationPlaybackOptions var5) {
      if (this.Fd != null) {
         if (!"base".equals(var1.getLayer())) {
            boolean var9 = this.Fd.playAnimationWithOptions(var1, var2, var3, var5);
            if (var9 && var4) {
               this.Fd.forceRefresh();
            }

            return var9;
         } else {
            this.Fa = 0;
            this.pB = 0L;
            this.EY = true;
            this.Fl = false;
            this.Fk = false;
            this.EZ = var2;
            this.EX = var3;
            boolean var8 = this.EU != var1;
            this.EU = var1;
            if (var8) {
               this.qU();
            }

            boolean var7 = this.Fd.playAnimationWithOptions(var1, var2, var3, var5);
            if (var7 && var4) {
               this.Fd.forceRefresh();
            }

            return var7;
         }
      } else {
         this.qB();
         this.Fa = 0;
         this.EY = true;
         this.Fl = false;
         this.EZ = var2;
         boolean var6 = this.EU != var1;
         if (!var6) {
            this.reset();
         } else {
            this.EU = var1;
            this.qU();
         }

         this.qV();
         this.qW();
         if (var6 && var4) {
            this.qE();
         }

         this.EU = var1;
         this.EX = var3;
         return true;
      }
   }

   private void qS() {
      this.ai(true);
   }

   private void ai(boolean var1) {
      if (this.EW != null) {
         ModelDefinitionData var2 = this.EW;
         this.EW = null;
         this.method8922(var2, this.EZ, null, var1);
      }
   }

   public void qT() {
      this.EW = null;
   }

   public void method8924(ModelDefinitionData var1) {
      this.EW = var1;
      if (!this.EY) {
         this.qS();
      }
   }

   private void qU() {
      List var1 = (List)this.ET.get(this.EU);
      if (var1 != null) {
         this.aj(false);
         this.EV = var1;
         this.Fj.set(true);
      } else if (this.EU == null) {
         this.EV = new ArrayList();
      }
   }

   private void qV() {
      if (this.ER.getModel().AN && !this.ER.getModel().AO && this.EU != null && this.EV != null) {
         boolean var1 = "hold".equals(this.EU.yL) && !this.EY && this.qX();
         int var2 = this.EU.sampleCharTable(this.Fa, var1);
         if (var2 != this.Fc) {
            this.Fc = var2;

            for (ModelPartBinding var4 : this.EV) {
               var4.Hh.setAnimationFrame(var2);
            }
         }
      }
   }

   private void qW() {
      if (this.ER.getModel().AN && !this.ER.getModel().AO && this.EU != null) {
         boolean var1 = "hold".equals(this.EU.yL) && !this.EY && this.qX();

         for (CustomEntityDefinition var3 : this.ER.getModel().og()) {
            ModelPartDisplay var4 = this.ER.getModelPart(var3.getId());
            if (var4 != null) {
               int var5 = var3.nH();
               var4.setGlow(this.EU.sampleBoneFlag(var5, this.Fa, var1));
               int var6 = this.EU.sampleBoneIndex(var5, this.Fa, var1);
               var4.setCustomModelData(var6 < 0 ? null : var6);
            }
         }
      }
   }

   public void aj(boolean var1) {
      if (var1 && this.EV != null) {
         this.EV.forEach(var1x -> {
            if (this.pB > 0L || this.Fa > 0) {
               var1x.Hh.updateRotationBase();
            }
         });
      }

      this.Fa = 0;
      this.pB = 0L;
   }

   public void reset() {
      this.aj(true);
   }

   public boolean qX() {
      return this.Fd != null ? this.Fd.isBaseAnimationFinished() : this.EU != null && this.pB >= this.EU.yO;
   }

   public boolean qY() {
      if (this.Fd != null && this.Fd.isBaseAnimationPaused()) {
         return false;
      } else if (this.EU == null) {
         return true;
      } else {
         return this.EU.yN && this.qX() ? true : !this.EU.yN;
      }
   }

   public boolean method8925(ModelDefinitionData var1) {
      if (this.Fd != null && this.Fd.isBaseAnimationPaused()) {
         return false;
      } else if (this.EU == null) {
         return true;
      } else if (this.qX()) {
         return this.EU.yN && var1.yM == AnimationState.IDLE ? this.EU.yM != AnimationState.OTHER : true;
      } else {
         return this.EU.yM == AnimationState.IDLE;
      }
   }

   public boolean bl(String var1) {
      ModelDefinitionData var2 = this.ER.getModelDefinition(var1);
      if (var2 != null) {
         TimedKey var3 = "base".equals(var2.getLayer()) ? (TimedKey)this.Fo.get() : (TimedKey)this.Fn.get(var2.getLayer());
         TimedKeyPayload var4 = var3 == null ? null : var3.rk();
         if (var4 != null) {
            return var4.rm() && var1.equals(var4.rl());
         }
      }

      if (this.Fd != null) {
         return this.Fd.isAnimationActive(var1);
      } else {
         return this.EU == null ? false : var1.equals(this.EU.yK);
      }
   }

   public boolean method8926(ModelDefinitionData var1) {
      if (this.Fd != null) {
         return this.Fd.isAnimationActive(var1.yK);
      } else {
         return this.EU == null ? false : this.EU.equals(var1);
      }
   }

   public String bm(String var1) {
      return this.EU == null ? var1 : this.EU.yK;
   }

   public boolean qZ() {
      return this.EY && this.EU != null && !this.EU.yN;
   }

   public void aM() {
      if (this.Fd != null) {
         this.Fd.clearAnimations();
      }

      this.qN();
      this.EU = null;
      if (this.EV != null) {
         this.EV.clear();
      }

      if (this.ET != null) {
         this.ET.clear();
      }
   }

   public boolean bn(String var1) {
      if (this.Fd != null && !"base".equals(var1)) {
         boolean var2 = this.Fd.stopLayer(var1);
         if (var2) {
            this.Fd.forceRefresh();
         }

         return var2;
      } else {
         return false;
      }
   }

   public long bo(String var1) {
      if (!this.bp(var1)) {
         return -1L;
      }

      long var2 = this.qQ();
      TimedKeyPayload var4 = new TimedKeyPayload(var2, null, false);
      this.Fn.compute(var1, (var3, var4x) -> (TimedKey)(var4x != null && var4x.jN() >= var2 ? var4x : new TimedKey(var2, var4)));
      return var2;
   }

   public boolean method8927(String var1, long var2) {
      TimedKeyPayload var4 = new TimedKeyPayload(var2, null, false);
      if (!this.method8928(var1, var4)) {
         return false;
      }

      this.bn(var1);
      return true;
   }

   public boolean bp(String var1) {
      TimedKey var2 = (TimedKey)this.Fn.get(var1);
      TimedKeyPayload var3 = var2 == null ? null : var2.rk();
      return var3 != null ? var3.rm() : this.Fd != null && this.Fd.isLayerActive(var1);
   }

   private boolean method8928(String var1, TimedKeyPayload var2) {
      AtomicBoolean var3 = new AtomicBoolean();
      this.Fn.computeIfPresent(var1, (var2x, var3x) -> {
         if (!var2.equals(var3x.rk())) {
            return (TimedKey)var3x;
         }

         var3.set(true);
         return new TimedKey(var3x.jN(), null);
      });
      return var3.get();
   }

   @Nullable
   public Matrix4fc bq(String var1) {
      return this.Fd == null ? null : this.Fd.getBoneTransform(var1);
   }

   void method8929(ModelPartDisplay var1) {
      if (this.Fd != null) {
         this.Fd.applyPartState(var1);
      }
   }
}
