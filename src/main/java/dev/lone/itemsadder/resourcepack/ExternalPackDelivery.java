package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Log;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ExternalPackDelivery extends PackDeliveryBase {
   public ExternalPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
   }

   @Override
   public String getName() {
      return "no-host";
   }

   @Nullable
   @Override
   public String getUrl() {
      return null;
   }

   @Override
   public void sendPackTo(Player var1) {
      if (this.tryApplyManually(var1)) {
         Log.sendLocalized(var1, "no-host-apply-selected");
      }
   }

   @Override
   public void stop() {
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
      if (var1 != null) {
         var1.run();
      }

      PackDeliveryBase.printStatus(var2);
   }
}
