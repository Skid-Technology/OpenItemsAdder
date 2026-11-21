package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.recipe.RecipeLoader;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RecipeLookupCommand extends IaCommandBase {
   public RecipeLookupCommand() {
      Main.getCommand("iarecipe").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(sender, command)) {
         return true;
      }

      if (!PermissionNodes.method1632(sender, "ia.user.iarecipe")) {
         return true;
      }

      if (args.length == 2) {
         if (!PermissionNodes.method1632(sender, "ia.user.iarecipe.others")) {
            return true;
         }

         var6 = Bukkit.getPlayer(args[1]);
         if (var6 == null) {
            Log.sendLocalized(sender, "player-offline");
            return true;
         }
      } else if (!(sender instanceof Player var6)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      }

      CustomItemDefinition var5;
      if (args.length == 0) {
         var5 = ContentManager.method8084().method8149(ItemStackUtils.dn(var6));
         if (var5 == null) {
            var6.sendMessage(Main.aFi.lj("iarecipe-no-custom-item"));
            return true;
         }
      } else {
         var5 = ContentManager.method8084().method8154(args[0]);
         if (var5 == null) {
            var6.sendMessage(Main.aFi.lj("iarecipe-wrong-usage"));
            return true;
         }
      }

      ((RecipeLoader)ContentManager.method8084().bB.aJX.aIQ.get(var5.getNamespacedID())).method2548(null, null, var6, true);
      return true;
   }
}
