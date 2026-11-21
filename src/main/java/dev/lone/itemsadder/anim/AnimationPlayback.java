package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.api.AnimationPlaybackOptions;
import dev.lone.itemsadder.entity.AnimationState;
import dev.lone.itemsadder.entity.ModelDefinitionData;
import dev.lone.itemsadder.entity.ModelInstance;
import dev.lone.itemsadder.entity.TransformBuilder;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class AnimationPlayback {
   @Nullable
   final ModelInstance field124;
   final ModelDefinitionData LM;
   long LN;
   long LO;
   boolean LP;
   private ParticleEmitterManager ES;
   private final List LQ = new ArrayList();
   final long LR;
   @Nullable
   TransformBuilder[] LS;
   @Nullable
   byte[] LT;
   @Nullable
   private int[] LU;
   @Nullable
   byte[] LV;
   @Nullable
   int[] LW;
   @Nullable
   Integer LX;
   @Nullable
   int[] LY;
   @Nullable
   boolean[] LZ;
   @Nullable
   boolean[] Ma;
   boolean Mb;
   final int Mc;
   final boolean Md;
   @Nullable
   Runnable EX;
   int EZ;
   int Fa;
   long pB;
   int Me;
   int Mf;
   int Mg;
   int Mh;
   int Mi;
   boolean Mj;
   boolean FE = true;
   boolean Mk;
   @Nullable
   PlaybackCursor Ml;
   boolean Mm;
   @Nullable
   TransformBuilder[] Mn;
   @Nullable
   byte[] Mo;
   boolean Mp;
   boolean Mq = true;
   boolean Mr;
   boolean Ms;
   private int Mt = -1;

   ParticleEmitterManager qA() {
      if (this.ES == null) {
         this.ES = new ParticleEmitterManager();
      }

      return this.ES;
   }

   void qB() {
      this.LQ.forEach(var0 -> var0.LK.close());
      this.LQ.clear();
      if (this.ES != null) {
         this.ES.close();
         this.ES = null;
      }
   }

   AnimationPlayback(
      @Nullable ModelInstance var1,
      int var2,
      ModelDefinitionData var3,
      int var4,
      @Nullable Runnable var5,
      long var6,
      @Nullable TransformBuilder[] var8,
      int var9,
      @Nullable byte[] var10,
      @Nullable int[] var11,
      @Nullable Integer var12,
      boolean var13
   ) {
      this.field124 = var1;
      this.LM = var3;
      this.EZ = var4;
      this.EX = var5;
      this.LR = var6;
      this.LS = var8;
      this.Mc = var9;
      this.Md = var13;
      this.LV = var10;
      this.LW = var11;
      this.LX = var12;
      this.LZ = var10 == null ? null : new boolean[var2];
      this.Ma = var11 == null ? null : new boolean[var2];

      for (int var14 = 0; var14 < var2; var14++) {
         if (this.LZ != null) {
            this.LZ[var14] = var10[var14] >= 0;
         }

         if (this.Ma != null) {
            this.Ma[var14] = var11[var14] >= 0;
         }
      }

      this.Mb = var12 != null;
      this.method9068(AnimationPlaybackOptions.DEFAULT);
   }

   void method9068(AnimationPlaybackOptions var1) {
      this.Ms = var1.equals(AnimationPlaybackOptions.DEFAULT);
      this.Mr = this.LM.isLocomotionPaused()
         && this.LM.yM != AnimationState.FLY
         && this.LM.yM != AnimationState.SWIM
         && var1.equals(AnimationPlaybackOptions.DEFAULT);
      if (!this.LM.isLocomotionDriven()) {
         if (!var1.equals(AnimationPlaybackOptions.DEFAULT) || this.LM.getFrameRate() != 20 || !this.LM.getTriggers().isEmpty()) {
            this.Ml = new PlaybackCursor(this.LM.yQ, this.LM.getLoopStart(), this.LM.getLoopEnd(), var1);
            this.sS();
         }
      }
   }

   private void sS() {
      this.Fa = this.LM.timeToFrameIndex(this.Ml.zW);
   }

   int sT() {
      return this.Ml == null ? this.Fa : (this.Ml.zW >= this.LM.yQ - 1.0E-9 ? (int)Math.ceil(this.LM.yQ * 20.0) : (int)Math.floor(this.Ml.zW * 20.0 + 1.0E-9));
   }

   float rp() {
      return this.Mc == 0 ? 1.0F : Math.min(1.0F, (float)this.Me / this.Mc);
   }

   float sU() {
      int var1 = this.LM.getAuxDurationTicks();
      return this.Mm && var1 != 0 ? Math.max(0.0F, 1.0F - (float)this.Mf / var1) : (this.Mm ? 0 : 1);
   }

   float sV() {
      return this.Mc == 0 ? 1.0F : Math.min(1.0F, (float)this.Mh / this.Mc);
   }

   float sW() {
      int var1 = this.LM.getAuxDurationTicks();
      return this.Mj && var1 != 0 ? Math.max(0.0F, 1.0F - (float)this.Mi / var1) : (this.Mj ? 0 : 1);
   }

   void sX() {
      this.Mg = this.Fa;
      this.Mh = this.Me;
      this.Mi = this.Mf;
      this.Mj = this.Mm;
   }

   boolean sY() {
      return this.Ml == null && !this.FE && ("hold".equals(this.LM.yL) || this.Fa >= this.LM.getMaxPartCount() - 1);
   }

   boolean sZ() {
      return this.FE && !this.Mk && (this.LM.getMaxPartCount() > 1 || this.pB == 0L) && (!this.LM.isWalkingState() || this.Mq);
   }

   int ta() {
      int var1 = this.Mt;
      this.Mt = -1;
      return var1;
   }

   void method9069(double var1) {
      int var3 = (int)Math.floor(var1);
      this.Mq = var3 != this.Fa;
      this.Fa = var3;
   }

   boolean af(int var1) {
      return this.LV != null && this.LZ != null && this.LZ[var1];
   }

   boolean ag(int var1) {
      return this.LW != null && this.Ma != null && this.Ma[var1];
   }

   int ah(int var1) {
      return this.LT != null && var1 >= 0 && var1 < this.LT.length ? this.LT[var1] : 0;
   }

   void method9070(@Nullable byte[] var1) {
      this.LT = var1;
      if (var1 == null) {
         this.LU = null;
      } else {
         int var2 = 0;
         int var3 = Math.max(var1.length, this.LM.getAnimatedBoneCount() == 0 ? 0 : this.LM.getAnimatedBoneIndex(this.LM.getAnimatedBoneCount() - 1) + 1);

         for (int var4 = 0; var4 < var3; var4++) {
            if (this.ah(var4) != 0 || this.LM.isBoneAnimated(var4)) {
               var2++;
            }
         }

         this.LU = new int[var2];
         int var6 = 0;

         for (int var5 = 0; var5 < var3; var5++) {
            if (this.ah(var5) != 0 || this.LM.isBoneAnimated(var5)) {
               this.LU[var6++] = var5;
            }
         }
      }
   }

   int mW() {
      return this.LU == null ? this.LM.getAnimatedBoneCount() : this.LU.length;
   }

   int method9071(int var1) {
      return this.LU == null ? this.LM.getAnimatedBoneIndex(var1) : this.LU[var1];
   }

   boolean method9072(boolean var1, boolean var2) {
      return this.tb() | this.method9073(var1, var2);
   }

   boolean tb() {
      if (this.Mk && !this.Mm) {
         return false;
      }

      boolean var1 = false;
      if (!this.Mm && this.Me < this.Mc) {
         this.Me++;
         var1 = true;
      }

      if (!this.Mm && this.Me >= this.Mc) {
         this.LS = null;
         this.LT = null;
         this.LU = null;
         this.LV = null;
         this.LW = null;
         this.LX = null;
         this.LY = null;
         this.LZ = null;
         this.Ma = null;
         this.Mb = false;
      }

      if (this.Mm) {
         this.Mf++;
         var1 = true;
      }

      return var1;
   }

   boolean method9073(boolean var1, boolean var2) {
      return this.method9074(var1, var2, false);
   }

   boolean method9074(boolean var1, boolean var2, boolean var3) {
      int var4 = this.LM.getMaxPartCount();
      if (!this.FE || this.Mk) {
         return false;
      }

      if (this.Ml != null && var4 > 1) {
         this.pB++;
         int var8 = this.Fa;
         if (this.Ml.method9136((this.LM.yN || this.LM.getLoopFrames() != null) && !var2, this.Mr ? var3 : this.Ml.QI)) {
            this.LN = this.LN + this.Ml.ul();
            this.sS();
            return var8 != this.Fa;
         }

         if (this.EZ > 1) {
            this.EZ--;
            this.pB = 0L;
            this.Ml = new PlaybackCursor(
               this.LM.yQ, this.LM.getLoopStart(), this.LM.getLoopEnd(), new AnimationPlaybackOptions(this.Ml.QJ * 20.0, this.Ml.QI, this.Ml.QE, this.Ml.QF)
            );
            this.sS();
            return true;
         }

         this.FE = false;
         if (!"hold".equals(this.LM.yL)) {
            this.LN++;
         }

         this.tc();
         if (var1 && !"hold".equals(this.LM.yL)) {
            this.Mm = true;
         }

         return true;
      } else if (var4 > 1) {
         this.pB++;
         IntPair var5 = this.LM.getLoopFrames();
         if (this.Mr && var3 && !var2) {
            int var6 = var5 == null ? 0 : var5.zU;
            int var7 = var5 == null ? var4 - 1 : var5.zV;
            this.Fa = this.Fa <= var6 ? Math.max(var6, var7 - 1) : this.Fa - 1;
            if (this.Fa == Math.max(var6, var7 - 1)) {
               this.LN++;
            }

            return true;
         } else {
            if (var5 != null && !var2 && this.Fa >= var5.zU && this.Fa + 1 >= var5.zV) {
               this.Fa = var5.zU;
               this.LN++;
               return true;
            }

            if (this.LM.yN && var5 == null && !var2 && this.Fa + 1 >= var4 - 1) {
               this.Mt = var4 - 1;
               this.LN++;
               this.Fa = 0;
               return true;
            }

            if (this.Fa + 1 < var4) {
               this.Fa++;
               return true;
            }

            if (this.EZ > 1) {
               this.EZ--;
               this.Fa = 0;
               this.pB = 0L;
               return true;
            }

            this.FE = false;
            if (!"hold".equals(this.LM.yL)) {
               this.LN++;
            }

            this.tc();
            if (var1 && !"hold".equals(this.LM.yL)) {
               this.Mm = true;
            }

            return true;
         }
      } else if (this.LM.yN && !var2) {
         this.pB++;
         if (this.pB % Math.max(1L, this.LM.yO) == 0L) {
            this.LN++;
         }

         return false;
      } else {
         this.FE = false;
         if (!"hold".equals(this.LM.yL)) {
            this.LN++;
         }

         this.tc();
         if (var1 && !"hold".equals(this.LM.yL)) {
            this.Mm = true;
         }

         return true;
      }
   }

   void tc() {
      this.qB();
      if (!this.Mp) {
         this.Mp = true;
         if (this.Md && this.field124 != null) {
            this.field124.Fl = true;
         }

         if (this.EX != null) {
            this.EX.run();
         }
      }
   }

   boolean td() {
      return this.Mm && this.sU() <= 0.0F;
   }
}
