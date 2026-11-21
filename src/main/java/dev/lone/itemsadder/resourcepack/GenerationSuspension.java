package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

final class GenerationSuspension {
   private final ResourcePackLoadState dO;
   private final Consumer dP;
   private final AtomicBoolean dQ;
   private Runnable dR;

   private boolean tryRepairVanillaAssets() {
      VanillaAssetManager var1 = ContentManager.method8084().field91;
      VanillaAssetException var2 = var1.getFailure();
      if (!this.dQ.get() && var2 != null && this.dO.handleAssetFailure(var2)) {
         Log.warn("Resource-pack generation suspended while vanilla assets are repaired. The previous pack is preserved.");
         var1.repairAll(var2, var1x -> {
            if (!this.dQ.get() && this.dO.isActive()) {
               if (var1x != null) {
                  Log.error("Unable to repair vanilla assets. The previous resource pack was preserved.", var1x);
                  this.completeWith(false);
               } else {
                  try {
                     Log.log("Vanilla assets repaired. Resuming resource-pack generation by rebuilding its content inputs.");
                     this.dR.run();
                  } catch (RuntimeException | Error var3) {
                     this.dS.handleGenerationFailure(this, var3);
                  }
               }
            }
         });
         return true;
      } else {
         return false;
      }
   }

   private GenerationSuspension(ResourcePackGenerationTask var1, @Nullable ResourcePackLoadState var2, Consumer var3) {
      this.dS = var1;
      this.dQ = new AtomicBoolean();
      this.dO = var2;
      this.dP = var3;
   }

   private void scheduleCompletion(boolean var1) {
      if (!this.dQ.get()) {
         this.runOnServerThread(() -> this.completeWith(var1));
      }
   }

   private void handleCompressionResult(boolean var1, @Nullable Player var2, long var3) {
      if (!this.dQ.get()) {
         this.runOnServerThread(() -> {
            if (!this.dO.isActive()) {
               this.dQ.compareAndSet(false, true);
            } else if (var1 || !this.dS.service.generator.hasVanillaAssetFailure() || !this.tryRepairVanillaAssets()) {
               boolean var5 = var1;

               try {
                  if (var1) {
                     this.dS.service.delivery.onPlayerDisconnect(var2);
                     Log.logToSender(var2, ChatColor.GREEN + "[Pack] Compressed in " + (System.currentTimeMillis() - var3) + "ms");
                     if (!Settings.get().obfuscateZipEntries && !Settings.get().obfuscateZipArchive) {
                        Log.log(ChatColor.YELLOW + "[!] NOT protected from unzip. Consider enabling protections in config.yml");
                     } else {
                        Log.log(ChatColor.GREEN + "[OK] Protected from unzip.");
                     }

                     BossBarManager.setTitleKeepProgress("Pack compressed.");
                  } else {
                     Log.logToSender(var2, ChatColor.RED + "[Pack] Generation failed. The previous resource pack was not replaced.");
                     BossBarManager.setTitleKeepProgress("Pack generation failed.");
                  }
               } catch (Throwable var7) {
                  var5 = false;
                  Log.error("Failed to finish resource-pack generation.", var7);
               }

               this.completeWith(var5);
            }
         });
      }
   }

   private void runOnServerThread(Runnable var1) {
      try {
         SchedulerUtils.runOnMainThread(var1);
      } catch (Throwable var3) {
         Log.error("Could not schedule the resource-pack completion callback on the server thread.", var3);
         this.abortIfNotCompleted();
      }
   }

   private void abortIfNotCompleted() {
      if (this.dQ.compareAndSet(false, true)) {
         this.dO.complete();
      }
   }

   private void completeWith(boolean var1) {
      if (this.dQ.compareAndSet(false, true)) {
         if (this.dO.complete()) {
            if (this.dP != null) {
               try {
                  this.dP.accept(var1);
               } catch (Throwable var3) {
                  Log.error("Resource-pack completion callback failed.", var3);
               }
            }
         }
      }
   }
}
