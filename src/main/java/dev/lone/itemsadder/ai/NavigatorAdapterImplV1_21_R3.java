package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R3 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, Mob var2, EntityInsentient var3) {
      this.bgr = var2;
      this.bgs = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bgr.getWorld() ? false : this.bgs.P().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bgs.P().m();
   }

   @Override
   public boolean OT() {
      return this.bgs.P().k();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bgr.getWorld()) {
         this.bgs.L().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
