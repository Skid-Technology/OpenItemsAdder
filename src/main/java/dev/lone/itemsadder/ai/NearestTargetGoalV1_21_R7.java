package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R7;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTameableAnimal;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.jetbrains.annotations.Nullable;

public class NearestTargetGoalV1_21_R7 extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bfy;
   @Nullable
   private final EntityTypePairRule[] bfz;
   @Nullable
   private final EntityTypePairRule[] bfA;

   NearestTargetGoalV1_21_R7(MobGoalFactoryImplV1_21_R7 var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bfB = var4;
      this.bfC = var5;
      this.bfy = this.bfB != null;
      this.bfz = this.bfC;
      this.bfA = this.bfB;
   }

   public boolean method4951() {
      int var1 = this.e.eM();
      EntityLiving var2 = this.e.eK();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.ay() == EntityTypes.cb && (Boolean)a(this.e).U().a(GameRules.UNIVERSAL_ANGER)) {
            return false;
         }

         if (this.bfz != null) {
            for (EntityTypePairRule var6 : this.bfz) {
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

   public void method4952() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R7.bft, this.e, this.e.eK(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.ag_();
      this.bdZ = this.e.eM();
      this.h = 300;
      if (this.bfy) {
         this.OW();
      }

      super.d();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dI()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.ao().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.ag_() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).U_() == ((EntityTameableAnimal)var6).U_())
            && !var6.t(this.e.eK())) {
            boolean var7 = false;
            if (this.bfA != null) {
               for (EntityTypePairRule var11 : this.bfA) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4953(var6, this.e.eK());
            }
         }
      }
   }

   private void method4953(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R7.bft, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
