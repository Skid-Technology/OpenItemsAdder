package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.ai.BlockInteractionGoalGenericDup4;
import dev.lone.itemsadder.ai.ConditionalWrappedGoalN;
import dev.lone.itemsadder.ai.CustomGoal;
import dev.lone.itemsadder.ai.CustomGoalAdapterM;
import dev.lone.itemsadder.ai.GotoTargetGoalGenericDup4;
import dev.lone.itemsadder.ai.HurtByTargetGoalGenericDup4;
import dev.lone.itemsadder.ai.LookAtPlayerGoalGenericDup4;
import dev.lone.itemsadder.ai.NavigatorAdapter;
import dev.lone.itemsadder.ai.NavigatorAdapterImplGenericDup4;
import dev.lone.itemsadder.ai.NearestTargetGoalGenericDup4;
import dev.lone.itemsadder.ai.RandomStrollGoalGenericDup4;
import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.util.Log;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.TryFindLiquidGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions.Selector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MobGoalFactoryImplGenericDup4 implements MobGoalFactory {
   private static final MethodHandle bjZ = MobGoalService.method4851(net.minecraft.world.entity.Mob.class, net.minecraft.world.entity.LivingEntity.class);
   private static final Map bka = new ConcurrentHashMap();

   private static boolean method4814(PathfinderMob var0) {
      if (var0.getAttributes().hasAttribute(Attributes.TEMPT_RANGE)) {
         return true;
      }

      try {
         Field var1 = AttributeMap.class.getDeclaredField("attributes");
         var1.setAccessible(true);
         Map var2 = (Map)var1.get(var0.getAttributes());
         var2.putIfAbsent(Attributes.TEMPT_RANGE, new AttributeInstance(Attributes.TEMPT_RANGE, var0x -> {}));
         return true;
      } catch (ReflectiveOperationException | SecurityException var3) {
         Log.warn("Cannot add FollowHeldItemGoal: missing tempt_range and attribute registration failed: " + var3);
         return false;
      }
   }

   static Goal method4667(Goal var0) {
      return var0 instanceof ConditionalWrappedGoalN var1 ? var1.getGoal() : var0;
   }

   @Override
   public void method4596(Mob bukkitMob, Predicate conditions, Runnable registerGoal) {
      net.minecraft.world.entity.Mob var4 = ((CraftMob)bukkitMob).getHandle();
      List var5 = List.of(var4.getGoalSelector(), var4.targetSelector);
      method4617(var5, () -> conditions.test(bukkitMob), registerGoal);
   }

   @Override
   public void method4594(Mob bukkitMob, boolean replaceVanilla, int priority, String kind, Function factory) {
      net.minecraft.world.entity.Mob var6 = ((CraftMob)bukkitMob).getHandle();
      GoalSelector var7 = var6.getGoalSelector();
      NavigatorAdapterImplGenericDup4 var8 = new NavigatorAdapterImplGenericDup4(this, bukkitMob, var6);
      if (replaceVanilla) {
         for (WrappedGoal var11 : new ArrayList(var7.getAvailableGoals())) {
            if (method4667(var11.getGoal()) instanceof CustomGoalAdapterM var9 && var9.bkv.equals(kind)) {
               var7.removeGoal(var11.getGoal());
            }
         }
      }

      var7.addGoal(priority, new CustomGoalAdapterM(kind, factory.apply(var8)));
   }

   @Override
   public void method4595(Mob bukkitMob) {
      net.minecraft.world.entity.Mob var2 = ((CraftMob)bukkitMob).getHandle();

      for (GoalSelector var4 : List.of(var2.getGoalSelector(), var2.targetSelector)) {
         for (WrappedGoal var6 : new ArrayList(var4.getAvailableGoals())) {
            if (method4667(var6.getGoal()) instanceof CustomGoalAdapterM) {
               var4.removeGoal(var6.getGoal());
            }
         }
      }
   }

   static void method4617(List var0, BooleanSupplier var1, Runnable var2) {
      Set var3 = Collections.newSetFromMap(new IdentityHashMap());

      for (GoalSelector var5 : var0) {
         for (WrappedGoal var7 : var5.getAvailableGoals()) {
            var3.add(var7.getGoal());
         }
      }

      var2.run();

      for (GoalSelector var9 : var0) {
         for (WrappedGoal var11 : new ArrayList(var9.getAvailableGoals())) {
            if (!var3.contains(var11.getGoal())) {
               var9.removeGoal(var11.getGoal());
               var9.addGoal(var11.getPriority(), new ConditionalWrappedGoalN(var11.getPriority(), var11.getGoal(), var1));
            }
         }
      }
   }

   private static void method4815(net.minecraft.world.entity.Mob var0, Class var1) {
      var0.getGoalSelector().removeAllGoals(var1x -> method4667(var1x).getClass().equals(var1));
      var0.targetSelector.removeAllGoals(var1x -> method4667(var1x).getClass().equals(var1));
   }

   private static Class method4816(Mob var0, @Nullable EntityType var1) {
      return bka.computeIfAbsent(
         var1,
         var1x -> {
            if (var1x == EntityType.PLAYER) {
               return Player.class;
            } else if (CraftEntityType.bukkitToMinecraft(var1x).create(((CraftMob)var0).getHandle().level(), EntitySpawnReason.COMMAND) instanceof net.minecraft.world.entity.LivingEntity var3
               )
             {
               return var3.getClass();
            } else {
               throw new IllegalArgumentException("Not a living entity type: " + var1x);
            }
         }
      );
   }

   @Override
   public void method4593(Mob bukkitMob) {
      net.minecraft.world.entity.Mob var2 = ((CraftMob)bukkitMob).getHandle();
      var2.getGoalSelector().removeAllGoals(var0 -> true);
      var2.targetSelector.removeAllGoals(var0 -> true);
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
      Class<net.minecraft.world.entity.LivingEntity> var9 = bukkitEntityType == null
         ? net.minecraft.world.entity.LivingEntity.class
         : method4816(bukkitMob, bukkitEntityType);
      net.minecraft.world.entity.Mob var10 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         var10.getGoalSelector().removeAllGoals(var0 -> method4667(var0) instanceof LookAtPlayerGoal);
      }

      Selector var11 = (var2x, var3x) -> (bukkitTargetPredicate == null || bukkitTargetPredicate.test((LivingEntity)var2x.getBukkitEntity()))
         && (!(var2x instanceof Player) || EntitySelector.notRiding(var10).test(var2x));
      var10.getGoalSelector().addGoal(priority, new NearestTargetGoalGenericDup4(this, var10, var9, range, chance, lookForward, var11));
   }

   @Override
   public void method4598(Mob bukkitMob, boolean replaceVanilla, int priority) {
      net.minecraft.world.entity.Mob var4 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var4, RandomLookAroundGoal.class);
      }

      var4.getGoalSelector().addGoal(priority, new RandomLookAroundGoal(var4));
   }

   @Override
   public void method4599(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, int interval) {
      PathfinderMob var7 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var7, RandomStrollGoal.class);
      }

      var7.getGoalSelector().addGoal(priority, new RandomStrollGoal(var7, speedModifier, interval));
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
      Class<net.minecraft.world.entity.LivingEntity> var9 = bukkitEntityType == null
         ? net.minecraft.world.entity.LivingEntity.class
         : method4816(bukkitMob, bukkitEntityType);
      net.minecraft.world.entity.Mob var10 = ((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var10, NearestAttackableTargetGoal.class);
      }

      Selector var11 = null;
      if (bukkitTargetPredicate != null) {
         var11 = (var1x, var2x) -> bukkitTargetPredicate.test((LivingEntity)var1x.getBukkitEntity()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(var1x);
      } else {
         var11 = (var0, var1x) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(var0);
      }

      var10.targetSelector.addGoal(priority, new NearestAttackableTargetGoal(var10, var9, interval, checkVisibility, checkCanNavigate, var11));
   }

   @Override
   public void method4601(Mob bukkitMob, boolean replaceVanilla, int priority, double speed, boolean pauseWhenIdle) {
      PathfinderMob var7 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var7, MeleeAttackGoal.class);
      }

      var7.getGoalSelector().addGoal(priority, new MeleeAttackGoal(var7, speed, pauseWhenIdle));
   }

   @Override
   public void method4602(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, int attackIntervalMin, float attackRadius) {
      this.method4620(bukkitMob, replaceVanilla, priority, speedModifier, attackIntervalMin, attackRadius);
   }

   public void method4620(Mob var1, boolean var2, int var3, double var4, int var6, float var7) {
      Monster var8 = (Monster)((CraftMob)var1).getHandle();
      if (var2) {
         method4815(var8, RangedBowAttackGoal.class);
      }

      var8.getGoalSelector().addGoal(var3, new RangedBowAttackGoal(var8, var4, var6, var7));
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
      Class<net.minecraft.world.entity.LivingEntity> var11 = bukkitEntityType == null
         ? net.minecraft.world.entity.LivingEntity.class
         : method4816(bukkitMob, bukkitEntityType);
      PathfinderMob var12 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var12, AvoidEntityGoal.class);
      }

      Predicate var13;
      if (bukkitTargetPredicate != null) {
         var13 = var1x -> bukkitTargetPredicate.test((LivingEntity)var1x.getBukkitEntity());
      } else {
         var13 = var0 -> true;
      }

      var12.getGoalSelector()
         .addGoal(
            priority,
            new AvoidEntityGoal(var12, var11, var13, maxDistance, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test)
         );
   }

   @Override
   public void method4604(Mob bukkitMob, boolean replaceVanilla, int priority, double speedModifier, boolean canScare, Predicate bukkitItemPredicate) {
      if (((CraftMob)bukkitMob).getHandle() instanceof PathfinderMob var9) {
         if (method4814(var9)) {
            if (replaceVanilla) {
               method4815(var9, TemptGoal.class);
            }

            var9.getGoalSelector()
               .addGoal(priority, new TemptGoal(var9, speedModifier, var1x -> bukkitItemPredicate.test(CraftItemStack.asBukkitCopy(var1x)), canScare));
         }
      } else {
         Log.warn("Cannot add FollowHeldItemGoal for " + bukkitMob.getType() + ": mob does not support PathfinderMob.");
      }
   }

   @Override
   public int method4605(Mob bukkitMob, boolean targetSelector, String goalClassName, @Nullable Integer priority, boolean enabled) {
      net.minecraft.world.entity.Mob var6 = ((CraftMob)bukkitMob).getHandle();
      GoalSelector var7 = targetSelector ? var6.targetSelector : var6.getGoalSelector();
      int var8 = 0;

      for (WrappedGoal var10 : new ArrayList(var7.getAvailableGoals())) {
         Goal var11 = var10.getGoal();
         Class var12 = method4667(var11).getClass();
         if (var12.getSimpleName().equals(goalClassName) || var12.getName().equals(goalClassName)) {
            int var13 = var10.getPriority();
            var7.removeGoal(var11);
            if (enabled) {
               var7.addGoal(priority == null ? var13 : priority, var11);
            }

            var8++;
         }
      }

      return var8;
   }

   @Override
   public void method4606(Mob bukkitMob, boolean replaceVanilla, int priority, String kind, double speed, float first, float second, int interval) {
      net.minecraft.world.entity.Mob var10 = ((CraftMob)bukkitMob).getHandle();
      Object var11;
      switch (kind) {
         case "seek_water":
         case "seek_air":
            if (!(var10 instanceof PathfinderMob var26)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = kind.equals("seek_water") ? new TryFindLiquidGoal(var26, FluidTags.WATER) : new BreathAirGoal(var26);
            break;
         case "sit_when_ordered":
            if (!(var10 instanceof TamableAnimal var25)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected TamableAnimal.");
               return;
            }

            var11 = new SitWhenOrderedToGoal(var25);
            break;
         case "panic":
         case "flee_sun":
            if (!(var10 instanceof PathfinderMob var24)) {
               Log.warn("Cannot add reusable vanilla goal '" + kind + "' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = kind.equals("flee_sun") ? new FleeSunGoal(var24, speed) : new PanicGoal(var24, speed);
            break;
         case "water_avoiding_wander":
            if (!(var10 instanceof PathfinderMob var23)) {
               Log.warn("Cannot add reusable vanilla goal 'water_avoiding_wander' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new WaterAvoidingRandomStrollGoal(var23, speed, first);
            break;
         case "random_swim":
            if (!(var10 instanceof PathfinderMob var22)) {
               Log.warn("Cannot add reusable vanilla goal 'random_swim' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new RandomSwimmingGoal(var22, speed, interval);
            break;
         case "move_towards_restriction":
            if (!(var10 instanceof PathfinderMob var21)) {
               Log.warn("Cannot add reusable vanilla goal 'move_towards_restriction' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new MoveTowardsRestrictionGoal(var21, speed);
            break;
         case "move_towards_target":
            if (!(var10 instanceof PathfinderMob var20)) {
               Log.warn("Cannot add reusable vanilla goal 'move_towards_target' for " + bukkitMob.getType() + ": expected PathfinderMob.");
               return;
            }

            var11 = new MoveTowardsTargetGoal(var20, speed, first);
            break;
         case "leap_at_target":
            var11 = new LeapAtTargetGoal(var10, first);
            break;
         case "follow_mob":
            if (!(var10.getNavigation() instanceof GroundPathNavigation) && !(var10.getNavigation() instanceof FlyingPathNavigation)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_mob' for " + bukkitMob.getType() + ": unsupported navigation.");
               return;
            }

            var11 = new FollowMobGoal(var10, speed, first, second);
            break;
         case "ranged_attack":
            if (!(var10 instanceof RangedAttackMob var19)) {
               Log.warn("Cannot add reusable vanilla goal 'ranged_attack' for " + bukkitMob.getType() + ": expected RangedAttackMob.");
               return;
            }

            var11 = new RangedAttackGoal(var19, speed, interval, first);
            break;
         case "breed":
            if (!(var10 instanceof Animal var18)) {
               Log.warn("Cannot add reusable vanilla goal 'breed' for " + bukkitMob.getType() + ": expected Animal.");
               return;
            }

            var11 = new BreedGoal(var18, speed);
            break;
         case "follow_parent":
            if (!(var10 instanceof Animal var17)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_parent' for " + bukkitMob.getType() + ": expected Animal.");
               return;
            }

            var11 = new FollowParentGoal(var17, speed);
            break;
         case "follow_owner":
            if (!(var10 instanceof TamableAnimal var14)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_owner' for " + bukkitMob.getType() + ": expected TamableAnimal.");
               return;
            }

            if (!(var14.getNavigation() instanceof GroundPathNavigation) && !(var14.getNavigation() instanceof FlyingPathNavigation)) {
               Log.warn("Cannot add reusable vanilla goal 'follow_owner' for " + bukkitMob.getType() + ": unsupported navigation.");
               return;
            }

            var11 = new FollowOwnerGoal(var14, speed, first, second);
            break;
         default:
            Log.warn("Unknown reusable vanilla goal kind '" + kind + "' for " + bukkitMob.getType() + ".");
            return;
      }

      GoalSelector var15 = var10.getGoalSelector();
      if (replaceVanilla) {
         Class var16 = method4667((Goal)var11).getClass();
         var15.removeAllGoals(var1x -> var1x.getClass().equals(var16));
      }

      var15.addGoal(priority, (Goal)var11);
   }

   @Override
   public void method4607(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityTypePairRule[] ignorePredicates,
      @Nullable EntityTypePairRule[] alertIgnorePredicates
   ) {
      PathfinderMob var6 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var6, HurtByTargetGoal.class);
      }

      var6.targetSelector.addGoal(priority, new HurtByTargetGoalGenericDup4(this, var6, true, alertIgnorePredicates, ignorePredicates));
   }

   @Override
   public void method4608(Mob bukkitMob, boolean replaceVanilla, int priority) {
      PathfinderMob var4 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var4, RestrictSunGoal.class);
      }

      var4.getGoalSelector().addGoal(priority, new RestrictSunGoal(var4));
   }

   @Override
   public void method4609(Mob bukkitMob, boolean replaceVanilla, int priority) {
      PathfinderMob var4 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var4, FloatGoal.class);
      }

      var4.getGoalSelector().addGoal(priority, new FloatGoal(var4));
   }

   @Override
   public void method4610(Mob bukkitMob, boolean replaceVanilla, int priority, Predicate bukkitPredicate, double speedModifier, int searchRange) {
      PathfinderMob var8 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var8, MoveToBlockGoal.class);
      }

      var8.getGoalSelector().addGoal(priority, new GotoTargetGoalGenericDup4(this, var8, speedModifier, searchRange, bukkitPredicate));
   }

   @Override
   public void method4611(Mob bukkitMob, boolean replaceVanilla, int priority, boolean closeDoor) {
      PathfinderMob var5 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var5, OpenDoorGoal.class);
      }

      var5.getGoalSelector().addGoal(priority, new OpenDoorGoal(var5, closeDoor));
   }

   @Override
   public void method4612(Mob bukkitMob, boolean replaceVanilla, int priority) {
      PathfinderMob var4 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var4, BreakDoorGoal.class);
      }

      var4.getGoalSelector().addGoal(priority, new BreakDoorGoal(var4, var0 -> true));
   }

   @Override
   public void method4613(Mob bukkitMob, boolean replaceVanilla, int priority, Predicate bukkitPredicate) {
      PathfinderMob var5 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      if (replaceVanilla) {
         method4815(var5, MoveToBlockGoal.class);
      }

      var5.getGoalSelector().addGoal(priority, new BlockInteractionGoalGenericDup4(this, bukkitMob, bukkitPredicate));
   }

   @Override
   public void method4614(Mob bukkitMob, int priority, int minDelay, int maxDelay) {
      PathfinderMob var5 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      var5.getGoalSelector().addGoal(priority, new RandomStrollGoalGenericDup4(this, var5, maxDelay, minDelay));
   }

   @Override
   public void method4615(Mob bukkitMob, int priority, boolean checkVisibility, float chance) {
      PathfinderMob var5 = (PathfinderMob)((CraftMob)bukkitMob).getHandle();
      var5.targetSelector.addGoal(priority, new LookAtPlayerGoalGenericDup4(this, var5, checkVisibility, chance));
   }
}
