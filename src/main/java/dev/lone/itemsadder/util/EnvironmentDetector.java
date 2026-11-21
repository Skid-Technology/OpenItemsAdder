package dev.lone.itemsadder.util;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EnvironmentDetector {
   public static boolean paperAdventure = false;
   public static boolean isPurpur = false;
   public static boolean useNativeAdventure;
   private static final List SUPPORTED_VERSIONS = Arrays.asList(
      Version.v1_20_6,
      Version.v1_21,
      Version.v1_21_1,
      Version.v1_21_3,
      Version.v1_21_4,
      Version.v1_21_5,
      Version.v1_21_6,
      Version.v1_21_7,
      Version.v1_21_8,
      Version.v1_21_10,
      Version.v1_21_11,
      Version.v26_1_1,
      Version.v26_1_2,
      Version.v26_2,
      Version.v26_3
   );

   private static boolean isSupportedVersion() {
      return SUPPORTED_VERSIONS.contains(Version.get());
   }

   private static String getSupportedVersionsText() {
      return SUPPORTED_VERSIONS.stream().map(var0 -> var0.getName()).reduce((var0, var1) -> var0 + ", " + var1).orElse("");
   }

   public static void init() {
      paperAdventure = ServerCompat.isPaper();
      isPurpur = Bukkit.getVersion().contains("Purpur");
      useNativeAdventure = paperAdventure;
      ServerCompat.nativeAdventure = paperAdventure && ServerCompat.locateClassJar("net.kyori.adventure.text.TextComponent") == JarLocation.SERVER_JAR;
      ServerCompat.viaVersionPresent = Bukkit.getPluginManager().getPlugin("ViaVersion") != null
         && ServerCompat.classExists("com.viaversion.viaversion.api.Via");
   }

   public static boolean checkServerSupport() {
      if (!ServerCompat.classExists("org.spigotmc.SpigotConfig")) {
         logFatalAndStop(
            ChatColor.RED
               + "You are using a server which has no SpigotAPI available. CraftBukkit and similar are not supported. \n"
               + ChatColor.DARK_GRAY
               + "Please use Spigot or Paper.\n"
               + ChatColor.DARK_GRAY
               + "Download: https://itemsadder.devs.beer/faq/download-spigot"
         );
         return false;
      }

      if (!isSupportedVersion()) {
         Log.error("════════════════════════════════════════════════════════════════════");
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
         Log.error("This server version is not supported: " + Version.get().getName());
         Log.error("Use one of the supported versions: " + getSupportedVersionsText());
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
         Log.error("════════════════════════════════════════════════════════════════════");
         Main.stop();
         return false;
      }

      if (Bukkit.getPluginManager().isPluginEnabled("LoneLibs")) {
         Log.warn("LoneLibs is enabled. This plugin is not needed anymore and it's recommended to remove it (if you are not using it for other plugins).");
      }

      return true;
   }

   private static void logFatalAndStop(String var0) {
      Log.error(" ");
      Log.error(" ");
      Log.error(" ");
      Log.error(var0);
      Log.error(" ");
      Log.error(" ");
      Log.error(" ");
      Main.stop();
   }
}
