package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RepairCommand extends IaCommandBase {
   public RepairCommand() {
      Main.getCommand("iarepair").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         ItemStack var6 = var5.getInventory().getItemInMainHand();
         if (!CustomItemDefinition.method8819(var6)) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("cannot-repair"));
            return true;
         } else {
            CustomItemDefinition.method8822(var6);
            Log.sendPrefixed(commandSender, Main.aFi.lj("repaired-successfully"));
            return true;
         }
      }
   }
}
