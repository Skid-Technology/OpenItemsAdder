package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.command.PermissionRuleConsumer;
import dev.lone.itemsadder.config.YamlConfig;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Raider;
import org.bukkit.entity.EnderDragon.Phase;
import org.jetbrains.annotations.Nullable;

public class CustomMobSettings {
   public final CustomEntityModel vn;
   @Nullable
   public final BoneStateList vo;
   @Nullable
   public final YamlConfig vp;
   @Nullable
   public final YamlConfig vq;
   public final String vr;
   public final boolean vs;
   @Nullable
   public final Double vt;
   public final boolean vu;
   @Nullable
   public final Double field108;
   public final EntityType vw;
   BarStyle vx;
   BarColor vy;
   public boolean vz;
   @Nullable
   public Double vA;
   @Nullable
   public final Double vB;
   @Nullable
   Float vC;
   @Nullable
   Double vD;
   public final boolean vE;
   public final boolean vF;
   public final boolean vG;
   public final boolean vH;
   public final boolean vI;
   public final boolean vJ;
   public final boolean vK;
   public final boolean vL;
   public final double vM;
   public final ControlScheme vN;
   public final boolean vO;
   public final boolean vP;
   @Nullable
   public final Boolean vQ;
   @Nullable
   public final Boolean vR;
   public final Map vS;
   public final Map vT;
   public final boolean vU;
   @Nullable
   public final Integer vV;
   @Nullable
   public final Boolean vW;
   public final List vX;
   public final boolean vY;
   public final boolean vZ;
   public final boolean wa;
   public final boolean wb;
   public final boolean wc;
   public final boolean wd;
   public final boolean we;
   public final SpawnLimits wf;
   public final double wg;
   @Nullable
   public final MobAttributeEntry wh;
   @Nullable
   public final String wi;
   @Nullable
   public final String wj;
   @Nullable
   public final String wk;
   @Nullable
   public final String wl;
   public final MobEventDispatcher wm = new MobEventDispatcher(this);
   public final CustomMobBehaviorLoader wn = new CustomMobBehaviorLoader(this);

   public String getNamespacedId() {
      return this.vn.getNamespacedID();
   }

   public void applyToEntity(@Nullable LivingEntity var1, @Nullable String var2, boolean var3) {
      if (var1 != null) {
         CustomEntityManager.tagEntityModel(var1, this.vn.getNamespacedID());
         if (var2 != null) {
            CustomEntityManager.tagEntitySkin(var1, var2);
         }

         if (!this.vP && var1 instanceof EnderDragon) {
            ((EnderDragon)var1).setPhase(Phase.CIRCLING);
         }

         if (this.vC != null && this.vC >= 0.0F) {
            ProjectileTypeHelper.getAdapter().method3271(var1, this.vC.floatValue());
         }

         if (this.vD != null && this.vD >= 0.0) {
            ProjectileTypeHelper.getAdapter().method3270(var1, this.vD);
         }

         this.vT.forEach((var1x, var2x) -> ProjectileTypeHelper.getAdapter().method3272(var1, NamespacedKey.minecraft(var1x), var2x));
         if (var1.getType() == EntityType.WITHER || var1.getType() == EntityType.ENDER_DRAGON) {
            var1.setCustomName(this.vr);
         }

         if (this.vA != null && this.vA > 0.0) {
            var1.setMaxHealth(this.vA);
            if (var3) {
               var1.setHealth(this.vA);
            }
         }

         if (var3 && var1 instanceof Ageable var4) {
            if (this.vV != null) {
               var4.setAge(this.vV);
            } else if (this.vW != null) {
               if (this.vW) {
                  var4.setBaby();
               } else {
                  var4.setAdult();
               }
            }
         }

         if (this.vP && this.vZ || !this.vP && (!var3 || this.vE)) {
            var1.setSilent(this.vE);
         }

         if (this.vP && this.wb || !this.vP && (!var3 || this.vJ)) {
            var1.setInvulnerable(this.vJ);
         }

         if (this.vP && this.wa || !this.vP && (!var3 || !this.vK)) {
            var1.setGravity(this.vK);
         }

         if (this.vP && this.wc || !this.vP && (!var3 || !this.vO)) {
            var1.setAI(this.vO);
         }

         if (this.vQ != null && var1 instanceof Mob var5) {
            ProjectileTypeHelper.getAdapter().method3255(var5, this.vQ);
         }

         if (this.vR != null && var1 instanceof Raider var6) {
            ProjectileTypeHelper.getAdapter().method3256(var6, this.vR);
         }
      }
   }

