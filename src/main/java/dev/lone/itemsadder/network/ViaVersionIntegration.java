package dev.lone.itemsadder.network;

import beer.devs.fastnbt.nms.Version;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.ServerProtocolVersionSingleton;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import io.netty.channel.Channel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ViaVersionIntegration implements Listener {
   private static boolean registered = false;

   public static int method4866(UUID var0) {
      if (!Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
         return Version.get().getProtocol();
      }

      try {
         return !Via.getAPI().isInjected(var0) ? -1 : Via.getAPI().getPlayerVersion(var0);
      } catch (RuntimeException | LinkageError var2) {
         return -1;
      }
   }

   public static int method4867(UUID var0, Object var1) {
      int var2 = method4866(var0);
      if (var2 < 0 && var1 instanceof Channel var3) {
         try {
            return method4868(var3.pipeline().get(Via.getManager().getInjector().getDecoderName()));
         } catch (RuntimeException | LinkageError var5) {
            return -1;
         }
      } else {
         return var2;
      }
   }

   private static int method4868(Object var0) {
      if (var0 == null) {
         return -1;
      }

      try {
         UserConnection var1;
         try {
            var1 = (UserConnection)var0.getClass().getMethod("connection").invoke(var0);
         } catch (NoSuchMethodException var4) {
            Field var3 = var0.getClass().getDeclaredField("connection");
            var3.setAccessible(true);
            var1 = (UserConnection)var3.get(var0);
         }

         return var1.getProtocolInfo().protocolVersion().getVersion();
      } catch (ReflectiveOperationException | RuntimeException | LinkageError var5) {
         return -1;
      }
   }

   public static Map co() {
      HashMap var0 = new HashMap();
      ViaAPI var1 = Via.getAPI();

      for (Player var3 : Bukkit.getOnlinePlayers()) {
         int var4 = var1.getPlayerVersion(var3.getUniqueId());
         if (var4 >= 0) {
            String var5 = ProtocolVersion.isRegistered(var4) ? ProtocolVersion.getProtocol(var4).getName() : "Unknown (" + var4 + ")";
            var0.merge(var5, 1, Integer::sum);
         }
      }

      return var0;
   }

   public static void register() {
      if (!registered) {
         EventRegistrar.registerListener(new ViaVersionIntegration(), Main.getInstance());
         registered = true;
      }
   }

   public static boolean method4869(Player var0) {
      return !registered
         ? Version.get().ordinal() < Version.v1_20_5.ordinal()
         : Via.getAPI().getPlayerVersion(var0.getUniqueId()) < Version.v1_20_5.getProtocol();
   }

   public static boolean method4870(Player var0) {
      return !registered
         ? Version.get().ordinal() >= Version.v1_21_3.ordinal()
         : Via.getAPI().getPlayerVersion(var0.getUniqueId()) >= Version.v1_21_3.getProtocol();
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method4871(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      if (Via.getAPI().getServerVersion() instanceof ServerProtocolVersionSingleton var4) {
         int var5 = Via.getAPI().getPlayerVersion(var2.getUniqueId());
         if (var4.highestSupportedVersion() != var5) {
            Log.warn(
               "'"
                  + var2.getName()
                  + "' is using a different protocol version of the game ("
                  + var5
                  + ") than the server ("
                  + var4.highestSupportedVersion()
                  + "). This may cause gameplay and graphical issues. Consider to avoid multi-version servers. Read more here: https://a.devs.beer/modern-model-viav"
            );
         }

         ArrayList var6 = new ArrayList();
         if (var5 < Version.v1_21_4.getProtocol()) {
            if (ContentManager.method8084().bE) {
               var6.add("modern items (1.21.4+ only)");
            }

            if (Settings.get().emoteModernMethod) {
               var6.add("modern emotes (1.21.4+ only)");
            }

            if (ContentManager.method8084().ao.wh() > 0) {
               var6.add("ENTITY blocks (1.21.4+ only)");
            }
         }

         if (!var6.isEmpty()) {
            Log.warn("Limitations: " + String.join(", ", var6));
         }
      }
   }

   public static boolean method4872(Player var0) {
      return !registered
         ? Version.get().ordinal() >= Version.v1_21_4.ordinal()
         : Via.getAPI().getPlayerVersion(var0.getUniqueId()) >= Version.v1_21_4.getProtocol();
   }
}
