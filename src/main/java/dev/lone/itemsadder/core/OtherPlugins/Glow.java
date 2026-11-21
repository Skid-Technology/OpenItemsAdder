package dev.lone.itemsadder.Core.OtherPlugins;

import fr.skytasul.glowingentities.GlowingEntities;
import java.util.Collection;
import java.util.function.BiConsumer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class Glow {
   @Nullable
   private static GlowingEntities API;
   private static RetPar getNearbyPlayers;
   private static boolean hasFailedOneTime = false;
   private static BiConsumer errorLogger;

   public static void init(Plugin plugin, RetPar getNearbyPlayers, BiConsumer/* @Nullable */ errorLogger) {
      Glow.errorLogger = errorLogger;

      try {
         Glow.getNearbyPlayers = getNearbyPlayers;
         plugin.getLogger().info("Loading GlowingEntities API");
         API = new GlowingEntities(plugin);
      } catch (Exception var4) {
         errorLogger.accept("Failed to initialize the Glow API.", var4);
         API = null;
      }
   }

   public static void set(Entity entity, String str) {
      if (API != null) {
         for (Player var3 : (Collection)getNearbyPlayers.call(entity.getLocation())) {
            try {
               API.setGlowing(entity, var3, ChatColor.valueOf(str));
            } catch (Exception var5) {
               if (!hasFailedOneTime) {
                  errorLogger.accept("Failed to set glow color for entity " + entity.getType().name() + " to " + str + ". Disabling feature.", var5);
                  hasFailedOneTime = true;
               }
            }
         }
      }
   }
}