   @Nullable
   public ModelDefinitionData getModelDefinition(String var1) {
      return (ModelDefinitionData)this.vn.AK.get(var1);
   }

   public double getDoubleValue() {
      return Objects.requireNonNullElse(this.vD, 0.23);
   }

   public float getFloatValue() {
      return Objects.requireNonNullElse(this.vC, 0.0F);
   }

   public void dispatchEvent(Entity var1, MobEventType var2, PermissionRuleConsumer var3) {
      this.wm.dispatch(var1, var2, var3);
   }

   @Generated
   private static BarStyle getBossBarStyle() {
      return BarStyle.SEGMENTED_6;
   }

   @Generated
   private static BarColor getBossBarColor() {
      return BarColor.PURPLE;
   }

   @Generated
   private static ControlScheme getControlScheme() {
      return ControlScheme.yc;
   }

   @Generated
   private static Map defaultDamageModifiers() {
      return Map.of();
   }

   @Generated
   private static Map defaultNativeAttributes() {
      return Map.of();
   }

   @Generated
   private static List defaultAgeStages() {
      return List.of();
   }

   @Generated
   private static boolean defaultBinaryAgeStages() {
      return false;
   }

   @Generated
   private static boolean defaultVanillaDeathAnimation() {
      return true;
   }

   @Generated
   private static SpawnLimits getSpawnLimits() {
      return SpawnLimits.wv;
   }

   @Generated
   private static double defaultEyesHeight() {
      return 0.25;
   }

   @Generated
   CustomMobSettings(
      CustomEntityModel var1,
      @Nullable BoneStateList var2,
      @Nullable YamlConfig var3,
      @Nullable YamlConfig var4,
      String var5,
      boolean var6,
      @Nullable Double var7,
      boolean var8,
      @Nullable Double var9,
      EntityType var10,
      BarStyle var11,
      BarColor var12,
      boolean var13,
      @Nullable Double var14,
      @Nullable Double var15,
      @Nullable Float var16,
      @Nullable Double var17,
      boolean var18,
      boolean var19,
      boolean var20,
      boolean var21,
      boolean var22,
      boolean var23,
      boolean var24,
      boolean var25,
      double var26,
      ControlScheme var28,
      boolean var29,
      boolean var30,
      @Nullable Boolean var31,
      @Nullable Boolean var32,
      Map var33,
      Map var34,
      boolean var35,
      @Nullable Integer var36,
      @Nullable Boolean var37,
      List var38,
      boolean var39,
      boolean var40,
      boolean var41,
      boolean var42,
      boolean var43,
      boolean var44,
      boolean var45,
      SpawnLimits var46,
      double var47,
      @Nullable MobAttributeEntry var49,
      @Nullable String var50,
      @Nullable String var51,
      @Nullable String var52,
      @Nullable String var53
   ) {
      this.vn = var1;
      this.vo = var2;
      this.vp = var3;
      this.vq = var4;
      this.vr = var5;
      this.vs = var6;
      this.vt = var7;
      this.vu = var8;
      this.field108 = var9;
      this.vw = var10;
      this.vx = var11;
      this.vy = var12;
      this.vz = var13;
      this.vA = var14;
      this.vB = var15;
      this.vC = var16;
      this.vD = var17;
      this.vE = var18;
      this.vF = var19;
      this.vG = var20;
      this.vH = var21;
      this.vI = var22;
      this.vJ = var23;
      this.vK = var24;
      this.vL = var25;
      this.vM = var26;
      this.vN = var28;
      this.vO = var29;
      this.vP = var30;
      this.vQ = var31;
      this.vR = var32;
      this.vS = var33;
      this.vT = var34;
      this.vU = var35;
      this.vV = var36;
      this.vW = var37;
      this.vX = var38;
      this.vY = var39;
      this.vZ = var40;
      this.wa = var41;
      this.wb = var42;
      this.wc = var43;
      this.wd = var44;
      this.we = var45;
      this.wf = var46;
      this.wg = var47;
      this.wh = var49;
      this.wi = var50;
      this.wj = var51;
      this.wk = var52;
      this.wl = var53;
   }

   @Generated
   public static CustomMobSettingsBuilder toBuilder() {
      return new CustomMobSettingsBuilder();
   }

   @Nullable
   @Generated
   public YamlConfig getConfig() {
      return this.vp;
   }
}
