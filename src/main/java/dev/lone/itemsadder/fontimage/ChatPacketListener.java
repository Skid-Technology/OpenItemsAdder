package dev.lone.itemsadder.fontimage;

import beer.devs.nativepackets.api.DisguisedChatPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeText;
import beer.devs.nativepackets.api.OpenScreenPacketView;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerChatPacketView;
import beer.devs.nativepackets.api.SystemChatPacketView;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.ColorTagParser;
import dev.lone.itemsadder.text.ColorTagReplacer;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.text.PendingMessageStore;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

public class ChatPacketListener implements IaListener {
   public static String aGK;
   private final MessageFormatter aGL;
   private final Settings aGM;
   private final String aGN;
   public static final Pattern aGO = Pattern.compile(":([a-z_\\-0-9]+):", 2);
   private PacketListenerHandle acH;
   private PacketListenerHandle aGP;
   private PacketListenerHandle aGQ;
   private PacketListenerHandle aGR;
   private BukkitTask aGS;
   final Cache aGT = CacheBuilder.newBuilder().expireAfterWrite(20L, TimeUnit.SECONDS).build();

   public ChatPacketListener(Plugin var1, ContentManager var2) {
      this.aGM = Settings.get();
      this.aGL = var2.aE;
      this.aGN = Main.aFi.lj("font-image-chat-hover");
      aGK = this.aGN;
      EventRegistrar.registerIfAbsent(this, var1);

      for (Player var4 : Bukkit.getOnlinePlayers()) {
         PlayerInteractionFacade.getHandler().dropItemsDup2(var4, this.aGL.Yo);
      }

      if (this.aGM.fontImagesChat || this.aGM.textEffectsChat) {
         this.aGP = NativePacketsHolder.PE()
            .method1888(
               this,
               PacketSelector.exact(PacketTypes.DISGUISED_CHAT),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1x -> this.method1365((DisguisedChatPacketView)var1x.view(DisguisedChatPacketView.class))
            );
         this.acH = NativePacketsHolder.PE()
            .method1888(
               this,
               PacketSelector.exact(PacketTypes.SYSTEM_CHAT),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1x -> this.method1366((SystemChatPacketView)var1x.view(SystemChatPacketView.class))
            );
         this.aGQ = NativePacketsHolder.PE()
            .method1888(
               this,
               PacketSelector.exact(PacketTypes.PLAYER_CHAT),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1x -> this.method1367((PlayerChatPacketView)var1x.view(PlayerChatPacketView.class))
            );
      }

      this.aGR = NativePacketsHolder.PE()
         .method1888(
            this,
            PacketSelector.exact(PacketTypes.OPEN_SCREEN),
            ListenerPriority.HIGH,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1x -> this.method1368(var1x, (OpenScreenPacketView)var1x.view(OpenScreenPacketView.class))
         );
      if (this.aGM.fontImagesScoreboardTeams) {
         this.aGS = SchedulerUtils.scheduleRepeatingSync(var1x -> {
            for (Team var3 : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
               var3.setDisplayName(EmojiReplacer.method2341(this.aGL, var3.getDisplayName()));
               var3.setPrefix(EmojiReplacer.method2341(this.aGL, var3.getPrefix()));
               var3.setSuffix(EmojiReplacer.method2341(this.aGL, var3.getSuffix()));
            }
         }, 20L, Main.config.getInt("font_images.scoreboard_teams.update_every_ticks"));
      }

      if (this.aGM.fontImagesChat && this.aGM.fontImagesChatTabComplete) {
         for (Player var6 : Bukkit.getOnlinePlayers()) {
            this.method1375(var6, false);
         }
      }
   }

   private void method1365(DisguisedChatPacketView var1) {
      NativeText var2 = this.method1369(var1.message(), true);
      if (var2 != null) {
         var1.message(var2);
      }
   }

   private void method1366(SystemChatPacketView var1) {
      if (!var1.overlay()) {
         NativeText var2 = this.method1369(var1.message(), false);
         if (var2 != null) {
            var1.message(var2);
         }
      }
   }

