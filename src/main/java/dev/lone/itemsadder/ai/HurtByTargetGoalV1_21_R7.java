package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R7;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalTarget;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class HurtByTargetGoalV1_21_R7 extends PathfinderGoalTarget {
   private int bes;

   HurtByTargetGoalV1_21_R7(MobGoalFactoryImplV1_21_R7 var1, EntityInsentient var2, boolean var3, float var4) {
      super(var2, var3);
      this.bfN = var4;
   }

   public boolean method4964() {
      int var1 = this.e.eM();
      EntityLiving var2 = this.e.eK();
      if (var1 != this.bes && var2 != null) {
         if (var2.ay() == EntityTypes.cb && (Boolean)a(this.e).U().a(GameRules.UNIVERSAL_ANGER)) {
            return false;
         }

         this.bes = var1;
         return this.e.ep().i() < this.bfN ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean method4965() {
      return false;
   }

   public void method4966() {
      MobGoalService.method4852(MobGoalFactoryImplV1_21_R7.bft, this.e, this.e.eK(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.g = this.e.ag_();
      this.h = 300;
      super.d();
   }

   private boolean Pa() {
      double var1 = this.e.dP() + (this.e.ep().j() - 0.5) * 32.0;
      double var3 = this.e.dR() + (this.e.ep().a(16) - 8);
      double var5 = this.e.dV() + (this.e.ep().j() - 0.5) * 32.0;
      return this.e.b(var1, var3, var5, true);
   }
}
