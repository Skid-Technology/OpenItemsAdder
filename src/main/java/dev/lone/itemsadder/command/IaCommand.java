package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.GuiIconRegistry;
import dev.lone.itemsadder.gui.GuiPage;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IaCommand extends IaCommandBase {
   public IaCommand() {
      Main.getCommand("ia").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      GuiIconRegistry var5 = ContentManager.method8084().bB;
      if (ContentManager.method8084().field82 && var5 != null) {
         if (args.length == 0) {
            if (!(commandSender instanceof Player)) {
               return method1525(commandSender);
            }

            var5.aJX.bT((Player)commandSender);
         } else {
            if (!PermissionNodes.method1632(commandSender, "ia.user.ia.categories")) {
               return true;
            }

            if (args.length == 2) {
               if (!PermissionNodes.method1632(commandSender, "ia.user.ia.categories.others")) {
                  return true;
               }

               var6 = Bukkit.getPlayer(args[1]);
               if (var6 == null) {
                  Log.sendLocalized(commandSender, "player-offline");
                  return true;
               }
            } else if (!(commandSender instanceof Player var6)) {
               return method1525(commandSender);
            }

            String var7 = args[0];
            GuiPage var8 = var5.aJX.eF(var7);
            if (var8 != null) {
               if (var8.method2527(commandSender)) {
                  var8.method2529(var6, true);
               } else {
                  Log.sendPrefixed(commandSender, Main.aFi.lj("no-permission"));
               }
            } else {
               Log.sendPrefixed(commandSender, Main.aFi.lj("unknown-category").replace("{name}", var7));
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean method1525(CommandSender var0) {
      Log.warnLocalized("command-ingame-only");
      return true;
   }
}
