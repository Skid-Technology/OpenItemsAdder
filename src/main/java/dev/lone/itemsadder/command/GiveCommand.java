package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.ComponentMapper;
import dev.lone.itemsadder.util.ComponentResult;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.function.UnaryOperator;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveCommand extends IaCommandBase {
   public GiveCommand() {
      Main.getCommand("iagive").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (!IaCommandBase.method1428(2, args)) {
         Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&e/iagive &b<player> <itemNameInConfig> [amount] [silent]"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&6Example: &e/iagive &bNotch sword2 3"));
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
            ItemStack var7 = ContentManager.method8084().method8173(args[1]);
            if (Main.aER) {
               ComponentMapper var8 = var1x -> method1517(var1x, var1xx -> PlaceholderAPI.setPlaceholders(var6, var1xx));
               CustomItemDefinition.method8809(var7, var8, var8);
            }

            int var9 = 1;
            if (args.length > 2) {
               var9 = Utils.parseIntOr(args[2], 1);
            }

            var7.setAmount(var9);
            ItemStackUtils.method7243(var6, var7);
            if (!var5) {
               Log.sendPrefixed(commandSender, Main.aFi.lj("given-item").replace("{item}", args[1]).replace("{player}", var6.getDisplayName()));
               Log.sendPrefixed(var6, Main.aFi.lj("obtained-item").replace("{item}", var7.getItemMeta().getDisplayName()));
            }
         } else if (!var5) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("item-not-found").replace("{item}", args[1]));
         }

         return true;
      }
   }

   static ComponentResult method1517(Component var0, UnaryOperator var1) {
      boolean[] var2 = new boolean[]{false};
      Component var3 = var0.replaceText(
         (TextReplacementConfig)TextReplacementConfig.builder().match(PlaceholderAPI.getPlaceholderPattern()).replacement(var2x -> {
            String var3x = var1.apply(var2x.content());
            var2[0] |= !var3x.equals(var2x.content());
            return var2x.content(var3x);
         }).build()
      );
      return new ComponentResult(var2[0] ? var3 : var0, var2[0]);
   }
}
