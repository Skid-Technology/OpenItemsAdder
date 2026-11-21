package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.config.YamlConfig;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

@Generated
public class CustomMobSettingsBuilder {
   @Generated
   private CustomEntityModel vn;
   @Generated
   private BoneStateList vo;
   @Generated
   private YamlConfig vp;
   @Generated
   private YamlConfig vq;
   @Generated
   private String vr;
   @Generated
   private boolean vs;
   @Generated
   private Double vt;
   @Generated
   private boolean vu;
   @Generated
   private Double field109;
   @Generated
   private EntityType vw;
   @Generated
   private boolean field110;
   @Generated
   private BarStyle wx;
   @Generated
   private boolean wy;
   @Generated
   private BarColor wz;
   @Generated
   private boolean vz;
   @Generated
   private Double vA;
   @Generated
   private Double vB;
   @Generated
   private Float vC;
   @Generated
   private Double vD;
   @Generated
   private boolean vE;
   @Generated
   private boolean vF;
   @Generated
   private boolean vG;
   @Generated
   private boolean vH;
   @Generated
   private boolean vI;
   @Generated
   private boolean vJ;
   @Generated
   private boolean vK;
   @Generated
   private boolean vL;
   @Generated
   private double vM;
   @Generated
   private boolean wA;
   @Generated
   private ControlScheme wB;
   @Generated
   private boolean vO;
   @Generated
   private boolean vP;
   @Generated
   private Boolean vQ;
   @Generated
   private Boolean vR;
   @Generated
   private boolean wC;
   @Generated
   private Map wD;
   @Generated
   private boolean wE;
   @Generated
   private Map wF;
   @Generated
   private boolean vU;
   @Generated
   private Integer vV;
   @Generated
   private Boolean vW;
   @Generated
   private boolean wG;
   @Generated
   private List wH;
   @Generated
   private boolean wI;
   @Generated
   private boolean wJ;
   @Generated
   private boolean vZ;
   @Generated
   private boolean wa;
   @Generated
   private boolean wb;
   @Generated
   private boolean wc;
   @Generated
   private boolean wd;
   @Generated
   private boolean wK;
   @Generated
   private boolean wL;
   @Generated
   private boolean wM;
   @Generated
   private SpawnLimits wN;
   @Generated
   private boolean wO;
   @Generated
   private double wP;
   @Generated
   private MobAttributeEntry wh;
   @Generated
   private String wi;
   @Generated
   private String wj;
   @Generated
   private String wk;
   @Generated
   private String wl;

   @Generated
   CustomMobSettingsBuilder() {
   }

