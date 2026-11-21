package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ProgressPackDelivery extends PackDeliveryBase {
   private volatile String bJd;
   private PackSocketServer bJe;

   @Override
   public boolean supportsPerClientDelivery() {
      return true;
   }

   @Override
   public String publishPackFile(File var1, String var2) {
      String var3 = Settings.get().selfHostPackUrl;
      return var3.substring(0, var3.lastIndexOf(47) + 1) + "client_packs/" + var2 + ".zip";
   }

   public ProgressPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
      int var3 = Main.config.getInt("pack_hosting.self_host.pack_port");
      Log.log(ChatColor.AQUA + "[Host] Started self-host webserver on port: " + var3);
      this.bJe = new PackSocketServer(var1, ContentManager.method8087(), var3);
      this.bJe.start(var1x -> {
         if (var1x) {
            Log.log(ChatColor.AQUA + "[Host] Stopped self-host webserver on port: " + var3);
         }
      });
   }

   @Override
   public String getName() {
      return "self-host";
   }

   @Override
   public void stop() {
      if (this.bJe != null) {
         this.bJe.stop();
         this.bJe = null;
      }
   }

   @Nullable
   @Override
   public String getUrl() {
      return this.bJd == null ? Settings.get().selfHostPackUrl : this.bJd;
   }

   @Override
   public void sendPackTo(Player var1) {
      if (this.tryApplyManually(var1)) {
         this.sendPackWhenReady(var1);
      }
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
      this.bIQ = null;
      if (!this.checkUnconfigured(true, var2)) {
         if (!ContentManager.method8084().aL.generator.packFileExists()) {
            PackDeliveryBase.sendGenerateZipHint(var2);
         } else {
            try {
               String var3 = this.bIP.generator.getPackFileSha1();
               String var4 = this.bIP.manager.publishUniversalPackUrl(var3);
               this.hash = var3;
               this.bJd = var4;
               this.bIT = false;
               this.saveLastHash(this.hash);
            } catch (IOException var5) {
               this.bIT = true;
               Log.error("Failed to publish self-hosted resourcepack.", var5);
               if (var1 != null) {
                  var1.run();
               }

               return;
            }

            if (!this.bIT) {
               this.applyServerPackState();
               PackDeliveryBase.printStatus(var2);
            }

            if (var1 != null) {
               this.bIP.manager.runAfterPacksReady(var1);
            }
         }
      }
   }
}
