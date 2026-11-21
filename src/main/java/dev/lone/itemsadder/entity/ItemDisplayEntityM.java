package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.DoubleVector3;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.ItemDisplay;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class ItemDisplayEntityM extends AbstractItemDisplayEntity {
   private final ItemDisplay bpM;

   public ItemDisplayEntityM(Location var1) {
      this.bpv = new DoubleVector3(var1.getX(), var1.getY() + 0.35, var1.getZ());
      ServerLevel var2 = ((CraftWorld)var1.getWorld()).getHandle();
      this.bpM = new ItemDisplay(EntityTypes.ITEM_DISPLAY, var2);
      this.bpw = this.bpM.getId();
      this.bpM.setPos(this.bpv.pW, this.bpv.pX, this.bpv.pY);
      this.bpM.setXRot(-90.0F);
      this.bpM.setShadowRadius(0.5F);
      this.bpM.getEntityData().set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, 1);
   }

   @Override
   public void appendSpawn(List viewers) {
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      Object var3 = var2.Pv()
         .itemDisplay(
            this.bpM.getId(),
            this.bpM.getUUID(),
            this.bpM.trackingPosition().x(),
            this.bpM.trackingPosition().y(),
            this.bpM.trackingPosition().z(),
            this.bpM.getXRot(),
            this.bpM.getYHeadRot(),
            this.bpM.getYHeadRot()
         );
      ClientboundSetEntityDataPacket var4 = new ClientboundSetEntityDataPacket(this.bpM.getId(), this.bpM.getEntityData().getNonDefaultValues());

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
      this.appendEntityRemove(this.bpM.getId(), viewers);
   }

   @Override
   public void appendMove(double x, double y, double z, List viewers) {
      if (x != this.bpv.pW || y != this.bpv.pX || z != this.bpv.pY) {
         this.bpv.method3161(x, y, z);
         this.appendEntityMove(this.bpM.getId(), x, y + 0.35, z, viewers);
      }
   }
}
