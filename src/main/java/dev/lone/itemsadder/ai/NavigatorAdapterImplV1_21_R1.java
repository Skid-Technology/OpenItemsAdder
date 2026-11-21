package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R1 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, Mob var2, EntityInsentient var3) {
      this.bez = var2;
      this.beA = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bez.getWorld() ? false : this.beA.N().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.beA.N().n();
   }

   @Override
   public boolean OT() {
      return this.beA.N().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bez.getWorld()) {
         this.beA.I().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
