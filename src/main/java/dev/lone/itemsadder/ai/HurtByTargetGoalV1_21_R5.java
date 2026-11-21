package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTameableAnimal;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.jetbrains.annotations.Nullable;

class HurtByTargetGoalV1_21_R5 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bhq;
   @Nullable
   private final EntityTypePairRule[] bhr;
   @Nullable
   private final EntityTypePairRule[] bhs;

   HurtByTargetGoalV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bht = var4;
      this.bhu = var5;
      this.bhq = this.bht != null;
      this.bhr = this.bhu;
      this.bhs = this.bht;
   }

   public boolean method5051() {
      int var1 = this.e.ey();
      EntityLiving var2 = this.e.ew();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.ap() == EntityTypes.bU && a(this.e).P().c(GameRules.Q)) {
            return false;
         }

         if (this.bhr != null) {
            for (EntityTypePairRule var6 : this.bhr) {
               if (var6 != null && var6.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var2.getBukkitEntity())) {
                  return false;
               }
            }
         }

         return this.a(var2, PathfinderTargetCondition.a().d().e());
      } else {
         return false;
      }
   }

   public void method5052() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R5.bhl, this.e, this.e.ew(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
      this.bdZ = this.e.ey();
      this.h = 300;
      if (this.bhq) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dv()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.ai().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.e() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).U_() == ((EntityTameableAnimal)var6).U_())
            && !var6.t(this.e.ew())) {
            boolean var7 = false;
            if (this.bhs != null) {
               for (EntityTypePairRule var11 : this.bhs) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method5053(var6, this.e.ew());
            }
         }
      }
   }

   private void method5053(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R5.bhl, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
