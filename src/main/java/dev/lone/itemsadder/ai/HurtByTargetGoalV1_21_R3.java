package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class HurtByTargetGoalV1_21_R3 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bgJ = var4;
   }

   public boolean method5010() {
      int var1 = this.e.eq();
      EntityLiving var2 = this.e.ep();
      if (var1 != this.bes && var2 != null) {
         if (var2.aq() == EntityTypes.bR && a(this.e).O().b(GameRules.P)) {
            return false;
         }

         this.bes = var1;
         return this.e.dY().i() < this.bgJ ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method5011() {
      return false;
   }

   public void method5012() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R3.bgp, this.e, this.e.ep(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.O_();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dA() + (this.e.dY().j() - 0.5) * 32.0;
      double var3 = this.e.dC() + (this.e.dY().a(16) - 8);
      double var5 = this.e.dG() + (this.e.dY().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
