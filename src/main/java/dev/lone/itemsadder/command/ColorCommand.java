package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.util.ColorParser;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

public class ColorCommand extends IaCommandBase {
   public ColorCommand() {
      Main.getCommand("iacolor").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (args.length == 1) {
         if (!(commandSender instanceof Player var5)) {
            return IaCommand.method1525(commandSender);
         }
      } else {
         if (args.length != 2) {
            Log.sendPrefixed(
               commandSender, Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iacolor <color> [player]. Example: /iacolor #32a852 LoneDev"
            );
            return true;
         }

         if (!PermissionNodes.method1632(commandSender, "ia.user.ia.categories.others")) {
            return true;
         }

         var5 = Bukkit.getPlayer(args[1]);
         if (var5 == null) {
            Log.sendLocalized(commandSender, "player-offline");
            return true;
         }
      }

      Integer var6 = ColorParser.parse(args[0]);
      if (var6 == null) {
         Log.sendLocalizedWith(commandSender, "invalid-hex-color", var1x -> var1x.replace("{color}", args[0]));
         return true;
      }

      Color var7 = Color.fromRGB(var6);
      ItemStack var8 = var5.getInventory().getItemInMainHand();
      ItemMeta var9 = var8.getItemMeta();
      if (var9 instanceof PotionMeta) {
         ((PotionMeta)var9).setColor(var7);
      } else {
         if (!(var9 instanceof LeatherArmorMeta)) {
            ItemOperationsService.getRegistryBridge().method3438(var8, var7);
            return true;
         }

         ((LeatherArmorMeta)var9).setColor(var7);
      }

      var8.setItemMeta(var9);
      Log.sendLocalized(commandSender, "colored-successfully");
      return true;
   }
}
