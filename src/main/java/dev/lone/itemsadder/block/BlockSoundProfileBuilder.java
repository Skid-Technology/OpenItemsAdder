package dev.lone.itemsadder.block;

import com.google.common.collect.ImmutableMap;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.FloatVector3Base;
import lombok.Generated;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

@Generated
public class BlockSoundProfileBuilder {
   @Generated
   private String namespacedId;
   @Generated
   private CustomItemDefinition jQ;
   @Generated
   private String jR;
   @Generated
   private String jS;
   @Generated
   private String jT;
   @Generated
   private FloatVector3Base jU;
   @Generated
   private ImmutableMap jV;
   @Generated
   private int jW;
   @Generated
   private String jX;
   @Generated
   private Material jY;
   @Generated
   private boolean jZ;
   @Generated
   private boolean ka;
   @Generated
   private boolean kq;
   @Generated
   private double kr;
   @Generated
   private boolean ks;
   @Generated
   private int kt;
   @Generated
   private Integer kd;
   @Generated
   private boolean ku;
   @Generated
   private float kv;
   @Generated
   private boolean kw;
   @Generated
   private double kx;
   @Generated
   private boolean ky;
   @Generated
   private float kz;
   @Generated
   private boolean kA;
   @Generated
   private float kB;
   @Generated
   private boolean kC;
   @Generated
   private float kD;
   @Generated
   private boolean kE;
   @Generated
   private boolean kF;
   @Generated
   private ImmutableMap field46;

   @Generated
   BlockSoundProfileBuilder() {
   }

   @Generated
   public BlockSoundProfileBuilder withNamespacedId(String var1) {
      this.namespacedId = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withSeed(CustomItemDefinition var1) {
      this.jQ = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withPlaceSound(String var1) {
      this.jR = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withBreakSound(String var1) {
      this.jS = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withInteractSound(String var1) {
      this.jT = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withHitbox(FloatVector3Base var1) {
      this.jU = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withAgeStatuses(ImmutableMap var1) {
      this.jV = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withMaxAge(int var1) {
      this.jW = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withRequiredCustomBottomBlockId(String var1) {
      this.jX = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withRequireBottomBlockMaterial(Material var1) {
      this.jY = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withRequiresFarmlandBottom(boolean var1) {
      this.jZ = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withUsesGameCropModel(boolean var1) {
      this.ka = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withAvgSecondsPerStage(double var1) {
      this.kr = var1;
      this.kq = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withMinLight(int var1) {
      this.kt = var1;
      this.ks = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withRequiredSpacing(@Nullable Integer var1) {
      this.kd = var1;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withBonemealGrowthMultiplier(float var1) {
      this.kv = var1;
      this.ku = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withMaxGrowthMoistureMultiplier(double var1) {
      this.kx = var1;
      this.kw = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withRainGrowthMultiplier(float var1) {
      this.kz = var1;
      this.ky = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withThunderGrowthMultiplier(float var1) {
      this.kB = var1;
      this.kA = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withSnowGrowthMultiplier(float var1) {
      this.kD = var1;
      this.kC = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withDropSeedIfImmature(boolean var1) {
      this.kF = var1;
      this.kE = true;
      return this;
   }

   @Generated
   public BlockSoundProfileBuilder withLoots(@Nullable ImmutableMap var1) {
      this.field46 = var1;
      return this;
   }

   @Generated
   public BlockSoundProfile build() {
      double var1 = this.kr;
      if (!this.kq) {
         var1 = BlockSoundProfile.defaultAvgSecondsPerStage();
      }

      int var3 = this.kt;
      if (!this.ks) {
         var3 = BlockSoundProfile.defaultMinLight();
      }

      float var4 = this.kv;
      if (!this.ku) {
         var4 = BlockSoundProfile.defaultBonemealGrowthMultiplier();
      }

      double var5 = this.kx;
      if (!this.kw) {
         var5 = BlockSoundProfile.defaultMaxGrowthMoistureMultiplier();
      }

      float var7 = this.kz;
      if (!this.ky) {
         var7 = BlockSoundProfile.defaultRainGrowthMultiplier();
      }

      float var8 = this.kB;
      if (!this.kA) {
         var8 = BlockSoundProfile.defaultThunderGrowthMultiplier();
      }

      float var9 = this.kD;
      if (!this.kC) {
         var9 = BlockSoundProfile.defaultSnowGrowthMultiplier();
      }

      boolean var10 = this.kF;
      if (!this.kE) {
         var10 = BlockSoundProfile.defaultDropSeedIfImmature();
      }

      return new BlockSoundProfile(
         this.namespacedId,
         this.jQ,
         this.jR,
         this.jS,
         this.jT,
         this.jU,
         this.jV,
         this.jW,
         this.jX,
         this.jY,
         this.jZ,
         this.ka,
         var1,
         var3,
         this.kd,
         var4,
         var5,
         var7,
         var8,
         var9,
         var10,
         this.field46
      );
   }

   @Generated
   @Override
   public String toString() {
      return "CropInfo.CropInfoBuilder(namespacedId="
         + this.namespacedId
         + ", seed="
         + this.jQ
         + ", placeSound="
         + this.jR
         + ", breakSound="
         + this.jS
         + ", interactSound="
         + this.jT
         + ", hitbox="
         + this.jU
         + ", ageStatuses="
         + this.jV
         + ", maxAge="
         + this.jW
         + ", requiredCustomBottomBlockId="
         + this.jX
         + ", requireBottomBlockMaterial="
         + this.jY
         + ", requiresFarmlandBottom="
         + this.jZ
         + ", usesGameCropModel="
         + this.ka
         + ", avgSecondsPerStage$value="
         + this.kr
         + ", minLight$value="
         + this.kt
         + ", requiredSpacing="
         + this.kd
         + ", bonemealGrowthMultiplier$value="
         + this.kv
         + ", maxGrowthMoistureMultiplier$value="
         + this.kx
         + ", rainGrowthMultiplier$value="
         + this.kz
         + ", thunderGrowthMultiplier$value="
         + this.kB
         + ", snowGrowthMultiplier$value="
         + this.kD
         + ", dropSeedIfImmature$value="
         + this.kF
         + ", loots="
         + this.field46
         + ")";
   }
}
