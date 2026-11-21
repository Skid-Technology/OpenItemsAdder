package dev.lone.itemsadder.Core.OtherPlugins.vulcan;

import dev.lone.itemsadder.Core.OtherPlugins.RetPar;
import java.util.function.Consumer;
import me.frep.vulcan.api.VulcanAPI.Factory;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class VulcanHook implements Listener {
   private final RetPar isPlayerProtected;
   private final Consumer msg;

   public static void register(Plugin plugin, RetPar isPlayerProtected, Consumer msg) {
      new VulcanHook(plugin, isPlayerProtected, msg);
   }

   VulcanHook(Plugin plugin, RetPar isPlayerProtected, Consumer msg) {
      this.isPlayerProtected = isPlayerProtected;
      this.msg = msg;

      try {
         if (Factory.getApi().getEnabledChecks().containsKey("BadPacketsV")) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
            FileConfiguration var4 = Bukkit.getPluginManager().getPlugin("Vulcan").getConfig();
            if (!var4.getBoolean("settings.enable-api") && !var4.getBoolean("enable-api")) {
               msg.accept("Please set 'enable-api: true' in Vulcan anticheat config.yml in order to enable compatibility and run '/vulcan reload'.");
            }
         }
      } catch (Throwable var5) {
         msg.accept("Failed to check Vulcan anticheat compatibility");
         var5.printStackTrace();
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onVulcanFlag(VulcanFlagEvent e) {
      Check var2 = e.getCheck();
      if (var2.getName().equals("badpackets") && var2.getType() == 'v' && (Boolean)this.isPlayerProtected.call(e.getPlayer())) {
         e.setCancelled(true);
      }
   }
}
