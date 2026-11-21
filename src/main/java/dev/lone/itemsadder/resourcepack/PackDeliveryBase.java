package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.AnnouncementSinkHolder;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class PackDeliveryBase {
   final Plugin bIO;
   public final ResourcePackService bIP;
   protected Boolean bIQ;
   private final YamlConfig bIR;
   public boolean bIS;
   public boolean bIT;
   protected volatile String hash;

   public String getHash() {
      return this.hash;
   }

   public boolean supportsPerClientDelivery() {
      return false;
   }

   @Nullable
   public String publishPackFile(File var1, String var2) {
      return null;
   }

   public void onShutdown() {
   }

   public PackDeliveryBase(Plugin var1, ResourcePackService var2) {
      this.bIO = var1;
      this.bIP = var2;
      this.bIR = new YamlConfig(var1, true, "storage/cache/various/resourcepacks.yml", false);
   }

   public abstract String getName();

   public void printNotConfigured(CommandSender var1) {
      Log.errorToSender(var1, "Pack hosting not configured. Please read the tutorials.");
   }

   public void onPlayerDisconnect(@Nullable Player var1) {
   }

   void applyServerPackState() {
      this.bIP.manager.reloadPackFor(this.hash);
      if (Settings.get().autoApplyForced) {
         AnnouncementSinkHolder.getSink().setServerResourcePack(this.getUrl(), Settings.get().packDeliveryUuid, this.hash, Settings.get().kickOnDecline, null);
      } else {
         AnnouncementSinkHolder.getSink().clearServerResourcePack();
      }
   }

   public abstract void stop();

   public abstract void hostPackAndNotify(@Nullable Runnable var1, CommandSender var2);

   @Nullable
   public abstract String getUrl();

   public boolean canRunCommand(CommandSender var1) {
      return true;
   }

   public abstract void sendPackTo(Player var1);

   public int broadcastToOnlinePlayers() {
      Collection var1 = Bukkit.getOnlinePlayers();
      var1.forEach(this::sendPackTo);
      return var1.size();
   }

   public void onPlayerJoin(PlayerJoinEvent var1) {
      if (!var1.getPlayer().isOp() || !this.checkUnconfigured(true, var1.getPlayer())) {
         if (Settings.get().autoApplyPack) {
            if (this.bIP.delivery.bIT) {
               Log.error("[!] Resourcepack invalid url, can't apply to player: " + this.getUrl());
            } else if (this.bIP.generator.generated && !this.bIS) {
               this.scheduleAutoApply(var1.getPlayer());
            } else {
               var1.getPlayer().kickPlayer(Main.aFi.lj("resourcepack-not-ready"));
            }
         }
      }
   }

   public void scheduleAutoApply(Player var1) {
      Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this.bIO, () -> {
         if (var1 != null && var1.isOnline()) {
            this.sendPackTo(var1);
         }
      }, Settings.get().autoApplyDelayTicks);
   }

   public boolean tryApplyManually(Player var1) {
      if (!this.bIP.generator.generated) {
         Log.sendPrefixed(var1, Main.aFi.lj("resourcepack-not-ready"));
         return false;
      } else if (this.checkUnconfigured(true, var1)) {
         return false;
      } else if (this.bIT) {
         Log.error("[!] Resourcepack invalid url, can't apply to player: " + this.bIP.delivery.getUrl());
         Log.sendPrefixed(var1, Main.aFi.lj("resourcepack-error"));
         return false;
      } else {
         return this.bIP.getPlayerStatus(var1) != PackPushStatus.SERVER_SENT;
      }
   }

   void sendPackWhenReady(Player var1) {
      this.bIP
         .manager
         .runWhenPackReady(
            var1,
            () -> AnnouncementSinkHolder.sendResourcePack(
               var1,
               this.getUrl(),
               Settings.get().packDeliveryUuid,
               this.hash,
               Settings.get().kickOnDecline && !var1.hasPermission("ia.resourcepack.bypasskick"),
               Main.aFi.method7071("resourcepack-popup-message", var1)
            )
         );
   }

   public boolean checkUnconfigured(boolean var1, @Nullable CommandSender var2) {
      if (this.bIQ == null) {
         if (this.bIP.delivery instanceof ExternalPackDelivery) {
            this.bIQ = false;
            return false;
         }

         String var3 = this.getUrl();
         this.bIQ = this.bIP.delivery instanceof PlayerJoinPackDelivery || var3 == null || var3.isEmpty();
      }

      if (this.bIQ && var1) {
         sendWelcomeMessage(var2);
      }

      return this.bIQ;
   }

   public boolean isUnconfigured() {
      return this.checkUnconfigured(false, null);
   }

   public boolean hasNoUrl() {
      String var1 = this.getUrl();
      return var1 == null || var1.isEmpty();
   }

   void clearCacheEntries() {
      this.bIR.jG("").forEach(this.bIR::jJ);
      this.bIR.save();
   }

   void saveLastHash(String var1) {
      try {
         this.bIR.method7048("last_url", this.getUrl());
         this.bIR.method7048("last_hash", var1);
         this.bIR.save();
      } catch (Throwable var3) {
         Log.error("Failed to save resourcepack hash to cache file.", var3);
      }
   }

   void saveLastEtag(String var1) {
      this.bIR.method7048("last_etag", var1);
      this.bIR.save();
   }

   @Nullable
   String loadLastEtag() {
      return this.bIR.getString("last_etag");
   }

   @Nullable
   String loadLastHash() {
      return this.bIR.getString("last_hash");
   }

   @Nullable
   public static HttpURLConnection openFollowingRedirects(String var0) {
      URL var1 = new URL(var0);
      HttpURLConnection var2 = (HttpURLConnection)var1.openConnection();
      var2.setInstanceFollowRedirects(false);
      var2.setRequestProperty("User-Agent", "Mozilla/5.0");
      var2.setConnectTimeout(20000);
      var2.setReadTimeout(20000);

      int var3;
      try {
         var3 = var2.getResponseCode();
      } catch (IOException var7) {
         InputStream var5 = var2.getErrorStream();
         if (var5 != null) {
            String var6 = new String(var5.readAllBytes(), StandardCharsets.UTF_8);
            Log.error("HTTP error for " + var0 + ": " + var2.getResponseCode() + " body=" + var6);
         }

         throw var7;
      }

      if (isRedirectStatus(var3)) {
         String var4 = var2.getHeaderField("Location");
         if (var4 != null) {
            URL var8 = new URL(var1, var4);
            Log.log("[Pack] Redirect: '" + var0 + "' -> '" + var8 + "'");
            return openFollowingRedirects(var8.toString());
         }
      }

      return var2;
   }

   public static boolean isRedirectStatus(int var0) {
      return var0 == 200 ? false : var0 == 302 || var0 == 301 || var0 == 303;
   }

   public static String sha1Hex(InputStream var0) {
      MessageDigest var1 = MessageDigest.getInstance("SHA-1");
      byte[] var2 = new byte[2048];

      int var3;
      while ((var3 = var0.read(var2)) != -1) {
         var1.update(var2, 0, var3);
      }

      byte[] var4 = var1.digest();
      StringBuilder var5 = new StringBuilder();

      for (byte var9 : var4) {
         String var10 = Integer.toHexString(255 & var9);
         if (var10.length() == 1) {
            var5.append('0');
         }

         var5.append(var10);
      }

      return var5.toString();
   }

   public static boolean isZipContentType(String var0) {
      return var0.equals("application/zip") || var0.equals("application/x-zip-compressed");
   }

   public static void printStatus(CommandSender var0) {
      if (var0 instanceof ConsoleCommandSender) {
         logStatus();
      } else {
         int var1 = ServerCompat.getFallbackLimit();
         Log.sendPlain(var0, ChatColor.GOLD + "ResourcePack");
         Log.sendPlain(var0, ChatColor.YELLOW + " - Path: " + ChatColor.AQUA + ContentManager.method8084().aL.generator.getPackFilePath());
         if (!(ContentManager.method8084().aL.delivery instanceof ExternalPackDelivery)) {
            Log.sendPlain(var0, ChatColor.YELLOW + " - URL: " + ChatColor.AQUA + ContentManager.method8084().aL.delivery.getUrl());
         }

         int var2 = (int)((double)ContentManager.method8084().aL.generator.getPackFileSize() / (var1 * 1024 * 1024) * 100.0);
         Log.sendPlain(
            var0,
            ChatColor.YELLOW
               + " - Size: "
               + ChatColor.AQUA
               + ContentManager.method8084().aL.generator.getPackFileSizeText()
               + ChatColor.YELLOW
               + "/"
               + ChatColor.AQUA
               + var1
               + " MB "
               + ChatColor.GRAY
               + "("
               + var2
               + "%)"
         );
      }
   }

   public static void logStatus() {
      int var0 = ServerCompat.getFallbackLimit();
      Log.log(ChatColor.GOLD + "ResourcePack");
      Log.log(ChatColor.YELLOW + " - Path: " + ChatColor.AQUA + ContentManager.method8084().aL.generator.getPackFilePath());
      if (!(ContentManager.method8084().aL.delivery instanceof ExternalPackDelivery)) {
         Log.log(ChatColor.YELLOW + " - Download: " + ChatColor.AQUA + ContentManager.method8084().aL.delivery.getUrl());
      }

      int var1 = (int)((double)ContentManager.method8084().aL.generator.getPackFileSize() / (var0 * 1024 * 1024) * 100.0);
      Log.log(
         ChatColor.YELLOW
            + " - Size: "
            + ChatColor.AQUA
            + ContentManager.method8084().aL.generator.getPackFileSizeText()
            + ChatColor.YELLOW
            + "/"
            + ChatColor.AQUA
            + var0
            + " MB "
            + ChatColor.GRAY
            + "("
            + var1
            + "%)"
      );
   }

   public static void sendWelcomeMessage(CommandSender var0) {
      if (Version.isAtLeast(Version.v1_21_6) && var0 instanceof Player var1) {
         NmsChatService.Nq().cm(var1);
      }

      var0.sendMessage(" ");
      var0.sendMessage(" ");
      var0.sendMessage(ChatColor.YELLOW + "Welcome! Thanks for buying ItemsAdder! " + ChatColor.RED + "<3");
      var0.sendMessage(ChatColor.YELLOW + "Before using the plugin you have to finish installation (it's easy).");
      var0.sendMessage(ChatColor.YELLOW + "Resourcepack hosting: " + ChatColor.AQUA + "https://a.devs.beer/ia-host-pack");
      var0.sendMessage(ChatColor.GREEN + "Have fun! :D");
      var0.sendMessage(" ");
      var0.sendMessage(" ");
   }

   protected static void sendGenerateZipHint(CommandSender var0) {
      var0.sendMessage(" ");
      var0.sendMessage(" ");
      var0.sendMessage(ChatColor.YELLOW + "Before using the plugin you have to run /iazip command to generate the resourcepack.");
      var0.sendMessage(" ");
      var0.sendMessage(" ");
   }
}
