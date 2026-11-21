package dev.lone.itemsadder.command;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IaCommandBase implements CommandExecutor {
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      return true;
   }

   protected static void method1422(CommandSender var0, String var1) {
      if (var0 instanceof Player) {
         Log.sendPrefixed(var0, var1);
      } else {
         Log.warn(var1);
      }
   }

   protected boolean method1423(CommandSender var1, Command var2) {
      if (!ContentManager.method8084().method8090()) {
         method1422(var1, ChatColor.YELLOW + "Plugin is still loading... Please wait before using " + ChatColor.AQUA + "/" + var2.getName());
         return false;
      } else {
         return true;
      }
   }

   public static String method1424(String[] var0, int var1, String var2) {
      return var1 > var0.length - 1 ? var2 : var0[var1];
   }

   public static int method1425(String[] var0, int var1, int var2) {
      return var1 > var0.length - 1 ? var2 : Utils.parseIntOr(var0[var1], var2);
   }

   public static float method1426(String[] var0, int var1, float var2) {
      return var1 > var0.length - 1 ? var2 : Utils.parseFloatOr(var0[var1], var2);
   }

   public static boolean method1427(@NotNull String[] var0, int var1, boolean var2) {
      return var1 > var0.length - 1 ? var2 : Utils.parseBoolOr(var0[var1], var2);
   }

   public static boolean method1428(int var0, String... var1) {
      if (var1.length != 0 && var1.length >= var0) {
         for (String var5 : var1) {
            if (var5 == null || var5.isEmpty()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
