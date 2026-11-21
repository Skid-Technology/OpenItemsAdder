package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R2;
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

class NearestTargetGoalV1_21_R2 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bfW;
   @Nullable
   private final EntityTypePairRule[] bfX;
   @Nullable
   private final EntityTypePairRule[] bfY;

   NearestTargetGoalV1_21_R2(MobGoalFactoryImplV1_21_R2 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bfZ = var4;
      this.bga = var5;
      this.bfW = this.bfZ != null;
      this.bfX = this.bga;
      this.bfY = this.bfZ;
   }

   public boolean method4974() {
      int var1 = this.e.er();
      EntityLiving var2 = this.e.eq();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.aq() == EntityTypes.bS && a(this.e).N().b(GameRules.P)) {
            return false;
         }

         if (this.bfX != null) {
            for (EntityTypePairRule var6 : this.bfX) {
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

   public void method4975() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R2.bfR, this.e, this.e.eq(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.O_();
      this.bdZ = this.e.er();
      this.h = 300;
      if (this.bfW) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.du()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.dW().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.O_() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).ah_() == ((EntityTameableAnimal)var6).ah_())
            && !var6.s(this.e.eq())) {
            boolean var7 = false;
            if (this.bfY != null) {
               for (EntityTypePairRule var11 : this.bfY) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4976(var6, this.e.eq());
            }
         }
      }
   }

   private void method4976(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R2.bfR, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
