package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R5 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, Mob var2, EntityInsentient var3) {
      this.bhn = var2;
      this.bho = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bhn.getWorld() ? false : this.bho.S().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bho.S().n();
   }

   @Override
   public boolean OT() {
      return this.bho.S().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bhn.getWorld()) {
         this.bho.P().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
