package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class HitboxCommand extends IaCommandBase {
   public HitboxCommand() {
      Main.getCommand("iahitbox").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      Player var5 = (Player)commandSender;
      if (var5.hasMetadata("DrawFurnitureHitbox")) {
         var5.removeMetadata("DrawFurnitureHitbox", Main.getInstance());
         Log.sendLocalized(var5, "disabled-furniture-hitboxes");
      } else {
         var5.setMetadata("DrawFurnitureHitbox", new FixedMetadataValue(Main.getInstance(), true));
         Log.sendLocalized(var5, "enabled-furniture-hitboxes");
      }

      return true;
   }
}
