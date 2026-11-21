package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.ResourcePackGenerator;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.net.URL;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sha1Command extends IaCommandBase {
   private boolean aHz;

   public Sha1Command() {
      Main.getCommand("iasha1").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (args.length == 0) {
         Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
         return true;
      } else if (this.aHz) {
         Log.sendLocalized(commandSender, "sha1-wait-previous-execution");
         return true;
      } else {
         Log.sendLocalized(commandSender, "sha1-loading");
         this.aHz = true;
         SchedulerUtils.runSync(() -> {
            try {
               String var3x;
               if (args[0].equals("online")) {
                  var3x = ResourcePackGenerator.toHex(DigestUtils.sha1(new URL(ContentManager.method8084().aL.delivery.getUrl()).openStream()));
               } else {
                  var3x = ContentManager.method8084().aL.generator.getPackFileSha1();
               }

               if (commandSender instanceof Player) {
                  Log.sendPrefixed(commandSender, "SHA1: " + var3x);
               }

               Log.log("SHA1: " + var3x);
               this.aHz = false;
            } catch (Exception var4x) {
               this.aHz = false;
               var4x.printStackTrace();
            }
         });
         return true;
      }
   }
}
