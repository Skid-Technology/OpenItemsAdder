package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R6;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R6 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R6(MobGoalFactoryImplV1_21_R6 var1, Mob var2, EntityInsentient var3) {
      this.beX = var2;
      this.beY = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.beX.getWorld() ? false : this.beY.S().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.beY.S().n();
   }

   @Override
   public boolean OT() {
      return this.beY.S().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.beX.getWorld()) {
         this.beY.P().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
