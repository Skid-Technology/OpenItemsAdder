package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R2;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R2 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R2(MobGoalFactoryImplV1_21_R2 var1, Mob var2, EntityInsentient var3) {
      this.bfT = var2;
      this.bfU = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bfT.getWorld() ? false : this.bfU.L().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bfU.L().o();
   }

   @Override
   public boolean OT() {
      return this.bfU.L().m();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bfT.getWorld()) {
         this.bfU.H().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
