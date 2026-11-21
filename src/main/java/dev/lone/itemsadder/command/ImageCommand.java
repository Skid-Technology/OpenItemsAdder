package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ImageCommand extends IaCommandBase {
   public ImageCommand() {
      Main.getCommand("iaimage").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (!PermissionNodes.method1632(var5, "ia.user.image.gui")) {
            return true;
         }

         if (args.length == 0) {
            ContentManager.method8084().bC.bM(var5);
         } else {
            String var6 = ChatColor.stripColor(String.join(" ", args));
            var5.chat(var6);
         }

         return true;
      }
   }
}
