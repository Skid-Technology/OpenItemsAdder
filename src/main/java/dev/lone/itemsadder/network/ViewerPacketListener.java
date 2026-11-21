package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.RespawnPacketView;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.CustomEntityManager;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ViewerPacketListener implements AutoCloseable {
   private final PacketListenerHandle Vk;
   private volatile boolean cV = true;

   public ViewerPacketListener() {
      this.Vk = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(), PacketSelector.exact(PacketTypes.RESPAWN), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var1 -> {
               var1.view(RespawnPacketView.class);
               NativePacketsBridge var2 = NativePacketsHolder.PE();
               UUID var3 = var2.method1883(var1);
               if (this.cV && var3 != null) {
                  long var4 = var1.connection().id();
                  Player var6 = var2.method1885(var3, var4);
                  if (var6 != null) {
                     SchedulerAccess var7 = var2.IP();
                     var7.entity(Main.getInstance(), var6, () -> {
                        if (this.cV && var2.method1885(var3, var4) == var6 && var7.ownsEntity(var6)) {
                           CustomEntityManager.getInstance().method8327(var6);
                        }
                     }, () -> {});
                  }
               }
            }
         );
   }

   @Override
   public void close() {
      this.cV = false;
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.Vk);
   }
}
