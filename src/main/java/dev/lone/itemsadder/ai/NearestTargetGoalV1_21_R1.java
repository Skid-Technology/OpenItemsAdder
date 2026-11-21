package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
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

class NearestTargetGoalV1_21_R1 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean beC;
   @Nullable
   private final EntityTypePairRule[] beD;
   @Nullable
   private final EntityTypePairRule[] beE;

   NearestTargetGoalV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.beF = var4;
      this.beG = var5;
      this.beC = this.beF != null;
      this.beD = this.beG;
      this.beE = this.beF;
   }

   public boolean method4883() {
      int var1 = this.e.ej();
      EntityLiving var2 = this.e.ei();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.am() == EntityTypes.by && this.e.dO().ab().b(GameRules.O)) {
            return false;
         }

         if (this.beD != null) {
            for (EntityTypePairRule var6 : this.beD) {
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

   public void method4884() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R1.bex, this.e, this.e.ei(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.p();
      this.bdZ = this.e.ej();
      this.h = 300;
      if (this.beC) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dm()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.dO().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.p() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).T_() == ((EntityTameableAnimal)var6).T_())
            && !var6.s(this.e.ei())) {
            boolean var7 = false;
            if (this.beE != null) {
               for (EntityTypePairRule var11 : this.beE) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4885(var6, this.e.ei());
            }
         }
      }
   }

   private void method4885(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R1.bex, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
