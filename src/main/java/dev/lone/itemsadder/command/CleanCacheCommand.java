package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CleanCacheCommand extends IaCommandBase {
   public CleanCacheCommand() {
      Main.getCommand("iacleancache").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      label69: {
         if (args.length <= 0) {
            commandSender.sendMessage(Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iacleancache <items|blocks>");
            return true;
         }

         if ((args[0].equals("items") || args[0].equals("blocks")) && !CustomBlockStateSettings.getInstance().isLoadCompleted()) {
            Log.sendPrefixed(commandSender, ChatColor.RED + "Cannot clean IDs because the latest content load did not complete successfully.");
            return true;
         }

         switch (args[0]) {
            case "items":
               int var8 = CustomBlockStateSettings.getInstance().cleanItemIds(commandSender);
               if (!(commandSender instanceof Player)) {
                  Log.sendPrefixed(commandSender, " ");
                  Log.sendPrefixed(commandSender, " ");
               }

               Log.sendLocalized(commandSender, "ia-cleancache-regenerate-pack");
               if (!(commandSender instanceof Player)) {
                  Log.sendPrefixed(commandSender, " ");
                  Log.sendPrefixed(commandSender, " ");
               }

               if (var8 > 0) {
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "iareload");
               }

               return true;
            case "blocks":
               int var7 = CustomBlockStateSettings.getInstance().cleanBlockIds();
               if (!(commandSender instanceof Player)) {
                  Log.sendPrefixed(commandSender, " ");
                  Log.sendPrefixed(commandSender, " ");
               }

               Log.sendLocalizedWith(commandSender, "ia-cleancache-removed-blocks", var1x -> var1x.replace("{count}", String.valueOf(var7)));
               Log.sendLocalized(commandSender, "ia-cleancache-regenerate-pack");
               if (!(commandSender instanceof Player)) {
                  Log.sendPrefixed(commandSender, " ");
                  Log.sendPrefixed(commandSender, " ");
               }

               if (var7 > 0) {
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "iareload");
               }

               return true;
            default:
               break label69;
         }
      }
   }
}
