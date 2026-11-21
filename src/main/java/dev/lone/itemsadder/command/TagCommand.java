package dev.lone.itemsadder.command;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.GsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TagCommand extends IaCommandBase {
   public TagCommand() {
      Main.getCommand("iatag").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warn(Main.aFi.lj("command-ingame-only"));
         return true;
      } else {
         if (!PermissionNodes.method1632(var5, "ia.admin.iatag")) {
            return true;
         }

         ItemStack var6 = var5.getInventory().getItemInMainHand();
         if (var6.getType() == Material.AIR) {
            return true;
         }

         CustomItemDefinition var7 = ContentManager.method8084().method8149(var6);
         if (var7 != null) {
            var5.sendMessage(ChatColor.DARK_GREEN + "Custom Item: " + ChatColor.GRAY + var7.getNamespacedID());
            var5.sendMessage(ChatColor.DARK_GREEN + "File: " + ChatColor.GRAY + var7.DJ());
         } else {
            var5.sendMessage(ChatColor.DARK_GREEN + "Not a Custom Item: " + ChatColor.GRAY + var6.getType());
         }

         NItem var8 = new NItem(var6);
         boolean var9 = Utils.containsString(args, "pretty-print");
         String var10 = "NBT data for the item in " + var5.getName() + "'s hand:";
         String var11 = var9 ? GsonHelper.prettify(var8.toString()) : var8.toString();
         boolean var12 = Utils.containsString(args, "show-in-console");
         boolean var13 = Utils.containsString(args, "show-in-chat") || !var12;
         if (var13) {
            var5.sendMessage(var10);
            var5.sendMessage(var11);
         }

         if (commandSender.hasPermission("ia.admin.iatag.show-in-console")) {
            if (var12) {
               var5.sendMessage(ChatColor.GRAY + "(NBT data sent to console)");
               Log.log(var10);
               Log.log(var11);
            }
         } else {
            var5.sendMessage(ChatColor.RED + "You don't have permission to show the NBT data in the console.");
         }

         return true;
      }
   }
}
