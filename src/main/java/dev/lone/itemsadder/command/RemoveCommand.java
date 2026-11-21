package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.ItemSlotHandle;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PlayerInventoryProxy;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand extends IaCommandBase {
   public RemoveCommand() {
      Main.getCommand("iaremove").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (!IaCommandBase.method1428(2, args)) {
         Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&e/iaremove &b<player> <itemNameInConfig> [amount] [silent]"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&6Example: &e/iaremove &bNotch sword2 1"));
         return true;
      }

      boolean var5 = args.length == 4 && args[3].equalsIgnoreCase("silent");
      Player var6 = Bukkit.getPlayer(args[0]);
      if (var6 == null) {
         if (!var5) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("player-offline"));
         }

         return true;
      } else {
         if (ContentManager.method8084().method8155(args[1])) {
            ItemStack var7 = ContentManager.method8084().method8154(args[1]).Ds();
            int var8 = 1;
            if (args.length > 2) {
               var8 = Utils.parseIntOr(args[2], 1);
            }

            var7.setAmount(var8);
            int var9 = method1502(var6, var7);
            var6.updateInventory();
            if (!var5) {
               Log.sendPrefixed(commandSender, Main.aFi.lj("removed-item").replace("{item}", args[1] + "x" + var9).replace("{player}", var6.getDisplayName()));
               Log.sendPrefixed(var6, Main.aFi.lj("lost-item").replace("{item}", args[1] + "x" + var9));
            }
         } else if (!var5) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("item-not-found").replace("{item}", args[1]));
         }

         return true;
      }
   }

   public static int method1502(Player var0, ItemStack var1) {
      int var2 = 0;
      PlayerInventoryProxy var3 = new PlayerInventoryProxy(var0);

      for (int var4 = 0; var4 < var3.getSize(); var4++) {
         ItemSlotHandle var5 = var3.getSlotHandle(var4);
         if (var5 != null && !var5.empty && CustomItemDefinition.method8780(var5.item, var1)) {
            while (var3.getAmount(var4) > 0 && var1.getAmount() > 0) {
               var3.clearSlot(var4);
               var1.setAmount(var1.getAmount() - 1);
               var2++;
            }
         }
      }

      var3.applyChanges();
      return var2;
   }
}
