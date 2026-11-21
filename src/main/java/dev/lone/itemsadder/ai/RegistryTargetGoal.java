package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

class RegistryTargetGoal extends PathfinderGoalTarget {
   private int bes;

   RegistryTargetGoal(MobGoalRegistry var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bet = var4;
   }

   public boolean method4873() {
      int var1 = this.e.en();
      EntityLiving var2 = this.e.em();
      if (var1 != this.bes && var2 != null) {
         if (var2.ak() == EntityTypes.by && this.e.dP().ab().b(GameRules.O)) {
            return false;
         }

         this.bes = var1;
         return this.e.el().i() < this.bet ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method4874() {
      return false;
   }

   public void method4875() {
      MobGoalService.method4852(MobGoalRegistry.bdU, this.e, this.e.em(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.p();
      this.h = 300;
      super.c();
   }

   private boolean Pa() {
      double var1 = this.e.du() + (this.e.el().j() - 0.5) * 32.0;
      double var3 = this.e.dw() + (this.e.el().a(16) - 8);
      double var5 = this.e.dA() + (this.e.el().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
