package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Log {
   public static Component prefixComponent = AdventureText.fromLegacy("[ItemsAdder] ");
   public static String prefix = "[ItemsAdder] ";
   private static final ColoredLogger logger = new ColoredLogger(prefix);

   public static void setPrefixComponent(Component var0) {
      prefixComponent = var0;
      logger.setPrefix(AdventureText.toPlainText(var0));
   }

   public static void setPrefix(String var0) {
      logger.setPrefix(var0);
   }

   public static void logToSender(CommandSender var0, String var1) {
      logToSenderWithTrace(var0, var1, null);
   }

   public static void logToSenderWithTrace(CommandSender var0, String var1, @Nullable Throwable var2) {
      log(var1);
      if (var0 != null && var0 != Bukkit.getConsoleSender()) {
         sendPrefixed(var0, var1);
      }

      if (var2 != null) {
         var2.printStackTrace();
      }
   }

   public static void log(String message) {
      logger.logColored(Level.INFO, message);
   }

   public static void logLevel(String var0, Level var1) {
      logger.logColored(var1, var0);
   }

   public static void error(String var0) {
      if (ChatColor.stripColor((String)var0).equals(var0)) {
         logger.logColored(Level.SEVERE, ChatColor.RED + var0);
      } else {
         logger.logColored(Level.SEVERE, (String)var0);
      }
   }

   public static void error(String var0, @Nullable Throwable var1) {
      if (ChatColor.stripColor((String)var0).equals(var0)) {
         logger.logColored(Level.SEVERE, ChatColor.RED + var0);
      } else {
         logger.logColored(Level.SEVERE, (String)var0);
      }

      if (var1 != null) {
         var1.printStackTrace();
      }
   }

   public static void errorToSender(CommandSender var0, String var1) {
      errorToSenderWithTrace(var0, var1, null);
   }

   public static void errorToSenderWithTrace(CommandSender var0, String var1, @Nullable Throwable var2) {
      if (var0 instanceof Player) {
         sendPrefixed(var0, (String)var1);
      } else if (ChatColor.stripColor((String)var1).equals(var1)) {
         logger.logColored(Level.SEVERE, ChatColor.RED + var1);
      } else {
         logger.logColored(Level.SEVERE, (String)var1);
      }

      if (var2 != null) {
         var2.printStackTrace();
      }
   }

   public static void warn(String var0) {
      if (ChatColor.stripColor((String)var0).equals(var0)) {
         logger.logColored(Level.WARNING, ChatColor.YELLOW + var0);
      } else {
         logger.logColored(Level.WARNING, (String)var0);
      }
   }

   public static void warnToSender(CommandSender var0, String var1) {
      warn((String)var1);
      sendPrefixed(var0, net.md_5.bungee.api.ChatColor.YELLOW + var1);
   }

   public static void warn(String var0, @Nullable Throwable var1) {
      warn(var0);
      if (var1 != null) {
         var1.printStackTrace();
      }
   }

   public static void sendPlain(CommandSender var0, String var1) {
      var0.sendMessage(var1);
   }

   public static void sendComponent(CommandSender var0, Component var1) {
      if (ServerCompat.isPaper()) {
         var0.sendMessage(var1);
      } else {
         var0.spigot().sendMessage(AdventureText.toBungee(var1));
      }
   }

   public static void sendPrefixed(CommandSender var0, String var1) {
      sendComponent(var0, prefixComponent.append(AdventureText.fromLegacy(var1)));
   }

   public static void sendLocalized(CommandSender var0, String var1) {
      sendComponent(var0, prefixComponent.append(Main.aFi.method7070(var1)));
   }

   public static void sendLocalizedWith(CommandSender var0, String var1, UnaryFunctionA var2) {
      String var3 = Main.aFi.lk(var1);
      if (var3 == null) {
         errorToSender(var0, "Localized message not found for path: " + var1);
         sendComponent(var0, prefixComponent.append(AdventureText.parseRichText(var1)));
      } else {
         if (var2 != null) {
            var3 = (String)var2.accept(var3);
         }

         sendComponent(var0, prefixComponent.append(AdventureText.parseRichText(var3)));
      }
   }

   public static void warnLocalized(String var0) {
      Object var1 = AdventureText.toPlainText(Main.aFi.method7070(var0));
      if (ChatColor.stripColor((String)var1).equals(var1)) {
         logger.logColored(Level.WARNING, ChatColor.YELLOW + var1);
      } else {
         logger.logColored(Level.WARNING, (String)var1);
      }
   }

   public static void broadcastToOps(String var0) {
      log(var0);

      for (Player var2 : Bukkit.getOnlinePlayers()) {
         if (var2.isOp()) {
            sendComponent(var2, prefixComponent.append(Component.text(var0)));
         }
      }
   }

   public static void printBanner() {
      log(
         ChatColor.AQUA
            + "\n"
            + ChatColor.AQUA
            + "                                               "
            + ChatColor.DARK_GREEN
            + "    ItemsAdder"
            + ChatColor.GRAY
            + " "
            + Main.loadPluginYaml(Main.getInstance().getFile()).get("version")
            + "\n"
            + ChatColor.AQUA
            + "  ___  ___        __        __   __   ___  __  "
            + ChatColor.DARK_GREEN
            + "    NativePackets"
            + ChatColor.GRAY
            + " "
            + Bukkit.getPluginManager().getPlugin("NativePackets").getDescription().getVersion()
            + "\n"
            + ChatColor.AQUA
            + "|  |  |__   |\\/| /__`  /\\  |  \\ |  \\ |__  |__) "
            + ChatColor.DARK_GRAY
            + "    "
            + Bukkit.getServer().getName()
            + " "
            + Bukkit.getServer().getVersion()
            + "\n"
            + ChatColor.AQUA
            + "|  |  |___  |  | .__/ /--\\ |__/ |__/ |___ |  \\ "
            + ChatColor.DARK_GRAY
            + "    Build Date: "
            + Main.loadPluginYaml(Main.getInstance().getFile()).get("build-timestamp")
            + "\n"
            + ChatColor.AQUA
            + "                                               "
            + ChatColor.DARK_GRAY
            + "    Java Version: "
            + System.getProperty("java.version")
            + "\n"
            + ChatColor.AQUA
            + "                                               "
            + ChatColor.DARK_GRAY
            + "    OS: "
            + System.getProperty("os.name")
            + " "
            + System.getProperty("os.version")
            + ChatColor.AQUA
            + "                                               "
      );
   }

   public static void devLog(String var0) {
      if (ServerCompat.isDevMode()) {
         log(ChatColor.AQUA + "### [DEV MODE LOG] " + ChatColor.RESET + var0);
      }
   }

   public static void devLogWithTrace(String var0, Throwable var1) {
      if (ServerCompat.isDevMode()) {
         logger.logColored(Level.INFO, ChatColor.RED + "### [DEV MODE LOG] " + ChatColor.RESET + var0, var1);
      }
   }

   public static void devWarn(String var0) {
      if (ServerCompat.isDevMode()) {
         warn(ChatColor.RED + "### [DEV MODE LOG] " + ChatColor.RESET + var0);
      }
   }
}
