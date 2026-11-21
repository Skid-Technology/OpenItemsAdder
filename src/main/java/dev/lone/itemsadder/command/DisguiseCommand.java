package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.DisguiseResolver;
import dev.lone.itemsadder.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DisguiseCommand implements CommandExecutor {
   public DisguiseCommand() {
      Main.getCommand("iadisguise").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!Settings.get().customEntitiesEnabled) {
         Log.sendLocalized(sender, "custom-entities-disabled");
         return true;
      }

      if (args.length > 0) {
         String var5 = args[0];
         if (!DisguiseResolver.isKnownEntity(var5)) {
            Log.sendLocalizedWith(sender, "unknown-entity", var1x -> var1x.replace("{entity}", var5));
            return true;
         }

         if (sender instanceof Player var6) {
            boolean var7 = CustomEntityManager.getInstance().startEmoteActor(var6, var5);
            if (!var7) {
               Log.sendLocalizedWith(sender, "disguise-failed", var1x -> var1x.replace("{entity}", var5));
            }

            return true;
         } else {
            Log.sendPrefixed(sender, Main.aFi.lj("command-ingame-only"));
            return true;
         }
      } else {
         this.method2369(sender);
         return true;
      }
   }

   private void method2369(CommandSender var1) {
      var1.sendMessage(ChatColor.YELLOW + "Command usage:");
      var1.sendMessage(ChatColor.YELLOW + "   /iadisguise <name> - Disguise yourself as an entity");
   }
}
