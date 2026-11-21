package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class CdnPackDelivery extends PackDeliveryBase {
   private volatile String bJd;

   public CdnPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
      PackHttpPipelineInstaller.install();
   }

   @Override
   public String getName() {
      return "simple-self-host";
   }

   @Override
   public boolean supportsPerClientDelivery() {
      return true;
   }

   @Override
   public String publishPackFile(File var1, String var2) {
      return Settings.get().simpleSelfHostUrl + "/client_packs/" + var2 + ".zip";
   }

   @Override
   public void stop() {
      PackHttpPipelineInstaller.uninstall();
   }

   @Nullable
   @Override
   public String getUrl() {
      return this.bJd == null ? Settings.get().simpleSelfHostUrl : this.bJd;
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
               Log.error("Failed to publish simple self-hosted resourcepack.", var5);
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
