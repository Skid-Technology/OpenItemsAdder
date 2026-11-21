package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.ai.BlockInteractionGoalV1_21_R6;
import dev.lone.itemsadder.ai.ConditionalWrappedGoalC;
import dev.lone.itemsadder.ai.CustomGoal;
import dev.lone.itemsadder.ai.CustomGoalAdapterB;
import dev.lone.itemsadder.ai.GotoTargetGoalV1_21_R6;
import dev.lone.itemsadder.ai.HurtByTargetGoalV1_21_R6;
import dev.lone.itemsadder.ai.LookAtPlayerGoalV1_21_R6;
import dev.lone.itemsadder.ai.NavigatorAdapter;
import dev.lone.itemsadder.ai.NavigatorAdapterImplV1_21_R6;
import dev.lone.itemsadder.ai.NearestTargetGoalV1_21_R6;
import dev.lone.itemsadder.ai.RandomStrollGoalV1_21_R6;
import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.util.Log;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTameableAnimal;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalArrowAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBowShoot;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBreakDoor;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBreath;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBreed;
import net.minecraft.world.entity.ai.goal.PathfinderGoalDoorOpen;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFleeSun;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowOwner;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowParent;
import net.minecraft.world.entity.ai.goal.PathfinderGoalGotoTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLeapAtTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMoveTowardsTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalPanic;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomSwim;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRestrictSun;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSit;
import net.minecraft.world.entity.ai.goal.PathfinderGoalTempt;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWater;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.navigation.NavigationFlying;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition.a;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.EntityBee;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.entity.animal.EntityChicken;
import net.minecraft.world.entity.animal.EntityCod;
import net.minecraft.world.entity.animal.EntityCow;
import net.minecraft.world.entity.animal.EntityDolphin;
import net.minecraft.world.entity.animal.EntityFox;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.animal.EntityMushroomCow;
import net.minecraft.world.entity.animal.EntityOcelot;
import net.minecraft.world.entity.animal.EntityPanda;
import net.minecraft.world.entity.animal.EntityParrot;
import net.minecraft.world.entity.animal.EntityPig;
import net.minecraft.world.entity.animal.EntityPolarBear;
import net.minecraft.world.entity.animal.EntityPufferFish;
import net.minecraft.world.entity.animal.EntityRabbit;
import net.minecraft.world.entity.animal.EntitySalmon;
import net.minecraft.world.entity.animal.EntitySnowman;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.animal.EntityTropicalFish;
import net.minecraft.world.entity.animal.EntityTurtle;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.entity.animal.horse.EntityHorseDonkey;
import net.minecraft.world.entity.animal.horse.EntityHorseMule;
import net.minecraft.world.entity.animal.horse.EntityHorseSkeleton;
import net.minecraft.world.entity.animal.horse.EntityHorseZombie;
import net.minecraft.world.entity.animal.horse.EntityLlama;
import net.minecraft.world.entity.animal.horse.EntityLlamaTrader;
import net.minecraft.world.entity.animal.sheep.EntitySheep;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.EntityWolf;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.EntityBlaze;
import net.minecraft.world.entity.monster.EntityCaveSpider;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityDrowned;
import net.minecraft.world.entity.monster.EntityEnderman;
import net.minecraft.world.entity.monster.EntityEndermite;
import net.minecraft.world.entity.monster.EntityEvoker;
import net.minecraft.world.entity.monster.EntityGhast;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityGuardian;
import net.minecraft.world.entity.monster.EntityGuardianElder;
import net.minecraft.world.entity.monster.EntityIllagerIllusioner;
import net.minecraft.world.entity.monster.EntityMagmaCube;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.monster.EntityPhantom;
import net.minecraft.world.entity.monster.EntityPigZombie;
import net.minecraft.world.entity.monster.EntityPillager;
import net.minecraft.world.entity.monster.EntityRavager;
import net.minecraft.world.entity.monster.EntityShulker;
import net.minecraft.world.entity.monster.EntitySilverfish;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySkeletonStray;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityStrider;
import net.minecraft.world.entity.monster.EntityVex;
import net.minecraft.world.entity.monster.EntityVindicator;
import net.minecraft.world.entity.monster.EntityWitch;
import net.minecraft.world.entity.monster.EntityZoglin;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.monster.EntityZombieHusk;
import net.minecraft.world.entity.monster.EntityZombieVillager;
import net.minecraft.world.entity.monster.IRangedEntity;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerTrader;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MobGoalFactoryImplV1_21_R6 implements MobGoalFactory {
   private static final MethodHandle beV = MobGoalService.method4851(EntityInsentient.class, EntityLiving.class);
   private static final Map beW = new HashMap();

   static PathfinderGoal method4616(PathfinderGoal var0) {
      return var0 instanceof ConditionalWrappedGoalC var1 ? var1.k() : var0;
   }

   @Override
   public void method4596(Mob bukkitMob, Predicate conditions, Runnable registerGoal) {
      EntityInsentient var4 = ((CraftMob)bukkitMob).getHandle();
      List var5 = List.of(var4.cq, var4.cr);
      method4617(var5, () -> conditions.test(bukkitMob), registerGoal);
   }

   @Override
   public void method4594(Mob bukkitMob, boolean replaceVanilla, int priority, String kind, Function factory) {
      EntityInsentient var6 = ((CraftMob)bukkitMob).getHandle();
      PathfinderGoalSelector var7 = var6.cq;
      NavigatorAdapterImplV1_21_R6 var8 = new NavigatorAdapterImplV1_21_R6(this, bukkitMob, var6);
      if (replaceVanilla) {
         for (PathfinderGoalWrapped var11 : new ArrayList(var7.b())) {
            if (method4616(var11.k()) instanceof CustomGoalAdapterB var9 && var9.bfr.equals(kind)) {
               var7.a(var11.k());
            }
         }
      }

      var7.a(priority, new CustomGoalAdapterB(kind, factory.apply(var8)));
   }

   @Override
   public void method4595(Mob bukkitMob) {
      EntityInsentient var2 = ((CraftMob)bukkitMob).getHandle();

      for (PathfinderGoalSelector var4 : List.of(var2.cq, var2.cr)) {
         for (PathfinderGoalWrapped var6 : new ArrayList(var4.b())) {
            if (method4616(var6.k()) instanceof CustomGoalAdapterB) {
               var4.a(var6.k());
            }
         }
      }
   }

   static void method4617(List var0, BooleanSupplier var1, Runnable var2) {
      Set var3 = Collections.newSetFromMap(new IdentityHashMap());

      for (PathfinderGoalSelector var5 : var0) {
         for (PathfinderGoalWrapped var7 : var5.b()) {
            var3.add(var7.k());
         }
      }

      var2.run();

      for (PathfinderGoalSelector var9 : var0) {
         for (PathfinderGoalWrapped var11 : new ArrayList(var9.b())) {
            if (!var3.contains(var11.k())) {
               var9.a(var11.k());
               var9.a(var11.i(), new ConditionalWrappedGoalC(var11.i(), var11.k(), var1));
            }
         }
      }
   }

   private static void method4618(EntityInsentient var0, Class var1) {
      var0.cq.a(var1x -> method4616(var1x).getClass().equals(var1));
      var0.cr.a(var1x -> method4616(var1x).getClass().equals(var1));
   }

   private static Class method4619(@Nullable EntityType var0) {
      return (Class)beW.get(CraftEntityType.bukkitToMinecraft(var0));
   }

   @Override
   public void method4593(Mob bukkitMob) {
      EntityInsentient var2 = ((CraftMob)bukkitMob).getHandle();
      var2.cq.a(var0 -> true);
      var2.cr.a(var0 -> true);
   }

   @Override
   public void method4597(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityType bukkitEntityType,
      float range,
      float chance,
      boolean lookForward,
      @Nullable Predicate bukkitTargetPredicate
   ) {
      Class<EntityLiving> var9 = bukkitEntityType == null ? EntityLiving.class : method4619(bukkitEntityType);
      EntityInsentient var10 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         var10.cq.a(var0 -> method4616(var0) instanceof PathfinderGoalLookAtPlayer);
      }

      a var11 = (var2x, var3x) -> (bukkitTargetPredicate == null || bukkitTargetPredicate.test((LivingEntity)var2x.getBukkitEntity()))
         && (!(var2x instanceof EntityHuman) || IEntitySelector.b(var10).test(var2x));
      var10.cq.a(priority, new LookAtPlayerGoalV1_21_R6(this, var10, var9, range, chance, lookForward, var11));
   }

   @Override
   public void method4598(Mob bukkitMob, boolean replaceVanilla, int priority) {
      EntityInsentient var4 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var4, PathfinderGoalRandomLookaround.class);
      }

      var4.cq.a(priority, new PathfinderGoalRandomLookaround(var4));
   }

   @Override
   public void method4599(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, int interval) {
      EntityCreature var7 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var7, PathfinderGoalRandomStroll.class);
      }

      var7.cq.a(priority, new PathfinderGoalRandomStroll(var7, speedModifier, interval));
   }

   @Override
   public void method4600(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityType bukkitEntityType,
      int interval,
      boolean checkVisibility,
      boolean checkCanNavigate,
      @Nullable Predicate bukkitTargetPredicate
   ) {
      Class<EntityLiving> var9 = bukkitEntityType == null ? EntityLiving.class : method4619(bukkitEntityType);
      EntityInsentient var10 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var10, PathfinderGoalNearestAttackableTarget.class);
      }

      a var11 = null;
      if (bukkitTargetPredicate != null) {
         var11 = (var1x, var2x) -> bukkitTargetPredicate.test((LivingEntity)var1x.getBukkitEntity()) && IEntitySelector.e.test(var1x);
      } else {
         var11 = (var0, var1x) -> IEntitySelector.e.test(var0);
      }

      var10.cr.a(priority, new PathfinderGoalNearestAttackableTarget(var10, var9, interval, checkVisibility, checkCanNavigate, var11));
   }

   @Override
   public void method4601(Mob bukkitMob, boolean replaceVanilla, int priority, double speed, boolean pauseWhenIdle) {
      EntityCreature var7 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var7, PathfinderGoalMeleeAttack.class);
      }

      var7.cq.a(priority, new PathfinderGoalMeleeAttack(var7, speed, pauseWhenIdle));
   }

   @Override
   public void method4602(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, int attackIntervalMin, float attackRadius) {
      this.method4620(bukkitMob, replaceVanilla, priority, speedModifier, attackIntervalMin, attackRadius);
   }

   public void method4620(Mob var1, boolean var2, int var3, double var4, int var6, float var7) {
      EntityMonster var8 = (EntityMonster)((CraftMob)var1).getHandle();
      if (var2) {
         method4618(var8, PathfinderGoalBowShoot.class);
      }

      var8.cq.a(var3, new PathfinderGoalBowShoot(var8, var4, var6, var7));
   }

   @Override
   public void method4603(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityType bukkitEntityType,
      float maxDistance,
      double walkSpeedModifier,
      double sprintSpeedModifier,
      @Nullable Predicate bukkitTargetPredicate
   ) {
      Class<EntityLiving> var11 = bukkitEntityType == null ? EntityLiving.class : method4619(bukkitEntityType);
      EntityCreature var12 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var12, PathfinderGoalAvoidTarget.class);
      }

      Predicate var13;
      if (bukkitTargetPredicate != null) {
         var13 = var1x -> bukkitTargetPredicate.test((LivingEntity)var1x.getBukkitEntity());
      } else {
         var13 = var0 -> true;
      }

      var12.cq.a(priority, new PathfinderGoalAvoidTarget(var12, var11, var13, maxDistance, walkSpeedModifier, sprintSpeedModifier, IEntitySelector.e::test));
   }

   @Override
   public void method4604(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, boolean canScare, Predicate bukkitItemPredicate) {
      if (((CraftMob)bukkitMob).getHandle() instanceof EntityCreature var9) {
         if (replaceVanilla) {
            method4618(var9, PathfinderGoalTempt.class);
         }

         var9.cq.a(priority, new PathfinderGoalTempt(var9, speedModifier, var1x -> bukkitItemPredicate.test(CraftItemStack.asBukkitCopy(var1x)), canScare));
      } else {
         Log.warn("Cannot add FollowHeldItemGoal for " + bukkitMob.getType() + ": mob does not support PathfinderMob.");
      }
   }

   @Override
   public int method4605(Mob bukkitMob, boolean targetSelector, String goalClassName, @Nullable Integer priority, boolean enabled) {
      EntityInsentient var6 = ((CraftMob)bukkitMob).getHandle();
      PathfinderGoalSelector var7 = targetSelector ? var6.cr : var6.cq;
      int var8 = 0;

      for (PathfinderGoalWrapped var10 : new ArrayList(var7.b())) {
         PathfinderGoal var11 = var10.k();
         Class var12 = method4616(var11).getClass();
         if (var12.getSimpleName().equals(goalClassName) || var12.getName().equals(goalClassName)) {
            int var13 = var10.i();
            var7.a(var11);
            if (enabled) {
               var7.a(priority == null ? var13 : priority, var11);
            }

            var8++;
         }
      }

      return var8;
   }

   @Override
   public void method4606(Mob bukkitMob, boolean replaceVanilla, int priority, String kind, double speed, float first, float second, int interval) {
      EntityInsentient var10 = ((CraftMob)bukkitMob).getHandle();
      Object var11;
      switch (kind) {
         case "seek_water":
         case "seek_air":
            if (!(var10 instanceof EntityCreature var26)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = kind.equals("seek_water") ? new PathfinderGoalWater(var26) : new PathfinderGoalBreath(var26);
            break;
         case "sit_when_ordered":
            if (!(var10 instanceof EntityTameableAnimal var25)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected TamableAnimal.");
               return;
            }

            var11 = new PathfinderGoalSit(var25);
            break;
         case "panic":
         case "flee_sun":
            if (!(var10 instanceof EntityCreature var24)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = kind.equals("flee_sun") ? new PathfinderGoalFleeSun(var24, speed) : new PathfinderGoalPanic(var24, speed);
            break;
         case "water_avoiding_wander":
            if (!(var10 instanceof EntityCreature var23)) {
               Log.warn("Cannot add reusable vanilla goal 'water_avoiding_wander' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new PathfinderGoalRandomStrollLand(var23, speed, first);
            break;
         case "random_swim":
            if (!(var10 instanceof EntityCreature var22)) {
               Log.warn("Cannot add reusable vanilla goal 'random_swim' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new PathfinderGoalRandomSwim(var22, speed, interval);
            break;
         case "move_towards_restriction":
            if (!(var10 instanceof EntityCreature var21)) {
               Log.warn("Cannot add reusable vanilla goal 'move_towards_restriction' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new PathfinderGoalMoveTowardsRestriction(var21, speed);
            break;
         case "move_towards_target":
            if (!(var10 instanceof EntityCreature var20)) {
               Log.warn("Cannot add reusable vanilla goal 'move_towards_target' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new PathfinderGoalMoveTowardsTarget(var20, speed, first);
            break;
         case "leap_at_target":
            var11 = new PathfinderGoalLeapAtTarget(var10, first);
            break;
         case "follow_mob":
            if (!(var10.S() instanceof Navigation) && !(var10.S() instanceof NavigationFlying)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_mob' for " + bukkitMob.getType() + ": unsupported navigation.");
               return;
            }

            var11 = new PathfinderGoalFollowEntity(var10, speed, first, second);
            break;
         case "ranged_attack":
            if (!(var10 instanceof IRangedEntity var19)) {
               Log.warn("Cannot add reusable vanilla goal 'ranged_attack' for " + bukkitMob.getType() + ": expected RangedAttackMob.");
               return;
            }

            var11 = new PathfinderGoalArrowAttack(var19, speed, interval, first);
            break;
         case "breed":
            if (!(var10 instanceof EntityAnimal var18)) {
               Log.warn("Cannot add reusable vanilla goal 'breed' for " + bukkitMob.getType() + ": expected Animal.");
               return;
            }

            var11 = new PathfinderGoalBreed(var18, speed);
            break;
         case "follow_parent":
            if (!(var10 instanceof EntityAnimal var17)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_parent' for " + bukkitMob.getType() + ": expected Animal.");
               return;
            }

            var11 = new PathfinderGoalFollowParent(var17, speed);
            break;
         case "follow_owner":
            if (!(var10 instanceof EntityTameableAnimal var14)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_owner' for " + bukkitMob.getType() + ": expected TamableAnimal.");
               return;
            }

            if (!(var14.S() instanceof Navigation) && !(var14.S() instanceof NavigationFlying)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_owner' for " + bukkitMob.getType() + ": unsupported navigation.");
               return;
            }

            var11 = new PathfinderGoalFollowOwner(var14, speed, first, second);
            break;
         default:
            Log.warn("Unknown reusable vanilla goal kind '" + kind + "' for " + bukkitMob.getType() + ".");
            return;
      }

      PathfinderGoalSelector var15 = var10.cq;
      if (replaceVanilla) {
         Class var16 = method4616((PathfinderGoal)var11).getClass();
         var15.a(var1x -> var1x.getClass().equals(var16));
      }

      var15.a(priority, (PathfinderGoal)var11);
   }

   @Override
   public void method4607(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityTypePairRule[] ignorePredicates,
      @Nullable EntityTypePairRule[] alertIgnorePredicates
   ) {
      EntityCreature var6 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var6, PathfinderGoalHurtByTarget.class);
      }

      var6.cr.a(priority, new NearestTargetGoalV1_21_R6(this, var6, true, alertIgnorePredicates, ignorePredicates));
   }

   @Override
   public void method4608(Mob bukkitMob, boolean replaceVanilla, int priority) {
      EntityCreature var4 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var4, PathfinderGoalRestrictSun.class);
      }

      var4.cq.a(priority, new PathfinderGoalRestrictSun(var4));
   }

   @Override
   public void method4609(Mob bukkitMob, boolean replaceVanilla, int priority) {
      EntityCreature var4 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var4, PathfinderGoalFloat.class);
      }

      var4.cq.a(priority, new PathfinderGoalFloat(var4));
   }

   @Override
   public void method4610(Mob bukkitMob, boolean replaceVanilla, int priority, Predicate bukkitPredicate, double speedModifier, int searchRange) {
      EntityCreature var8 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var8, PathfinderGoalGotoTarget.class);
      }

      var8.cq.a(priority, new GotoTargetGoalV1_21_R6(this, var8, speedModifier, searchRange, bukkitPredicate));
   }

   @Override
   public void method4611(Mob bukkitMob, boolean replaceVanilla, int priority, boolean closeDoor) {
      EntityCreature var5 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var5, PathfinderGoalDoorOpen.class);
      }

      var5.cq.a(priority, new PathfinderGoalDoorOpen(var5, closeDoor));
   }

   @Override
   public void method4612(Mob bukkitMob, boolean replaceVanilla, int priority) {
      EntityCreature var4 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var4, PathfinderGoalBreakDoor.class);
      }

      var4.cq.a(priority, new PathfinderGoalBreakDoor(var4, var0 -> true));
   }

   @Override
   public void method4613(Mob bukkitMob, boolean replaceVanilla, int priority, Predicate bukkitPredicate) {
      EntityCreature var5 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4618(var5, PathfinderGoalGotoTarget.class);
      }

      var5.cq.a(priority, new BlockInteractionGoalV1_21_R6(this, bukkitMob, bukkitPredicate));
   }

   @Override
   public void method4614(Mob bukkitMob, int priority, int minDelay, int maxDelay) {
      EntityCreature var5 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      var5.cq.a(priority, new RandomStrollGoalV1_21_R6(this, var5, maxDelay, minDelay));
   }

   @Override
   public void method4615(Mob bukkitMob, int priority, boolean checkVisibility, float chance) {
      EntityCreature var5 = (EntityCreature)((CraftMob)bukkitMob).getHandle();
      var5.cr.a(priority, new HurtByTargetGoalV1_21_R6(this, var5, checkVisibility, chance));
   }

   static {
      beW.put(EntityTypes.e, Allay.class);
      beW.put(EntityTypes.g, Armadillo.class);
      beW.put(EntityTypes.j, Axolotl.class);
      beW.put(EntityTypes.m, EntityBat.class);
      beW.put(EntityTypes.n, EntityBee.class);
      beW.put(EntityTypes.q, EntityBlaze.class);
      beW.put(EntityTypes.s, Bogged.class);
      beW.put(EntityTypes.t, Breeze.class);
      beW.put(EntityTypes.v, Camel.class);
      beW.put(EntityTypes.w, EntityCat.class);
      beW.put(EntityTypes.x, EntityCaveSpider.class);
      beW.put(EntityTypes.B, EntityChicken.class);
      beW.put(EntityTypes.C, EntityCod.class);
      beW.put(EntityTypes.F, EntityCow.class);
      beW.put(EntityTypes.H, EntityCreeper.class);
      beW.put(EntityTypes.K, EntityDolphin.class);
      beW.put(EntityTypes.L, EntityHorseDonkey.class);
      beW.put(EntityTypes.N, EntityDrowned.class);
      beW.put(EntityTypes.P, EntityGuardianElder.class);
      beW.put(EntityTypes.Q, EntityEnderman.class);
      beW.put(EntityTypes.R, EntityEndermite.class);
      beW.put(EntityTypes.V, EntityEvoker.class);
      beW.put(EntityTypes.ad, EntityFox.class);
      beW.put(EntityTypes.ae, Frog.class);
      beW.put(EntityTypes.ag, EntityGhast.class);
      beW.put(EntityTypes.ai, EntityGiantZombie.class);
      beW.put(EntityTypes.ak, GlowSquid.class);
      beW.put(EntityTypes.al, Goat.class);
      beW.put(EntityTypes.am, EntityGuardian.class);
      beW.put(EntityTypes.an, EntityHoglin.class);
      beW.put(EntityTypes.ap, EntityHorse.class);
      beW.put(EntityTypes.aq, EntityZombieHusk.class);
      beW.put(EntityTypes.ar, EntityIllagerIllusioner.class);
      beW.put(EntityTypes.at, EntityIronGolem.class);
      beW.put(EntityTypes.aB, EntityLlama.class);
      beW.put(EntityTypes.aD, EntityMagmaCube.class);
      beW.put(EntityTypes.aJ, EntityMushroomCow.class);
      beW.put(EntityTypes.aK, EntityHorseMule.class);
      beW.put(EntityTypes.aN, EntityOcelot.class);
      beW.put(EntityTypes.aS, EntityPanda.class);
      beW.put(EntityTypes.aT, EntityParrot.class);
      beW.put(EntityTypes.aU, EntityPhantom.class);
      beW.put(EntityTypes.aV, EntityPig.class);
      beW.put(EntityTypes.aW, EntityPiglin.class);
      beW.put(EntityTypes.aX, EntityPiglinBrute.class);
      beW.put(EntityTypes.aY, EntityPillager.class);
      beW.put(EntityTypes.aZ, EntityPolarBear.class);
      beW.put(EntityTypes.bc, EntityPufferFish.class);
      beW.put(EntityTypes.bd, EntityRabbit.class);
      beW.put(EntityTypes.be, EntityRavager.class);
      beW.put(EntityTypes.bf, EntitySalmon.class);
      beW.put(EntityTypes.bg, EntitySheep.class);
      beW.put(EntityTypes.bh, EntityShulker.class);
      beW.put(EntityTypes.bj, EntitySilverfish.class);
      beW.put(EntityTypes.bk, EntitySkeleton.class);
      beW.put(EntityTypes.bl, EntityHorseSkeleton.class);
      beW.put(EntityTypes.bm, EntitySlime.class);
      beW.put(EntityTypes.bo, Sniffer.class);
      beW.put(EntityTypes.bq, EntitySnowman.class);
      beW.put(EntityTypes.bt, EntitySpider.class);
      beW.put(EntityTypes.bw, EntitySquid.class);
      beW.put(EntityTypes.bx, EntitySkeletonStray.class);
      beW.put(EntityTypes.by, EntityStrider.class);
      beW.put(EntityTypes.bz, Tadpole.class);
      beW.put(EntityTypes.bD, EntityLlamaTrader.class);
      beW.put(EntityTypes.bF, EntityTropicalFish.class);
      beW.put(EntityTypes.bG, EntityTurtle.class);
      beW.put(EntityTypes.bH, EntityVex.class);
      beW.put(EntityTypes.bI, EntityVillager.class);
      beW.put(EntityTypes.bJ, EntityVindicator.class);
      beW.put(EntityTypes.bK, EntityVillagerTrader.class);
      beW.put(EntityTypes.bL, Warden.class);
      beW.put(EntityTypes.bN, EntityWitch.class);
      beW.put(EntityTypes.bO, EntityWither.class);
      beW.put(EntityTypes.bP, EntitySkeletonWither.class);
      beW.put(EntityTypes.bR, EntityWolf.class);
      beW.put(EntityTypes.bS, EntityZoglin.class);
      beW.put(EntityTypes.bT, EntityZombie.class);
      beW.put(EntityTypes.bU, EntityHorseZombie.class);
      beW.put(EntityTypes.bV, EntityZombieVillager.class);
      beW.put(EntityTypes.bW, EntityPigZombie.class);
      beW.put(EntityTypes.bX, EntityHuman.class);
   }
}
