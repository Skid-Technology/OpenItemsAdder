package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.particle.ParticleEmitterBase;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import lombok.Generated;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.Nullable;

public class ModelDefinitionData {
   public static final byte yG = 1;
   public static final byte yH = 2;
   public static final byte yI = 4;
   public static final byte yJ = 7;
   public final String yK;
   public final String yL;
   public final AnimationState yM;
   public final boolean yN;
   public final long yO;
   public final int yP;
   public final double yQ;
   @Nullable
   public final IntPair yR;
   private final double yS;
   private final double yT;
   @Nullable
   private IntPair yU;
   private List yV;
   private char[] yW = new char[0];
   private boolean yX;
   private short[][] yY = new short[0][];
   private ModelBinding[] yZ = new ModelBinding[0];
   private MatrixTable[] za = new MatrixTable[0];
   private boolean[] zb = new boolean[0];
   private AutoRotationMode zc = AutoRotationMode.INHERIT;
   private int zd;
   @Nullable
   private BoneTransformTable ze;
   private byte[] zf = new byte[0];
   private int[] zg = new int[0];
   private byte[][] zh = new byte[0][];
   private int[][] zi = new int[0][];
   private String zj = "base";
   private int zk;
   private int zl;
   private int zm;
   private int zn;
   private int zo = 20;
   private final double zp;
   private boolean zq;
   private boolean zr;
   private AnimationVariable zs = AnimationVariable.NONE;
   private double zt = -1.0;
   private String zu = "";
   private int zv = 1;
   public LinkedList zw;
   public LinkedList zx;
   private List zy = new ArrayList();
   public boolean field114 = false;
   public boolean zA = false;
   public boolean zB = false;
   public boolean zC = true;
   public boolean zD = false;
   boolean zE = true;
   boolean zF = true;
   public boolean zG = true;
   public boolean zH = true;
   public boolean zI = true;
   public boolean zJ = true;
   public boolean zK = false;
   public boolean zL = false;
   @Nullable
   public Float zM;
   @Nullable
   public Float zN;
   @Nullable
   private SoundEffectSpec zO;

   public void setSelectionWeight(int var1) {
      if (var1 >= 1 && var1 <= 1000000) {
         this.zv = var1;
      } else {
         throw new IllegalArgumentException("Animation selectionWeight must be from 1 to 1000000");
      }
   }

   public List getTriggers() {
      return this.zy;
   }

   public int findTriggerIndex(double var1) {
      int var3 = 0;
      int var4 = this.zy.size();

      while (var3 < var4) {
         int var5 = var3 + var4 >>> 1;
         if (((TriggerRecord)this.zy.get(var5)).time() < var1) {
            var3 = var5 + 1;
         } else {
            var4 = var5;
         }
      }

      return var3;
   }

   public void addEmitterTrigger(double var1, ParticleEmitterBase var3) {
      this.zy.add(new TriggerRecord(var1, var3, null));
   }

   public void addTagTrigger(double var1, String var3) {
      this.zy.add(new TriggerRecord(var1, null, var3));
   }

   public double getLoopStart() {
      return this.yS;
   }

   public double getLoopEnd() {
      return this.yT;
   }

   public ModelDefinitionData(String var1, String var2, boolean var3, String var4, double var5, double var7, double var9) {
      this(var1, var2, var3, var4, var5, var7, var9, -1.0);
   }

   public ModelDefinitionData(String var1, String var2, boolean var3, String var4, double var5, double var7, double var9, double var11) {
      this(var1, var2, var3, var4, var5, var7, var9, var11, true);
   }