   private void method1367(PlayerChatPacketView var1) {
      NativeText var2 = var1.unsignedContent();
      Component var3;
      if (var2 == null) {
         String var4 = NmsChatService.Nq().method2958(var1.body().nativeHandle());
         if (!this.ez(var4)) {
            return;
         }

         var3 = AdventureText.fromLegacy(var4);
      } else {
         String var5 = AdventureText.nativeToString(var2.nativeHandle());
         if (!this.ez(var5)) {
            return;
         }

         var3 = AdventureText.fromNative(var2.nativeHandle());
         if (var3 == null) {
            Log.error("Player chat component is null, this is a bug.");
            return;
         }
      }

      if (this.aGM.textEffectsChat) {
         var3 = ColorTagParser.method2358(var3);
      }

      if (this.aGM.fontImagesChat) {
         var3 = EmojiReplacer.method2338(this.aGL, null, var3, this.aGM.fontImagesChatHoverSuggestion, this.aGN, NullableBox.empty());
      }

      var1.unsignedContent(method1370(var3));
   }

   private void method1368(PacketEvent var1, OpenScreenPacketView var2) {
      UUID var3 = NativePacketsHolder.PE().method1883(var1);
      if (var3 != null) {
         Player var4 = NativePacketsHolder.PE().method1885(var3, var1.connection().id());
         if (var4 != null) {
            Component var5 = PendingMessageStore.ce(var4);
            if (var5 != null) {
               var2.title(method1370(var5));
            } else if (this.aGM.fontImagesInventoryTitle) {
               Component var6 = AdventureText.fromNative(var2.title().nativeHandle());
               if (var6 == null) {
                  Log.devLog("Inventory title component is null, ignoring.");
               } else {
                  NullableBox var7 = NullableBox.of(false);
                  var6 = EmojiReplacer.method2338(this.aGL, null, var6, false, null, var7);
                  if (this.aGM.textEffectsEnabled) {
                     var6 = ColorTagParser.method2359(var6, var7);
                  }

                  if ((Boolean)var7.value) {
                     var2.title(method1370(var6));
                  }
               }
            }
         }
      }
   }

   private NativeText method1369(NativeText var1, boolean var2) {
      int var3 = -1;
      Component var4 = null;
      if (this.aGM.cacheFontImageReplacements) {
         var3 = NmsChatService.Nq().method2961(var1.nativeHandle());
         ChatReplacementEntry var5 = (ChatReplacementEntry)this.aGT.getIfPresent(var3);
         if (var5 != null) {
            if (!ServerCompat.isPaper() && var5.Lo().equals(var1.nativeHandle())) {
               return method1370(var5.aGV);
            }

            var4 = NmsChatService.Nq().method2962(var1.nativeHandle());
            if (var5.aGU.equals(var4)) {
               return method1370(var5.aGV);
            }
         }
      }

      String var8 = NmsChatService.Nq().method2963(var1.nativeHandle());
      if (var2) {
         Log.devLog(var8);
      }

      if (!this.ez(var8)) {
         return null;
      }

      if (var4 == null) {
         var4 = NmsChatService.Nq().method2962(var1.nativeHandle());
      }

      Component var6 = var4;
      if (var6 == null) {
         Log.devLog(var2 ? "Disguised chat component is null, ignoring." : "System chat component is null, ignoring.");
         return null;
      }

      NullableBox var7 = NullableBox.of(false);
      if (this.aGM.fontImagesChat) {
         var6 = EmojiReplacer.method2338(this.aGL, null, var6, this.aGM.fontImagesChatHoverSuggestion, this.aGN, var7);
      }

      if (this.aGM.textEffectsChat) {
         var6 = ColorTagParser.method2359(var6, var7);
      }

      if (!(Boolean)var7.value) {
         return null;
      }

      if (this.aGM.cacheFontImageReplacements) {
         this.aGT.put(var3, new ChatReplacementEntry(var4, var6));
      }

      return method1370(var6);
   }

