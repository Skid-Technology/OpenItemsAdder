package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup4;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public class NavigatorAdapterImplGenericDup4 implements NavigatorAdapter {
   NavigatorAdapterImplGenericDup4(MobGoalFactoryImplGenericDup4 var1, Mob var2, net.minecraft.world.entity.Mob var3) {
      this.bkb = var2;
      this.bkc = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bkb.getWorld() ? false : this.bkc.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bkc.getNavigation().stop();
   }

   @Override
   public boolean OT() {
      return this.bkc.getNavigation().isDone();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bkb.getWorld()) {
         this.bkc.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
      }
   }
}