   public ModelDefinitionData(String var1, String var2, boolean var3, String var4, double var5, double var7, double var9, double var11, boolean var13) {
      this.yK = var1;
      this.yQ = var5;
      this.zC = var3;
      AnimationState var14 = AnimationState.fromString(var4);
      if (var14 == AnimationState.OTHER && var13) {
         var14 = AnimationState.fromString(var1);
      }

      this.yM = var14;
      if (this.yM == AnimationState.DEATH && var2.equals("loop")) {
         this.yL = "once";
         this.yN = false;
      } else {
         this.yL = var2;
         this.yN = var2.equals("loop");
      }

      double var15 = var5 * 20.0;
      if (var15 - (int)var15 > 0.0) {
         var15 = (int)var15 + 1;
      }

      this.yO = (long)var15;
      this.yP = (int)this.yO;
      this.yS = var7;
      this.yT = var9;
      this.zp = var11;
      int var17;
      int var18;
      if (this.yN) {
         if (var7 > 0.0) {
            var17 = (int)(var7 * 20.0);
         } else {
            var17 = -1;
         }

         if (var9 > 0.0) {
            var18 = (int)(var9 * 20.0);
         } else {
            var18 = -1;
         }
      } else {
         var17 = -1;
         var18 = -1;
      }

      if (this.yN && var17 > 0 && var18 > 0) {
         this.yR = new IntPair(var17, var18);
      } else {
         this.yR = null;
      }
   }

   public List getBindings() {
      return this.yV;
   }

   public List resolvePartBindings(CustomEntityImpl var1) {
      List var2 = Collections.synchronizedList(new ArrayList());

      for (int var3 = 0; var3 < this.yV.size(); var3++) {
         ModelBinding var4 = (ModelBinding)this.yV.get(var3);
         ModelPartDisplay var5 = var1.getModelPart(var4.AC.getId());
         if (var5 != null) {
            var2.add(new ModelPartBinding(var4, var5));
         }
      }

      return var2;
   }

   public void setBindings(List var1) {
      this.yV = var1;
   }

   public void setCharTable(char[] var1) {
      this.yW = var1;
      this.yX = true;
   }

   public int sampleCharTable(int var1, boolean var2) {
      if (this.yW.length == 0) {
         return 0;
      }

      int var3 = var2 ? this.yW.length - 1 : Math.min(Math.max(var1, 0), this.yW.length - 1);
      return this.yW[var3];
   }

   public void setShortTable(short[][] var1) {
      this.yY = var1;
   }

   public int sampleShortTable(int var1, int var2, boolean var3) {
      if (var1 >= 0 && var1 < this.yY.length && this.yY[var1] != null && this.yY[var1].length != 0) {
         short[] var4 = this.yY[var1];
         return var4[var3 ? var4.length - 1 : Math.min(Math.max(var2, 0), var4.length - 1)];
      } else {
         return -1;
      }
   }

   @Nullable
   public Integer sampleCharTableOrNull(int var1, boolean var2) {
      return this.yX ? this.sampleCharTable(var1, var2) : null;
   }

   public boolean isLooping() {
      return this.yX;
   }

   public void configureFromFlags(String var1, int var2, double var3, double var5, boolean[] var7, ModelBinding[] var8) {
      byte[] var9 = new byte[var7.length];

      for (int var10 = 0; var10 < var7.length; var10++) {
         if (var7[var10]) {
            var9[var10] = 7;
         }
      }

      this.configure(var1, var2, var3, var5, var9, var8);
   }

   public void configure(String var1, int var2, double var3, double var5, byte[] var7, ModelBinding[] var8) {
      this.zj = var1;
      this.zk = var2;
      this.zl = (int)Math.ceil(var3 * 20.0);
      this.zm = (int)Math.ceil(var5 * 20.0);
      this.zf = (byte[])var7.clone();
      this.yZ = (ModelBinding[])var8.clone();
      this.zb = new boolean[var8.length];

      for (int var9 = 0; var9 < var8.length; var9++) {
         ModelBinding var10 = var8[var9];
         if (var10 != null && var10.getPartCount() >= 2) {
            ModelPartTransform var11 = var10.getPart(0);

            for (int var12 = 1; var12 < var10.getPartCount(); var12++) {
               if (var10.getPart(var12) != var11) {
                  this.zb[var9] = true;
                  break;
               }
            }
         }
      }

      int var15 = 0;

      for (byte var13 : this.zf) {
         if (var13 != 0) {
            var15++;
         }
      }

      this.zg = new int[var15];
      int var17 = 0;

      for (int var19 = 0; var19 < this.zf.length; var19++) {
         if (this.zf[var19] != 0) {
            this.zg[var17++] = var19;
         }
      }

      for (ModelBinding var14 : var8) {
         if (var14 != null) {
            this.zn = Math.max(this.zn, var14.getPartCount());
         }
      }
   }

