package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DurabilityCommand extends IaCommandBase {
   public DurabilityCommand() {
      Main.getCommand("iadurability").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else if (!IaCommandBase.method1428(1, args)) {
         Log.sendPrefixed(var5, Main.aFi.lj("wrong-command-usage"));
         Log.sendPrefixed(var5, Utils.toSectionCodes("&e/iadurability &b<amount>"));
         Log.sendPrefixed(var5, Utils.toSectionCodes("&6Example: &e/iadurability &b300"));
         return true;
      } else {
         ItemStack var6 = var5.getInventory().getItemInMainHand();
         if (!ItemStackUtils.aA(var6)) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("cannot-set-durability"));
            return true;
         } else if (!CustomItemDefinition.method8819(var6)) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("set-durability-successfully"));
            ItemStackUtils.method7235(var6, Integer.parseInt(args[0]));
            return true;
         } else {
            CustomItemDefinition.method8816(var6, Integer.parseInt(args[0]));
            Log.sendPrefixed(commandSender, Main.aFi.lj("set-durability-successfully"));
            return true;
         }
      }
   }
}
