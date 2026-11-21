package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R6;
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

class NearestTargetGoalV1_21_R6 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bfa;
   @Nullable
   private final EntityTypePairRule[] bfb;
   @Nullable
   private final EntityTypePairRule[] bfc;

   NearestTargetGoalV1_21_R6(MobGoalFactoryImplV1_21_R6 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bfd = var4;
      this.bfe = var5;
      this.bfa = this.bfd != null;
      this.bfb = this.bfe;
      this.bfc = this.bfd;
   }

   public boolean method4906() {
      int var1 = this.e.eH();
      EntityLiving var2 = this.e.eF();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.ax() == EntityTypes.bX && a(this.e).S().c(GameRules.Q)) {
            return false;
         }

         if (this.bfb != null) {
            for (EntityTypePairRule var6 : this.bfb) {
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

   public void method4907() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R6.beV, this.e, this.e.eF(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
      this.bdZ = this.e.eH();
      this.h = 300;
      if (this.bfa) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dD()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.an().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.e() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).V_() == ((EntityTameableAnimal)var6).V_())
            && !var6.t(this.e.eF())) {
            boolean var7 = false;
            if (this.bfc != null) {
               for (EntityTypePairRule var11 : this.bfc) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4908(var6, this.e.eF());
            }
         }
      }
   }

   private void method4908(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R6.beV, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
