package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.BlockInteractionListener;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CropsCommand extends IaCommandBase {
   public static Map aHf = new WeakHashMap();

   public CropsCommand() {
      Main.getCommand("crops").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (args.length != 0 && args.length <= 3) {
         boolean var5 = args[0].equalsIgnoreCase("enable");
         boolean var6 = PermissionNodes.method1632(commandSender, "ia.admin.crops.others");
         if (var5 && !var6 && System.currentTimeMillis() - aHf.getOrDefault(commandSender, 0L) < 10000L) {
            Log.sendLocalized(commandSender, "please-wait-command-cooldown");
            return true;
         }

         Player var7 = null;
         if (args.length == 1) {
            if (!(commandSender instanceof Player)) {
               return IaCommand.method1525(commandSender);
            }

            var7 = (Player)commandSender;
         } else if (args.length == 2) {
            if (!var6) {
               return true;
            }

            var7 = Bukkit.getPlayer(args[1]);
            if (var7 == null) {
               Log.sendLocalized(commandSender, "player-offline");
               return true;
            }
         }

         assert var7 != null;
         if (var5) {
            ContentManager.method8084().method8142().mZ.revealAllCrops(var7);
            BlockInteractionListener.setLightweightCrops(var7, false);
            Log.sendPrefixed(commandSender, Main.aFi.lj("crops-view-enabled"));
         } else {
            ContentManager.method8084().method8142().mZ.renderCropStagesFor(var7);
            BlockInteractionListener.setLightweightCrops(var7, true);
            ContentManager.method8084().method8142().nb.resetPlayer(var7);
            Log.sendPrefixed(commandSender, Main.aFi.lj("crops-view-disabled"));
         }

         aHf.put(var7, System.currentTimeMillis());
         return true;
      } else {
         Log.sendPrefixed(
            commandSender, Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /crops <enable|disable> [player]. Example: /crops enable LoneDev"
         );
         return true;
      }
   }
}
