package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

class NavigatorAdapterImplV1_21_R4 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, Mob var2, EntityInsentient var3) {
      this.bgP = var2;
      this.bgQ = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bgP.getWorld() ? false : this.bgQ.N().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bgQ.N().m();
   }

   @Override
   public boolean OT() {
      return this.bgQ.N().k();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bgP.getWorld()) {
         this.bgQ.I().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
