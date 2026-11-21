package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
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

class NearestTargetGoalV1_21_R3 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bgu;
   @Nullable
   private final EntityTypePairRule[] bgv;
   @Nullable
   private final EntityTypePairRule[] bgw;

   NearestTargetGoalV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bgx = var4;
      this.bgy = var5;
      this.bgu = this.bgx != null;
      this.bgv = this.bgy;
      this.bgw = this.bgx;
   }

   public boolean method4997() {
      int var1 = this.e.eq();
      EntityLiving var2 = this.e.ep();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.aq() == EntityTypes.bR && a(this.e).O().b(GameRules.P)) {
            return false;
         }

         if (this.bgv != null) {
            for (EntityTypePairRule var6 : this.bgv) {
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

   public void method4998() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R3.bgp, this.e, this.e.ep(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.O_();
      this.bdZ = this.e.eq();
      this.h = 300;
      if (this.bgu) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dt()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.dV().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.O_() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).ag_() == ((EntityTameableAnimal)var6).ag_())
            && !var6.s(this.e.ep())) {
            boolean var7 = false;
            if (this.bgw != null) {
               for (EntityTypePairRule var11 : this.bgw) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4999(var6, this.e.ep());
            }
         }
      }
   }

   private void method4999(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R3.bgp, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
