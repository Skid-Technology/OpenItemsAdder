package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElement;
import dev.lone.itemsadder.core.PlayerEffectState;
import dev.lone.itemsadder.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HudCommand extends IaCommandBase {
   public HudCommand() {
      Main.getCommand("iahud").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (args.length == 0) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iahud <hud>. Example: /iahud magiccraft:mana_bar");
            return true;
         }

         for (NamespacedElement var7 : ((PlayerEffectState)ContentManager.method8084().aH.acB.get(var5)).acJ) {
            if (var7.getNamespacedID().equals(args[0])) {
               var7.setVisible(!var7.isVisible());
            }
         }

         ContentManager.method8084().aH.az(var5);
         return true;
      }
   }
}
