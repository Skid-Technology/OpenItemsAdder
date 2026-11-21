package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
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

class NearestTargetGoalV1_21_R4 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bgS;
   @Nullable
   private final EntityTypePairRule[] bgT;
   @Nullable
   private final EntityTypePairRule[] bgU;

   NearestTargetGoalV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bgV = var4;
      this.bgW = var5;
      this.bgS = this.bgV != null;
      this.bgT = this.bgW;
      this.bgU = this.bgV;
   }

   public boolean method5028() {
      int var1 = this.e.et();
      EntityLiving var2 = this.e.er();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.an() == EntityTypes.bT && a(this.e).O().c(GameRules.Q)) {
            return false;
         }

         if (this.bgT != null) {
            for (EntityTypePairRule var6 : this.bgT) {
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

   public void method5029() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R4.bgN, this.e, this.e.er(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
      this.bdZ = this.e.et();
      this.h = 300;
      if (this.bgS) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dt()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.dV().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.e() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).T_() == ((EntityTameableAnimal)var6).T_())
            && !var6.s(this.e.er())) {
            boolean var7 = false;
            if (this.bgU != null) {
               for (EntityTypePairRule var11 : this.bgU) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method5030(var6, this.e.er());
            }
         }
      }
   }

   private void method5030(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R4.bgN, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