   private static NativeText method1370(Component var0) {
      Object var1 = AdventureText.toNative(var0);
      return () -> var1;
   }

   private boolean ez(String var1) {
      return this.aGM.fontImagesChat && EmojiReplacer.eB(var1) || this.aGM.textEffectsChat && ColorTagParser.eB(var1);
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.method1371(this.aGP);
      this.method1371(this.acH);
      this.method1371(this.aGQ);
      this.method1371(this.aGR);
      if (this.aGS != null) {
         this.aGS.cancel();
         this.aGS = null;
      }

      this.aGP = null;
      this.acH = null;
      this.aGQ = null;
      this.aGR = null;
      this.aGT.invalidateAll();
      PendingMessageStore.clear();
   }

   private void method1371(PacketListenerHandle var1) {
      if (var1 != null) {
         NativePacketsHolder.PE().method1889(this, var1);
      }
   }

   public static String method1372(Player var0, String var1) {
      ContentManager var2 = ContentManager.method8084();
      return method1373(var2 == null ? null : var2.method8109(), var0, var1);
   }

   private static String method1373(@Nullable MessageFormatter var0, Player var1, String var2) {
      if (var0 == null) {
         return var2;
      }

      Matcher var3 = aGO.matcher(var2);

      while (var3.find()) {
         String var4 = var3.group();
         CustomTexture var5 = var0.bB(var4);
         if (var5 != null && !var5.hasPermission(var1)) {
            var2 = StringUtils.replace(var2, var4, StringUtils.replace(var4, ":", " "));
         }
      }

      return var2;
   }

   public String method1374(Player var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      try {
         int var5 = var2.length();

         for (int var6 = 0; var6 < var5; var6++) {
            CustomTexture var4 = this.aGL.method9494(var2, var6);
            if (var4 != null) {
               if (!var4.hasPermission(var1)) {
                  var3.append(var4.getId());
               } else {
                  var3.append(var4.getLegacyId());
                  if (var4.isPrivateUse()) {
                     var6++;
                  }
               }
            } else {
               var3.append(var2.charAt(var6));
            }
         }
      } catch (Exception var7) {
      }

      return var3.toString();
   }

   private void bL(Player var1) {
      this.method1375(var1, true);
   }

