package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UndisguiseCommand implements CommandExecutor {
   public UndisguiseCommand() {
      Main.getCommand("iaundisguise").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!Settings.get().customEntitiesEnabled) {
         Log.sendPrefixed(sender, ChatColor.YELLOW + "custom-entities is disabled in config.yml");
         return true;
      }

      if (sender instanceof Player var5) {
         if (CustomEntityManager.getInstance().stopEmote(var5)) {
            Log.sendLocalized(sender, "undisguised");
         } else {
            Log.sendLocalized(sender, "not-disguised");
         }

         return true;
      } else {
         Log.sendPrefixed(sender, Main.aFi.lj("command-ingame-only"));
         return true;
      }
   }
}
