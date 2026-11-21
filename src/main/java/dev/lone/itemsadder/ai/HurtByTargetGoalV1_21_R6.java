package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R6;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class HurtByTargetGoalV1_21_R6 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R6(MobGoalFactoryImplV1_21_R6 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bfp = var4;
   }

   public boolean method4919() {
      int var1 = this.e.eH();
      EntityLiving var2 = this.e.eF();
      if (var1 != this.bes && var2 != null) {
         if (var2.ax() == EntityTypes.bX && a(this.e).S().c(GameRules.Q)) {
            return false;
         }

         this.bes = var1;
         return this.e.ek().i() < this.bfp ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method4920() {
      return false;
   }

   public void method4921() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R6.beV, this.e, this.e.eF(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dK() + (this.e.ek().j() - 0.5) * 32.0;
      double var3 = this.e.dM() + (this.e.ek().a(16) - 8);
      double var5 = this.e.dQ() + (this.e.ek().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