   public void setInterpolation(String var1, int var2) {
      this.zc = switch (var1) {
         case "automatic" -> AutoRotationMode.AUTOMATIC;
         case "manual" -> AutoRotationMode.MANUAL;
         default -> throw new IllegalArgumentException("Unknown display interpolation mode: " + var1);
      };
      if (var2 >= 1 && var2 <= 8) {
         this.zd = var2;
      } else {
         throw new IllegalArgumentException("Display interpolation interval must be from 1 to 8");
      }
   }

   public AutoRotationMode getAutoRotation() {
      return this.zc;
   }

   public int getFrameCount() {
      return this.zd;
   }

   public boolean isMultiPartBone(int var1) {
      return var1 >= 0 && var1 < this.zb.length && this.zb[var1];
   }

   public void useDefaultFrameRate() {
      this.setFrameRate(20);
   }

   public void setFrameRate(int var1) {
      this.zo = var1;
      if (this.yN && !(this.yS < 0.0) && !(this.yT < 0.0)) {
         this.yU = new IntPair(this.toFrameIndexCeil(this.yS), this.toFrameIndexCeil(this.yT));
      }
   }

   @Nullable
   public IntPair getLoopFrames() {
      return this.yU;
   }

   private int toFrameIndexCeil(double var1) {
      return (int)Math.ceil(var1 * this.zo - 1.0E-9);
   }

   public int getFrameRate() {
      return this.zo;
   }

   public int timeToFrameIndex(double var1) {
      return var1 >= this.yQ - 1.0E-9 ? this.zn - 1 : (int)Math.floor(var1 * this.zo + 1.0E-9);
   }

   public void setMatrixTables(MatrixTable[] var1) {
      this.za = (MatrixTable[])var1.clone();
   }

   public void buildBoneTable(List var1) {
      this.ze = new BoneTransformTable(this, var1);
   }

   @Nullable
   public BoneTransformTable getBoneTable() {
      return this.ze;
   }

   @Nullable
   public MatrixTable getMatrixTable(int var1) {
      return var1 >= 0 && var1 < this.za.length ? this.za[var1] : null;
   }

   public void setBoneStates(byte[][] var1, int[][] var2) {
      this.zh = (byte[][])var1.clone();
      this.zi = (int[][])var2.clone();
   }

   public String getLayer() {
      return this.zj;
   }

   public int getPriority() {
      return this.zk;
   }

   public int getDurationTicks() {
      return this.zl;
   }

   public int getAuxDurationTicks() {
      return this.zm;
   }

   public int getMaxPartCount() {
      return this.zn;
   }

   public void setVariable(@Nullable String var1) {
      this.setVariableWithRange(var1, -1.0);
   }

   public void setVariableWithRange(@Nullable String var1, double var2) {
      this.zs = AnimationVariable.fromString(var1);
      this.zt = var2;
   }

   public AnimationVariable getVariable() {
      return this.zs;
   }

   public void setName(@Nullable String var1) {
      this.zu = var1 == null ? "" : var1;
   }

   public String getName() {
      return this.zu;
   }

   public boolean isBoneAnimated(int var1) {
      return this.getBoneFlag(var1) != 0;
   }

   public int getAnimatedBoneCount() {
      return this.zg.length;
   }

   public int getAnimatedBoneIndex(int var1) {
      return this.zg[var1];
   }

   public byte getBoneFlag(int var1) {
      return var1 >= 0 && var1 < this.zf.length ? this.zf[var1] : 0;
   }

   public byte[] getPackedBytes() {
      return (byte[])this.zf.clone();
   }

   public boolean isWalkingState() {
      return this.yM == AnimationState.WALK
         || this.yM == AnimationState.WALK_ANGRY
         || this.yM == AnimationState.SNEAK_WALK
         || this.yM == AnimationState.FLY
         || this.yM == AnimationState.SWIM;
   }

