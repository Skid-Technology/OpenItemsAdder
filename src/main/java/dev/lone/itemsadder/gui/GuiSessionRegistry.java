package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.text.PendingMessageStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.stream.IntStream;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class GuiSessionRegistry {
   private static final ConcurrentHashMap aIy = new ConcurrentHashMap();
   private static boolean Ok;
   public final ScrollTrackManager aIz = new ScrollTrackManager();
   private final Plugin aIA;
   private final ScrollableGuiTitle aIB;
   private volatile List Zd;
   private final BiConsumer aIC;
   private final ConcurrentHashMap aID = new ConcurrentHashMap();

   public GuiSessionRegistry(Plugin var1, ScrollableGuiTitle var2, List var3, BiConsumer var4) {
      this.aIA = Objects.requireNonNull(var1, "owner");
      this.aIB = Objects.requireNonNull(var2, "title");
      this.aIC = Objects.requireNonNull(var4, "onClick");
      this.Zd = method2445(var3);
   }

   private static List method2445(List var0) {
      return Objects.requireNonNull(var0, "items").stream().map(var0x -> Objects.requireNonNull(var0x, "item").clone()).toList();
   }

   public synchronized void setItems(List items) {
      this.Zd = method2445(items);
      this.refresh();
   }

   public synchronized void setItem(int index, ItemStack item) {
      ArrayList var3 = new ArrayList(this.Zd);
      var3.set(index, Objects.requireNonNull(item, "item").clone());
      this.Zd = List.copyOf(var3);
      this.refresh();
   }

   public int size() {
      return this.Zd.size();
   }

   public ItemStack getItem(int index) {
      return ((ItemStack)this.Zd.get(index)).clone();
   }

   private List method2446(List var1) {
      return IntStream.range(0, var1.size())
         .mapToObj(var2 -> new GuiButton(((ItemStack)var1.get(var2)).clone(), var3 -> this.method2456(var2, var3, var1)))
         .toList();
   }

   public void method2447(NamespacedKey var1, NamespacedKey var2) {
      this.method2448(var1, var2, null);
   }

   public void method2448(NamespacedKey var1, NamespacedKey var2, NamespacedKey var3) {
      this.aIz.method2596(this.aIA, var1, var2, var3);
      this.refresh();
   }

   public void refresh() {
      for (GuiSessionEntry var2 : List.copyOf(aIy.values())) {
         if (var2.aIE == this && var2.aIJ.compareAndSet(false, true)) {
            method2449(var2.aIF, () -> {
               var2.aIJ.set(false);
               if (aIy.get(var2.aIF.getUniqueId()) == var2 && var2.aIF.getOpenInventory().getTopInventory() == var2.aIG.getInventory()) {
                  List var2x = this.Zd;
                  boolean var3 = ContainerPacketListener.cb(var2.aIF) && var2x.size() > 45;
                  if (var3 != var2.aIH) {
                     this.method2450(var2.aIF, var2.aIw, var2.aII);
                  } else {
                     var2.aIt = this.aIz.Mi();
                     if (var3) {
                        if (!ContainerPacketListener.method2607(var2.aIF, var2.aIG.getInventory(), this.method2446(var2x), this.aIz.ca(var2.aIF))) {
                           this.method2450(var2.aIF, var2.aIw, var2.aII);
                           return;
                        }
                     } else {
                        this.method2451(var2, var2.aIw);
                     }

                     Component var4 = this.aIB.MG();
                     if (!var4.equals(var2.aIu)) {
                        var2.aIu = var4;
                        ItemOperationsService.getRegistryBridge().method3426(var2.aIF, var4);
                     }
                  }
               }
            });
         }
      }
   }

   static void LE() {
      if (!Ok) {
         Ok = true;
         GuiSessionManager.LE();
         Bukkit.getPluginManager().registerEvents(new GuiSessionCleanupListener(), Main.getInstance());
      }
   }

   static void LF() {
      for (GuiSessionEntry var1 : List.copyOf(aIy.values())) {
         var1.close();
      }

      GuiSessionManager.LF();
   }

   public void open(Player player) {
      Objects.requireNonNull(player, "player");
      UUID var2 = player.getUniqueId();
      Object var3 = new Object();
      this.aID.put(var2, var3);
      NativePacketsHolder.PE().IP().entity(Main.getInstance(), player, () -> {
         if (this.aID.get(var2) == var3) {
            try {
               this.method2450(player, 0, var3);
            } finally {
               if (!this.aID.remove(var2, var3)) {
                  GuiSessionEntry var6 = (GuiSessionEntry)aIy.get(var2);
                  if (var6 != null && var6.aIE == this && var6.aII == var3) {
                     var6.close();
                  }
               }
            }
         }
      }, () -> this.aID.remove(var2, var3));
   }

   public void close(Player player) {
      Objects.requireNonNull(player, "player");
      this.aID.remove(player.getUniqueId());
      GuiSessionEntry var2 = (GuiSessionEntry)aIy.get(player.getUniqueId());
      if (var2 != null && var2.aIE == this) {
         method2449(player, () -> {
            GuiSessionEntry var3 = (GuiSessionEntry)aIy.get(player.getUniqueId());
            if (var3 != null && var3.aIE == this && var3.aII == var2.aII) {
               var3.close();
            }
         });
      }
   }

   private static void method2449(Player var0, Runnable var1) {
      NativePacketsHolder.PE().IP().entity(Main.getInstance(), var0, var1, () -> {});
   }

   private void method2450(Player var1, int var2, Object var3) {
      if (this.aIA.isEnabled() && var1.isOnline()) {
         List var4 = this.Zd;
         if (ContainerPacketListener.cb(var1)) {
            boolean var7 = var4.size() > 45;
            int var8 = var7 ? Math.min(var2, (new ScrollableState(var4.size())).aKB) : 0;
            ContainerPacketListener.method2602(var1, "IA_CUST_GUI", this.method2446(var4), null, null, var8, var2x -> {
               GuiSessionEntry var3x = (GuiSessionEntry)aIy.get(var1.getUniqueId());
               if (var3x != null && var3x.aIE == this) {
                  var3x.aIw = var2x;
               }
            }, var5x -> this.method2453(var1, var5x, var8, var7, var3), this.aIz.ca(var1));
            if (this.Zd != var4) {
               this.refresh();
            }
         } else {
            CustomInventoryHolder var5 = new CustomInventoryHolder(54, "IA_CUST_GUI");
            GuiSessionEntry var6 = this.method2453(var1, var5, var2, false, var3);
            this.method2451(var6, var2);
            var5.open(var1);
            method2455(var6);
            if (this.Zd != var4) {
               this.refresh();
            }
         }
      }
   }

   private void method2451(GuiSessionEntry var1, int var2) {
      List var3 = this.Zd;
      boolean var4 = var3.size() > 45;
      int var5 = var4 ? 8 : 9;
      int var6 = (int)(((long)var3.size() + var5 - 1L) / var5);
      int var7 = Math.max(0, var6 - 5);
      var1.aIw = Math.max(0, Math.min(var2, var7));
      int var8 = var1.aIw * var5;

      for (int var9 = 0; var9 < 54; var9++) {
         int var10 = var8 + var9 / 9 * var5 + var9 % 9;
         if (var9 < 45 && var9 % 9 < var5 && var10 < var3.size()) {
            var1.aIG.method2666(var9, ((ItemStack)var3.get(var10)).clone(), var3x -> this.method2456(var10, var3x, var3));
         } else {
            var1.aIG.cq(var9);
         }
      }

      if (var4) {
         var1.aIG
            .method2666(
               8, var1.aIt.method35(var1.aIF, false, var1.aIw > 0), var2x -> this.method2452(var1, ScrollableState.method2591(-1, var2x.isShiftClick()))
            );

         for (int var11 = 0; var11 < 4; var11++) {
            int var12 = var11;
            var1.aIG
               .method2666(
                  17 + var11 * 9,
                  var1.aIt.method36(var1.aIF, var11, var1.aIw, var7, var6),
                  var4x -> this.method2452(var1, ScrollableState.method2591(ScrollableState.method2592(var12, 4, var1.aIw, var7), var4x.isShiftClick()))
               );
         }

         var1.aIG
            .method2666(
               53, var1.aIt.method35(var1.aIF, true, var1.aIw < var7), var2x -> this.method2452(var1, ScrollableState.method2591(1, var2x.isShiftClick()))
            );
      }
   }

   private void method2452(GuiSessionEntry var1, int var2) {
      method2449(var1.aIF, () -> {
         if (aIy.get(var1.aIF.getUniqueId()) == var1 && var1.aIF.getOpenInventory().getTopInventory() == var1.aIG.getInventory()) {
            this.method2451(var1, var1.aIw + var2);
         }
      });
   }

   private GuiSessionEntry method2453(Player var1, CustomInventoryHolder var2, int var3, boolean var4, Object var5) {
      GuiSessionEntry var6 = new GuiSessionEntry(this, var1, var2, var3, var4, var5);
      aIy.put(var1.getUniqueId(), var6);
      var2.method2681(var2x -> {
         if (!var2x.isCancelled()) {
            PendingMessageStore.method2753(var1, this.aIB.MG());
         }
      });
      var2.method2682(var2x -> aIy.remove(var1.getUniqueId(), var6));
      return var6;
   }

   static void method2454(CustomInventoryHolder var0, Player var1) {
      GuiSessionEntry var2 = (GuiSessionEntry)aIy.get(var1.getUniqueId());
      if (var2 != null && var2.aIG == var0) {
         method2455(var2);
      }
   }

   private static void method2455(GuiSessionEntry var0) {
      if (var0.aIF.getOpenInventory().getTopInventory() != var0.aIG.getInventory()) {
         PendingMessageStore.ce(var0.aIF);
         aIy.remove(var0.aIF.getUniqueId(), var0);
      }
   }

   private void method2456(int var1, InventoryClickEvent var2, List var3) {
      var2.setCancelled(true);
      if (var3 != this.Zd) {
         this.refresh();
      } else {
         try {
            if (this.aIA.isEnabled()) {
               this.aIC.accept(var1, var2);
            }
         } catch (RuntimeException var8) {
            this.aIA.getLogger().log(Level.SEVERE, "Scrollable inventory click handler failed", var8);
         } finally {
            var2.setCancelled(true);
         }
      }
   }
}
