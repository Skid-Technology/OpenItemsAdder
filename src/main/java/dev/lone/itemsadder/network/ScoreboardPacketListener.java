package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.ScoreboardScorePacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.event.Listener;

public class ScoreboardPacketListener implements Listener {
   private PacketListenerHandle auv;
   private static ScoreboardPacketListener auw;

   public static void method2041(boolean var0) {
      if (var0) {
         register();
      } else {
         unregister();
      }
   }

   public static void register() {
      if (auw == null) {
         auw = new ScoreboardPacketListener();
         auw.bX();
      }
   }

   public static void unregister() {
      if (auw != null) {
         auw.bY();
         auw = null;
      }
   }

   private void bX() {
      this.auv = NativePacketsHolder.PE()
         .method1888(this, PacketSelector.exact(PacketTypes.SCOREBOARD_SCORE), ListenerPriority.NORMAL, false, ListenerFailurePolicy.PASS_ORIGINAL, var0 -> {
            if (NativePacketsHolder.PE().method1883(var0) != null) {
               ((ScoreboardScorePacketView)var0.view(ScoreboardScorePacketView.class)).numberFormat(NativePacketsHolder.PE().Pv().blankNumberFormat());
            }
         });
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   private void bY() {
      if (this.auv != null) {
         NativePacketsHolder.PE().method1889(this, this.auv);
      }

      this.auv = null;
   }
}
