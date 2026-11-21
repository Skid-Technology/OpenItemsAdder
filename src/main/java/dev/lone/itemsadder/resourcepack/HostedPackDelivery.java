package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.ZipCommand;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.awt.Color;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.Date;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class HostedPackDelivery extends PackDeliveryBase {
   private final String bIU;
   private PackRequestHandler bIV;
   public final YamlConfig bIW;
   private final String bIX;
   private PackCommandGuard bIY;
   private boolean bIZ;
   public boolean bJa;
   public boolean bJb = false;

   public HostedPackDelivery(Plugin var1, String var2, ResourcePackService var3, String var4, String var5) {
      super(var1, var3);
      this.bIW = new YamlConfig(
         var1, true, "storage" + File.separator + "cache" + File.separator + "various" + File.separator + var5 + ".yml", false, false, false, null
      );
      this.bIU = var4;
      this.bIX = var2;
      this.bIZ = this.bIW.getBoolean("accepted-privacy", false);
      if (!this.bIZ) {
         this.bIY = new PackCommandGuard(var1, this);
      }
   }

   public void setRequestHandler(PackRequestHandler var1) {
      this.bIV = var1;
   }

   @Override
   public boolean supportsPerClientDelivery() {
      return this.bIZ && this.bIV instanceof BossBarPackRequest;
   }

   @Override
   public String publishPackFile(File var1, String var2) {
      return !this.supportsPerClientDelivery() ? null : BossBarPackRequest.publishFile(var1);
   }

   @Override
   public void onShutdown() {
      BossBarPackRequest.abortAll();
   }

   @Override
   public String getName() {
      return this.bIX;
   }

   @Override
   public void printNotConfigured(CommandSender var1) {
      Log.errorToSender(
         var1,
         ChatColor.RED
            + "[Pack] You have to run "
            + ChatColor.YELLOW
            + "/iazip"
            + ChatColor.RED
            + " command which will compress and upload the pack online automatically."
      );
      Log.errorToSender(var1, ChatColor.RED + "[Pack] Then you have to accept the privacy policy.");
      Log.errorToSender(var1, ChatColor.RED + "[Pack] Please read the wiki for more information: " + ChatColor.AQUA + "https://a.devs.beer/ia-complete-install");
   }

   @Override
   public void stop() {
      if (this.bIY != null) {
         this.bIY.stop();
      }
   }

   @Override
   public boolean canRunCommand(CommandSender var1) {
      if (this.bIZ) {
         return true;
      }

      this.bJa = true;
      this.askPrivacyPolicy(Bukkit.getConsoleSender(), true);
      if (var1 instanceof Player) {
         this.sendPrivacyPrompt((Player)var1);
      }

      return false;
   }

   @Override
   public void sendPackTo(Player var1) {
      if (!this.bIZ || this.hasNoUrl()) {
         this.printNotConfigured(var1);
      } else if (this.tryApplyManually(var1)) {
         this.sendPackWhenReady(var1);
      }
   }

   @Override
   public void onPlayerJoin(PlayerJoinEvent var1) {
      if (this.bIZ) {
         super.onPlayerJoin(var1);
      }
   }

   @Nullable
   @Override
   public String getUrl() {
      String var1 = this.bIW.getString("pack_url", null);
      if (var1 != null && !var1.isEmpty()) {
         if (!var1.startsWith("http://") && !var1.startsWith("https://")) {
            var1 = "http://" + var1;
         }

         return var1;
      } else {
         return null;
      }
   }

   @Override
   public void onPlayerDisconnect(@Nullable Player var1) {
      if (!this.bIZ) {
         this.askPrivacyPolicy(var1, false);
      } else {
         Log.log(ChatColor.GREEN + "[Pack] Resource pack successfully compressed. Uploading...");
         this.uploadCompressedPack(var1);
      }
   }

   private void uploadCompressedPack(Player var1) {
      String var2 = this.bIV.uploadPack(this.bIP.generator, var1);
      if (var2 != null) {
         this.bIW.method7048("pack_url", var2);
         this.bIW.save();
         this.bIW.reload();
      }

      this.bJb = var2 == null;
   }

   @Override
   public void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2) {
      this.bIQ = null;
      if (!this.bIV.isBusy()) {
         if (this.bIZ) {
            if (!this.checkUnconfigured(true, var2)) {
               if (!ContentManager.method8084().aL.generator.packFileExists()) {
                  PackDeliveryBase.sendGenerateZipHint(var2);
               } else {
                  this.hash = this.bIP.generator.getPackFileSha1();
                  this.saveLastHash(this.hash);
                  String var3 = this.getUrl();
                  if (var3 != null && !var3.isEmpty()) {
                     try {
                        this.bIS = true;
                        HttpURLConnection var4 = jg(var3);
                        if (var4 != null) {
                           String var5 = var4.getContentType();
                           long var6 = var4.getLastModified();
                           if (!this.bIT) {
                              this.bIT = !jh(var5);
                           }

                           var4.disconnect();
                           String var8 = var6 != 0L ? ", LastEdit: " + Utils.formatTimestamp(new Date(var6)) : "";
                           if (this.bIT) {
                              Log.logToSender(
                                 var2,
                                 ChatColor.RED
                                    + "[Pack] Resourcepack URL is not valid, not a zip file ("
                                    + this.getName()
                                    + "). Type: "
                                    + var5
                                    + var8
                                    + " URL: "
                                    + var3
                              );
                           } else {
                              Log.logToSender(var2, ChatColor.GREEN + "[Pack] Resourcepack URL is valid (" + this.getName() + ")" + var8 + ". URL: " + var3);
                           }
                        } else {
                           this.bIT = true;
                        }
                     } catch (Exception var9) {
                        Log.logToSender(var2, ChatColor.RED + "[Pack] Resourcepack URL is not valid. Please check config.yml. URL: " + var3);
                        this.bIT = true;
                        var9.printStackTrace();
                     }

                     this.bIS = false;
                     if (!this.bIT) {
                        this.applyServerPackState();
                        PackDeliveryBase.printStatus(var2);
                     }

                     if (var1 != null) {
                        var1.run();
                     }
                  } else {
                     if (!this.bJb) {
                        this.printNotConfigured(var2);
                     }

                     this.bIT = true;
                     if (var1 != null) {
                        this.bIP.manager.runAfterPacksReady(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public void setPrivacyAccepted(CommandSender var1, boolean var2) {
      this.bJa = false;
      this.bIZ = var2;
      this.bIW.method7048("accepted-privacy", var2);
      this.bIW.save();
      this.bIW.reload();
      if (var2) {
         Log.sendLocalized(var1, "hosting-privacy-policy-accepted");
         Log.sendLocalized(var1, "compressing-resourcepack");
         ZipCommand.method1505(var1);
      } else {
         Log.sendLocalized(var1, "hosting-privacy-policy-declined");
      }
   }

   private void askPrivacyPolicy(CommandSender var1, boolean var2) {
      if (var2) {
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
      }

      Log.sendLocalizedWith(var1, "hosting-privacy-policy-ask", var1x -> {
         var1x = var1x.replace("{privacy_url}", this.bIU);
         return var1x.replace("{provider}", this.getName());
      });
      if (var2) {
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
         Log.sendPrefixed(var1, ChatColor.RED + " ");
      }
   }

   private void sendPrivacyPrompt(Player var1) {
      Component var2 = AdventureText.parseRichText(Main.aFi.lj("hosting-privacy-policy-title"))
         .append(AdventureText.parseRichText(Main.aFi.lj("hosting-privacy-policy-content")))
         .append(Component.newline())
         .append(
            ((TextComponent)Component.text(this.bIU).color(TextColor.color(Color.CYAN.getRGB()))).clickEvent(ClickEvent.clickEvent(Action.OPEN_URL, this.bIU))
         )
         .append(Component.newline())
         .append(
            ((TextComponent)Component.text("ACCEPT (/acceptprivacy)").color(TextColor.color(Color.GREEN.getRGB())))
               .clickEvent(ClickEvent.clickEvent(Action.RUN_COMMAND, "/acceptprivacy"))
         )
         .append(Component.newline())
         .append(
            ((TextComponent)Component.text("DECLINE (/declineprivacy)").color(TextColor.color(Color.RED.getRGB())))
               .clickEvent(ClickEvent.clickEvent(Action.RUN_COMMAND, "/declineprivacy"))
         );
      AdventureText.getAudiences().player(var1).sendMessage(var2);
   }
}
