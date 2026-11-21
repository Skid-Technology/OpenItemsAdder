package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class HurtByTargetGoalV1_21_R4 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bhh = var4;
   }

   public boolean method5041() {
      int var1 = this.e.et();
      EntityLiving var2 = this.e.er();
      if (var1 != this.bes && var2 != null) {
         if (var2.an() == EntityTypes.bT && a(this.e).O().c(GameRules.Q)) {
            return false;
         }

         this.bes = var1;
         return this.e.dY().i() < this.bhh ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method5042() {
      return false;
   }

   public void method5043() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R4.bgN, this.e, this.e.er(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.e();
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
