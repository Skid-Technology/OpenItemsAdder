package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class SelfHostedPackDelivery extends PackDeliveryBase {
   public SelfHostedPackDelivery(Plugin var1, ResourcePackService var2) {
      super(var1, var2);
   }

   @Override
   public String getName() {
      return "external";
   }

   @Override
   public void printNotConfigured(CommandSender var1) {
      Log.errorToSender(
         var1, ChatColor.RED + "[Pack] You have to run " + ChatColor.YELLOW + "/iazip" + ChatColor.RED + " command which will compress the pack."
      );
      Log.errorToSender(var1, ChatColor.RED + "[Pack] - upload the pack on a supported website and get the URL.");
      Log.errorToSender(var1, ChatColor.RED + "[Pack] - copy the URL into the config.yml file");
      Log.errorToSender(var1, ChatColor.RED + "[Pack] - run /iareload");
      Log.errorToSender(
         var1, ChatColor.RED + "[Pack] More info on resourcepack hosting websites here: " + ChatColor.AQUA + "https://a.devs.beer/ia-external-host"
      );
   }

   @Override
   public void stop() {
   }

   @Nullable
   @Override
   public String getUrl() {
      String var1 = Main.config.getString("pack_hosting.external_host.url");
      if (var1 != null && !var1.isEmpty()) {
         if (var1.endsWith("dl=0")) {
            var1 = var1.replace("dl=0", "raw=1");
         } else if (var1.endsWith("dl=1")) {
            var1 = var1.replace("dl=1", "raw=1");
         }

         if (!var1.startsWith("http://") && !var1.startsWith("https://")) {
            var1 = "http://" + var1;
         }

         return var1;
      } else {
         return null;
      }
   }

   @Override
   public void sendPackTo(Player var1) {
      if (this.tryApplyManually(var1)) {
         if (this.bIP.delivery.hasNoUrl()) {
            this.printNotConfigured(var1);
         } else {
            this.sendPackWhenReady(var1);
         }
      }
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
      this.bIQ = null;
      if (!this.checkUnconfigured(true, var2)) {
         String var3 = this.getUrl();
         if (var3 != null && !var3.isEmpty()) {
            Log.logToSender(var2, ChatColor.AQUA + "[Pack] Checking resourcepack url: " + var3);

            try {
               this.bIS = true;
               HttpURLConnection var4 = openFollowingRedirects(var3);
               if (var4 != null) {
                  long var5 = var4.getLastModified();
                  String var7;
                  if (var5 == 0L) {
                     var7 = var4.getHeaderField("etag");
                     Log.logToSender(var2, ChatColor.AQUA + "[Pack] ETag: " + var7);
                  } else {
                     var7 = Long.toString(var5);
                     Log.logToSender(var2, ChatColor.AQUA + "[Pack] LastEdit: " + Utils.formatTimestamp(new Date(var5)));
                  }

                  String var8 = this.loadLastEtag();
                  String var9 = this.loadLastHash();
                  if (var8 != null && var9 != null && var8.equals(var7)) {
                     Log.logToSender(var2, ChatColor.GREEN + "[Pack] URL should be valid. Remote file didn't change. Skipped hash calculation.");
                     this.hash = var9;
                     var4.disconnect();
                     this.bIT = false;
                     this.bIS = false;
                     if (var1 != null) {
                        var1.run();
                     }

                     return;
                  }

                  this.saveLastEtag(var7);
                  String var10 = var4.getContentType();
                  if (Settings.get().skipUrlFileTypeCheck) {
                     Log.logToSender(
                        var2,
                        ChatColor.YELLOW
                           + "[Pack] Skipped file type checking, this may cause resourcepack apply loop and other issues if the file is not valid. DON'T ASK FOR SUPPORT!"
                     );
                     this.bIT = false;
                  } else {
                     boolean var11 = isZipContentType(var10);
                     if (!var11) {
                        Log.logToSender(var2, ChatColor.RED + "[Pack] URL is not a zip file (" + this.getName() + "). Type: " + var10 + " URL: " + var3);
                     }

                     if (!this.bIT) {
                        this.bIT = !var11;
                     }
                  }

                  InputStream var14 = var4.getInputStream();
                  this.hash = sha1Hex(var14);
                  var4.disconnect();
                  if (this.bIT) {
                     Log.logToSender(var2, ChatColor.RED + "[Pack] URL is not valid (" + this.getName() + "). Type: " + var10 + " URL: " + var3);
                  } else {
                     Log.logToSender(var2, ChatColor.GREEN + "[Pack] URL is valid (" + this.getName() + "). URL: " + var3);
                  }
               } else {
                  this.bIT = true;
               }
            } catch (FileNotFoundException var12) {
               Log.logToSender(var2, ChatColor.RED + "[Pack] No resourcepack found in this URL. It was probably deleted. URL: " + var3);
               var12.printStackTrace();
               this.bIT = true;
            } catch (Exception var13) {
               Log.logToSender(var2, ChatColor.RED + "[Pack] URL is not valid. Please check config.yml. URL: " + var3);
               var13.printStackTrace();
               this.bIT = true;
            }

            this.saveLastHash(this.hash);
            this.bIS = false;
            if (!this.bIT) {
               this.applyServerPackState();
            }

            if (var1 != null) {
               this.bIP.manager.runAfterPacksReady(var1);
            }

            if (!this.bIT) {
               this.clearCacheEntries();
               PackDeliveryBase.printStatus(var2);
            }
         } else {
            Log.logToSender(var2, ChatColor.RED + "[Pack] No URL set. Please upload your pack and set the 'url' in config.yml");
            this.bIT = true;
         }
      }
   }
}
