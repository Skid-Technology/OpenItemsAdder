package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GetCommand extends IaCommandBase {
   public GetCommand() {
      Main.getCommand("iaget").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (!IaCommandBase.method1428(1, args)) {
            Log.sendPrefixed(var5, Main.aFi.lj("wrong-command-usage"));
            Log.sendPrefixed(var5, ChatColor.YELLOW + "/iaget " + ChatColor.AQUA + "<itemNameInConfig> [amount]");
            Log.sendPrefixed(var5, ChatColor.GOLD + "Example: " + ChatColor.YELLOW + "/iaget " + ChatColor.AQUA + "sword1");
            return true;
         }

         if (ContentManager.method8084().method8155(args[0])) {
            ItemStack var6 = ContentManager.method8084().method8173(args[0]);
            int var7 = 1;
            if (args.length > 1) {
               var7 = Utils.parseIntOr(args[1], 1);
            }

            var6.setAmount(var7);
            ItemStackUtils.method7243(var5, var6);
            Log.sendPrefixed(var5, Main.aFi.lj("obtained-item").replace("{item}", args[0]));
         } else {
            Log.sendPrefixed(var5, Main.aFi.lj("item-not-found").replace("{item}", args[0]));
         }

         return true;
      }
   }
}
