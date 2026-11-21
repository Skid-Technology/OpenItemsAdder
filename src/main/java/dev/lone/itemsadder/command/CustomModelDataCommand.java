package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CustomModelDataCommand extends IaCommandBase {
   public CustomModelDataCommand() {
      Main.getCommand("iacustommodeldata").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (!IaCommandBase.method1428(1, args)) {
         Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
         Log.sendPrefixed(commandSender, ChatColor.YELLOW + "/iacustommodeldata " + ChatColor.AQUA + "<itemNameInConfig>");
         Log.sendPrefixed(commandSender, ChatColor.GOLD + "Example: " + ChatColor.YELLOW + "/iacustommodeldata " + ChatColor.AQUA + "sword1");
         return true;
      }

      if (ContentManager.method8084().method8155(args[0])) {
         CustomItemDefinition var5 = ContentManager.method8084().method8154(args[0]);
         Log.sendPrefixed(commandSender, var5.getNamespacedID() + " -> " + var5.getType() + " {CustomModelData: " + var5.nz() + "}");
      } else {
         Log.sendPrefixed(commandSender, Main.aFi.lj("item-not-found").replace("{item}", args[0]));
      }

      return true;
   }
}
