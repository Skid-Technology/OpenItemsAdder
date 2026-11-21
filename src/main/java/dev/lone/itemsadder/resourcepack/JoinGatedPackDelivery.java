package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Log;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class JoinGatedPackDelivery extends PackDeliveryBase {
   public JoinGatedPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
      this.logPloudOsShutdown();
   }

   @Override
   public String getName() {
      return "auto-external-legacy";
   }

   private void logPloudOsShutdown() {
      Log.error("");
      Log.error("");
      Log.error("ERROR: auto-external-host won't work anymore because the hosting platform (PloudOS) is shutting down.");
      Log.error("Use another hosting method: https://a.devs.beer/ia-host-pack");
      Log.error("I do not own PloudOS so I cannot do anything, thanks for the patience.");
      Log.error("");
      Log.error("");
   }

   @Override
   public void stop() {
   }

   @Override
   public boolean canRunCommand(CommandSender var1) {
      return false;
   }

   @Override
   public void sendPackTo(Player var1) {
      this.logPloudOsShutdown();
   }

   @Nullable
   @Override
   public String getUrl() {
      return null;
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
   }

   @Override
   public void onPlayerJoin(PlayerJoinEvent var1) {
   }
}
