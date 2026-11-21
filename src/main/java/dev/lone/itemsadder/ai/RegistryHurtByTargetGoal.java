package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.EntityTypePairRule;
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

class RegistryHurtByTargetGoal extends PathfinderGoalTarget {
   private int bdZ;
   private final boolean bea;
   @Nullable
   private final EntityTypePairRule[] beb;
   @Nullable
   private final EntityTypePairRule[] bec;

   RegistryHurtByTargetGoal(MobGoalRegistry var1, EntityInsentient var2, boolean var3, EntityTypePairRule[] var4, EntityTypePairRule[] var5) {
      super(var2, var3);
      this.bed = var4;
      this.bee = var5;
      this.bea = this.bed != null;
      this.beb = this.bee;
      this.bec = this.bed;
   }

   public boolean method4853() {
      int var1 = this.e.en();
      EntityLiving var2 = this.e.em();
      if (var1 != this.bdZ && var2 != null) {
         if (var2.ak() == EntityTypes.by && this.e.dP().ab().b(GameRules.O)) {
            return false;
         }

         if (this.beb != null) {
            for (EntityTypePairRule var6 : this.beb) {
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

   public void method4854() {
      MobGoalService.method4852(MobGoalRegistry.bdU, this.e, this.e.em(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.p();
      this.bdZ = this.e.en();
      this.h = 300;
      if (this.bea) {
         this.OW();
      }

      super.c();
   }

   private void OW() {
      double var1 = this.l();
      AxisAlignedBB var3 = AxisAlignedBB.a(this.e.dn()).c(var1, 10.0, var1);

      for (EntityInsentient var6 : this.e.dP().a(this.e.getClass(), var3, IEntitySelector.f)) {
         if (this.e != var6
            && var6.p() == null
            && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal)this.e).P_() == ((EntityTameableAnimal)var6).P_())
            && !var6.s(this.e.em())) {
            boolean var7 = false;
            if (this.bec != null) {
               for (EntityTypePairRule var11 : this.bec) {
                  if (var11 != null && var11.test((LivingEntity)this.e.getBukkitEntity(), (LivingEntity)var6.getBukkitEntity())) {
                     var7 = true;
                     break;
                  }
               }
            }

            if (!var7) {
               this.method4855(var6, this.e.em());
            }
         }
      }
   }

   private void method4855(EntityInsentient var1, EntityLiving var2) {
      MobGoalService.method4852(MobGoalRegistry.bdU, var1, var2, TargetReason.TARGET_ATTACKED_ENTITY);
   }
}
