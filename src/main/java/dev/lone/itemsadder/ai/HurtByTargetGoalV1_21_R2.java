package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R2;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class HurtByTargetGoalV1_21_R2 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R2(MobGoalFactoryImplV1_21_R2 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bgl = var4;
   }

   public boolean method4987() {
      int var1 = this.e.er();
      EntityLiving var2 = this.e.eq();
      if (var1 != this.bes && var2 != null) {
         if (var2.aq() == EntityTypes.bS && a(this.e).N().b(GameRules.P)) {
            return false;
         }

         this.bes = var1;
         return this.e.dZ().i() < this.bgl ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method4988() {
      return false;
   }

   public void method4989() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R2.bfR, this.e, this.e.eq(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.O_();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dB() + (this.e.dZ().j() - 0.5) * 32.0;
      double var3 = this.e.dD() + (this.e.dZ().a(16) - 8);
      double var5 = this.e.dH() + (this.e.dZ().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