   public double getLength() {
      return this.zp;
   }

   public void setPlaybackFlags(boolean var1, boolean var2) {
      this.zq = var1;
      this.zr = var2;
   }

   public boolean isLocomotionDriven() {
      return this.isWalkingState() && !this.zq;
   }

   public boolean isLocomotionPaused() {
      return this.isWalkingState() && this.zq;
   }

   public boolean isStoppable() {
      return this.zr;
   }

   public double wrapTime(double var1) {
      int var3 = Math.max(1, this.zn - 1);
      double var4 = var1 * var3;
      if (this.yU == null) {
         return Math.floorMod((long)Math.floor(var4), var3) + var4 - Math.floor(var4);
      } else {
         int var6 = this.yU.zU;
         int var7 = this.yU.zV;
         if (var4 < 0.0) {
            double var8 = (var4 - var6) % (var7 - var6);
            return var6 + (var8 < 0.0 ? var8 + var7 - var6 : var8);
         } else {
            return var4 < var7 ? var4 : var6 + (var4 - var7) % (var7 - var6);
         }
      }
   }

   public boolean applyBoneAt(int var1, double var2, TransformBuilder var4, TransformBuilder var5) {
      ModelBinding var6 = this.getBindingAt(var1);
      return var6 != null && this.interpolateBinding(var6, this.wrapTime(var2), var4, var5);
   }

   public boolean applyBoneAtRaw(int var1, double var2, TransformBuilder var4, TransformBuilder var5) {
      ModelBinding var6 = this.getBindingAt(var1);
      return var6 != null && this.interpolateBinding(var6, var2, var4, var5);
   }

   @Nullable
   private ModelBinding getBindingAt(int var1) {
      if (this.isBoneAnimated(var1) && var1 >= 0 && var1 < this.yZ.length) {
         ModelBinding var2 = this.yZ[var1];
         return var2 != null && var2.getPartCount() != 0 ? var2 : null;
      } else {
         return null;
      }
   }

