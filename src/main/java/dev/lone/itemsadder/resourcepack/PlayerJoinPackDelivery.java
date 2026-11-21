package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PlayerJoinPackDelivery extends PackDeliveryBase {
   public PlayerJoinPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
   }

   @Override
   public String getName() {
      return "unknown";
   }

   @Nullable
   @Override
   public String getUrl() {
      return null;
   }

   @Override
   public void sendPackTo(Player var1) {
      if (var1.isOp()) {
         SchedulerUtils.runLaterSync(() -> this.hostPackAndNotify(null, var1), 60L);
      }

      Log.log(ChatColor.YELLOW + "[Pack] " + ChatColor.RED + "Failed to apply resourcepack to player {player}.".replace("{player}", var1.getName()));
      Log.log(ChatColor.RED + "You have to setup the resourcepack hosting before using the plugin: https://a.devs.beer/ia-host-pack");
   }

   @Override
   public void stop() {
   }

   @Override
   public void onPlayerJoin(PlayerJoinEvent var1) {
      this.sendPackTo(var1.getPlayer());
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
      sendWelcomeMessage(var2);
      if (var1 != null) {
         var1.run();
      }
   }
}
