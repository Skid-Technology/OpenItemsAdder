package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup3;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.jetbrains.annotations.Nullable;

public class HurtByTargetGoalGenericDup3 extends TargetGoal {
   private int bdZ;
   private final boolean bjG;
   @Nullable
   private final EntityTypePairRule[] bjH;
   @Nullable
   private final EntityTypePairRule[] bjI;

   HurtByTargetGoalGenericDup3(MobGoalFactoryImplGenericDup3 var1, Mob var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bjJ = var4;
      this.bjK = var5;
      this.bjG = this.bjJ != null;
      this.bjH = this.bjK;
      this.bjI = this.bjJ;
   }

   public boolean canUse() {
      int var1 = this.mob.getLastHurtByMobTimestamp();
      LivingEntity var2 = this.mob.getLastHurtByMob();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.getType() == EntityTypes.PLAYER && (Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
            return false;
         }

         if (this.bjH != null) {
            for (EntityTypePairRule var6 : this.bjH) {
               if (var6 != null
                  && var6.test((org.bukkit.entity.LivingEntity)this.mob.getBukkitEntity(), (org.bukkit.entity.LivingEntity)var2.getBukkitEntity())) {
                  return false;
               }
            }
         }

         return this.canAttack(var2, TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting());
      } else {
         return false;
      }
   }

   public void start() {
      MobGoalService.method4852(MobGoalFactoryImplGenericDup3.bjB, this.mob, this.mob.getLastHurtByMob(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.targetMob = this.mob.getTarget();
      this.bdZ = this.mob.getLastHurtByMobTimestamp();
      this.unseenMemoryTicks = 300;
      if (this.bjG) {
         this.OW();
      }

      super.start();
   }

   private void OW() {
      double var1 = this.getFollowDistance();
      AABB var3 = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(var1, 10.0, var1);

      for (Mob var6 : this.mob.level().getEntitiesOfClass(this.mob.getClass(), var3, EntitySelector.NO_SPECTATORS)) {
         if (this.mob != var6
            && var6.getTarget() == null
            && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal)this.mob).getOwner() == ((TamableAnimal)var6).getOwner())
            && !var6.isAlliedTo(this.mob.getLastHurtByMob())) {
            boolean var7 = false;
            if (this.bjI != null) {
               for (EntityTypePairRule var11 : this.bjI) {
                  if (var11 != null
                     && var11.test((org.bukkit.entity.LivingEntity)this.mob.getBukkitEntity(), (org.bukkit.entity.LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method5126(var6, this.mob.getLastHurtByMob());
            }
         }
      }
   }

   private void method5126(Mob var1, LivingEntity var2) {
      MobGoalService.method4852(MobGoalFactoryImplGenericDup3.bjB, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
