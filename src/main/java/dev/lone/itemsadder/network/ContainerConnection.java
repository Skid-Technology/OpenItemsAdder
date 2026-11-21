package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.ProtocolPhase;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.CustomInventoryHolder;
import dev.lone.itemsadder.gui.GuiButton;
import dev.lone.itemsadder.gui.GuiIconRegistry;
import dev.lone.itemsadder.gui.GuiSessionManager;
import dev.lone.itemsadder.gui.ScrollTrackButton;
import dev.lone.itemsadder.gui.ScrollableGuiInventory;
import dev.lone.itemsadder.gui.ScrollableState;
import dev.lone.itemsadder.gui.SlotLayout;
import dev.lone.itemsadder.gui.VisibilityCooldown;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.text.PendingMessageStore;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.RefreshingValue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public final class ContainerConnection {
   static final int aKQ = 67;
   static final int aKR = 54;
   static final int aKS = 55;
   final boolean aKT;
   final boolean aKU;
   final boolean aKV;
   final boolean aKW;
   final Object aKX;
   final Object aKY;
   final Object aKZ;
   final Consumer aLa;
   volatile Map aLb;
   volatile Map aLc;
   final Player aLd;
   final ConnectionHandle aLe;
   final CustomInventoryHolder aLf;
   final Inventory aLg;
   final GuiSessionManager aLh;
   volatile String aLi;
   volatile List jg;
   final ScrollableState aLj;
   final Map aLk;
   int aLl;
   volatile boolean aLm;
   volatile boolean aLn;
   List aLo;
   List aLp;
   int aLq;
   final VisibilityCooldown aLr;
   ScheduledFuture aLs;
   ScheduledFuture aLt;
   ScheduledFuture aLu;
   boolean aLv;
   final ChannelOutboundHandlerAdapter aLw;
   final boolean[] aLx;
   final AtomicBoolean aLy;
   final AtomicInteger aLz;
   Consumer aLA;
   final IntConsumer aLB;
   final RefreshingValue aLC;
   volatile int aLD;
   volatile int aAD;
   volatile int aLE;
   volatile boolean cz;

   ContainerConnection(ContainerPacketListener var1, Player var2, String var3, List var4, ItemStack var5, Consumer var6, int var7, IntConsumer var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, null, "_iainternal:gui/scroll_track");
   }

   ContainerConnection(
      ContainerPacketListener var1, Player var2, String var3, List var4, ItemStack var5, Consumer var6, int var7, IntConsumer var8, GuiSessionManager var9
   ) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var9.aIa.ca(var2));
   }

   ContainerConnection(
      ContainerPacketListener var1,
      Player var2,
      String var3,
      List var4,
      ItemStack var5,
      Consumer var6,
      int var7,
      IntConsumer var8,
      GuiSessionManager var9,
      String var10
   ) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, false);
   }

   ContainerConnection(
      ContainerPacketListener var1,
      Player var2,
      String var3,
      List var4,
      ItemStack var5,
      Consumer var6,
      int var7,
      IntConsumer var8,
      GuiSessionManager var9,
      String var10,
      boolean var11
   ) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, null, var5 != null && !var11 ? Map.of(67, new GuiButton(var5, var6)) : Map.of());
   }

   ContainerConnection(
      ContainerPacketListener var1,
      Player var2,
      String var3,
      List var4,
      ItemStack var5,
      Consumer var6,
      int var7,
      IntConsumer var8,
      GuiSessionManager var9,
      String var10,
      boolean var11,
      CustomInventoryHolder var12,
      Map var13
   ) {
      this.aLF = var1;
      this.aLk = new HashMap();
      this.aLo = List.of();
      this.aLp = List.of();
      this.aLr = new VisibilityCooldown();
      this.aLw = new ContainerWriteHandler(this);
      this.aLy = new AtomicBoolean();
      this.aLz = new AtomicInteger();
      this.aLD = -1;
      this.aAD = -1;
      this.aKT = var11;
      this.aKW = ContainerPacketListener.cb(var2);
      this.aKV = var12 != null;
      this.aKU = this.aKV ? SlotLayout.cd(var4.size()) : !var11 || var4.size() > 54;
      this.aKX = var1.aKK.an(new ItemStack(Material.AIR));
      this.aKY = var11 ? var1.aKK.an(GuiIconRegistry.aKi) : this.aKX;
      this.aKZ = var11 && PermissionNodes.bJ(var2) ? var1.aKK.an(GuiIconRegistry.aKj) : this.aKX;
      this.aLa = var5 != null ? var6 : var1x -> ContentManager.method8084().bB.aJX.bW(var2);
      HashMap var14 = new HashMap();
      HashMap var15 = new HashMap();
      var13.forEach((var3x, var4x) -> {
         if (var3x >= 54 && var3x < 90) {
            var14.put(var3x, var1.aKK.an(var4x.aKO));
            if (var4x.aKP != null) {
               var15.put(var3x, var4x.aKP);
            }
         } else {
            throw new IllegalArgumentException("Invalid lower control slot: " + var3x);
         }
      });
      this.aLb = Map.copyOf(var14);
      this.aLc = Map.copyOf(var15);
      this.aLi = var10;
      this.aLd = var2;
      this.aLh = var9;
      this.jg = List.copyOf(var4);
      this.aLj = new ScrollableState(var9 == null ? var4.size() + (this.aKV ? 2 : 0) : var9.getSize(), this.aKV ? 4 : (var11 ? 6 : 5));
      this.aLx = new boolean[this.aLj.aKy * 9];
      this.aLj.cg(this.aKU ? var7 : 0);
      this.aLB = var8;
      this.aLe = NativePacketsHolder.PE().cB(var2).PC();
      this.aLC = (RefreshingValue)var1.aKM.get(this.aLe);
      if (var9 == null) {
         this.aLf = var12 == null ? (var11 ? new ScrollableGuiInventory(var3) : new CustomInventoryHolder(54, var3)) : var12;
         this.aLg = this.aLf.getInventory();
         if (var11) {
            ((ScrollableGuiInventory)this.aLf).aLM = this;
            if (!this.aKV) {
               this.aLo = this.aLp = List.of(AdventureText.legacySerializer.deserialize(var3));
            }
         }

         if (var5 != null && !var11) {
            this.aLf.method2666(49, var5, var6);
         }

         if (!var11) {
            this.aLf.method2682(var1x -> this.close());
         }
      } else {
         this.aLf = null;
         this.aLg = var9.method2416(var2, this::method2656, this::close);
      }

      this.Mr();
   }

   void Mn() {
      if (this.aKT) {
         ScrollableGuiInventory var1 = (ScrollableGuiInventory)this.aLf;

         for (int var2 = 0; var2 < 54; var2++) {
            if (this.aLg.getItem(var2) != null) {
               var1.method2666(var2, this.aLg.getItem(var2), (Consumer)var1.aLI.get(var2));
            }
         }

         this.aLg.clear();
      }

      if (this.aKT) {
         this.aLF.aKN.put(this.aLd.getUniqueId(), this);
      }

      if (this.aLe.nativeChannel() instanceof Channel var3) {
         var3.eventLoop().execute(() -> {
            if (!this.cz && var3.isActive()) {
               if (var3.pipeline().get("itemsadder_ia_scroll") != null) {
                  var3.pipeline().remove("itemsadder_ia_scroll");
               }

               var3.pipeline().addAfter("unbundler", "itemsadder_ia_scroll", this.aLw);
            }
         });
      }

      if (this.aLf != null) {
         this.aLf.open(this.aLd);
      } else {
         this.aLd.openInventory(this.aLg);
      }

      if (this.aLd.getOpenInventory().getTopInventory() != this.aLg) {
         PendingMessageStore.ce(this.aLd);
         this.close();
      } else {
         this.aLD = ItemOperationsService.getRegistryBridge().cx(this.aLd);
         this.aLF.aKN.put(this.aLd.getUniqueId(), this);
         if (this.aKU) {
            this.Mu();
            if (this.aKW) {
               this.aLe.eventLoop().execute(() -> {
                  if (!this.cz && this.aLF.aKN.get(this.aLd.getUniqueId()) == this && this.aLe.outboundPhase() == ProtocolPhase.PLAY) {
                     this.aLC.update(this, this.aLD, this::Mq);
                  }
               });
            }
         }
      }
   }

   synchronized Object method2652(int var1, Object var2) {
      if (this.aKW && this.aKU && var1 >= 0 && var1 < this.aLx.length) {
         boolean var3 = var1 % 9 == 8;
         this.aLx[var1] = var3 || this.aLF.aKK.ac(var2);
         if (!this.aLx[var1]) {
            return var2;
         }

         int var4 = this.aLz.incrementAndGet();
         float var5 = this.co(var1 / 9);
         return var3 ? this.aLF.aKK.method3563(var2, var5, var4, true, this.aLi, this.aLj.Mf()) : this.aLF.aKK.method3562(var2, var4);
      } else {
         return var2;
      }
   }

   synchronized Object cm(int var1) {
      Object var2 = this.aKT && var1 == 54 ? this.aKZ : (this.aKT && var1 == 55 && this.aLA != null ? this.aKY : this.aLb.getOrDefault(var1, this.aKX));
      return this.aKV && this.aLC.appliedValue == this ? this.method2652(var1 - 54, var2) : var2;
   }

   synchronized Object cn(int var1) {
      Object var2 = ((ScrollableGuiInventory)this.aLf).aLK.getOrDefault(var1, this.aKX);
      return !this.aKV && this.aLC.appliedValue == this ? this.method2652(var1, var2) : var2;
   }

   void method2653(List var1, List var2) {
      this.aLo = List.copyOf(var1);
      this.aLp = List.copyOf(var2);
      if (var1.size() >= 2 && !this.cz && this.aLe.nativeChannel() instanceof Channel var3) {
         this.aLu = var3.eventLoop()
            .scheduleAtFixedRate(
               () -> NativePacketsHolder.PE()
                  .IP()
                  .entity(
                     Main.getInstance(),
                     this.aLd,
                     () -> {
                        if (!this.cz && this.aLF.aKN.get(this.aLd.getUniqueId()) == this) {
                           if (!this.Mt()
                              || this.aLd.getOpenInventory().getTopInventory() != this.aLg
                              || ItemOperationsService.getRegistryBridge().cx(this.aLd) != this.aLD) {
                              this.Ms();
                           } else if (this.aLd.getItemOnCursor().getType().isAir()) {
                              this.aLq = (this.aLq + 1) % this.aLo.size();
                              this.Mo();
                           }
                        }
                     },
                     this::close
                  ),
               500L,
               500L,
               TimeUnit.MILLISECONDS
            );
      }
   }

   private boolean Mo() {
      List var1 = this.aLm ? this.aLp : this.aLo;
      if (var1.isEmpty()) {
         return false;
      }

      ItemOperationsService.getRegistryBridge().method3426(this.aLd, (Component)var1.get(this.aLq));
      return true;
   }

   private void Mp() {
      synchronized (this) {
         if (this.cz || this.aLn) {
            return;
         }

         this.aLm = !this.aLm;
         this.aLn = true;
         this.aLl++;
      }

      if (!this.Mo()) {
         this.aLd.updateInventory();
      }
   }

   void method2654(InventoryClickEvent var1) {
      PlayerInventory var2 = this.aLd.getInventory();
      if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
         var1.setCancelled(true);
         ItemStack var3 = var1.getCurrentItem();
         if (var3 == null || var3.getType().isAir()) {
            return;
         }

         ItemStack var4 = var3.clone();
         int var5 = var1.getSlot() < 9 ? 9 : 0;
         int var6 = var1.getSlot() < 9 ? 36 : 9;

         for (int var7 = 0; var7 < 2 && var4.getAmount() > 0; var7++) {
            for (int var8 = var5; var8 < var6 && var4.getAmount() > 0; var8++) {
               ItemStack var9 = var2.getItem(var8);
               boolean var10 = var9 == null || var9.getType().isAir();
               if ((var7 != 0 || !var10 && var9.isSimilar(var4)) && (var7 != 1 || var10)) {
                  int var11 = Math.max(0, Math.min(var4.getAmount(), Math.min(var4.getMaxStackSize(), var2.getMaxStackSize()) - (var10 ? 0 : var9.getAmount())));
                  if (var11 != 0) {
                     ItemStack var12 = var4.clone();
                     var12.setAmount(var11 + (var10 ? 0 : var9.getAmount()));
                     var2.setItem(var8, var12);
                     var4.setAmount(var4.getAmount() - var11);
                  }
               }
            }
         }

         var1.setCurrentItem(var4.getAmount() == 0 ? null : var4);
         this.aLd.updateInventory();
      } else if (var1.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
         var1.setCancelled(true);
         ItemStack var13 = this.aLd.getItemOnCursor().clone();
         if (var13.getType().isAir()) {
            return;
         }

         for (int var14 = 0; var14 < 36 && var13.getAmount() < var13.getMaxStackSize(); var14++) {
            ItemStack var15 = var2.getItem(var14);
            if (var15 != null && !var15.getType().isAir() && var15.isSimilar(var13)) {
               int var16 = Math.min(var13.getMaxStackSize() - var13.getAmount(), var15.getAmount());
               var13.setAmount(var13.getAmount() + var16);
               ItemStack var17 = var15.clone();
               var17.setAmount(var15.getAmount() - var16);
               var2.setItem(var14, var17.getAmount() == 0 ? null : var17);
            }
         }

         this.aLd.setItemOnCursor(var13);
         this.aLd.updateInventory();
      }
   }

   void method2655(int var1, int var2, boolean var3) {
      Consumer var4 = this.aKT && var1 == 54 ? this.aLa : (Consumer)this.aLc.get(var1);
      if (var4 != null) {
         NativePacketsHolder.PE()
            .IP()
            .entity(
               Main.getInstance(),
               this.aLd,
               () -> {
                  if (!this.cz
                     && !this.aLm
                     && !this.aLn
                     && this.aLF.aKN.get(this.aLd.getUniqueId()) == this
                     && this.aLd.getOpenInventory().getTopInventory() == this.aLg) {
                     if (!this.aKT || var1 != 54 || PermissionNodes.bJ(this.aLd) && this.aLd.getItemOnCursor().getType().isAir()) {
                        var4.accept(
                           new InventoryClickEvent(
                              this.aLd.getOpenInventory(),
                              SlotType.CONTAINER,
                              var1,
                              var3 ? (var2 == 0 ? ClickType.SHIFT_LEFT : ClickType.SHIFT_RIGHT) : (var2 == 0 ? ClickType.LEFT : ClickType.RIGHT),
                              InventoryAction.NOTHING
                           )
                        );
                     }
                  }
               },
               this::close
            );
      }
   }

   synchronized void Mq() {
      if (!this.cz) {
         if (this.aLj.Mh()) {
            this.aLv = true;
         } else if (this.aLy.compareAndSet(false, true)) {
            NativePacketsHolder.PE()
               .IP()
               .entity(
                  Main.getInstance(),
                  this.aLd,
                  () -> {
                     synchronized (this) {
                        this.aLy.set(false);
                        if (this.aLj.Mh()) {
                           this.aLv = true;
                           return;
                        }
                     }

                     if (!this.cz && this.aLF.aKN.get(this.aLd.getUniqueId()) == this) {
                        if (this.Mt()
                           && ItemOperationsService.getRegistryBridge().cx(this.aLd) == this.aLD
                           && this.aLd.getOpenInventory().getTopInventory() == this.aLg) {
                           this.Mr();
                           this.aLd.updateInventory();
                        } else {
                           this.close();
                           if (this.aLd.getOpenInventory().getTopInventory() == this.aLg) {
                              this.aLd.closeInventory();
                           }
                        }
                     }
                  },
                  this::close
               );
         }
      }
   }

   void Mr() {
      if (this.aKV) {
         this.aLE = this.aLj.Me();
         HashMap var7 = new HashMap();
         HashMap var8 = new HashMap();

         for (int var9 = 0; var9 < 36; var9++) {
            int var11 = 54 + var9;
            if (this.aKU && var9 % 9 == 8) {
               var7.put(var11, this.aLF.aKK.an(this.cp(var9 / 9)));
               int var13 = var9 / 9;
               var8.put(var11, var2x -> this.method2656(var13, var2x));
            } else {
               int var12 = SlotLayout.method2584(this.jg.size(), this.aLE, var9);
               if (var12 >= 0) {
                  GuiButton var6 = (GuiButton)this.jg.get(var12);
                  var7.put(var11, this.aLF.aKK.an(var6.aKO));
                  if (var6.aKP != null) {
                     var8.put(var11, var6.aKP);
                  }
               }
            }
         }

         this.aLb = Map.copyOf(var7);
         this.aLc = Map.copyOf(var8);
      } else {
         this.aLE = this.aLj.Me();
         this.aLB.accept(this.aLE);
         int var1 = this.aKU ? 8 : 9;
         int var2 = this.aKU ? this.aLE * var1 : 0;
         if (this.aLh != null) {
            this.aLh.method2417(this.aLd, this.aLE);
         }

         for (int var3 = 0; var3 < this.aLj.aKy * 9; var3++) {
            if (this.aKU && var3 % 9 == 8) {
               int var10 = var3 / 9;
               if (this.aLf == null) {
                  this.aLg.setItem(var3, this.cp(var10));
               } else {
                  this.aLf.method2666(var3, this.cp(var10), var2x -> this.method2656(var10, var2x));
               }
            } else if (this.aLh == null) {
               int var4 = var2 + var3 / 9 * var1 + var3 % 9;
               if (var4 >= this.jg.size()) {
                  this.aLf.cq(var3);
               } else {
                  GuiButton var5 = (GuiButton)this.jg.get(var4);
                  this.aLf.method2666(var3, var5.aKO, var5.aKP);
               }
            }
         }
      }
   }

   void method2656(int var1, InventoryClickEvent var2) {
      if (!this.cz && this.aLF.aKN.get(this.aLd.getUniqueId()) == this) {
         if (this.aKW && var2.isRightClick()) {
            this.aLj.Mg();
            this.Mu();
            this.Mq();
         } else {
            int var3 = ScrollableState.method2592(var1, this.aLj.aKy, this.aLE, this.aLj.aKB) * (var2.isShiftClick() ? this.aLj.aKy : 1);
            if (var3 != 0) {
               boolean var4 = this.aLj.cg(this.aLj.Me() + var3);
               boolean var5 = this.Mu();
               if (var4 || var5) {
                  this.Mq();
               }
            }
         }
      }
   }

   synchronized boolean ci(int var1) {
      boolean var2 = this.aLj.ci(var1);
      Integer var3 = (Integer)this.aLk.remove(var1);
      if (var3 != null && var3 == this.aLl) {
         this.aLn = false;
      }

      if (!var2 && var3 == null) {
         return false;
      }

      if (!this.aLj.Mh() && this.aLk.isEmpty()) {
         if (this.aLt != null) {
            this.aLt.cancel(false);
         }

         this.aLt = null;
         if (this.aLv) {
            this.aLv = false;
            this.Mq();
         }
      }

      return true;
   }

   void Ms() {
      this.close();
      NativePacketsHolder.PE().IP().entity(Main.getInstance(), this.aLd, () -> {
         if (this.aLd.getOpenInventory().getTopInventory() == this.aLg) {
            this.aLd.closeInventory();
         }
      }, () -> {});
   }

   private boolean Mt() {
      return this.aKW ? ContainerPacketListener.cb(this.aLd) : ContainerPacketListener.method2599(this.aLd);
   }

   private int co(int var1) {
      return ScrollableState.method2590(var1, this.aLE, this.aLj.aKB, this.aKV ? 76 : this.aLj.aKy * 18) - (this.aKV && var1 == 3 ? 4 : 0);
   }

   ItemStack cp(int var1) {
      int var2 = this.aLh != null ? this.aLh.getRows() : ScrollableState.cf(this.jg.size() + (this.aKV ? 2 : 0));
      ItemStack var3 = this.aKW
         ? new ItemStack(Material.PAPER)
         : new ScrollTrackButton(null, null, this.aLi).method37(this.aLd, this.co(var1), this.aLE, var2, this.aLj.aKy);
      ItemMeta var4 = var3.getItemMeta();
      var4.setDisplayName(
         Main.aFi
            .lj("ia-scrollbar-position")
            .replace("{first}", Integer.toString(this.aLE + 1))
            .replace("{last}", Integer.toString(Math.min(var2, this.aLE + this.aLj.aKy)))
            .replace("{total}", Integer.toString(var2))
      );
      ArrayList var5 = new ArrayList();
      if (this.aLr.visible()) {
         if (this.aKW) {
            var5.add(Main.aFi.lj(this.aLj.Mf() ? "ia-scrollbar-mode-page" : "ia-scrollbar-mode-row"));
            var5.add(Main.aFi.lj("ia-scrollbar-wheel"));
         }

         var5.add(Main.aFi.lj("ia-scrollbar-click"));
         var5.add(Main.aFi.lj("ia-scrollbar-page"));
         if (this.aKW) {
            var5.add(Main.aFi.lj("ia-scrollbar-toggle"));
         }
      }

      var4.setLore(var5);
      var3.setItemMeta(var4);
      return var3;
   }

   synchronized boolean Mu() {
      boolean var1 = this.aLr.visible();
      this.aLr.method2585(System.nanoTime());
      if (this.aLs == null) {
         this.method2657(VisibilityCooldown.aKu + 1L);
      }

      return var1;
   }

   private void method2657(long var1) {
      if (this.aLe.nativeChannel() instanceof Channel var3 && var3.isActive()) {
         this.aLs = var3.eventLoop().schedule(this::Mv, var1, TimeUnit.NANOSECONDS);
      }
   }

   private void Mv() {
      synchronized (this) {
         this.aLs = null;
         if (this.cz) {
            return;
         }

         long var2 = System.nanoTime();
         long var4 = this.aLr.method2586(var2);
         if (var4 > 0L) {
            this.method2657(var4);
            return;
         }

         if (!this.aLr.method2587(var2)) {
            return;
         }
      }

      NativePacketsHolder.PE().IP().entity(Main.getInstance(), this.aLd, () -> {
         if (!this.cz && this.aLF.aKN.get(this.aLd.getUniqueId()) == this) {
            if (this.Mt() && ItemOperationsService.getRegistryBridge().cx(this.aLd) == this.aLD && this.aLd.getOpenInventory().getTopInventory() == this.aLg) {
               if (!this.aKT && !this.aKV) {
                  for (byte var1 = 8; var1 < this.aLj.aKy * 9; var1 += 9) {
                     this.aLg.setItem(var1, this.cp(var1 / 9));
                  }
               } else {
                  this.Mq();
               }
            } else {
               this.close();
            }
         }
      }, this::close);
   }

   synchronized void close() {
      if (!this.cz) {
         this.cz = true;
         if (this.aLs != null) {
            this.aLs.cancel(false);
         }

         this.aLs = null;
         if (this.aLu != null) {
            this.aLu.cancel(false);
         }

         this.aLu = null;
         if (this.aLt != null) {
            this.aLt.cancel(false);
         }

         this.aLt = null;
         if (this.aLe.nativeChannel() instanceof Channel var1) {
            var1.eventLoop().execute(() -> {
               if (var1.pipeline().context(this.aLw) != null) {
                  var1.pipeline().remove(this.aLw);
               }
            });
         }

         this.aLF.aKN.remove(this.aLd.getUniqueId(), this);
         if (this.aKT && Main.getInstance().isEnabled()) {
            NativePacketsHolder.PE().IP().entity(Main.getInstance(), this.aLd, this.aLd::updateInventory, () -> {});
         }
      }
   }
}
