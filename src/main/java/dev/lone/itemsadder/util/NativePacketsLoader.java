package dev.lone.itemsadder.util;

import ia.sh.nativepackets.bootstrap.NativePacketsBootstrap;
import ia.sh.nativepackets.bootstrap.NativePacketsBootstrap.Environment;
import ia.sh.nativepackets.bootstrap.NativePacketsBootstrap.Mode;
import ia.sh.nativepackets.bootstrap.NativePacketsBootstrap.Requirement;
import ia.sh.nativepackets.bootstrap.NativePacketsBootstrap.Result;
import java.net.URI;
import java.util.logging.Logger;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class NativePacketsLoader {
   static final Requirement REQUIREMENT = new Requirement(2, "0.1.9");

   private NativePacketsLoader() {
   }

   static boolean isCompatible(JavaPlugin var0) {
      try {
         return NativePacketsBootstrap.compatible(NativePacketsBootstrap.capture(var0), REQUIREMENT);
      } catch (RuntimeException var2) {
         var0.getLogger().warning("Cannot inspect NativePackets: " + var2.getMessage());
         return false;
      }
   }

   static boolean ensureAvailable(JavaPlugin var0) {
      try {
         Environment var1 = NativePacketsBootstrap.capture(var0);
         if (NativePacketsBootstrap.available(var1, REQUIREMENT)) {
            return true;
         }

         Logger var2 = var0.getLogger();
         var2.warning("NativePackets is missing, disabled or incompatible; ItemsAdder initialization is paused.");
         if (!var0.getConfig().getBoolean("nativepackets.auto_download", true)) {
            var2.warning(
               "NativePackets automatic download is disabled (nativepackets.auto_download: false). Install a compatible NativePackets JAR in the plugins folder and restart the server. Download: https://github.com/LoneDev6/NativePackets-Releases/releases/latest"
            );
            var0.getServer().getPluginManager().disablePlugin(var0);
            return false;
         }

         URI var3 = NativePacketsBootstrap.manifestEndpoint();
         BukkitScheduler var4 = var0.getServer().getScheduler();
         var4.runTaskAsynchronously(var0, () -> {
            Result var5x = NativePacketsBootstrap.run(var1, REQUIREMENT, var3, Mode.REQUIRE);
            var2.warning(var5x.message());

            try {
               var4.runTask(var0, () -> var0.getServer().getPluginManager().disablePlugin(var0));
            } catch (IllegalPluginAccessException var7) {
            }
         });
      } catch (RuntimeException var5) {
         var0.getLogger().severe("NativePackets bootstrap failed: " + var5.getMessage());
         var0.getServer().getPluginManager().disablePlugin(var0);
      }

      return false;
   }
}
