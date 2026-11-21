package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.DoubleVector3;
import java.util.List;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.ItemDisplay;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;

public class ItemDisplayEntityC extends AbstractItemDisplayEntity {
   private final ItemDisplay bpC;

   public ItemDisplayEntityC(Location var1) {
      this.bpv = new DoubleVector3(var1.getX(), var1.getY() + 0.35, var1.getZ());
      WorldServer var2 = ((CraftWorld)var1.getWorld()).getHandle();
      this.bpC = new ItemDisplay(EntityTypes.av, var2);
      this.bpw = this.bpC.az();
      this.bpC.a_(this.bpv.pW, this.bpv.pX, this.bpv.pY);
      this.bpC.w(-90.0F);
      this.bpC.c(0.5F);
      this.bpC.aC().a(Display.r, 1);
   }

   @Override
   public void appendSpawn(List viewers) {
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      Object var3 = var2.Pv()
         .itemDisplay(this.bpC.az(), this.bpC.cT(), this.bpC.dE().a(), this.bpC.dE().b(), this.bpC.dE().c(), this.bpC.dZ(), this.bpC.cN(), this.bpC.cN());
      PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(this.bpC.az(), this.bpC.aC().c());

      for (int var5 = 0; var5 < viewers.size(); var5++) {
         EntityViewer var6 = viewers.get(var5);
         if (var6.kW()) {
            var2.method1868(var6.li, var3);
            var2.method1868(var6.li, var4);
         }
      }
   }

   @Override
   public void appendUpdate(List viewers) {
      this.appendEntityRemove(this.bpC.az(), viewers);
   }

   @Override
   public void appendMove(double x, double y, double z, List viewers) {
      if (x != this.bpv.pW || y != this.bpv.pX || z != this.bpv.pY) {
         this.bpv.method3161(x, y, z);
         this.appendEntityMove(this.bpC.az(), x, y + 0.35, z, viewers);
      }
   }
}
