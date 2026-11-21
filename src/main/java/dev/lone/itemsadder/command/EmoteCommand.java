package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.EmoteResult;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EmoteCommand implements CommandExecutor {
   public EmoteCommand() {
      Main.getCommand("iaemote").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!Settings.get().customEntitiesEnabled) {
         Log.sendLocalized(sender, "custom-entities-disabled");
         return true;
      }

      if (!Settings.get().emotesEnabled) {
         Log.sendLocalized(sender, "emotes-disabled");
         return true;
      }

      if (args.length > 0) {
         if (CustomEntityManager.getInstance().fG()) {
            if (args.length > 1) {
               if (!sender.hasPermission("ia.user.iaemote.others")) {
                  Log.sendPrefixed(sender, Main.aFi.lj("no-permission-detail").replace("{permission}", "ia.user.iaemote.others"));
                  return true;
               }

               var5 = Bukkit.getPlayer(args[1]);
            } else if (!(sender instanceof Player var5)) {
               Log.warnLocalized("command-ingame-only");
               return true;
            }

            if (var5 == null) {
               Log.sendPrefixed(sender, Main.aFi.lj("player-offline"));
               return true;
            }

            if (args[0].equalsIgnoreCase("stop-now")) {
               CustomEntityManager.getInstance().method8327(var5);
               return true;
            }

            if (args[0].equalsIgnoreCase("stop")) {
               CustomEntityManager.getInstance().method8328(var5);
               return true;
            }

            if (PermissionNodes.method1631(sender, PermissionNodes.et(args[0]), true)) {
               if (args[0].equals("idle")) {
                  Log.sendLocalized(sender, "this-animation-cannot-be-used-as-emote");
                  return true;
               }

               EmoteResult var6 = CustomEntityManager.getInstance().method8315(var5, args[0]);
               if (var6 == EmoteResult.UNKNOWN_EMOTE) {
                  Log.sendLocalizedWith(sender, "unknown-emote", var1x -> var1x.replace("{emote}", args[0]));
               }
            }
         } else {
            Log.sendPrefixed(sender, ChatColor.RED + "FATAL ERROR! Can't find player model.");
            Log.error("FATAL ERROR! Can't find player model.");
         }
      } else {
         Log.sendLocalized(sender, "no-emote-specified");
      }

      return true;
   }
}
