package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGeneric;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public class NavigatorAdapterImplGeneric implements NavigatorAdapter {
   NavigatorAdapterImplGeneric(MobGoalFactoryImplGeneric var1, Mob var2, net.minecraft.world.entity.Mob var3) {
      this.biH = var2;
      this.biI = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.biH.getWorld() ? false : this.biI.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.biI.getNavigation().stop();
   }

   @Override
   public boolean OT() {
      return this.biI.getNavigation().isDone();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.biH.getWorld()) {
         this.biI.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
      }
   }
}
