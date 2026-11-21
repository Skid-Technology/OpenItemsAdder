package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerStatCommand extends IaCommandBase {
   public PlayerStatCommand() {
      Main.getCommand("iaplayerstat").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (args.length == 3) {
         if (args[0].equalsIgnoreCase("read")) {
            if (!PermissionNodes.method1632(commandSender, "ia.admin.iaplayerstat.read")) {
               return true;
            }

            Player var5 = Bukkit.getPlayer(args[1]);
            if (var5 == null) {
               Log.sendLocalized(commandSender, "player-offline");
               return true;
            }

            if (ContentManager.method8084().aG.method703(var5, args[2])) {
               String var6 = ContentManager.method8084().aG.method1429(var5, args[2], 0.0F) + "";
               commandSender.sendMessage(Main.aFi.lj("player-stat-value-read").replace("{value}", var6));
            } else {
               commandSender.sendMessage(
                  Main.aFi
                     .lj("player-stat-value-read")
                     .replace("{value}", ChatColor.WHITE + "stat " + ChatColor.AQUA + args[2] + ChatColor.WHITE + " not found")
               );
            }

            return true;
         }
      } else if (args.length >= 4) {
         if (args[0].equalsIgnoreCase("write")) {
            if (!PermissionNodes.method1632(commandSender, "ia.admin.iaplayerstat.write")) {
               return true;
            }

            Player var8 = Bukkit.getPlayer(args[1]);
            if (var8 == null) {
               Log.sendLocalized(commandSender, "player-offline");
               return true;
            }

            boolean var10 = args[args.length - 1].equals("silent");
            if (ContentManager.method8084().aG.method703(var8, args[2])) {
               if (ContentManager.method8084().aH.method877(args[2], var8)) {
                  if (var8 != commandSender) {
                     if (!var10) {
                        commandSender.sendMessage(Main.aFi.lj("player-stat-value-write-bypass"));
                     }

                     return true;
                  }

                  if (!var10) {
                     commandSender.sendMessage(Main.aFi.lj("player-stat-value-write-bypass-self"));
                  }
               }

               ContentManager.method8084().aH.method876(var8, args[2], Float.valueOf(args[3]));
               if (!var10) {
                  commandSender.sendMessage(Main.aFi.lj("player-stat-value-write"));
               }
            } else if (!var10) {
               commandSender.sendMessage(
                  Main.aFi
                     .lj("player-stat-value-read")
                     .replace("{value}", ChatColor.WHITE + "stat " + ChatColor.AQUA + args[2] + ChatColor.WHITE + " not found")
               );
            }
         } else if (args[0].equalsIgnoreCase("increment") || args[0].equalsIgnoreCase("decrement")) {
            if (!PermissionNodes.method1632(commandSender, "ia.admin.iaplayerstat.write")) {
               return true;
            }

            Player var9 = Bukkit.getPlayer(args[1]);
            if (var9 == null) {
               Log.sendLocalized(commandSender, "player-offline");
               return true;
            }

            boolean var11 = args[args.length - 1].equals("silent");
            if (ContentManager.method8084().aG.method703(var9, args[2])) {
               if (ContentManager.method8084().aH.method877(args[2], var9)) {
                  if (var9 != commandSender) {
                     if (!var11) {
                        commandSender.sendMessage(Main.aFi.lj("player-stat-value-write-bypass"));
                     }

                     return true;
                  }

                  if (!var11) {
                     commandSender.sendMessage(Main.aFi.lj("player-stat-value-write-bypass-self"));
                  }
               }

               float var7 = method1541(args, 3);
               if (args[0].equalsIgnoreCase("decrement")) {
                  var7 = -var7;
               }

               ContentManager.method8084().aH.method876(var9, args[2], ContentManager.method8084().aG.method1429(var9, args[2], 0.0F) + var7);
               if (!var11) {
                  commandSender.sendMessage(Main.aFi.lj("player-stat-value-write"));
               }
            } else if (!var11) {
               commandSender.sendMessage(Main.aFi.lj("player-stat-value-not-found").replace("{value}", args[2]));
            }
         }

         return true;
      }

      commandSender.sendMessage(
         Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iaplayerstat <read|write|increment|decrement> <player> <attribute> [value]"
      );
      return true;
   }

   private static float method1541(String[] var0, int var1) {
      try {
         return Float.valueOf(var0[var1]);
      } catch (Exception var3) {
         return 1.0F;
      }
   }
}
