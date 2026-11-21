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
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;

public class ItemDisplayEntityB extends AbstractItemDisplayEntity {
   private final ItemDisplay bpB;

   public ItemDisplayEntityB(Location var1) {
      this.bpv = new DoubleVector3(var1.getX(), var1.getY() + 0.35, var1.getZ());
      WorldServer var2 = ((CraftWorld)var1.getWorld()).getHandle();
      this.bpB = new ItemDisplay(EntityTypes.ah, var2);
      this.bpw = this.bpB.an();
      this.bpB.a_(this.bpv.pW, this.bpv.pX, this.bpv.pY);
      this.bpB.u(-90.0F);
      this.bpB.c(0.5F);
      this.bpB.ar().a(Display.s, 1);
   }

   @Override
   public void appendSpawn(List viewers) {
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      Object var3 = var2.Pv()
         .itemDisplay(this.bpB.an(), this.bpB.cz(), this.bpB.dn().a(), this.bpB.dn().b(), this.bpB.dn().c(), this.bpB.dG(), this.bpB.ct(), this.bpB.ct());
      PacketPlayOutEntityMetadata var4 = new PacketPlayOutEntityMetadata(this.bpB.an(), this.bpB.ar().c());

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
      this.appendEntityRemove(this.bpB.an(), viewers);
   }

   @Override
   public void appendMove(double x, double y, double z, List viewers) {
      if (x != this.bpv.pW || y != this.bpv.pX || z != this.bpv.pY) {
         this.bpv.method3161(x, y, z);
         this.appendEntityMove(this.bpB.an(), x, y + 0.35, z, viewers);
      }
   }
}
