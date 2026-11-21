package dev.lone.itemsadder.ai;

import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class RegistryNavigatorAdapter implements NavigatorAdapter {
   RegistryNavigatorAdapter(MobGoalRegistry var1, Mob var2, EntityInsentient var3) {
      this.bdW = var2;
      this.bdX = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bdW.getWorld() ? false : this.bdX.K().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bdX.K().n();
   }

   @Override
   public boolean OT() {
      return this.bdX.K().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bdW.getWorld()) {
         this.bdX.G().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
