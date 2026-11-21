package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R7;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.entity.Mob;

public class NavigatorAdapterImplV1_21_R7 implements NavigatorAdapter {
   NavigatorAdapterImplV1_21_R7(MobGoalFactoryImplV1_21_R7 var1, Mob var2, EntityInsentient var3) {
      this.bfv = var2;
      this.bfw = var3;
   }

   @Override
   public boolean method4591(Location target, double speed) {
      return target.getWorld() != this.bfv.getWorld() ? false : this.bfw.N().a(target.getX(), target.getY(), target.getZ(), speed);
   }

   @Override
   public void stop() {
      this.bfw.N().n();
   }

   @Override
   public boolean OT() {
      return this.bfw.N().l();
   }

   @Override
   public void method4592(Location target) {
      if (target.getWorld() == this.bfv.getWorld()) {
         this.bfw.J().a(target.getX(), target.getY(), target.getZ());
      }
   }
}
