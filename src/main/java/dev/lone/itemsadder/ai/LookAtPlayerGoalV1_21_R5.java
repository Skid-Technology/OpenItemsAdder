package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class LookAtPlayerGoalV1_21_R5 extends PathfinderGoalTarget {
   private int bes;

   LookAtPlayerGoalV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bhF = var4;
   }

   public boolean method5064() {
      int var1 = this.e.ey();
      EntityLiving var2 = this.e.ew();
      if (var1 != this.bes && var2 != null) {
         if (var2.ap() == EntityTypes.bU && a(this.e).P().c(GameRules.Q)) {
            return false;
         }

         this.bes = var1;
         return this.e.ec().i() < this.bhF ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method5065() {
      return false;
   }

   public void method5066() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R5.bhl, this.e, this.e.ew(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dC() + (this.e.ec().j() - 0.5) * 32.0;
      double var3 = this.e.dE() + (this.e.ec().a(16) - 8);
      double var5 = this.e.dI() + (this.e.ec().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
