package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

class ArrowDisplayState {
   private final Arrow akv;
   private final ItemDisplay akw;
   private final List akx;
   private final Location aky;
   private final double akz;
   private final double akA;
   private int ticks;
   private boolean akB;

   ArrowDisplayState(Arrow var1, ItemDisplay var2, List var3, Location var4, double var5, double var7) {
      this.akv = var1;
      this.akw = var2;
      this.akx = var3;
      this.aky = var4;
      this.akz = var5;
      this.akA = var7;
   }

   boolean isExpired() {
      if (this.ticks++ != 200 && !this.akB && this.akv.isValid() && !this.akv.isOnGround()) {
         Location var1 = this.akv.getLocation();
         if (var1.getWorld() == this.aky.getWorld() && !(var1.distanceSquared(this.aky) >= this.akz)) {
            Object var2 = null;

            for (Player var4 : this.akx) {
               if (var4.isOnline() && var4.getWorld() == var1.getWorld()) {
                  if (var2 == null) {
                     var2 = NativePacketsHolder.PE()
                        .Pv()
                        .teleport(this.akw.getEntityId(), var1.getX(), var1.getY(), var1.getZ(), var1.getYaw(), var1.getPitch(), false);
                  }

                  NativePacketsHolder.PE().method1868(var4, var2);
               }
            }

            return true;
         } else {
            this.destroy();
            return false;
         }
      } else {
         this.destroy();
         return false;
      }
   }

   private void destroy() {
      if (this.akv.isValid()) {
         this.akv.remove();
      }

      for (Player var2 : this.akx) {
         if (var2.isOnline() && var2.getWorld() == this.aky.getWorld()) {
            NativePacketsHolder.PD().removeFor(this.akw, var2);
         }
      }
   }
}