   private boolean interpolateBinding(ModelBinding var1, double var2, TransformBuilder var4, TransformBuilder var5) {
      int var6 = Math.min((int)Math.floor(var2), var1.getPartCount() - 1);
      int var7 = var6 + 1;
      int var8 = Math.max(1, var1.getPartCount() - 1);
      if (this.yU != null && var7 >= this.yU.zV) {
         var7 = this.yU.zU;
      } else if (var7 >= var8) {
         var7 = this.yU == null ? 0 : this.yU.zU;
      }

      ModelPartTransform var9 = var1.getPart(var6);
      ModelPartTransform var10 = var1.getPart(var7);
      if (var9 != null && var9.Db != null) {
         double var11 = var2 - Math.floor(var2);
         if (!(var11 <= 1.0E-12) && var10 != null && var10.Db != null) {
            var9.Db.applyTo(var5);
            var4.method8665(var5, var10.Db, (float)var11);
         } else {
            var9.Db.applyTo(var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean applyBoneClamped(int var1, double var2, TransformBuilder var4, TransformBuilder var5) {
      if (this.isBoneAnimated(var1) && var1 >= 0 && var1 < this.yZ.length) {
         ModelBinding var6 = this.yZ[var1];
         if (var6 != null && var6.getPartCount() != 0) {
            int var7 = var6.getPartCount() - 1;
            double var8;
            if (this.zs == AnimationVariable.AGE && this.zt > 0.0 && var7 > 0) {
               double var10 = var2 - Math.floor(var2 / this.zt) * this.zt;
               var8 = var10 / this.zt * var7;
            } else if (this.zs == AnimationVariable.AGE) {
               var8 = Math.max(0.0, Math.min(var2, var7));
            } else {
               var8 = Math.max(0.0, Math.min(var2, 1.0)) * var7;
            }

            int var16 = Math.min((int)Math.floor(var8), var7);
            int var11 = Math.min(var16 + 1, var7);
            ModelPartTransform var12 = var6.getPart(var16);
            ModelPartTransform var13 = var6.getPart(var11);
            if (var12 != null && var12.Db != null) {
               double var14 = var8 - Math.floor(var8);
               if (!(var14 <= 1.0E-12) && var13 != null && var13.Db != null) {
                  var12.Db.applyTo(var5);
                  var4.method8665(var5, var13.Db, (float)var14);
               } else {
                  var12.Db.applyTo(var4);
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Nullable
   public TransformDecomposition getPartDecomposition(int var1, int var2, boolean var3) {
      if (var1 >= 0 && var1 < this.yZ.length) {
         ModelBinding var4 = this.yZ[var1];
         if (var4 != null && var4.getPartCount() != 0) {
            int var5 = var3 ? var4.getPartCount() - 1 : Math.min(Math.max(var2, 0), var4.getPartCount() - 1);
            ModelPartTransform var6 = var4.getPart(var5);
            return var6 == null ? null : var6.Db;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Nullable
   public Boolean sampleBoneFlag(int var1, int var2, boolean var3) {
      if (var1 >= 0 && var1 < this.zh.length && this.zh[var1] != null) {
         byte[] var4 = this.zh[var1];
         int var5 = var3 ? var4.length - 1 : Math.min(Math.max(var2, 0), var4.length - 1);
         return var4[var5] < 0 ? null : var4[var5] != 0;
      } else {
         return null;
      }
   }

   public boolean hasBoneFlags(int var1) {
      return var1 >= 0 && var1 < this.zh.length && this.zh[var1] != null;
   }

   public int sampleBoneIndex(int var1, int var2, boolean var3) {
      if (var1 >= 0 && var1 < this.zi.length && this.zi[var1] != null) {
         int[] var4 = this.zi[var1];
         int var5 = var3 ? var4.length - 1 : Math.min(Math.max(var2, 0), var4.length - 1);
         return var4[var5];
      } else {
         return -1;
      }
   }

   public boolean hasBoneIndices(int var1) {
      return var1 >= 0 && var1 < this.zi.length && this.zi[var1] != null;
   }

   public ModelBinding getBindingById(String var1) {
      for (ModelBinding var3 : this.yV) {
         if (var3.AC.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public void setEmitters(LinkedList var1, LinkedList var2) {
      this.zy.sort(Comparator.comparingDouble(TriggerRecord::time));
      this.zy = List.copyOf(this.zy);
      this.zw = var1;
      this.zx = var2;
   }

   public boolean hasEntityEmitters() {
      return !this.zx.isEmpty();
   }

   public boolean hasOffsetEmitters() {
      return !this.zw.isEmpty();
   }

   public boolean hasLoopRange() {
      return this.yR != null;
   }

   public void setFlags(boolean var1, boolean var2, boolean var3) {
      this.field114 = var1;
      this.zA = var2;
      this.zB = var3;
   }

   public void ac(boolean var1) {
      this.zC = var1;
   }

   public void method8728(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.zG = var1;
      this.zH = var2;
      this.zI = var3;
      this.zJ = var4;
   }

   public void ad(boolean var1) {
      this.zK = var1;
   }

   public void ae(boolean var1) {
      this.zL = var1;
   }

   public void setOptionalRange(@Nullable Float var1, @Nullable Float var2) {
      this.zM = var1;
      this.zN = var2;
   }

   public void setSound(@Nullable String var1, SoundCategory var2, float var3, float var4, float var5) {
      this.zO = var1 == null ? null : new SoundEffectSpec(var1, var2, var3, var4, var5);
   }

   @Generated
   public int getSelectionWeight() {
      return this.zv;
   }

   @Generated
   public void af(boolean var1) {
      this.zD = var1;
   }

   @Generated
   public void ag(boolean var1) {
      this.zE = var1;
   }

   @Generated
   public boolean nh() {
      return this.zE;
   }

   @Generated
   public void ah(boolean var1) {
      this.zF = var1;
   }

   @Generated
   public boolean ni() {
      return this.zF;
   }

   @Nullable
   @Generated
   public SoundEffectSpec getSound() {
      return this.zO;
   }
}
