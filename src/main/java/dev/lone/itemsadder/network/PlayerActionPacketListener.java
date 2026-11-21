package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerAction;
import beer.devs.nativepackets.api.PlayerActionPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.event.PlayerHeldItemEvent;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class PlayerActionPacketListener {
   private static PacketListenerHandle UM;
   private static PacketListenerHandle aHH;
   private static final Map aHI = Collections.synchronizedMap(new WeakHashMap());
   private static volatile boolean cV;
   private static volatile long bG;

   public static synchronized void init() {
      br();
      cV = true;
      long var0 = ++bG;
      UM = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.PLAYER_ACTION),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.CANCEL_PACKET,
            var2 -> method2395(var2, (PlayerActionPacketView)var2.view(PlayerActionPacketView.class), var0)
         );
      aHH = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.PLAYER_ACTION),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.CANCEL_PACKET,
            PlayerActionPacketListener::method2396
         );
   }

   private static void method2395(PacketEvent var0, PlayerActionPacketView var1, long var2) {
      PlayerAction var4 = var1.action();
      if (var4 == PlayerAction.DROP_ITEM || var4 == PlayerAction.DROP_ALL_ITEMS) {
         UUID var5 = NativePacketsHolder.PE().method1883(var0);
         if (cV && bG == var2 && var5 != null) {
            ConnectionHandle var6 = var0.connection();
            long var7 = var6.id();
            var0.sync((var5x, var6x) -> {
               if (cV && bG == var2 && var6x.player() instanceof Player var7x && var7x == NativePacketsHolder.PE().method1885(var5, var7) && var7x.isOnline()) {
                  PlayerInventory var9 = var7x.getInventory();
                  aHI.put(var5x, new PendingItemUpdate(var5, var7, var2, var9.getItemInMainHand().clone(), var9.getHeldItemSlot()));
               }
            });
         }
      }
   }

   private static void method2396(PacketEvent var0) {
      PendingItemUpdate var1 = (PendingItemUpdate)aHI.remove(var0);
      if (var1 != null && cV && bG == var1.ai()) {
         Player var2 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
         if (var2 != null) {
            if (!EventRegistrar.callEvent(new PlayerHeldItemEvent(var2, var1.dp(), var1.Lw()))) {
               var0.cancel();
               var0.sync((var2x, var3) -> {
                  if (cV && bG == var1.ai() && var3.player() == var2 && var2 == NativePacketsHolder.PE().method1885(var1.bk(), var1.bl()) && var2.isOnline()) {
                     var2.updateInventory();
                  }
               });
            }
         }
      }
   }

   public static synchronized void br() {
      cV = false;
      bG++;
      aHI.clear();
      if (aHH != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), aHH);
         aHH = null;
      }

      if (UM != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), UM);
         UM = null;
      }
   }
}
