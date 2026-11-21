package dev.lone.itemsadder.resourcepack;

import beer.devs.nativepackets.api.NativePacketFactory;
import dev.lone.itemsadder.network.EntityMetadataListener;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class PackPushTimeout {
   private int entityId = -1;
   private final Player player;

   public PackPushTimeout(Player var1) {
      this.player = var1;
   }

   public void lockMovement() {
      try {
         Location var1 = this.player.getLocation();
         NativePacketsBridge var2 = NativePacketsHolder.PE();
         NativePacketFactory var3 = var2.Pv();
         this.entityId = var2.Pz().allocateEntityId(var1.getWorld());
         Object var4 = var3.addEntity(
            this.entityId,
            UUID.randomUUID(),
            var2.Pz().nativeEntityType(EntityType.MARKER),
            var1.getX(),
            var1.getY(),
            var1.getZ(),
            0.0F,
            0.0F,
            0.0F,
            0,
            0.0,
            0.0,
            0.0
         );
         var2.method1868(this.player, var3.bundle(List.of(var4, var3.passengers(this.entityId, new int[]{this.player.getEntityId()}))));
         SchedulerUtils.runLaterSync(() -> NmsChatService.cl(this.player), 1L);
         EntityMetadataListener.Ng().cf(this.player);
      } catch (Throwable var5) {
         Log.error("Failed to lock player movements.", var5);
      }
   }

   public void unlockMovement() {
      if (this.entityId != -1) {
         NativePacketsHolder.PE().method1868(this.player, NativePacketsHolder.PE().Pv().removeEntities(new int[]{this.entityId}));
         this.entityId = -1;
         EntityMetadataListener.Ng().cg(this.player);
      }
   }
}
