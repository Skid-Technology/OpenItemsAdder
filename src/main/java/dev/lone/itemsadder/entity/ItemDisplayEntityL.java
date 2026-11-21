package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.DoubleVector3;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class ItemDisplayEntityL extends AbstractItemDisplayEntity {
   private final ItemDisplay bpL;

   public ItemDisplayEntityL(Location var1) {
      this.bpv = new DoubleVector3(var1.getX(), var1.getY() + 0.35, var1.getZ());
      ServerLevel var2 = ((CraftWorld)var1.getWorld()).getHandle();
      this.bpL = new ItemDisplay(EntityType.ITEM_DISPLAY, var2);
      this.bpw = this.bpL.getId();
      this.bpL.setPos(this.bpv.pW, this.bpv.pX, this.bpv.pY);
      this.bpL.setXRot(-90.0F);
      this.bpL.setShadowRadius(0.5F);
      this.bpL.getEntityData().set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, 1);
   }

   @Override
   public void appendSpawn(List viewers) {
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      Object var3 = var2.Pv()
         .itemDisplay(
            this.bpL.getId(),
            this.bpL.getUUID(),
            this.bpL.trackingPosition().x(),
            this.bpL.trackingPosition().y(),
            this.bpL.trackingPosition().z(),
            this.bpL.getXRot(),
            this.bpL.getYHeadRot(),
            this.bpL.getYHeadRot()
         );
      ClientboundSetEntityDataPacket var4 = new ClientboundSetEntityDataPacket(this.bpL.getId(), this.bpL.getEntityData().getNonDefaultValues());

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
      this.appendEntityRemove(this.bpL.getId(), viewers);
   }

   @Override
   public void appendMove(double x, double y, double z, List viewers) {
      if (x != this.bpv.pW || y != this.bpv.pX || z != this.bpv.pY) {
         this.bpv.method3161(x, y, z);
         this.appendEntityMove(this.bpL.getId(), x, y + 0.35, z, viewers);
      }
   }
}
