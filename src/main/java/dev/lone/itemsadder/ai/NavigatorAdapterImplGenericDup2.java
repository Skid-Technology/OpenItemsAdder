package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup2;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public class NavigatorAdapterImplGenericDup2 implements NavigatorAdapter {
   NavigatorAdapterImplGenericDup2(MobGoalFactoryImplGenericDup2 var1, Mob var2, net.minecraft.world.entity.Mob var3) {
      this.bjf = var2;
      this.bjg = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bjf.getWorld() ? false : this.bjg.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bjg.getNavigation().stop();
   }

   @Override
   public boolean OT() {
      return this.bjg.getNavigation().isDone();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bjf.getWorld()) {
         this.bjg.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
      }
   }
}
