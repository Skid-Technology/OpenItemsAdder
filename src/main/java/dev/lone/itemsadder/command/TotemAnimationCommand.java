package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SoundPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TotemAnimationCommand extends IaCommandBase implements IaListener {
   public TotemAnimationCommand() {
      Main.getCommand("iaplaytotemanimation").setExecutor(this);
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (args.length < 2) {
         Log.sendPrefixed(
            commandSender,
            Main.aFi.lj("wrong-command-usage")
               + ChatColor.YELLOW
               + " /iaplaytotemanimation <totem> <player> [silent]. Example: /iaplaytotemanimation totem LoneDev"
         );
         return true;
      }

      boolean var5 = args.length == 3 && args[2].equalsIgnoreCase("silent");
      Player var6 = Bukkit.getPlayer(args[1]);
      if (var6 == null) {
         if (!var5) {
            Log.sendLocalized(commandSender, "player-offline");
         }

         return true;
      } else {
         CustomItemDefinition var7 = ContentManager.method8084().method8154(args[0]);
         if (var7 == null) {
            if (!var5) {
               Log.sendPrefixed(commandSender, Main.aFi.lj("item-not-found").replace("{item}", args[0]));
            }

            return true;
         } else {
            SoundPlayer.method7390(Sound.ITEM_TOTEM_USE, var6, SoundCategory.PLAYERS, 20L, () -> PlayerInteractionFacade.method5319(var6, var7));
            if (!var5) {
               Log.sendPrefixed(
                  commandSender, Main.aFi.lj("played-totem-animation").replace("{totem}", var7.getNamespacedID()).replace("{player}", var6.getName())
               );
            }

            return true;
         }
      }
   }
}
