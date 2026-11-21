package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaySoundCommand extends IaCommandBase {
   public PlaySoundCommand() {
      Main.getCommand("iaplaysound").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (args.length < 2) {
         Log.sendPrefixed(
            commandSender,
            Main.aFi.lj("wrong-command-usage")
               + ChatColor.YELLOW
               + " /iaplaysound <sound> <player>. Example: /iaplaysound itemsadder:ambient.angelic LoneDev 10 1"
         );
         return true;
      } else {
         Player var5 = Bukkit.getPlayer(args[1]);
         if (var5 == null) {
            Log.sendLocalized(commandSender, "player-offline");
            return true;
         } else {
            var5.playSound(var5.getLocation(), args[0], IaCommandBase.method1426(args, 2, 10.0F), IaCommandBase.method1426(args, 3, 1.0F));
            return true;
         }
      }
   }
}
