package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.CloseScreenPacketView;
import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.ContainerClickAction;
import beer.devs.nativepackets.api.ContainerClickPacketView;
import beer.devs.nativepackets.api.ContainerClosePacketView;
import beer.devs.nativepackets.api.ContainerContentPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.OpenScreenPacketView;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListener;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketType;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerInventoryPacketView;
import beer.devs.nativepackets.api.PongPacketView;
import beer.devs.nativepackets.api.ProtocolPhase;
import beer.devs.nativepackets.api.ResourcePackPopPacketView;
import beer.devs.nativepackets.api.SelectBundleItemPacketView;
import beer.devs.nativepackets.api.SetSlotPacketView;
import beer.devs.nativepackets.api.UpdateTagsPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.CustomInventoryHolder;
import dev.lone.itemsadder.gui.GuiButton;
import dev.lone.itemsadder.gui.GuiInventoryHolder;
import dev.lone.itemsadder.gui.GuiSessionManager;
import dev.lone.itemsadder.gui.GuiSessionRegistry;
import dev.lone.itemsadder.gui.ScrollableGuiInventory;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.nms.ContainerClickAdapter;
import dev.lone.itemsadder.text.PendingMessageStore;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.RefreshingValue;
import io.netty.channel.Channel;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class ContainerPacketListener implements Listener {
   private static ContainerPacketListener aKI;
   private static final AtomicInteger aKJ = new AtomicInteger(ThreadLocalRandom.current().nextInt());
   private final ContainerClickAdapter aKK;
   private final NativePacketFactory aKL = NativePacketsHolder.PE().Pv();
   private final Map aKM = Collections.synchronizedMap(new WeakHashMap());
   private final Map aKN = new ConcurrentHashMap();

   private ContainerPacketListener(ContainerClickAdapter var1) {
      this.aKK = var1;
      this.method2597(PacketTypes.UPDATE_TAGS, this::method2610);
      this.method2597(PacketTypes.CONFIGURATION_UPDATE_TAGS, this::method2610);
      this.method2597(PacketTypes.OPEN_SCREEN, var1x -> {
         RefreshingValue var2 = this.method2609(var1x.connection());
         int var3 = ((OpenScreenPacketView)var1x.view(OpenScreenPacketView.class)).containerId();
         ContainerConnection var4 = this.method2605(var1x);
         if (var4 != null && var4.aLD == -1) {
            var4.aLD = var3;
         }

         this.method2611(var1x, () -> var2.enterGeneration(var3));
      });
      this.method2597(PacketTypes.CLOSE_SCREEN, var1x -> {
         RefreshingValue var2 = this.method2609(var1x.connection());
         int var3 = ((CloseScreenPacketView)var1x.view(CloseScreenPacketView.class)).containerId();
         this.method2611(var1x, () -> var2.leaveGeneration(var3));
      });
      this.method2597(PacketTypes.START_CONFIGURATION, var1x -> {
         RefreshingValue var2 = (RefreshingValue)this.aKM.remove(var1x.connection());
         if (var2 != null) {
            var2.reset();
         }

         ContainerConnection var3 = this.method2605(var1x);
         if (var3 != null) {
            var3.close();
         }
      });
      this.method2597(PacketTypes.DISCONNECT, var1x -> {
         RefreshingValue var2 = (RefreshingValue)this.aKM.remove(var1x.connection());
         if (var2 != null) {
            var2.reset();
         }

         ContainerConnection var3 = this.method2605(var1x);
         if (var3 != null) {
            var3.close();
         }
      });
      this.method2598(PacketTypes.SET_SLOT, this::method2612);
      this.method2598(PacketTypes.CONTAINER_CONTENT, this::method2613);
      this.method2598(PacketTypes.SET_PLAYER_INVENTORY, var1x -> {
         ContainerConnection var2 = this.method2605(var1x);
         if (var2 != null && var2.aKT && !var1x.isCancelled()) {
            PlayerInventoryPacketView var3 = (PlayerInventoryPacketView)var1x.view(PlayerInventoryPacketView.class);
            int var4 = var3.slot();
            if (var4 >= 0 && var4 < 36) {
               Object var5 = var2.aLm ? var3.item().nativeHandle() : var2.cm(var4 < 9 ? 81 + var4 : 45 + var4);
               var3.item(() -> var5);
            }
         }
      });
      this.method2598(PacketTypes.SELECT_BUNDLE_ITEM, this::method2614);
      this.method2598(PacketTypes.PONG, var1x -> {
         ContainerConnection var2 = this.method2605(var1x);
         if (var2 != null && var2.ci(((PongPacketView)var1x.view(PongPacketView.class)).id())) {
            var1x.cancel();
         }
      });
      this.method2598(
         PacketTypes.RESOURCE_PACK_POP,
         var1x -> {
            if (!var1x.isCancelled()) {
               UUID var2 = NativePacketsHolder.PE().method1883(var1x);
               if (var2 != null) {
                  ContentManager.method8084()
                     .aL
                     .manager
                     .clearRecipient(var2, var1x.connection().id(), ((ResourcePackPopPacketView)var1x.view(ResourcePackPopPacketView.class)).packId());
                  ContainerConnection var3 = this.method2605(var1x);
                  if (var3 != null) {
                     var3.Mq();
                  }
               }
            }
         }
      );
      this.method2598(PacketTypes.CONTAINER_CLICK, this::method2615);
      this.method2597(PacketTypes.CONTAINER_CLOSE, var1x -> {
         int var2 = ((ContainerClosePacketView)var1x.view(ContainerClosePacketView.class)).containerId();
         RefreshingValue var3 = (RefreshingValue)this.aKM.get(var1x.connection());
         if (var3 != null) {
            var3.leaveGeneration(var2);
         }

         ContainerConnection var4 = this.method2605(var1x);
         if (var4 != null && var2 == var4.aLD) {
            var4.close();
         }
      });
      Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
   }

   public static void LE() {
      GuiSessionRegistry.LE();
      if (aKI == null) {
         ContainerClickAdapter var0 = ItemOperationsService.getRegistryBridge().OH();
         if (var0 != null) {
            aKI = new ContainerPacketListener(var0);
         }
      }
   }

   public static void Ml() {
      if (aKI != null) {
         for (ContainerConnection var1 : List.copyOf(aKI.aKN.values())) {
            var1.aLe.eventLoop().execute(() -> {
               if (var1.aLe.outboundPhase() == ProtocolPhase.PLAY && var1.aLe.isOpen()) {
                  var1.aLe.sendSilently(aKI, aKI.aKL.closeScreen(var1.aLD), var1x -> {
                     if (var1x.successful()) {
                        var1.aLC.leaveGeneration(var1.aLD);
                     }
                  });
               }
            });
            if (var1.aLd.getOpenInventory().getTopInventory() == var1.aLg) {
               var1.aLd.closeInventory();
            }

            var1.close();
         }
      }

      GuiSessionRegistry.LF();
   }

   private void method2597(PacketType var1, PacketListener var2) {
      NativePacketsHolder.PE().method1888(this, PacketSelector.exact(var1), ListenerPriority.MONITOR, false, ListenerFailurePolicy.PASS_ORIGINAL, var2);
   }

   private void method2598(PacketType var1, PacketListener var2) {
      NativePacketsHolder.PE().method1888(this, PacketSelector.exact(var1), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var2);
   }

   static boolean cb(Player var0) {
      if (!method2599(var0)) {
         return false;
      }

      int var1 = ViaVersionIntegration.method4866(var0.getUniqueId());
      return var1 >= 769 && var1 <= 777;
   }

   static boolean method2599(Player var0) {
      if (aKI != null && Settings.get().guiScrollable && cd(var0)) {
         ConnectionRecord var1 = NativePacketsHolder.PE().cB(var0);
         return var1 != null
            && var1.PC().nativeChannel() instanceof Channel var2
            && var2.pipeline().get("unbundler") != null
            && aKI.aKM.containsKey(var1.PC())
            && ((RefreshingValue)aKI.aKM.get(var1.PC())).present
            && ContentManager.method8084().aL.manager.isPackActive(var0.getUniqueId(), var1.bl());
      } else {
         return false;
      }
   }

   private static boolean cd(Player var0) {
      if (var0.getUniqueId().getMostSignificantBits() == 0L) {
         return false;
      }

      try {
         if (Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
            Class var1 = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object var2 = var1.getMethod("getInstance").invoke(null);
            if ((Boolean)var1.getMethod("isFloodgatePlayer", UUID.class).invoke(var2, var0.getUniqueId())) {
               return false;
            }
         }

         if (Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot")) {
            Class var4 = Class.forName("org.geysermc.geyser.api.GeyserApi");
            Object var5 = var4.getMethod("api").invoke(null);
            if (var4.getMethod("connectionByUuid", UUID.class).invoke(var5, var0.getUniqueId()) != null) {
               return false;
            }
         }

         return true;
      } catch (ReflectiveOperationException | LinkageError var3) {
         return false;
      }
   }

   static void method2600(Player var0, String var1, List var2, ItemStack var3, Consumer var4, int var5, IntConsumer var6) {
      method2601(var0, var1, var2, var3, var4, var5, var6, var0x -> {});
   }

   static void method2601(Player var0, String var1, List var2, ItemStack var3, Consumer var4, int var5, IntConsumer var6, Consumer var7) {
      method2602(var0, var1, var2, var3, var4, var5, var6, var7, "_iainternal:gui/scroll_track");
   }

   static void method2602(Player var0, String var1, List var2, ItemStack var3, Consumer var4, int var5, IntConsumer var6, Consumer var7, String var8) {
      method2603(var0, AdventureText.legacySerializer.deserialize(var1), var2, var3, var4, var5, var6, var7, var8, false, null, null);
   }

   static void method2603(
      Player var0,
      Component var1,
      List var2,
      ItemStack var3,
      Consumer var4,
      int var5,
      IntConsumer var6,
      Consumer var7,
      String var8,
      boolean var9,
      Component var10,
      Consumer var11
   ) {
      String var12 = AdventureText.legacySerializer.serialize(var1);
      if (!var9 && var2.size() <= 45) {
         CustomInventoryHolder var16 = new CustomInventoryHolder(54, var12);

         for (int var14 = 0; var14 < var2.size(); var14++) {
            GuiButton var15 = (GuiButton)var2.get(var14);
            var16.method2666(var14, var15.aKO, var15.aKP);
         }

         if (var3 != null) {
            var16.method2666(49, var3, var4);
         }

         var7.accept(var16);
         var16.open(var0);
         GuiSessionRegistry.method2454(var16, var0);
      } else {
         ContainerPacketListener var10002 = aKI;
         Objects.requireNonNull(aKI);
         ContainerConnection var13 = new ContainerConnection(var10002, var0, var12, var2, var3, var4, var5, var6, null, var8, var9);
         if (var11 != null) {
            var13.aLA = var11;
         }

         if (var9 && var10 != null) {
            var13.aLp = List.of(var10);
         }

         var7.accept(var13.aLf);
         if (var9) {
            var13.aLo = List.of(var1);
            PendingMessageStore.method2753(var0, var1);
         }

         var13.Mn();
         GuiSessionRegistry.method2454(var13.aLf, var0);
      }
   }

   static void method2604(Player var0, CustomInventoryHolder var1, List var2, String var3, List var4, List var5, Consumer var6) {
      ContainerPacketListener var10002 = aKI;
      Objects.requireNonNull(aKI);
      ContainerConnection var7 = new ContainerConnection(var10002, var0, "", var2, null, null, 0, var0x -> {}, null, var3, true, var1, Map.of());
      var7.aLA = var6;
      var7.Mn();
      var7.method2653(var4, var5);
      GuiSessionRegistry.method2454(var1, var0);
   }

   private ContainerConnection method2605(PacketEvent var1) {
      UUID var2 = NativePacketsHolder.PE().method1883(var1);
      ContainerConnection var3 = var2 == null ? null : (ContainerConnection)this.aKN.get(var2);
      return var3 != null && var3.aLe.id() == var1.connection().id() && !var3.cz ? var3 : null;
   }

   static void method2606(Player var0, GuiSessionManager var1) {
      ContainerPacketListener var10002 = aKI;
      Objects.requireNonNull(aKI);
      ContainerConnection var2 = new ContainerConnection(var10002, var0, "IA_CUST_GUI", List.of(), null, null, 0, var0x -> {}, var1);
      var2.Mn();
      var1.bS(var0);
   }

   static boolean method2607(Player var0, Inventory var1, List var2, String var3) {
      ContainerConnection var4 = aKI == null ? null : (ContainerConnection)aKI.aKN.get(var0.getUniqueId());
      if (var4 != null && !var4.cz && var4.aLg == var1 && var2.size() > 45) {
         synchronized (var4) {
            var4.jg = List.copyOf(var2);
            var4.aLj.ce(var2.size());
            var4.aLi = var3;
            var4.Mq();
            return true;
         }
      } else {
         return false;
      }
   }

   static void method2608(Player var0, Inventory var1, String var2) {
      ContainerConnection var3 = aKI == null ? null : (ContainerConnection)aKI.aKN.get(var0.getUniqueId());
      if (var3 != null && !var3.cz && var3.aLg == var1) {
         var3.aLi = var2;
         var3.Mq();
      }
   }

   private RefreshingValue method2609(ConnectionHandle var1) {
      return this.aKM.computeIfAbsent(var1, var1x -> {
         WeakReference var2 = new WeakReference<>(var1x);
         RefreshingValue var3 = new RefreshingValue(this.aKK.OQ(), this.aKK::ab, (var2x, var3x) -> {
            ConnectionHandle var4x = (ConnectionHandle)var2.get();
            if (var4x != null && var4x.isOpen() && var4x.outboundPhase() == ProtocolPhase.PLAY) {
               var4x.sendSilently(this, this.aKL.updateTags(() -> var2x), var3xx -> {
                  if (var3xx.successful()) {
                     var3x.run();
                  } else {
                     RefreshingValue var4xx = (RefreshingValue)this.aKM.remove(var4x);
                     if (var4xx != null) {
                        var4xx.reset();
                     }
                  }
               });
            }
         });
         if (var1x.nativeChannel() instanceof Channel var4) {
            var4.closeFuture().addListener(var3x -> {
               this.aKM.remove(var1x);
               var3.reset();
            });
         }

         return var3;
      });
   }

   private void method2610(PacketEvent var1) {
      RefreshingValue var2 = this.method2609(var1.connection());
      Map var3 = (Map)this.aKK.method3561(((UpdateTagsPacketView)var1.view(UpdateTagsPacketView.class)).tags().nativeHandle());
      if (var3.containsKey(this.aKK.OQ())) {
         var2.appliedValue = null;
      }

      this.method2611(var1, () -> var2.mergeValues(var3));
   }

   private void method2611(PacketEvent var1, Runnable var2) {
      try {
         var1.afterWrite(var1x -> {
            if (var1x.successful()) {
               var2.run();
            }
         });
      } catch (UnsupportedOperationException var8) {
         ConnectionHandle var4 = var1.connection();
         RefreshingValue var5 = (RefreshingValue)this.aKM.get(var4);
         if (!(var4.nativeChannel() instanceof Channel var6)) {
            return;
         }

         var6.eventLoop().execute(() -> {
            if (var4.isOpen() && var4.outboundPhase() == ProtocolPhase.PLAY && this.aKM.get(var4) == var5) {
               var2.run();
            }
         });
      }
   }

   private void method2612(PacketEvent var1) {
      ContainerConnection var2 = this.method2605(var1);
      if (var2 != null && !var1.isCancelled()) {
         SetSlotPacketView var3 = (SetSlotPacketView)var1.view(SetSlotPacketView.class);
         if (var2.aKT) {
            if (var3.containerId() == -1 && var3.slot() == -1) {
               return;
            }

            if (var3.containerId() == var2.aLD) {
               var2.aAD = var3.stateId();
            }

            int var4 = var3.slot();
            int var5 = var3.containerId() == var2.aLD
               ? var4
               : (
                  var3.containerId() == 0 && var4 >= 9 && var4 < 45
                     ? var4 + 45
                     : (var3.containerId() == -2 && var4 >= 0 && var4 < 36 ? (var4 < 9 ? var4 + 81 : var4 + 45) : -1)
               );
            if (var5 >= 54 && var5 < 90) {
               Object var9 = var2.aLm ? var3.item().nativeHandle() : var2.cm(var5);
               var3.item(() -> var9);
               return;
            }

            if (var3.containerId() == var2.aLD && var4 >= 0 && var4 < 54) {
               Object var6 = var2.cn(var4);
               var3.item(() -> var6);
               return;
            }
         }

         if (!var2.aKV && var2.aLC.appliedValue == var2) {
            if (var3.containerId() == var2.aLD) {
               var2.aAD = var3.stateId();
               Object var7 = var3.item().nativeHandle();
               Object var8 = var2.method2652(var3.slot(), var7);
               if (var8 != var7) {
                  var3.item(() -> var8);
               }
            }
         }
      }
   }

   private void method2613(PacketEvent var1) {
      ContainerConnection var2 = this.method2605(var1);
      if (var2 != null && !var1.isCancelled()) {
         ContainerContentPacketView var3 = (ContainerContentPacketView)var1.view(ContainerContentPacketView.class);
         if (var2.aKT && (var3.containerId() == var2.aLD || var3.containerId() == 0)) {
            boolean var7 = var3.containerId() == var2.aLD;
            if (var7) {
               var2.aAD = var3.stateId();

               for (int var8 = 0; var8 < Math.min(54, var3.itemCount()); var8++) {
                  Object var10 = var2.cn(var8);
                  var3.item(var8, () -> var10);
               }
            }

            for (int var9 = var7 ? 54 : 9; var9 < Math.min(var7 ? 90 : 45, var3.itemCount()); var9++) {
               Object var11 = var2.aLm ? var3.item(var9).nativeHandle() : var2.cm(var7 ? var9 : var9 + 45);
               var3.item(var9, () -> var11);
            }
         } else if (!var2.aKV && var2.aLC.appliedValue == var2) {
            if (var3.containerId() == var2.aLD) {
               var2.aAD = var3.stateId();

               for (int var4 = 0; var4 < Math.min(var2.aLj.aKy * 9, var3.itemCount()); var4++) {
                  Object var5 = var3.item(var4).nativeHandle();
                  Object var6 = var2.method2652(var4, var5);
                  if (var6 != var5) {
                     var3.item(var4, () -> var6);
                  }
               }
            }
         }
      }
   }

   private void method2614(PacketEvent var1) {
      ContainerConnection var2 = this.method2605(var1);
      if (var2 != null && !var1.isCancelled()) {
         SelectBundleItemPacketView var3 = (SelectBundleItemPacketView)var1.view(SelectBundleItemPacketView.class);
         int var4 = var3.slotId();
         if (var2.aKT && var4 >= 54 && var4 < 90) {
            if (var2.aLm && !var2.aLn) {
               return;
            }

            var1.cancel();
            if (!var2.aKV || var2.aLC.appliedValue != var2) {
               return;
            }

            var4 -= 54;
         } else if (var2.aKV) {
            return;
         }

         if (var2.aLC.appliedValue == var2) {
            synchronized (var2) {
               if (var4 >= 0 && var4 < var2.aLx.length && var2.aLx[var4]) {
                  var1.cancel();
                  int var6 = var3.selectedItemIndex();
                  boolean var7 = var2.aLj.method2589(var4, var6);
                  boolean var8 = var6 >= 0 && var6 <= 2 && var2.Mu();
                  if (var7 || var8) {
                     var2.Mq();
                  }
               }
            }
         }
      }
   }

   private void method2615(PacketEvent var1) {
      ContainerConnection var2 = this.method2605(var1);
      if (var2 != null && !var1.isCancelled()) {
         ContainerClickPacketView var3;
         var3 = (ContainerClickPacketView)var1.view(ContainerClickPacketView.class);
         label121:
         if (var2.aKT && var3.containerId() == var2.aLD) {
            boolean var4 = this.aKK.method3555(var3) != var2.aAD;
            int var5 = this.aKK.method3556(var3);
            int var6 = this.aKK.method3557(var3);
            boolean var7 = var5 == 55
               && var2.aLA != null
               && !var2.aLm
               && (var6 == 0 || var6 == 1)
               && (var3.action() == ContainerClickAction.PICKUP || var3.action() == ContainerClickAction.QUICK_MOVE);
            if (var5 < 0 || var5 >= 90 || (var3.action() != ContainerClickAction.SWAP || var6 != 40) && !var7) {
               if (!var2.aLn
                  && var2.aLm
                  && (
                     var5 >= 54 && var5 < 90
                        || var5 == -999 && (var3.action() == ContainerClickAction.PICKUP || var3.action() == ContainerClickAction.QUICK_CRAFT)
                  )) {
                  return;
               }

               if (!var4 && var5 >= 0 && var5 < 54 && (var3.action() == ContainerClickAction.PICKUP || var3.action() == ContainerClickAction.QUICK_MOVE)) {
                  break label121;
               }

               var1.cancel();
               var2.Mq();
               boolean var8 = var2.aKV && var5 >= 54 && var5 < 90 && var5 % 9 == 8;
               if (!var4
                  && (var6 == 0 || var6 == 1)
                  && (var3.action() == ContainerClickAction.PICKUP || var8 && var3.action() == ContainerClickAction.QUICK_MOVE)) {
                  var2.method2655(var5, var6, var3.action() == ContainerClickAction.QUICK_MOVE);
               }

               return;
            }

            if (var4 || var2.aLn) {
               var1.cancel();
               var2.Mq();
            }

            return;
         }

         if (var2.aLC.appliedValue == var2) {
            if (var3.containerId() == var2.aLD && this.aKK.method3555(var3) != var2.aAD) {
               var1.cancel();
               var2.Mq();
            }
         }
      }
   }

   private ContainerConnection method2616(Inventory var1) {
      return var1.getHolder() instanceof GuiInventoryHolder var3 && var3.aLH instanceof ScrollableGuiInventory var2 ? var2.aLM : null;
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void method2617(InventoryClickEvent var1) {
      ContainerConnection var2 = this.method2616(var1.getInventory());
      if (var2 != null) {
         if (var1.getWhoClicked().getUniqueId().equals(var2.aLd.getUniqueId()) && !var2.cz && this.aKN.get(var2.aLd.getUniqueId()) == var2 && !var2.aLn) {
            int var3 = var1.getRawSlot();
            if (var3 >= 0 && var3 < 90 && var1.getClick() == ClickType.SWAP_OFFHAND) {
               var1.setCancelled(true);
               if (var2.aLd.getItemOnCursor().getType().isAir()) {
                  var2.Mp();
               }
            } else if (var3 != 55 || var2.aLA == null || var2.aLm || !var1.getClick().isLeftClick() && !var1.getClick().isRightClick()) {
               if (var1.getClick() == ClickType.DROP || var1.getClick() == ClickType.CONTROL_DROP) {
                  var1.setCancelled(true);
               } else {
                  if (var3 >= 0 && var3 < 54) {
                     var1.setCancelled(true);

                     try {
                        ((ScrollableGuiInventory)var2.aLf).method2668(var3, var1);
                     } finally {
                        var1.setCancelled(true);
                     }
                  } else if (var2.aLm && var3 < 90 && (var3 >= 0 || var3 == -999)) {
                     if (var3 >= 54) {
                        var2.method2654(var1);
                     }
                  } else {
                     var1.setCancelled(true);
                  }
               }
            } else {
               var1.setCancelled(true);
               if (var2.aLd.getItemOnCursor().getType().isAir()) {
                  var2.aLA.accept(var1);
               }
            }
         } else {
            var1.setCancelled(true);
            var2.Mq();
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
   public void method2618(InventoryClickEvent var1) {
      ContainerConnection var2 = this.method2616(var1.getInventory());
      if (var2 != null) {
         var2.Mq();
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void method2619(InventoryDragEvent var1) {
      ContainerConnection var2 = this.method2616(var1.getInventory());
      if (var2 != null
         && (
            !var1.getWhoClicked().getUniqueId().equals(var2.aLd.getUniqueId())
               || var2.cz
               || this.aKN.get(var2.aLd.getUniqueId()) != var2
               || var2.aLn
               || !var2.aLm
               || var1.getRawSlots().stream().anyMatch(var0 -> var0 < 54 || var0 >= 90)
         )) {
         var1.setCancelled(true);
      }
   }

   @EventHandler
   public void method2620(InventoryCloseEvent var1) {
      ContainerConnection var2 = this.method2616(var1.getInventory());
      if (var2 != null && var1.getPlayer().getUniqueId().equals(var2.aLd.getUniqueId())) {
         var2.close();
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   public void method2621(PlayerResourcePackStatusEvent var1) {
      ContainerConnection var2 = (ContainerConnection)this.aKN.get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         var2.Mq();
      }
   }

   @EventHandler
   public void method2622(PlayerQuitEvent var1) {
      ContainerConnection var2 = (ContainerConnection)this.aKN.get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         var2.close();
         this.aKM.remove(var2.aLe);
         var2.aLe.eventLoop().execute(var2.aLC::reset);
      }

      ConnectionRecord var3 = NativePacketsHolder.PE().cB(var1.getPlayer());
      if (var3 != null) {
         RefreshingValue var4 = (RefreshingValue)this.aKM.remove(var3.PC());
         if (var4 != null) {
            var3.PC().eventLoop().execute(var4::reset);
         }
      }
   }
}
