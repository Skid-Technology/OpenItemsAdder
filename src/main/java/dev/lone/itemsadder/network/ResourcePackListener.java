package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionIdentity;
import beer.devs.nativepackets.api.ConnectionState;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.LoginFinishedPacketView;
import beer.devs.nativepackets.api.NativeGameProfile;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.ResourcePackPushPacketView;
import beer.devs.nativepackets.api.ResourcePackResponsePacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.ResourcePackConfigurationSendEvent;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.internal.SwitchMap73;
import dev.lone.itemsadder.resourcepack.ExternalPackDelivery;
import dev.lone.itemsadder.resourcepack.PackDeliveryBase;
import dev.lone.itemsadder.resourcepack.PackPushStatus;
import dev.lone.itemsadder.resourcepack.PlayerJoinPackDelivery;
import dev.lone.itemsadder.resourcepack.ResourcePackService;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import java.text.MessageFormat;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ResourcePackListener implements IaListener {
   private PacketListenerHandle bzi;
   private PacketListenerHandle bzj;
   private PacketListenerHandle bzk;
   private PacketListenerHandle bzl;
   private PacketListenerHandle bzm;
   private final ResourcePackService bzn;
   private volatile boolean cV;

   public ResourcePackListener(ResourcePackService var1) {
      this.bzn = var1;
   }

   public void register(Plugin plugin) {
      this.cV = true;
      EventRegistrar.registerIfAbsent(this, plugin);
      this.bzl = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.LOGIN_FINISHED),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.DISCONNECT,
            this::method789
         );
      this.bzm = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.LOGIN_GAME_PROFILE),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.DISCONNECT,
            this::method789
         );
      this.bzi = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.CONFIGURATION_RESOURCE_PACK_PUSH),
            ListenerPriority.HIGHEST,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            this::method792
         );
      this.bzj = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.RESOURCE_PACK_PUSH),
            ListenerPriority.HIGHEST,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1x -> this.method794(var1x, (ResourcePackPushPacketView)var1x.view(ResourcePackPushPacketView.class))
         );
      this.bzk = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.CONFIGURATION_RESOURCE_PACK_RESPONSE),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            this::method790
         );
   }

   @Override
   public void unregister() {
      this.cV = false;
      EventRegistrar.unregisterListener(this);
      if (this.bzl != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.bzl);
      }

      if (this.bzm != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.bzm);
      }

      if (this.bzi != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.bzi);
      }

      if (this.bzj != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.bzj);
      }

      if (this.bzk != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.bzk);
      }
   }

   private void method789(PacketEvent var1) {
      if (this.cV && Settings.get().perClientVersionDelivery) {
         NativeGameProfile var2 = ((LoginFinishedPacketView)var1.view(LoginFinishedPacketView.class)).profile();
         int var3 = ViaVersionIntegration.method4867(var2.id(), var1.connection().nativeChannel());
         if (!this.bzn.manager.canDeliverVersion(var3)) {
            var1.cancel();
            var1.connection().send(NativePacketsHolder.PE().Pv().loginDisconnect(NativePacketsHolder.PE().Px().literal(Main.aFi.lj("resourcepack-not-ready"))));
         }
      }
   }

   private void method790(PacketEvent var1) {
      ConnectionIdentity var2 = var1.connection().identity();
      if (this.cV && var2 != null && !(this.bzn.delivery instanceof ExternalPackDelivery)) {
         ResourcePackResponsePacketView var3 = (ResourcePackResponsePacketView)var1.view(ResourcePackResponsePacketView.class);
         if (method791(var3.packId()) || this.bzn.manager.hasRecipient(var2.uniqueId(), var3.packId())) {
            if (var3.action().nativeHandle() instanceof Enum var5) {
               PackPushStatus var6 = this.bzn.manager.hasSession(var2.uniqueId())
                  ? this.bzn.manager.updatePackStatus(var2.uniqueId(), var1.connection().id(), var3.packId(), var5.name())
                  : gN(var5.name());
               if (var6 != null) {
                  var3.packId(this.bzn.manager.resolveRecipientUuid(var2.uniqueId(), var1.connection().id(), var3.packId()));
                  ResourcePackPushKey var7 = new ResourcePackPushKey(var1.connection().id(), var2.uniqueId(), var2.name(), var1.connection().clientLanguage());
                  var1.syncGlobal(
                     var3x -> {
                        ConnectionIdentity var4 = var3x.connection().identity();
                        if (this.cV
                           && var4 != null
                           && method798(var3x.connection().state())
                           && var7.method124(var3x.connection().id(), var4.uniqueId(), var4.name())) {
                           this.bzn.setPlayerStatusById(var7.bk(), var6);
                        }
                     }
                  );
               }
            }
         }
      }
   }

   @Nullable
   static PackPushStatus gN(String var0) {
      return switch (var0) {
         case "ACCEPTED", "DOWNLOADED" -> PackPushStatus.PLAYER_DOWNLOADING;
         case "SUCCESSFULLY_LOADED" -> PackPushStatus.INSTALLED;
         case "DECLINED" -> PackPushStatus.DECLINED;
         case "FAILED_DOWNLOAD", "INVALID_URL", "FAILED_RELOAD", "DISCARDED" -> PackPushStatus.FAILED_DOWNLOAD;
         default -> null;
      };
   }

   static boolean method791(UUID var0) {
      return UUID.fromString(Settings.get().packDeliveryUuid).equals(var0);
   }

   private void method792(PacketEvent var1) {
      if (this.cV && !(this.bzn.delivery instanceof ExternalPackDelivery)) {
         ConnectionIdentity var2 = var1.connection().identity();
         ConnectionState var3 = var1.connection().state();
         if (var2 != null && method798(var3)) {
            ResourcePackPushKey var4 = new ResourcePackPushKey(var1.connection().id(), var2.uniqueId(), var2.name(), var1.connection().clientLanguage());
            ResourcePackPushPacketView var5 = (ResourcePackPushPacketView)var1.view(ResourcePackPushPacketView.class);
            UUID var6 = var5.packId();
            String var7 = var5.url();
            String var8 = var5.hash();
            if (method799(var3)) {
               var1.sync(
                  (var5x, var6x) -> {
                     ConnectionIdentity var7x = var5x.connection().identity();
                     if (this.cV
                        && !(this.bzn.delivery instanceof ExternalPackDelivery)
                        && var7x != null
                        && var5x.connection().state() == ConnectionState.PLAYER_ASSOCIATED
                        && var4.method124(var5x.connection().id(), var7x.uniqueId(), var7x.name())
                        && var6x.player() instanceof Player var8x
                        && var8x.isOnline()
                        && NativePacketsHolder.PE().method1885(var4.bk(), var4.bl()) == var8x) {
                        this.method793(var5x, var4, var6, var7, var8, var8x);
                     }
                  }
               );
            } else {
               var1.syncGlobal(
                  var5x -> {
                     ConnectionIdentity var6x = var5x.connection().identity();
                     if (this.cV
                        && !(this.bzn.delivery instanceof ExternalPackDelivery)
                        && var6x != null
                        && var5x.connection().state() == ConnectionState.CONFIGURATION
                        && var4.method124(var5x.connection().id(), var6x.uniqueId(), var6x.name())) {
                        this.method793(var5x, var4, var6, var7, var8, null);
                     }
                  }
               );
            }
         }
      }
   }

   private void method793(PacketEvent var1, ResourcePackPushKey var2, UUID var3, String var4, String var5, @Nullable Player var6) {
      ResourcePackPushPacketView var7 = (ResourcePackPushPacketView)var1.view(ResourcePackPushPacketView.class);
      boolean var8 = this.bzn.manager.isOwnPackUrl(var4);
      boolean var9 = var7.required();
      if (var8) {
         if (var6 == null) {
            this.bzn.setPlayerStatusById(var2.bk(), PackPushStatus.SERVER_SENT);
         } else {
            this.bzn.setPlayerStatus(var6, PackPushStatus.SERVER_SENT);
         }

         var9 = var6 == null ? bE(Settings.get().kickOnDecline) : method797(Settings.get().kickOnDecline, var6);
         var7.required(var9);
         Component var10 = AdventureText.fromLegacy(Main.aFi.bh("resourcepack-popup-message", var2.method125()));
         Object var11 = AdventureText.toNative(var10);
         var7.prompt(() -> var11);
         this.bzn.manager.onResourcePackPush(var1);
         if (var1.isCancelled()) {
            return;
         }

         var3 = var7.packId();
         var4 = var7.url();
         var5 = var7.hash();
      }

      if (Settings.get().logPlayerPackInstall) {
         Log.log("Installing resourcepack: " + var2.method125() + " | " + var4 + " | " + var3 + " | " + var5);
      }

      if (var6 == null) {
         EventRegistrar.callEvent(new ResourcePackConfigurationSendEvent(var2.bk(), var2.method125(), var2.VW(), var3, var4, var5, var9, var8));
      } else {
         EventRegistrar.callEvent(new ResourcePackSendEvent(var6, var4, var5, var8));
      }

      if (!var8 && Settings.get().autoApplyPack) {
         if (!Settings.get().allowOtherPluginsResourcepacks) {
            var1.cancel();
            if (Settings.get().notifyOtherPluginsResourcepacks) {
               Log.warn("Another plugin sent a resourcepack. Cancelling it. URL: " + ChatColor.GRAY + var4);
            }
         } else if (Settings.get().notifyOtherPluginsResourcepacks) {
            Log.log(ChatColor.AQUA + "Another plugin sent a resourcepack. URL: " + ChatColor.GRAY + var4);
         }
      }
   }

   private void method794(PacketEvent var1, ResourcePackPushPacketView var2) {
      if (this.cV && !(this.bzn.delivery instanceof ExternalPackDelivery)) {
         this.bzn.manager.onResourcePackPush(var1);
         if (!var1.isCancelled()) {
            long var3 = var1.connection().id();
            UUID var5 = var2.packId();
            String var6 = var2.url();
            String var7 = var2.hash();
            var1.sync(
               (var6x, var7x) -> {
                  if (this.cV
                     && !(this.bzn.delivery instanceof ExternalPackDelivery)
                     && var7x.player() instanceof Player var8
                     && var8.isOnline()
                     && NativePacketsHolder.PE().method1885(var8.getUniqueId(), var3) == var8) {
                     if (Settings.get().logPlayerPackInstall) {
                        Log.log("Installing resourcepack: " + var8.getName() + " | " + var6 + " | " + var5 + " | " + var7);
                     }

                     boolean var10 = this.bzn.manager.isOwnPackUrl(var6);
                     if (var10 && !this.bzn.manager.isRecipientSent(var8.getUniqueId(), var5)) {
                        this.bzn.setPlayerStatus(var8, PackPushStatus.SERVER_SENT);
                     }

                     EventRegistrar.callEvent(new ResourcePackSendEvent(var8, var6, var7, var10));
                     if (!var10 && Settings.get().autoApplyPack) {
                        if (!Settings.get().allowOtherPluginsResourcepacks) {
                           var6x.cancel();
                           if (Settings.get().notifyOtherPluginsResourcepacks) {
                              Log.warn("Another plugin sent a resourcepack. Cancelling it. URL: " + ChatColor.GRAY + var6);
                           }
                        } else if (Settings.get().notifyOtherPluginsResourcepacks) {
                           Log.log(ChatColor.AQUA + "Another plugin sent a resourcepack. URL: " + ChatColor.GRAY + var6);
                        }
                     }
                  }
               }
            );
         }
      }
   }

   @EventHandler
   public void method795(PlayerJoinEvent var1) {
      this.bzn.manager.sendShaderPackTo(var1.getPlayer().getUniqueId());
      if (this.bzn.delivery instanceof PlayerJoinPackDelivery) {
         Player var2 = var1.getPlayer();
         PackDeliveryBase.sendWelcomeMessage(var2);
      }

      if (!Settings.get().autoApplyForced) {
         if (!(this.bzn.delivery instanceof ExternalPackDelivery)) {
            this.bzn.delivery.onPlayerJoin(var1);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @EventHandler
   public void method796(PlayerResourcePackStatusEvent var1) {
      if (method791(var1.getID()) || this.bzn.manager.hasRecipient(var1.getPlayer().getUniqueId(), var1.getID())) {
         UUID var2 = var1.getPlayer().getUniqueId();
         if (var1.getStatus() == Status.DISCARDED && this.bzn.manager.isRecipientAcked(var2, var1.getID())) {
            this.bzn.setPlayerStatus(var1.getPlayer(), PackPushStatus.INSTALLED);
            if (this.bzn.manager.isFullyAccepted(var2)) {
               this.bzn.delivery.sendPackTo(var1.getPlayer());
            }
         } else {
            PackPushStatus var3 = this.bzn.manager.hasSession(var1.getPlayer().getUniqueId())
               ? this.bzn.manager.updatePackStatus(var1.getPlayer().getUniqueId(), -1L, var1.getID(), var1.getStatus().name())
               : gN(var1.getStatus().name());
            if (var3 != null) {
               this.bzn.setPlayerStatus(var1.getPlayer(), var3);
               if (this.bzn.manager.isRecipientSent(var2, var1.getID())) {
                  if (this.bzn.manager.isFullyAccepted(var2)) {
                     this.bzn.delivery.sendPackTo(var1.getPlayer());
                  }
               } else {
                  if (var1.getStatus() == Status.SUCCESSFULLY_LOADED) {
                     this.bzn.manager.sendShaderPackTo(var1.getPlayer().getUniqueId());
                  }

                  switch (SwitchMap73.bzo[var1.getStatus().ordinal()]) {
                     case 1:
                        if (method797(Main.config.getBoolean("pack_delivery.kick_player_on_decline"), var1.getPlayer())) {
                           var1.getPlayer().kickPlayer(Main.aFi.lj("resourcepack-kick-declined"));
                        }
                        break;
                     case 2:
                        if (method797(Main.config.getBoolean("pack_delivery.kick_player_on_fail"), var1.getPlayer())) {
                           var1.getPlayer().kickPlayer(Main.aFi.lj("resourcepack-error-failed-download"));
                        }
                        break;
                     case 3:
                        if (method797(Main.config.getBoolean("pack_delivery.kick_player_on_fail"), var1.getPlayer())) {
                           var1.getPlayer().kickPlayer(Main.aFi.lj("resourcepack-error-discarded"));
                        }
                        break;
                     case 4:
                        var1.getPlayer().sendMessage(Main.aFi.lj("resourcepack-error-invalid-url"));
                        Log.warn(MessageFormat.format("Invalid resourcepack URL. Failed to apply it to player {0}", var1.getPlayer().getName()));
                        Log.warn(MessageFormat.format("Pack UUID: {0}", var1.getID()));
                        break;
                     case 5:
                        if (method797(Main.config.getBoolean("pack_delivery.kick_player_on_fail"), var1.getPlayer())) {
                           var1.getPlayer().kickPlayer(Main.aFi.lj("resourcepack-error-failed-reload"));
                        }
                  }

                  if (var1.getPlayer().isOnline()) {
                     if (var3 != PackPushStatus.PLAYER_DOWNLOADING && var3 != PackPushStatus.SERVER_SENT) {
                        this.bzn.downloadGate.releasePlayer(var1.getPlayer());
                     } else if (!this.bzn.downloadGate.isLocked(var1.getPlayer())) {
                        this.bzn.downloadGate.lockPlayer(var1.getPlayer());
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method587(PlayerQuitEvent var1) {
      this.bzn.clearPlayerStatus(var1.getPlayer());
      this.bzn.manager.removeStaleSession(var1.getPlayer());
   }

   static boolean bE(boolean var0) {
      return var0;
   }

   static boolean method797(boolean var0, Player var1) {
      return var0 && !var1.hasPermission("ia.resourcepack.bypasskick");
   }

   static boolean method798(ConnectionState var0) {
      return var0 == ConnectionState.CONFIGURATION || var0 == ConnectionState.PLAYER_ASSOCIATED;
   }

   static boolean method799(ConnectionState var0) {
      return var0 == ConnectionState.PLAYER_ASSOCIATED;
   }
}
