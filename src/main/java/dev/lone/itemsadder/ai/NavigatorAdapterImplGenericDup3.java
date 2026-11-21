package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup3;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public class NavigatorAdapterImplGenericDup3 implements NavigatorAdapter {
   NavigatorAdapterImplGenericDup3(MobGoalFactoryImplGenericDup3 var1, Mob var2, net.minecraft.world.entity.Mob var3) {
      this.bjD = var2;
      this.bjE = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bjD.getWorld() ? false : this.bjE.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bjE.getNavigation().stop();
   }

   @Override
   public boolean OT() {
      return this.bjE.getNavigation().isDone();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bjD.getWorld()) {
         this.bjE.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
      }
   }
}
