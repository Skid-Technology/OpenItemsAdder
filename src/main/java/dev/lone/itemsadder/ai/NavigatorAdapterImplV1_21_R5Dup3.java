package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup3;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R5Dup3 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R5Dup3(MobGoalFactoryImplV1_21_R5Dup3 var1, Mob var2, EntityInsentient var3) {
      this.bij = var2;
      this.bik = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bij.getWorld() ? false : this.bik.S().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bik.S().n();
   }

   @Override
   public boolean OT() {
      return this.bik.S().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bij.getWorld()) {
         this.bik.P().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
