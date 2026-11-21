package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DropCommand extends IaCommandBase {
   public DropCommand() {
      Main.getCommand("iadrop").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (!IaCommandBase.method1428(2, args)) {
         this.method1568(commandSender);
         return true;
      }

      boolean var5 = false;
      boolean var8 = args.length == 4 && args[3].equalsIgnoreCase("silent");
      if (!ContentManager.method8084().method8155(args[0])) {
         if (!var8) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("item-not-found").replace("{item}", args[0]));
         }

         return true;
      } else {
         ItemStack var7 = ContentManager.method8084().method8154(args[0]).Ds();
         Location var6;
         if (Utils.isNumeric(args[1])) {
            if (args.length < 5) {
               this.method1568(commandSender);
               return true;
            }

            if (!Utils.isNumeric(args[1]) || !Utils.isNumeric(args[2]) || !Utils.isNumeric(args[3])) {
               this.method1568(commandSender);
               return true;
            }

            World var9 = Bukkit.getWorld(args[4]);
            if (var9 == null) {
               if (!var8) {
                  Log.sendLocalizedWith(commandSender, "world-not-found", var1x -> var1x.replace("{world}", args[4]));
               }

               return true;
            }

            var6 = new Location(var9, Utils.parseFloatOr(args[1], 0.0F), Utils.parseFloatOr(args[2], 0.0F), Utils.parseFloatOr(args[3], 0.0F));
            int var11 = 1;
            if (args.length > 5) {
               var11 = Utils.parseIntOr(args[5], 1);
            }

            var7.setAmount(var11);
         } else {
            Player var12 = Bukkit.getPlayer(args[1]);
            if (var12 == null) {
               if (!var8) {
                  Log.sendPrefixed(commandSender, Main.aFi.lj("player-offline"));
               }

               return true;
            }

            var6 = var12.getLocation();
            int var10 = 1;
            if (args.length > 2) {
               var10 = Utils.parseIntOr(args[2], 1);
            }

            var7.setAmount(var10);
            var5 = true;
         }

         LocationScanner.dropItem(var6, var7);
         if (!var8) {
            if (var5) {
               Log.sendPrefixed(commandSender, Main.aFi.lj("dropped-item-player").replace("{item}", args[0]).replace("{player}", args[1]));
            } else {
               Log.sendPrefixed(
                  commandSender,
                  Main.aFi
                     .lj("dropped-item-coords")
                     .replace("{item}", args[0])
                     .replace("{coords}", var6.getBlockX() + " " + var6.getBlockY() + " " + var6.getBlockZ() + " " + var6.getWorld().getName())
               );
            }
         }

         return true;
      }
   }

   private void method1568(CommandSender var1) {
      Log.sendPrefixed(var1, Main.aFi.lj("wrong-command-usage"));
      Log.sendPrefixed(var1, Utils.toSectionCodes("&e/iadrop <itemname> <player|x y z world> [amount]"));
      Log.sendPrefixed(var1, Utils.toSectionCodes("&6Example: &e/iadrop ruby Notch 3"));
   }
}
