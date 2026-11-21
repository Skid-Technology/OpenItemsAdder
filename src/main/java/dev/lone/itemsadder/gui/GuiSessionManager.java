package dev.lone.itemsadder.gui;

import beer.devs.nativepackets.api.SchedulerAccess.ScheduledTask;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.InventoryAdapter;
import dev.lone.itemsadder.text.PendingMessageStore;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class GuiSessionManager {
   private static final Set aHZ = ConcurrentHashMap.newKeySet();
   public final ScrollTrackManager aIa = new ScrollTrackManager();
   private final Plugin aIb;
   private final ScrollableGuiTitle aIc;
   private final TreeMap aId = new TreeMap();
   private InventoryAdapter aIe;
   private int aIf;
   private final Map aIg = new ConcurrentHashMap();
   private final Map aIh = new ConcurrentHashMap();
   private volatile Consumer aIi;
   private volatile Consumer aIj;
   private volatile BiConsumer aIk;
   private volatile BiConsumer aIl;
   private final Map aIm = new TreeMap();
   private volatile ScheduledTask aIn;

   public void method2404(Consumer var1) {
      this.aIi = var1;
   }

   public void method2405(Consumer var1) {
      this.aIj = var1;
   }

   public void method2406(BiConsumer var1) {
      this.aIk = var1;
   }

   public void method2407(BiConsumer var1) {
      this.aIl = var1;
   }

   private boolean method2408(String var1, Runnable var2) {
      try {
         var2.run();
         return true;
      } catch (RuntimeException var4) {
         this.aIb.getLogger().log(Level.SEVERE, "Scrollable container " + var1 + " callback failed", var4);
         return false;
      }
   }

   private void bW(int var1) {
      if (this.aIi != null) {
         if (!this.aIm.containsKey(var1)) {
            this.aIm.put(var1, this.getItem(var1));
         }

         if (this.aIn == null) {
            this.aIn = NativePacketsHolder.PE().IP().globalLater(Main.getInstance(), this::LD, 1L);
         }

         aHZ.add(this);
      }
   }

   private void method2409(GuiSession var1) {
      if (this.aIi != null && !var1.cz) {
         for (int var2 = 0; var2 < 45; var2++) {
            if (var1.bZ(var2)) {
               this.bW((var1.aIw + var2 / 9) * 8 + var2 % 9);
            }
         }
      }
   }

   private void LD() {
      if (this.aIn != null) {
         this.aIn.cancel();
      }

      this.aIn = null;
      TreeMap var1 = new TreeMap();
      this.aIm.forEach((var2x, var3) -> {
         ItemStack var4 = var2x < this.aIf * 8 ? this.getItem(var2x) : null;
         if (!Objects.equals(var3, var4)) {
            var1.put(var2x, var4);
         }
      });
      this.aIm.clear();
      Consumer var2 = this.aIi;
      if (var2 != null && !var1.isEmpty()) {
         this.method2408("change", () -> var2.accept(Collections.unmodifiableMap(var1)));
      }

      this.LI();
   }

   public GuiSessionManager(Plugin var1, ScrollableGuiTitle var2, int var3) {
      this.aIb = Objects.requireNonNull(var1, "owner");
      this.aIc = Objects.requireNonNull(var2, "title");
      bX(var3);
      this.aIf = var3;
   }

   static void LE() {
      Bukkit.getPluginManager().registerEvents(new GuiInventoryListener(), Main.getInstance());
   }

   static void LF() {
      for (GuiSessionManager var1 : List.copyOf(aHZ)) {
         var1.LH();
      }
   }

   private static void LG() {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Use the primary server thread for container contents");
      }
   }

   private static void bX(int var0) {
      if (var0 < 1 || var0 > 268435455) {
         throw new IllegalArgumentException("rows must be between 1 and Integer.MAX_VALUE / 8");
      }
   }

   public int getRows() {
      LG();
      return this.aIf;
   }

   public int getSize() {
      LG();
      return this.aIf * 8;
   }

   public List getViewers() {
      LG();
      return this.aIg.values().stream().map(var0 -> var0.aIp).toList();
   }

   public int getLogicalSlot(Player player, int rawSlot) {
      LG();
      Objects.requireNonNull(player, "player");
      GuiSession var3 = (GuiSession)this.aIg.get(player.getUniqueId());
      return var3 != null && var3.bZ(rawSlot) ? (var3.aIw + rawSlot / 9) * 8 + rawSlot % 9 : -1;
   }

   public void setRows(int rows) {
      LG();
      bX(rows);
      if (this.aIg.isEmpty() && this.aIh.isEmpty()) {
         this.getContents();
         if (!this.aId.isEmpty() && (Integer)this.aId.lastKey() >= rows * 8) {
            throw new IllegalArgumentException("Resizing would discard nonempty slots");
         }

         this.aIf = rows;
      } else {
         throw new IllegalStateException("Close the container before resizing");
      }
   }

   private void bY(int var1) {
      if (var1 < 0 || var1 >= this.aIf * 8) {
         throw new IndexOutOfBoundsException("slot: " + var1);
      }
   }

   public ItemStack getItem(int slot) {
      LG();
      this.bY(slot);
      Object var2 = this.aId.get(slot);
      ItemStack var3 = var2 == null ? null : (this.aIe == null ? (ItemStack)var2 : this.aIe.method3553(var2));
      return method2410(var3) ? null : var3.clone();
   }

   public void setItem(int slot, ItemStack item) {
      LG();
      this.bY(slot);
      this.bW(slot);
      this.method2411(slot, item);
   }

   public Map getContents() {
      LG();
      TreeMap var1 = new TreeMap();
      Iterator var2 = this.aId.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ItemStack var4 = this.aIe == null ? (ItemStack)var3.getValue() : this.aIe.method3553(var3.getValue());
         if (method2410(var4)) {
            var2.remove();
         } else {
            var1.put((Integer)var3.getKey(), var4.clone());
         }
      }

      return Collections.unmodifiableMap(var1);
   }

   private static boolean method2410(ItemStack var0) {
      return var0 == null
         || var0.getAmount() <= 0
         || var0.getType() == Material.AIR
         || var0.getType() == Material.CAVE_AIR
         || var0.getType() == Material.VOID_AIR;
   }

   private void method2411(int var1, ItemStack var2) {
      if (method2410(var2)) {
         this.aId.remove(var1);
      } else {
         this.aId.put(var1, this.aIe == null ? var2.clone() : this.aIe.an(var2));
      }
   }

   public void method2412(NamespacedKey var1, NamespacedKey var2) {
      this.method2413(var1, var2, null);
   }

   public void method2413(NamespacedKey var1, NamespacedKey var2, NamespacedKey var3) {
      this.aIa.method2596(this.aIb, var1, var2, var3);
      this.refresh();
   }

   public void refresh() {
      for (GuiSession var2 : List.copyOf(this.aIg.values())) {
         if (var2.aIv.compareAndSet(false, true)) {
            method2414(var2.aIp, () -> {
               var2.aIv.set(false);
               if (!var2.cz && this.aIg.get(var2.aIp.getUniqueId()) == var2 && var2.aIp.getOpenInventory().getTopInventory() == var2.aIq) {
                  var2.aIt = this.aIa.Mi();
                  if (var2.aIr == null) {
                     var2.LM();
                  } else {
                     ContainerPacketListener.method2608(var2.aIp, var2.aIq, this.aIa.ca(var2.aIp));
                  }

                  Component var2x = this.aIc.MG();
                  if (!var2x.equals(var2.aIu)) {
                     var2.aIu = var2x;
                     ItemOperationsService.getRegistryBridge().method3426(var2.aIp, var2x);
                  }
               }
            });
         }
      }
   }

   public void open(Player player) {
      Objects.requireNonNull(player, "player");
      UUID var2 = player.getUniqueId();
      Object var3 = new Object();
      this.aIh.put(var2, var3);
      aHZ.add(this);
      NativePacketsHolder.PE().IP().entity(Main.getInstance(), player, () -> {
         if (this.aIh.get(var2) != var3) {
            this.LI();
         } else {
            try {
               if (!this.aIb.isEnabled() || !player.isOnline()) {
                  return;
               }

               GuiSession var4 = (GuiSession)this.aIg.get(var2);
               if (var4 != null) {
                  var4.aIp.closeInventory();
               }

               this.LJ();
               if (this.aIf > 5 && ContainerPacketListener.cb(player)) {
                  ContainerPacketListener.method2606(player, this);
               } else {
                  this.method2418(player, 0);
               }
            } finally {
               if (!this.aIh.remove(var2, var3)) {
                  GuiSession var6 = (GuiSession)this.aIg.get(var2);
                  if (var6 != null) {
                     this.method2415(var6);
                  }
               }

               this.LI();
            }
         }
      }, () -> {
         this.aIh.remove(var2, var3);
         this.LI();
      });
   }

   public void close() {
      this.aIh.clear();

      for (GuiSession var2 : List.copyOf(this.aIg.values())) {
         method2414(var2.aIp, () -> this.method2415(var2));
      }

      this.LI();
   }

   private void LH() {
      this.aIh.clear();

      for (GuiSession var2 : List.copyOf(this.aIg.values())) {
         this.method2415(var2);
      }

      this.LD();
      this.LI();
   }

   public void close(Player player) {
      Objects.requireNonNull(player, "player");
      this.aIh.remove(player.getUniqueId());
      GuiSession var2 = (GuiSession)this.aIg.get(player.getUniqueId());
      if (var2 != null) {
         method2414(player, () -> this.method2415(var2));
      }

      this.LI();
   }

   private static void method2414(Player var0, Runnable var1) {
      NativePacketsHolder.PE().IP().entity(Main.getInstance(), var0, var1, () -> {});
   }

   private void method2415(GuiSession var1) {
      if (this.aIg.get(var1.aIp.getUniqueId()) == var1) {
         if (var1.aIp.getOpenInventory().getTopInventory() == var1.aIq) {
            var1.aIp.closeInventory();
         }

         var1.method2439();
      }
   }

   private void LI() {
      if (this.aIg.isEmpty() && this.aIh.isEmpty() && this.aIn == null) {
         aHZ.remove(this);
      }
   }

   private void LJ() {
      if (this.aIe == null) {
         InventoryAdapter var1 = ItemOperationsService.getRegistryBridge().OG();
         if (var1 == null) {
            throw new UnsupportedOperationException("Scrollable containers are unavailable on this server");
         }

         this.aId.replaceAll((var1x, var2) -> var1.an((ItemStack)var2));
         this.aIe = var1;
      }
   }

   Inventory method2416(Player var1, BiConsumer var2, Runnable var3) {
      GuiSession var4 = new GuiSession(this, var1, var2, var3);
      this.aIg.put(var1.getUniqueId(), var4);
      aHZ.add(this);
      return var4.aIq;
   }

   void method2417(Player var1, int var2) {
      GuiSession var3 = (GuiSession)this.aIg.get(var1.getUniqueId());
      var3.aIw = var2;
   }

   private void method2418(Player var1, int var2) {
      Inventory var3 = this.method2416(var1, null, () -> {});
      this.method2417(var1, var2);
      ((GuiSession)this.aIg.get(var1.getUniqueId())).LM();
      var1.openInventory(var3);
      this.bS(var1);
   }

   void bS(Player var1) {
      GuiSession var2 = (GuiSession)this.aIg.get(var1.getUniqueId());
      if (var2 != null) {
         if (var1.getOpenInventory().getTopInventory() != var2.aIq) {
            PendingMessageStore.ce(var1);
            var2.method2439();
         } else {
            var2.aIx = true;
         }
      }
   }
}