   @Generated
   public CustomMobSettingsBuilder withModel(CustomEntityModel var1) {
      this.vn = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withDamageableParts(@Nullable BoneStateList var1) {
      this.vo = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withConfigFile(@Nullable YamlConfig var1) {
      this.vp = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withGoalsConfigFile(@Nullable YamlConfig var1) {
      this.vq = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withDisplayName(String var1) {
      this.vr = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withShowNameTag(boolean var1) {
      this.vs = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withNameTagYOffset(@Nullable Double var1) {
      this.vt = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withHasBossbar(boolean var1) {
      this.vu = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withBossBarDistance(@Nullable Double var1) {
      this.field109 = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withEntityType(EntityType var1) {
      this.vw = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withBossBarStyle(BarStyle var1) {
      this.wx = var1;
      this.field110 = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withBossBarColor(BarColor var1) {
      this.wz = var1;
      this.wy = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withJumpOnAttack(boolean var1) {
      this.vz = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withMaxHealth(@Nullable Double var1) {
      this.vA = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withRenderDistance(@Nullable Double var1) {
      this.vB = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withFlyingSpeed(@Nullable Float var1) {
      this.vC = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withMovementSpeed(@Nullable Double var1) {
      this.vD = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSilent(boolean var1) {
      this.vE = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withCanSunBurn(boolean var1) {
      this.vF = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withCanDropLoot(boolean var1) {
      this.vG = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withMountOnInteract(boolean var1) {
      this.vH = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withShadow(boolean var1) {
      this.vI = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withInvulnerable(boolean var1) {
      this.vJ = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withGravity(boolean var1) {
      this.vK = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withFloatOnWater(boolean var1) {
      this.vL = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withWaterlineOffset(double var1) {
      this.vM = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withVehicleControls(ControlScheme var1) {
      this.wB = var1;
      this.wA = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withAi(boolean var1) {
      this.vO = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withVanillaDefaults(boolean var1) {
      this.vP = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withItemPickup(@Nullable Boolean var1) {
      this.vQ = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withRaidParticipation(@Nullable Boolean var1) {
      this.vR = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withDamageModifiers(Map var1) {
      this.wD = var1;
      this.wC = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withNativeAttributes(Map var1) {
      this.wF = var1;
      this.wE = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withAgeVisualScale(boolean var1) {
      this.vU = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withInitialAgeTicks(@Nullable Integer var1) {
      this.vV = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withInitialBaby(@Nullable Boolean var1) {
      this.vW = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withAgeStages(List var1) {
      this.wH = var1;
      this.wG = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withBinaryAgeStages(boolean var1) {
      this.wJ = var1;
      this.wI = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSilentConfigured(boolean var1) {
      this.vZ = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withGravityConfigured(boolean var1) {
      this.wa = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withInvulnerableConfigured(boolean var1) {
      this.wb = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withAiConfigured(boolean var1) {
      this.wc = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withRagdollOnDeath(boolean var1) {
      this.wd = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withVanillaDeathAnimation(boolean var1) {
      this.wL = var1;
      this.wK = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withClientInterpolation(SpawnLimits var1) {
      this.wN = var1;
      this.wM = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withEyesHeight(double var1) {
      this.wP = var1;
      this.wO = true;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSoundAmbient(@Nullable MobAttributeEntry var1) {
      this.wh = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSoundAttack(@Nullable String var1) {
      this.wi = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSoundHurt(@Nullable String var1) {
      this.wj = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withSoundDeath(@Nullable String var1) {
      this.wk = var1;
      return this;
   }

   @Generated
   public CustomMobSettingsBuilder withAllActionsPermission(@Nullable String var1) {
      this.wl = var1;
      return this;
   }

   @Generated
   public CustomMobSettings build() {
      BarStyle var1 = this.wx;
      if (!this.field110) {
         var1 = CustomMobSettings.getBossBarStyle();
      }

      BarColor var2 = this.wz;
      if (!this.wy) {
         var2 = CustomMobSettings.getBossBarColor();
      }

      ControlScheme var3 = this.wB;
      if (!this.wA) {
         var3 = CustomMobSettings.getControlScheme();
      }

      Map var4 = this.wD;
      if (!this.wC) {
         var4 = CustomMobSettings.defaultDamageModifiers();
      }

      Map var5 = this.wF;
      if (!this.wE) {
         var5 = CustomMobSettings.defaultNativeAttributes();
      }

      List var6 = this.wH;
      if (!this.wG) {
         var6 = CustomMobSettings.defaultAgeStages();
      }

      boolean var7 = this.wJ;
      if (!this.wI) {
         var7 = CustomMobSettings.defaultBinaryAgeStages();
      }

      boolean var8 = this.wL;
      if (!this.wK) {
         var8 = CustomMobSettings.defaultVanillaDeathAnimation();
      }

      SpawnLimits var9 = this.wN;
      if (!this.wM) {
         var9 = CustomMobSettings.getSpawnLimits();
      }

      double var10 = this.wP;
      if (!this.wO) {
         var10 = CustomMobSettings.defaultEyesHeight();
      }

      return new CustomMobSettings(
         this.vn,
         this.vo,
         this.vp,
         this.vq,
         this.vr,
         this.vs,
         this.vt,
         this.vu,
         this.field109,
         this.vw,
         var1,
         var2,
         this.vz,
         this.vA,
         this.vB,
         this.vC,
         this.vD,
         this.vE,
         this.vF,
         this.vG,
         this.vH,
         this.vI,
         this.vJ,
         this.vK,
         this.vL,
         this.vM,
         var3,
         this.vO,
         this.vP,
         this.vQ,
         this.vR,
         var4,
         var5,
         this.vU,
         this.vV,
         this.vW,
         var6,
         var7,
         this.vZ,
         this.wa,
         this.wb,
         this.wc,
         this.wd,
         var8,
         var9,
         var10,
         this.wh,
         this.wi,
         this.wj,
         this.wk,
         this.wl
      );
   }

   @Generated
   @Override
   public String toString() {
      return "EntitySettings.EntitySettingsBuilder(model="
         + this.vn
         + ", damageableParts="
         + this.vo
         + ", configFile="
         + this.vp
         + ", goalsConfigFile="
         + this.vq
         + ", displayName="
         + this.vr
         + ", showNameTag="
         + this.vs
         + ", nameTagYOffset="
         + this.vt
         + ", hasBossbar="
         + this.vu
         + ", bossBarDistance="
         + this.field109
         + ", entityType="
         + this.vw
         + ", bossBarStyle$value="
         + this.wx
         + ", bossBarColor$value="
         + this.wz
         + ", jumpOnAttack="
         + this.vz
         + ", maxHealth="
         + this.vA
         + ", renderDistance="
         + this.vB
         + ", flyingSpeed="
         + this.vC
         + ", movementSpeed="
         + this.vD
         + ", isSilent="
         + this.vE
         + ", canSunBurn="
         + this.vF
         + ", canDropLoot="
         + this.vG
         + ", mountOnInteract="
         + this.vH
         + ", shadow="
         + this.vI
         + ", invulnerable="
         + this.vJ
         + ", gravity="
         + this.vK
         + ", floatOnWater="
         + this.vL
         + ", waterlineOffset="
         + this.vM
         + ", vehicleControls$value="
         + this.wB
         + ", ai="
         + this.vO
         + ", vanillaDefaults="
         + this.vP
         + ", itemPickup="
         + this.vQ
         + ", raidParticipation="
         + this.vR
         + ", damageModifiers$value="
         + this.wD
         + ", nativeAttributes$value="
         + this.wF
         + ", ageVisualScale="
         + this.vU
         + ", initialAgeTicks="
         + this.vV
         + ", initialBaby="
         + this.vW
         + ", ageStages$value="
         + this.wH
         + ", binaryAgeStages$value="
         + this.wJ
         + ", silentConfigured="
         + this.vZ
         + ", gravityConfigured="
         + this.wa
         + ", invulnerableConfigured="
         + this.wb
         + ", aiConfigured="
         + this.wc
         + ", ragdollOnDeath="
         + this.wd
         + ", vanillaDeathAnimation$value="
         + this.wL
         + ", clientInterpolation$value="
         + this.wN
         + ", eyesHeight$value="
         + this.wP
         + ", soundAmbient="
         + this.wh
         + ", soundAttack="
         + this.wi
         + ", soundHurt="
         + this.wj
         + ", soundDeath="
         + this.wk
         + ", allActionsPermission="
         + this.wl
         + ")";
   }
}
