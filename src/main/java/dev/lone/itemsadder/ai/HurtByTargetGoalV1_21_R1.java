package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class HurtByTargetGoalV1_21_R1 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.beR = var4;
   }

   public boolean method4896() {
      int var1 = this.e.ej();
      EntityLiving var2 = this.e.ei();
      if (var1 != this.bes && var2 != null) {
         if (var2.am() == EntityTypes.by && this.e.dO().ab().b(GameRules.O)) {
            return false;
         }

         this.bes = var1;
         return this.e.dR().i() < this.beR ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method4897() {
      return false;
   }

   public void method4898() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R1.bex, this.e, this.e.ei(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.p();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dt() + (this.e.dR().j() - 0.5) * 32.0;
      double var3 = this.e.dv() + (this.e.dR().a(16) - 8);
      double var5 = this.e.dz() + (this.e.dR().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