   private void method1375(Player var1, boolean var2) {
      if (var2) {
         PlayerInteractionFacade.getHandler().dropItemsDup2(var1, this.aGL.Yo);
      }

      ArrayList var3 = new ArrayList();
      LinkedHashMap var4 = this.aGL.Yp;

      for (Entry var6 : var4.entrySet()) {
         CustomTexture var7 = (CustomTexture)var6.getValue();
         if (var7.xf() && var7.hasPermission(var1)) {
            var3.add(var7.getLegacyId());
         }
      }

      if (!var3.isEmpty()) {
         PlayerInteractionFacade.getHandler().dropItems(var1, var3);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method515(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      if (!var2.hasMetadata("NPC")) {
         if (Settings.get().fontImagesChatTabComplete) {
            this.bL(var2);
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
   private void method1376(AsyncPlayerChatEvent var1) {
      Settings var2 = Settings.get();
      if (var2.fontImagesChat) {
         var1.setMessage(method1373(this.aGL, var1.getPlayer(), var1.getMessage()));
         var1.setMessage(this.method1374(var1.getPlayer(), var1.getMessage()));
      }

      if (var2.textEffectsChat) {
         var1.setMessage(ColorTagParser.method2362(var1.getPlayer(), var1.getMessage()));
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1377(PlayerCommandPreprocessEvent var1) {
      Settings var2 = Settings.get();
      if (Main.config.getBoolean("font_images.command.enabled")) {
         if (var1.getMessage().startsWith("/")) {
            if (var2.fontImagesChat) {
               var1.setMessage(method1373(this.aGL, var1.getPlayer(), var1.getMessage()));
               var1.setMessage(this.method1374(var1.getPlayer(), var1.getMessage()));
            }

            if (var2.textEffectsChat) {
               var1.setMessage(ColorTagParser.method2362(var1.getPlayer(), var1.getMessage()));
            }

            if (var1.getPlayer().hasPermission("ia.user.image.command")) {
               for (String var4 : Main.config.jZ("font_images.command.excluded")) {
                  if (var1.getMessage().startsWith("/" + var4)) {
                     return;
                  }
               }

               var1.setMessage(EmojiReplacer.method2340(this.aGL, var1.getPlayer(), var1.getMessage(), true));
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1378(ServerCommandEvent var1) {
      Settings var2 = Settings.get();
      if (Main.config.getBoolean("font_images.command.enabled") && var2.fontImagesCommandBlocks) {
         for (String var4 : var2.fontImagesCommandExcluded) {
            if (var1.getCommand().startsWith(var4)) {
               return;
            }
         }

         var1.setCommand(EmojiReplacer.method2340(this.aGL, var1.getSender(), var1.getCommand(), true));
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1379(BroadcastMessageEvent var1) {
      if (Settings.get().fontImagesChat && ServerCompat.nativeAdventure) {
         var1.message(ChatComponentFormatter.method2349(this.aGL, var1.message()));
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1380(SignChangeEvent var1) {
      Settings var2 = Settings.get();
      if (EnvironmentDetector.paperAdventure) {
         if (var2.fontImagesSign && var1.getPlayer().hasPermission("ia.user.image.sign")) {
            var1.lines().replaceAll(var1x -> ChatComponentFormatter.method2349(this.aGL, var1x));
         }

         if (var2.textEffectsSign && var1.getPlayer().hasPermission("ia.user.text_effect.sign")) {
            var1.lines().replaceAll(ColorTagReplacer::method2366);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1381(PlayerEditBookEvent var1) {
      Settings var2 = Settings.get();
      if (EnvironmentDetector.paperAdventure) {
         boolean var3 = var2.textEffectsBook && var1.getPlayer().hasPermission("ia.user.text_effect.book");
         boolean var4 = var2.fontImagesBook && var1.getPlayer().hasPermission("ia.user.image.book");
         if (var3 || var4) {
            BookMeta var5 = var1.getNewBookMeta();

            for (int var6 = 1; var6 <= var5.getPageCount(); var6++) {
               Component var7 = var5.page(var6);
               if (var3) {
                  var7 = ColorTagReplacer.method2367(var1.getPlayer(), var7);
               }

               if (var4) {
                  var7 = ChatComponentFormatter.method2351(this.aGL, var1.getPlayer(), var7);
               }

               var5.page(var6, var7);
            }

            if (var5.title() != null && var5.author() != null) {
               if (var3) {
                  var5.title(ColorTagReplacer.method2367(var1.getPlayer(), var5.title()));
                  var5.author(ColorTagReplacer.method2367(var1.getPlayer(), var5.author()));
               }

               if (var4) {
                  var5.title(ChatComponentFormatter.method2351(this.aGL, var1.getPlayer(), var5.title()));
                  var5.author(ChatComponentFormatter.method2351(this.aGL, var1.getPlayer(), var5.author()));
               }
            }

            var1.setNewBookMeta(var5);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1382(PrepareAnvilEvent var1) {
      Settings var2 = Settings.get();
      if (EnvironmentDetector.paperAdventure) {
         if (var2.fontImagesAnvil || var2.textEffectsAnvil) {
            ItemStack var3 = var1.getResult();
            if (var3 != null) {
               ItemMeta var4 = var3.getItemMeta();
               if (var4 != null && var4.hasDisplayName()) {
                  if (var2.textEffectsAnvil && var1.getView().getPlayer().hasPermission("ia.user.text_effect.anvil")) {
                     var4.displayName(ColorTagReplacer.method2366(var4.displayName()));
                  }

                  if (var2.fontImagesAnvil && var1.getView().getPlayer().hasPermission("ia.user.image.anvil")) {
                     var4.displayName(ChatComponentFormatter.method2349(this.aGL, var4.displayName()));
                  }

                  var3.setItemMeta(var4);
               }
            }
         }
      }
   }
}
