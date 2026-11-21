package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup2;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R5Dup2 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R5Dup2(MobGoalFactoryImplV1_21_R5Dup2 var1, Mob var2, EntityInsentient var3) {
      this.bhL = var2;
      this.bhM = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bhL.getWorld() ? false : this.bhM.S().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bhM.S().n();
   }

   @Override
   public boolean OT() {
      return this.bhM.S().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bhL.getWorld()) {
         this.bhM.P().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
