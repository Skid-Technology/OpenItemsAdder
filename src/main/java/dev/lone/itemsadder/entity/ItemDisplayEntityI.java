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
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;

public class ItemDisplayEntityI extends AbstractItemDisplayEntity {
   private final ItemDisplay bpI;

   public ItemDisplayEntityI(Location var1) {
      this.bpv = new DoubleVector3(var1.getX(), var1.getY() + 0.35, var1.getZ());
      WorldServer var2 = ((CraftWorld)var1.getWorld()).getHandle();
      this.bpI = new ItemDisplay(EntityTypes.at, var2);
      this.bpw = this.bpI.ar();
      this.bpI.a_(this.bpv.pW, this.bpv.pX, this.bpv.pY);
      this.bpI.w(-90.0F);
      this.bpI.c(0.5F);
      this.bpI.au().a(Display.r, 1);
   }

   @Override
   public void appendSpawn(List viewers) {
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      Object var3 = var2.Pv()
         .itemDisplay(this.bpI.ar(), this.bpI.cK(), this.bpI.dw().a(), this.bpI.dw().b(), this.bpI.dw().c(), this.bpI.dR(), this.bpI.cE(), this.bpI.cE());
      PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(this.bpI.ar(), this.bpI.au().c());

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
      this.appendEntityRemove(this.bpI.ar(), viewers);
   }

   @Override
   public void appendMove(double x, double y, double z, List viewers) {
      if (x != this.bpv.pW || y != this.bpv.pX || z != this.bpv.pY) {
         this.bpv.method3161(x, y, z);
         this.appendEntityMove(this.bpI.ar(), x, y + 0.35, z, viewers);
      }
   }
}
